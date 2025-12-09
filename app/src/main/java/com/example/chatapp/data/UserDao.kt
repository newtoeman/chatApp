package com.example.chatapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE phone = :phone AND password = :password")
    suspend fun login(phone: String, password: String): User?

    @Query("SELECT * FROM users WHERE phone = :phone")
    suspend fun getUserByPhone(phone: String): User?

    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getUserById(id: Int): User?

    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<User>

    @Insert
    suspend fun insertUser(user: User)

    @Query("UPDATE users SET username = :username, password = :password WHERE id = :id")
    suspend fun updateUser(id: Int, username: String, password: String)

    // 新增：统计用户总数
    @Query("SELECT COUNT(*) FROM users")
    suspend fun getUserCount(): Int
}