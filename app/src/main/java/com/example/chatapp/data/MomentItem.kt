package com.example.chatapp.data

import java.time.LocalDateTime

data class MomentItem(
    val id: Int,
    val avatarUrl: String? = null,
    val username: String,
    val content: String,
    val timestamp: String,
    val imageUrl: String? = null, // 朋友圈中的图片
    val likes: List<String> = emptyList(), // 点赞用户名列表
    val comments: List<MomentComment> = emptyList() // 评论列表
)

data class MomentComment(
    val id: Int,
    val username: String,
    val content: String,
    val timestamp: String
)