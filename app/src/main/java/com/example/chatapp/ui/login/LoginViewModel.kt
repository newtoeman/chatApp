package com.example.chatapp.ui.login

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.data.User
import com.example.chatapp.data.UserRepository
import com.example.chatapp.data.repository.PreferencesRepository
import com.example.chatapp.network.ApiService
import com.example.chatapp.network.MockApiService
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: UserRepository,
    private val preferencesRepository: PreferencesRepository,
    private val apiService: ApiService = MockApiService()
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    // 用于管理手机号输入的防抖协程Job
    private var phoneInputDebounceJob: Job? = null
    val uiState: StateFlow<LoginUiState> = _uiState

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    init {
        loadCachedPhoneNumbers()
        loadInitialPhoneAndCredentials()
    }

    private fun loadCachedPhoneNumbers() {
        val cachedNumbers = preferencesRepository.getAccountsByLRU()
        _uiState.update { it.copy(cachedPhoneNumbers = cachedNumbers) }
    }

    private fun loadInitialPhoneAndCredentials() {
        val initialPhone = preferencesRepository.getAccountsByLRU().firstOrNull() ?: ""
        val initialPassword = if (initialPhone.isNotEmpty()) {
            preferencesRepository.getPasswordForAccount(initialPhone)
        } else {
            ""
        }
        val isRememberPassword = initialPhone.isNotEmpty() && initialPassword.isNotEmpty()

        _uiState.update {
            it.copy(
                phone = initialPhone,
                selectedPhone = initialPhone,
                password = initialPassword,
                isRememberPassword = isRememberPassword
            )
        }
    }

    fun updateIsLoginMode(isLoginMode: Boolean) {
        _uiState.update { it.copy(isLoginMode = isLoginMode) }
        if (!isLoginMode) {
            // 切换到注册模式时，清空注册相关字段
            _uiState.update {
                it.copy(
                    phone = "",
                    selectedPhone = "",
                    password = "",
                    username = ""
                )
            }
        } else {
            // 切换到登录模式时，加载初始数据
            loadInitialPhoneAndCredentials()
        }
    }

    fun updatePhone(phone: String) {
        // 同步更新手机号状态（UI层需要实时显示输入内容，这一步不能防抖）
        _uiState.update { it.copy(phone = phone, selectedPhone = phone) }

        // 取消之前未执行的防抖协程
        phoneInputDebounceJob?.cancel()

        // 启动防抖协程，延迟执行缓存查询
        phoneInputDebounceJob = viewModelScope.launch {
            // 防抖延迟：500ms（可根据实际场景调整为300~800ms）
            delay(300)
            // 执行缓存查询（增加手机号长度过滤，进一步减少无效查询）
            queryPasswordCache(phone)
        }
    }
    // 实际的缓存查询逻辑（防抖后执行）
    private suspend fun queryPasswordCache(phone: String) {
        // 仅当手机号长度≥11位时，才查询缓存（核心优化：过滤非完整手机号）
        val password = if (phone.length >= 11) {
            preferencesRepository.getPasswordForAccount(phone)
        } else {
            "" // 手机号长度不足时，清空密码缓存
        }
        val isRememberPassword = password.isNotEmpty()
        // 延迟更新密码状态，避免UI高频刷新
        _uiState.update {
            it.copy(
                password = password,
                isRememberPassword = isRememberPassword
            )
        }
    }

    fun updateSelectedPhone(selectedPhone: String) {
        _uiState.update { it.copy(selectedPhone = selectedPhone, phone = selectedPhone) }
        
        // 检查该手机号是否有缓存的密码
        val password = preferencesRepository.getPasswordForAccount(selectedPhone)
        val isRememberPassword = password.isNotEmpty()
        _uiState.update {
            it.copy(
                password = password,
                isRememberPassword = isRememberPassword
            )
        }
    }

    fun updatePassword(password: String) {
        _uiState.update { it.copy(password = password) }
    }

    fun updateUsername(username: String) {
        _uiState.update { it.copy(username = username) }
    }

    fun updateRememberPassword(isRememberPassword: Boolean) {
        _uiState.update { it.copy(isRememberPassword = isRememberPassword) }
    }

    fun toggleDropdown() {
        _uiState.update { it.copy(showDropdown = !_uiState.value.showDropdown) }
    }

    fun hideDropdown() {
        _uiState.update { it.copy(showDropdown = false) }
    }

    fun selectPhoneFromDropdown(phone: String) {
        updateSelectedPhone(phone)
        hideDropdown()
    }

    fun login(phone: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading

            if (!isValidPhone(phone)) {
                _loginState.value = LoginState.Error("请输入有效的手机号")
                return@launch
            }

            if (password.length < 6) {
                _loginState.value = LoginState.Error("密码长度至少为6位")
                return@launch
            }

            try {
                // 首先尝试本地数据库验证
                val localUser = repository.login(phone, password)
                if (localUser != null) {
                    // 本地验证成功
                    // 根据是否记住密码保存凭据
                    if (_uiState.value.isRememberPassword) {
                        preferencesRepository.saveOrUpdateAccount(phone, password) // 保存特定手机号的密码
                    } else {
                        preferencesRepository.saveOrUpdateAccount(phone, "") // 清除特定手机号的密码
                    }
                    _loginState.value = LoginState.LoginSuccess(localUser)
                    return@launch
                }

                // 如果本地验证失败，尝试服务器验证
                val serverResult = apiService.login(phone, password)
                if (serverResult.isSuccess) {
                    val user = serverResult.getOrNull()
                    if (user != null) {
                        // 将用户信息保存到本地数据库
                        repository.registerUser(user)
                        // 根据是否记住密码保存凭据
                        if (_uiState.value.isRememberPassword) {
                            preferencesRepository.saveOrUpdateAccount(phone, password) // 保存特定手机号的密码
                        } else {
                            preferencesRepository.saveOrUpdateAccount(phone, "") // 清除特定手机号的密码
                        }
                        _loginState.value = LoginState.LoginSuccess(user)
                    } else {
                        _loginState.value = LoginState.Error("手机号或密码错误")
                    }
                } else {
                    _loginState.value = LoginState.Error("登录失败1")
                }
            } catch (e: Exception) {
                _loginState.value = LoginState.Error("登录失败: ${e.message}")
            }
        }
    }

    fun registerUser(phone: String, password: String, username: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading

            if (!isValidPhone(phone)) {
                _loginState.value = LoginState.Error("请输入有效的手机号")
                return@launch
            }

            if (password.length < 6) {
                _loginState.value = LoginState.Error("密码长度至少为6位")
                return@launch
            }
            if (username.isBlank()) {
                _loginState.value = LoginState.Error("用户名不能为空")
                return@launch
            }
            try {
                val existingUser = repository.getUserByPhone(phone)
                if (existingUser != null) {
                    _loginState.value = LoginState.Error("该手机号已被注册")
                    return@launch
                }

                // 调用服务器注册接口
                val serverResult = apiService.register(phone, password, username)
                if (serverResult.isSuccess) {
                    val user = serverResult.getOrNull()
                    if (user != null) {
                        // 将用户信息保存到本地数据库
                        repository.registerUser(user)
                        // 注册成功后，将新注册的手机号添加到缓存中
                        preferencesRepository.saveOrUpdateAccount(phone, "")
                        // 注意：注册时不保存密码，因为这是新账号，用户可能不希望立即记住密码
                        _loginState.value = LoginState.RegisterSuccess(user)
                    } else {
                        _loginState.value = LoginState.Error("服务器返回空,注册失败")
                    }
                } else {
                    _loginState.value = LoginState.Error("注册失败, 服务器拒绝请求")
                }
            } catch (e: Exception) {
                _loginState.value = LoginState.Error("注册失败: ${e.message}")
            }
        }
    }

    private fun isValidPhone(phone: String): Boolean {
        return Patterns.PHONE.matcher(phone).matches()
    }
}

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class LoginSuccess(val user: User) : LoginState()
    data class RegisterSuccess(val user: User) : LoginState()
    data class Error(val message: String) : LoginState()
}