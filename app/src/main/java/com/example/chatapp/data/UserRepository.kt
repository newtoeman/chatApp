package com.example.chatapp.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository(private val userDao: UserDao) {
    suspend fun login(phone: String, password: String): User? {
        return withContext(Dispatchers.IO) {
            userDao.login(phone, password)
        }
    }

    suspend fun getUserByPhone(phone: String): User? {
        return withContext(Dispatchers.IO) {
            userDao.getUserByPhone(phone)
        }
    }

    suspend fun registerUser(user: User) {
        return withContext(Dispatchers.IO) {
            userDao.insertUser(user)
        }
    }
}