package com.example.chatapp.network

data class JwtToken(
    val accessToken: String,
    val refreshToken: String
)

data class LoginRequest(
    val phone: String,
    val password: String
)

data class RegisterRequest(
    val phone: String,
    val password: String,
    val username: String
)

data class RefreshTokenRequest(
    val refreshToken: String
)

data class LoginResponse(
    val accessToken: String,
    val refreshToken: String,
    val user: UserDto
)

data class UserDto(
    val id: Int,
    val phone: String,
    val username: String
)