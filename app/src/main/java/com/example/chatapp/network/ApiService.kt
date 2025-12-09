package com.example.chatapp.network

import com.example.chatapp.data.User
import com.example.chatapp.data.UserRepository

// 预留服务器接口
interface ApiService {
    // 用户登录
    suspend fun login(phone: String, password: String): Result<LoginResponse>
    
    // 用户注册
    suspend fun register(phone: String, password: String, username: String): Result<LoginResponse>
    
    // 刷新Token
    suspend fun refreshToken(refreshToken: String): Result<JwtToken>
    
    // 获取用户信息
    suspend fun getUserInfo(accessToken: String, userId: Int): Result<User>
    
    // 发送消息
    suspend fun sendMessage(accessToken: String, senderId: Int, receiverId: Int, message: String): Result<Boolean>
    
    // 获取消息列表
    suspend fun getMessages(accessToken: String, userId: Int): Result<List<Message>>
}

// 消息数据类
data class Message(
    val id: Int,
    val senderId: Int,
    val receiverId: Int,
    val content: String,
    val timestamp: Long,
    val messageType: MessageType = MessageType.TEXT
)

enum class MessageType {
    TEXT, IMAGE, VOICE, VIDEO
}

// 为了模拟服务器响应，创建一个模拟实现
class MockApiService() : ApiService {
    // 在实际应用中，这应该连接到真实的服务器
    // 为了演示目的，我们维护一个预设的用户列表

    private val mockUsers = listOf(
        User(1, "13800138000", "123456", "张三"),
        User(2, "13900139000", "654321", "李四"),
        User(3, "13700137000", "password", "王五")
    )

    override suspend fun login(phone: String, password: String): Result<LoginResponse> {
        // 在实际应用中，这里会调用服务器API
        // 模拟服务器验证：检查凭据是否匹配预设用户
        val user = mockUsers.find { it.phone == phone && it.password == password }
        return if (user != null) {
            // 模拟生成JWT Token
            val accessToken = "mock_access_token_${user.id}_${System.currentTimeMillis()}"
            val refreshToken = "mock_refresh_token_${user.id}_${System.currentTimeMillis()}"
            val userDto = UserDto(user.id, user.phone, user.username ?: "undefined")
            Result.success(LoginResponse(accessToken, refreshToken, userDto))
        } else {
            Result.failure(Exception("手机号或密码错误"))
        }
    }

    override suspend fun register(phone: String, password: String, username: String): Result<LoginResponse> {
        // 在实际应用中，这里会调用服务器API
        // 模拟服务器响应

        // 检查是否已存在相同手机号的用户
        if (mockUsers.any { it.phone == phone }) {
            return Result.failure(Exception("该手机号已被注册"))
        }
        val random = java.util.Random()
        val id = 100000 + random.nextInt(900000000)
        // 模拟注册成功
        val newUser = User(id, phone, password, username)
        val accessToken = "mock_access_token_${newUser.id}_${System.currentTimeMillis()}"
        val refreshToken = "mock_refresh_token_${newUser.id}_${System.currentTimeMillis()}"
        val userDto = UserDto(newUser.id, newUser.phone, newUser.username ?: "undefined")
        return Result.success(LoginResponse(accessToken, refreshToken, userDto))
    }

    override suspend fun refreshToken(refreshToken: String): Result<JwtToken> {
        // 在实际应用中，这里会调用服务器API验证刷新令牌
        // 模拟刷新Token
        if (refreshToken.startsWith("mock_refresh_token_")) {
            // 从refreshToken中提取用户ID
            val parts = refreshToken.split("_")
            val userId = if (parts.size > 3) parts[3] else "unknown"
            val newAccessToken = "mock_access_token_${userId}_${System.currentTimeMillis()}"
            val newRefreshToken = "mock_refresh_token_${userId}_${System.currentTimeMillis()}"
            return Result.success(JwtToken(newAccessToken, newRefreshToken))
        } else {
            return Result.failure(Exception("无效的刷新令牌"))
        }
    }

    override suspend fun getUserInfo(accessToken: String, userId: Int): Result<User> {
        // 在实际应用中，这里会使用JWT Token验证用户权限
        // 模拟验证Token有效性
        if (accessToken.startsWith("mock_access_token_")) {
            val user = mockUsers.find { it.id == userId }
            return if (user != null) {
                Result.success(user)
            } else {
                Result.failure(Exception("用户不存在"))
            }
        } else {
            return Result.failure(Exception("无效的访问令牌"))
        }
    }

    override suspend fun sendMessage(accessToken: String, senderId: Int, receiverId: Int, message: String): Result<Boolean> {
        // 在实际应用中，这里会使用JWT Token验证用户权限
        if (accessToken.startsWith("mock_access_token_")) {
            // 模拟发送消息
            return Result.success(true)
        } else {
            return Result.failure(Exception("无效的访问令牌"))
        }
    }

    override suspend fun getMessages(accessToken: String, userId: Int): Result<List<Message>> {
        // 在实际应用中，这里会使用JWT Token验证用户权限
        if (accessToken.startsWith("mock_access_token_")) {
            // 模拟获取消息列表
            return Result.success(emptyList())
        } else {
            return Result.failure(Exception("无效的访问令牌"))
        }
    }
}

