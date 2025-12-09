package com.example.chatapp.data

class UserRepository(private val userDao: UserDao) {
    suspend fun saveUser(user: User) {
        try {
            userDao.insertUser(user)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun getUserByPhone(phone: String): User? {
        return try {
            userDao.getUserByPhone(phone)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getUserById(id: Int): User? {
        return try {
            userDao.getUserById(id)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getAllUsers(): List<User> {
        return try {
            userDao.getAllUsers()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
    
    suspend fun updateUser(id: Int, username: String, password: String) {
        try {
            userDao.updateUser(id, username, password)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}