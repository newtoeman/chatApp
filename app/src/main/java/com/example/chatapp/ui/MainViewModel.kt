package com.example.chatapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.data.ChatItem
import com.example.chatapp.data.ContactItem
import com.example.chatapp.data.MomentComment
import com.example.chatapp.data.MomentItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _currentTab = MutableStateFlow(0) // 0: 消息, 1: 联系人, 2: 朋友圈
    val currentTab: StateFlow<Int> = _currentTab

    private val _chatList = MutableStateFlow<List<ChatItem>>(emptyList())
    val chatList: StateFlow<List<ChatItem>> = _chatList

    private val _contactList = MutableStateFlow<List<ContactItem>>(emptyList())
    val contactList: StateFlow<List<ContactItem>> = _contactList

    private val _momentList = MutableStateFlow<List<MomentItem>>(emptyList())
    val momentList: StateFlow<List<MomentItem>> = _momentList

    init {
        loadSampleData()
    }

    private fun loadSampleData() {
        // 加载示例聊天数据
        val sampleChats = listOf(
            ChatItem(
                id = 1,
                username = "张三",
                lastMessage = "今天天气不错",
                timestamp = "12:30",
                unreadCount = 2
            ),
            ChatItem(
                id = 2,
                username = "李四",
                lastMessage = "项目进度如何？",
                timestamp = "昨天",
                unreadCount = 0
            ),
            ChatItem(
                id = 3,
                username = "王五",
                lastMessage = "会议改到下午三点",
                timestamp = "周一",
                unreadCount = 5
            ),
            ChatItem(
                id = 4,
                username = "赵六",
                lastMessage = "收到，谢谢",
                timestamp = "周日",
                unreadCount = 0
            ),
            ChatItem(
                id = 5,
                username = "测试群",
                lastMessage = "张三: 大家下午好",
                timestamp = "周六",
                unreadCount = 0
            )
        )

        // 加载示例联系人数据
        val sampleContacts = listOf(
            ContactItem(
                id = 1,
                username = "微信团队",
                signature = "微信，是一个生活方式"
            ),
            ContactItem(
                id = 2,
                username = "张三",
                signature = "好好学习，天天向上"
            ),
            ContactItem(
                id = 3,
                username = "李四",
                signature = "工作加油"
            ),
            ContactItem(
                id = 4,
                username = "王五",
                signature = "热爱生活"
            ),
            ContactItem(
                id = 5,
                username = "赵六",
                signature = "快乐每一天"
            )
        )

        // 加载示例朋友圈数据
        val sampleMoments = listOf(
            MomentItem(
                id = 1,
                username = "张三",
                content = "今天天气真好，出去走走",
                timestamp = "2小时前",
                likes = listOf("李四", "王五")
            ),
            MomentItem(
                id = 2,
                username = "李四",
                content = "新项目启动，加油干！",
                timestamp = "5小时前",
                likes = listOf("张三", "赵六"),
                comments = listOf(
                    MomentComment(1, "张三", "加油！", "3小时前"),
                    MomentComment(2, "王五", "祝你成功", "2小时前")
                )
            ),
            MomentItem(
                id = 3,
                username = "王五",
                content = "分享一张美景照片",
                timestamp = "昨天",
                likes = listOf("张三", "李四", "赵六")
            )
        )

        _chatList.value = sampleChats
        _contactList.value = sampleContacts
        _momentList.value = sampleMoments
    }

    fun setCurrentTab(index: Int) {
        viewModelScope.launch {
            _currentTab.value = index
        }
    }
}