package com.example.chatapp.utils

import android.content.Context
import com.example.chatapp.data.AppDatabase
import com.example.chatapp.data.UserRepository
import com.example.chatapp.network.ApiService
import com.example.chatapp.network.MockApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Date

class TokenValidator(
    private val context: Context
) {
    private val tokenManager = TokenManager(context)
    private val apiService: ApiService = MockApiService()
//    private val database = AppDatabase.getDatabase(context)
//    private val userRepository = UserRepository(database.userDao())
    
    /**
     * 在后台线程验证Token有效性
     */
    suspend fun validateTokenOnStartup(): Boolean = withContext(Dispatchers.IO) {
        try {
            // 首先检查本地是否有Token
            if (!tokenManager.hasValidTokens()) {
                return@withContext false
            }
            
            // 检查Token是否在本地过期（通过解析JWT中的过期时间）
            val accessToken = tokenManager.getAccessToken()
            if (accessToken.isNullOrEmpty()) {
                return@withContext false
            }
            
            // 如果本地有Token，尝试验证Token有效性
            val refreshToken = tokenManager.getRefreshToken()
            val userId = tokenManager.getUserId()
            
            if (refreshToken.isNullOrEmpty()) {
                // 清除不完整的Token数据
                tokenManager.clearTokens()
                return@withContext false
            }
            
            // 尝试使用accessToken获取用户信息以验证其有效性
            val userResult = try {
                apiService.getUserInfo(accessToken, userId)
            } catch (e: Exception) {
                // 网络异常，尝试刷新Token
                null
            }
            
            if (userResult?.isSuccess == true) {
                // Token有效
                return@withContext true
            } else {
                // accessToken无效或网络问题，尝试刷新Token
                val refreshResult = try {
                    apiService.refreshToken(refreshToken)
                } catch (e: Exception) {
                    // 刷新Token失败，网络不可用或Token已失效
                    null
                }
                
                if (refreshResult?.isSuccess == true) {
                    val newTokens = refreshResult.getOrNull()
                    if (newTokens != null) {
                        // 保存新的Token
                        tokenManager.saveTokens(
                            newTokens.accessToken,
                            newTokens.refreshToken,
                            userId,
                            tokenManager.getUserPhone() ?: "",
                            tokenManager.getUserName() ?: ""
                        )
                        
                        return@withContext true
                    } else {
                        // 刷新失败，清除无效Token
                        tokenManager.clearTokens()
                        return@withContext false
                    }
                } else {
                    // 刷新Token失败，清除无效Token
                    tokenManager.clearTokens()
                    return@withContext false
                }
            }
        } catch (e: Exception) {
            // 发生异常，清除可能已损坏的Token
            tokenManager.clearTokens()
            return@withContext false
        }
    }
}