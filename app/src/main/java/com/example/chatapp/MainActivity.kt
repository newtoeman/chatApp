package com.example.chatapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.chatapp.ui.MainScreen
import com.example.chatapp.ui.login.LoginScreen
import com.example.chatapp.ui.login.LoginViewModelFactory
import com.example.chatapp.ui.theme.ChatAppTheme
import com.example.chatapp.utils.TokenValidator
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // 安装启动画面并设置保持条件
        val splashScreen = installSplashScreen()
        var currentStartDestination by mutableStateOf("login") // 使用Compose状态
        var isReady = false // 标记是否已完成Token验证

        // 设置启动屏保持显示的条件
        splashScreen.setKeepOnScreenCondition {
            !isReady
        }
        
        super.onCreate(savedInstanceState)
        
        // 验证Token
        lifecycleScope.launch {
            val tokenValidator = TokenValidator(applicationContext)
            val isValid = tokenValidator.validateTokenOnStartup()
            currentStartDestination = if (isValid) "main" else "login"
            isReady = true // 标记已准备就绪，启动屏可以消失
        }
        
        setContent {
            ChatAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val loginFactory = LoginViewModelFactory(application)
                    
                    NavHost(
                        navController = navController,
                        startDestination = currentStartDestination  // 使用Compose状态，会自动重组
                    ) {
                        composable("login") {
                            LoginScreen(
                                navController = navController,
                                viewModel = androidx.lifecycle.viewmodel.compose.viewModel(factory = loginFactory)
                            )
                        }
                        composable("main") {
                            MainScreen(
                                navController = navController
                            )
                        }
                    }
                }
            }
        }
    }
}