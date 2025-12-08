package com.example.chatapp.data

data class ContactItem(
    val id: Int,
    val avatarUrl: String? = null,
    val username: String,
    val signature: String? = null,
    val status: String? = null, // online, offline, away等状态
    val isTopContact: Boolean = false
)