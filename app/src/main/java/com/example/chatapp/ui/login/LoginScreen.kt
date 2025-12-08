package com.example.chatapp.ui.login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.chatapp.R

@Composable
fun LoginScreen(navController: NavController, viewModel: LoginViewModel) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val loginState by viewModel.loginState.collectAsState()
    
    LaunchedEffect(loginState) {
        when (loginState) {
            is LoginState.LoginSuccess -> {
                Toast.makeText(context, "登录成功", Toast.LENGTH_SHORT).show()
                // 跳转到主界面
                navController.navigate("main") {
                    // 清空登录页之前的所有页面（包括登录页）
                    popUpTo("login") { inclusive = true }
                    // 防止重复点击创建多个主页面
                    launchSingleTop = true
                }
            }
            is LoginState.RegisterSuccess -> {
                Toast.makeText(context, "注册成功", Toast.LENGTH_SHORT).show()
            }
            is LoginState.Error -> {
                Toast.makeText(context, (loginState as LoginState.Error).message, Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // 1. 背景图片层
        Image(
            painter = painterResource(id = R.drawable.login_bg), // 替换为你的背景图资源名
            contentDescription = "Login Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop // 适配屏幕（可选：FillBounds/Fit）
        )
        // 2. 遮罩层
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.28f)) // 30% 黑色遮罩
        )
        // 3. 主内容层
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(184.dp))

            Text(
                text = if (uiState.isLoginMode) "登录" else "注册",
                fontSize = 24.sp,
                color = Color(0xFF07C160) // WeChat green color
            )

            Spacer(modifier = Modifier.height(40.dp))

            if (uiState.isLoginMode) {
                // 登录模式：显示带下拉框的手机号输入
                OutlinedTextField(
                    value = uiState.selectedPhone,
                    onValueChange = { viewModel.updatePhone(it) },
                    label = { Text("手机号") },
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        IconButton(onClick = { viewModel.toggleDropdown() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "展开手机号列表"
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )
                
                // 使用官方的 DropdownMenu 组件
                DropdownMenu(
                    expanded = uiState.showDropdown,
                    onDismissRequest = { viewModel.hideDropdown() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    uiState.cachedPhoneNumbers.forEach { cachedPhone ->
                        if (cachedPhone != uiState.selectedPhone) {
                            DropdownMenuItem(
                                text = { Text(cachedPhone) },
                                onClick = {
                                    viewModel.selectPhoneFromDropdown(cachedPhone)
                                }
                            )
                        }
                    }
                }
            } else {
                // 注册模式：只显示普通手机号输入框，无下拉选项
                OutlinedTextField(
                    value = uiState.phone,
                    onValueChange = { viewModel.updatePhone(it) },
                    label = { Text("手机号") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (!uiState.isLoginMode) {
                OutlinedTextField(
                    value = uiState.username,
                    onValueChange = { viewModel.updateUsername(it) },
                    label = { Text("用户名") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            OutlinedTextField(
                value = uiState.password,
                onValueChange = { viewModel.updatePassword(it) },
                label = { Text("密码") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 只在登录模式下显示记住密码复选框
            if (uiState.isLoginMode) {
                // 记住密码复选框
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Checkbox(
                        checked = uiState.isRememberPassword,
                        onCheckedChange = { viewModel.updateRememberPassword(it) }
                    )
                    Text(
                        text = "记住密码",
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .clickable { viewModel.updateRememberPassword(!uiState.isRememberPassword) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 显示加载状态
            if (loginState is LoginState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(48.dp)
                )
            } else {
                Button(
                    onClick = {
                        if (uiState.isLoginMode) {
                            viewModel.login(uiState.phone, uiState.password)
                        } else {
                            viewModel.registerUser(uiState.phone, uiState.password, uiState.username)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF07C160))
                ) {
                    Text(
                        text = if (uiState.isLoginMode) "登录" else "注册",
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            TextButton(
                onClick = {
                    viewModel.updateIsLoginMode(!uiState.isLoginMode)
                }
            ) {
                Text(
                    text = if (uiState.isLoginMode) "还没有账号？点击注册" else "已有账号？点击登录",
                    color = Color(0xFF07C160),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
