package com.example.chatapp.data

data class ChatItem(
    val id: Int,
    val avatarUrl: String? = null,
    val username: String,
    val lastMessage: String,
    val timestamp: String,
    val unreadCount: Int = 0,
    val isMuted: Boolean = false,
    val isOnline: Boolean = false
)