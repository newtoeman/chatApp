package com.example.chatapp.data.repository

import android.content.Context
import com.example.chatapp.json.AccountInfo
import com.example.chatapp.utils.PreferencesManager

class PreferencesRepositoryImpl(context: Context) : PreferencesRepository {
    private val preferencesManager = PreferencesManager.getInstance(context)

    override fun saveOrUpdateAccount(phone: String, encryptedPassword: String?) {
        preferencesManager.saveOrUpdateAccount(phone, encryptedPassword)
    }

    override fun getPasswordForAccount(phone: String): String {
        return preferencesManager.getPasswordForAccount(phone)
    }

    override fun clearPasswordForAccount(phone: String) {
        preferencesManager.clearPasswordForAccount(phone)
    }

    override fun getAccountsByLRU(): List<String> {
        return preferencesManager.getAccountsByLRU()
    }

    override fun getAccountInfo(phone: String): AccountInfo? {
        return preferencesManager.getAccountInfo(phone)
    }

    override fun removeAccount(phone: String) {
        preferencesManager.removeAccount(phone)
    }

    override fun clearAllCache() {
        preferencesManager.clearAllCache()
    }
}