package com.example.chatapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey val id: Int,
    val phone: String,
    val password: String,
    val username: String? = null,
    val avatar: String? = null
)