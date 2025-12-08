package com.example.chatapp.ui.login

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.chatapp.data.UserRepository
import com.example.chatapp.data.AppDatabase
import com.example.chatapp.data.repository.PreferencesRepositoryImpl
import com.example.chatapp.network.ApiService
import com.example.chatapp.network.MockApiService

class LoginViewModelFactory(
    private val application: Application,
    private val apiService: ApiService = MockApiService()
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            val database = AppDatabase.getDatabase(application)
            val repository = UserRepository(database.userDao())
            val preferencesRepository = PreferencesRepositoryImpl(application)
            return LoginViewModel(repository, preferencesRepository, apiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}