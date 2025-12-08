package com.example.chatapp.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.chatapp.json.AccountCache
import com.example.chatapp.json.AccountInfo
import com.google.gson.Gson

class PreferencesManager private constructor(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.applicationContext.getSharedPreferences("chat_app_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val ACCOUNT_CACHE_KEY = "account_cache" // JSON缓存的键
    private val MAX_CACHE_COUNT = 10 // 最大缓存账号数量（可配置）

    //
    fun saveOrUpdateAccount(phone: String, encryptedPassword: String?){
        val accountCache = getAccountCache()
        val existingAccount = accountCache.accounts.find { it.phone == phone }
        if(existingAccount != null){
            existingAccount.password = encryptedPassword
            existingAccount.lastUsedTime = System.currentTimeMillis()
        } else {
            val newAccount = AccountInfo(
                phone = phone,
                password = encryptedPassword
            )
            accountCache.accounts.add(newAccount)
            // 超过最大数量时，删除最久未使用的账号
            if(accountCache.accounts.size > MAX_CACHE_COUNT){
                val leastUsedAccount = accountCache.accounts.minByOrNull { it.lastUsedTime }
                leastUsedAccount?.let { accountCache.accounts.remove(it) }
            }
        }
        val json = gson.toJson(accountCache)
        sharedPreferences.edit().putString(ACCOUNT_CACHE_KEY,json).apply()
    }
    // 获取账号的密码（需解密）
    fun getPasswordForAccount(phone: String): String {
        val accountCache = getAccountCache()
        return accountCache.accounts.find { it.phone == phone }?.password ?: ""
    }

    // 清除账号的密码
    fun clearPasswordForAccount(phone: String) {
        val accountCache = getAccountCache()
        accountCache.accounts.find { it.phone == phone }?.let {
            it.password = null
            it.lastUsedTime = System.currentTimeMillis()
        }
        val json = gson.toJson(accountCache)
        sharedPreferences.edit().putString(ACCOUNT_CACHE_KEY, json).apply()
    }

    fun getAccountsByLRU(): List<String>{
        val accountCache = getAccountCache()
        return accountCache.accounts.sortedByDescending { it.lastUsedTime }
            .map { it.phone } // 提取手机号列表
    }

    fun getAccountInfo(phone: String): AccountInfo? {
        val accountCache = getAccountCache()
        return accountCache.accounts.find { it.phone == phone }
    }

    fun removeAccount(phone: String){
        val accountCache = getAccountCache()
        accountCache.accounts.removeIf { it.phone == phone }
        val json = gson.toJson(accountCache)
        sharedPreferences.edit().putString(ACCOUNT_CACHE_KEY,json).apply()
    }
    // 清除所有缓存数据
    fun clearAllCache() {
        sharedPreferences.edit().remove(ACCOUNT_CACHE_KEY).apply()
    }

    // 读取账号缓存（反序列化JSON）
    private fun getAccountCache(): AccountCache {
        val json = sharedPreferences.getString(ACCOUNT_CACHE_KEY, null)
        return if (json != null) {
            gson.fromJson(json, AccountCache::class.java)
        } else {
            AccountCache() // 无缓存时返回空容器
        }
    }


    companion object {

        @Volatile
        private var INSTANCE: PreferencesManager? = null

        fun getInstance(context: Context): PreferencesManager {
            return INSTANCE ?: synchronized(this) {
                val instance = PreferencesManager(context)
                INSTANCE = instance
                instance
            }
        }
    }
}