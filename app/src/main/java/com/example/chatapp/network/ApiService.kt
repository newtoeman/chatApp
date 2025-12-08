package com.example.chatapp.network

import com.example.chatapp.data.User
import com.example.chatapp.data.UserRepository


// 预留服务器接口
interface ApiService {
    // 用户登录
    suspend fun login(phone: String, password: String): Result<User>
    
    // 用户注册
    suspend fun register(phone: String, password: String, username: String): Result<User>
    
    // 获取用户信息
    suspend fun getUserInfo(userId: Int): Result<User>
    
    // 发送消息
    suspend fun sendMessage(senderId: Int, receiverId: Int, message: String): Result<Boolean>
    
    // 获取消息列表
    suspend fun getMessages(userId: Int): Result<List<Message>>
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

    override suspend fun login(phone: String, password: String): Result<User> {
        // 在实际应用中，这里会调用服务器API
        // 模拟服务器验证：检查凭据是否匹配预设用户
        val user = mockUsers.find { it.phone == phone && it.password == password }
        return if (user != null) {
            Result.success(user)
        } else {
            Result.failure(Exception("手机号或密码错误"))
        }
    }

    override suspend fun register(phone: String, password: String, username: String): Result<User> {
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
        return Result.success(newUser)
    }

    override suspend fun getUserInfo(userId: Int): Result<User> {
        // 这里是预留的获取用户信息接口实现
        val user = mockUsers.find { it.id == userId }
        return if (user != null) {
            Result.success(user)
        } else {
            Result.failure(Exception("用户不存在"))
        }
    }

    override suspend fun sendMessage(senderId: Int, receiverId: Int, message: String): Result<Boolean> {
        // 这里是预留的发送消息接口实现
        return Result.success(true)
    }

    override suspend fun getMessages(userId: Int): Result<List<Message>> {
        // 这里是预留的获取消息列表接口实现
        return Result.success(emptyList())
    }
}

