package com.example.chatapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.chatapp.ui.main.ContactListScreen
import com.example.chatapp.ui.main.MessageListScreen
import com.example.chatapp.ui.main.MomentListScreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController, viewModel: MainViewModel = viewModel()) {
    val currentTab by viewModel.currentTab.collectAsState()
    val chatList by viewModel.chatList.collectAsState()
    val contactList by viewModel.contactList.collectAsState()
    val momentList by viewModel.momentList.collectAsState()

    // 添加状态管理 + 号下拉框的展开状态
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            // 添加一个带有搜索和+号下拉菜单的顶部工具栏
            TopAppBar(
                title = { Text("微信", fontWeight = FontWeight.Bold, fontSize = 20.sp) },
                actions = {
                    // 搜索图标
                    IconButton(onClick = { /* 处理搜索点击 */ }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "搜索"
                        )
                    }
                    
                    // + 号图标，点击展开下拉菜单
                    Box {
                        IconButton(onClick = { expanded = true }) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "更多操作"
                            )
                        }
                        
                        // 下拉菜单
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("发起群聊") },
                                onClick = { 
                                    // 处理发起群聊操作
                                    expanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("添加朋友") },
                                onClick = { 
                                    // 处理添加朋友操作
                                    expanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("扫一扫") },
                                onClick = { 
                                    // 处理扫一扫操作
                                    expanded = false
                                }
                            )
                        }
                    }
                    
                    // 登出按钮移到菜单中或者保留
                    IconButton(onClick = { 
                        // 返回登录界面
                        navController.navigate("login") {
                            // 清空到导航图起始页，兼容所有栈结构
                            popUpTo(navController.graph.startDestinationId) {
                                inclusive = true // 清空起始页（避免残留）
                            }
                            launchSingleTop = true // 登录页仅一个实例
                        }
                    }) {
                        Text("登出", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(
                currentTab = currentTab,
                onTabSelected = { index ->
                    viewModel.setCurrentTab(index)
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                0 -> MessageListScreen(
                    chatList = chatList,
                    onItemClick = { chatItem ->
                        // 处理聊天项点击事件
                    }
                )
                1 -> ContactListScreen(
                    contactList = contactList,
                    onItemClick = { contactItem ->
                        // 处理联系人项点击事件
                    }
                )
                2 -> MomentListScreen(
                    momentList = momentList
                )
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    currentTab: Int,
    onTabSelected: (Int) -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Chat,
                    contentDescription = "消息"
                )
            },
            label = {
                Text("消息")
            },
            selected = currentTab == 0,
            onClick = { onTabSelected(0) }
        )
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Contacts,
                    contentDescription = "联系人"
                )
            },
            label = {
                Text("联系人")
            },
            selected = currentTab == 1,
            onClick = { onTabSelected(1) }
        )
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.PhotoCamera,
                    contentDescription = "朋友圈"
                )
            },
            label = {
                Text("朋友圈")
            },
            selected = currentTab == 2,
            onClick = { onTabSelected(2) }
        )
    }
}

