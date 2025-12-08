package com.example.chatapp.json

import com.google.gson.annotations.SerializedName

// 单个账号信息实体类
data class AccountInfo(
    @SerializedName("phone") val phone: String,
    @SerializedName("lastUsedTime") var lastUsedTime: Long = System.currentTimeMillis(),
    @SerializedName("password") var password: String? = null, // 可选：保存密码（加密后）
    @SerializedName("accessToken") var accessToken: String? = null, // 访问令牌（加密后）
    @SerializedName("refreshToken") var refreshToken: String? = null, // 刷新令牌（加密后）
    @SerializedName("tokenExpireTime") var tokenExpireTime: Long? = null // 令牌过期时间（毫秒）
)


