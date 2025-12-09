package com.example.chatapp.utils

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class TokenManager(context: Context) {
    private val masterKey = MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val encryptedPrefs = EncryptedSharedPreferences.create(
        context,
        "token_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    companion object {
        private const val ACCESS_TOKEN_KEY = "access_token"
        private const val REFRESH_TOKEN_KEY = "refresh_token"
        private const val USER_ID_KEY = "user_id"
        private const val USER_PHONE_KEY = "user_phone"
        private const val USER_NAME_KEY = "user_name"
    }

    fun saveTokens(accessToken: String, refreshToken: String, userId: Int, phone: String, username: String) {
        with(encryptedPrefs.edit()) {
            putString(ACCESS_TOKEN_KEY, accessToken)
            putString(REFRESH_TOKEN_KEY, refreshToken)
            putInt(USER_ID_KEY, userId)
            putString(USER_PHONE_KEY, phone)
            putString(USER_NAME_KEY, username)
            apply()
        }
    }

    fun getAccessToken(): String? = encryptedPrefs.getString(ACCESS_TOKEN_KEY, null)
    fun getRefreshToken(): String? = encryptedPrefs.getString(REFRESH_TOKEN_KEY, null)
    fun getUserId(): Int = encryptedPrefs.getInt(USER_ID_KEY, -1)
    fun getUserPhone(): String? = encryptedPrefs.getString(USER_PHONE_KEY, null)
    fun getUserName(): String? = encryptedPrefs.getString(USER_NAME_KEY, null)

    fun hasValidTokens(): Boolean {
        return getAccessToken() != null && getRefreshToken() != null
    }

    fun clearTokens() {
        with(encryptedPrefs.edit()) {
            remove(ACCESS_TOKEN_KEY)
            remove(REFRESH_TOKEN_KEY)
            remove(USER_ID_KEY)
            remove(USER_PHONE_KEY)
            remove(USER_NAME_KEY)
            apply()
        }
    }
}