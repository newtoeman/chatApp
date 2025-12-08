package com.example.chatapp.json

import com.google.gson.annotations.SerializedName

// 账号列表的缓存容器（便于JSON序列化）
data class AccountCache(
    @SerializedName("accounts") val accounts: MutableList<AccountInfo> = mutableListOf()
)