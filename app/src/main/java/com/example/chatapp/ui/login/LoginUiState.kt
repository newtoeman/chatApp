package com.example.chatapp.ui.login

import com.example.chatapp.data.User

data class LoginUiState(
    val isLoginMode: Boolean = true,
    val phone: String = "",
    val selectedPhone: String = "",
    val password: String = "",
    val username: String = "",
    val isRememberPassword: Boolean = false,
    val showDropdown: Boolean = false,
    val cachedPhoneNumbers: List<String> = emptyList(),
    val loginState: LoginState = LoginState.Idle
)