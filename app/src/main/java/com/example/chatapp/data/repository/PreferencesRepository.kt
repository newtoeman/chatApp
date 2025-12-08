package com.example.chatapp.data.repository

import com.example.chatapp.json.AccountInfo

interface PreferencesRepository {
    fun saveOrUpdateAccount(phone: String, encryptedPassword: String?)
    fun getPasswordForAccount(phone: String): String
    fun clearPasswordForAccount(phone: String)
    fun getAccountsByLRU(): List<String>
    fun getAccountInfo(phone: String): AccountInfo?
    fun removeAccount(phone: String)
    fun clearAllCache()
}