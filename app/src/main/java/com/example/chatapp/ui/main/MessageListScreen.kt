package com.example.chatapp.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatapp.data.ChatItem
import com.example.chatapp.ui.components.CircularAvatar
import com.example.chatapp.utils.TimeUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageListScreen(
    chatList: List<ChatItem>,
    onItemClick: (ChatItem) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        items(chatList) { chat ->
            ChatItemComponent(
                chatItem = chat,
                onItemClick = onItemClick
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatItemComponent(
    chatItem: ChatItem,
    onItemClick: (ChatItem) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    
    Box {
        ListItem(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .combinedClickable(
                    onClick = { onItemClick(chatItem) },
                    onLongClick = { expanded = true }
                ),
            headlineContent = {
                Text(
                    text = chatItem.username,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            supportingContent = {
                Text(
                    text = chatItem.lastMessage,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            trailingContent = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = TimeUtils.formatTimestamp(chatItem.timestamp),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
//                    IconButton(
//                        onClick = { expanded = true },
//                        modifier = Modifier.size(24.dp) // 设置图标大小
//                    ) {
//                        Icon(
//                            imageVector = Icons.Default.MoreVert,
//                            contentDescription = "更多选项",
//                            modifier = Modifier.size(20.dp) // 设置图标大小
//                        )
//                    }
                    if (chatItem.unreadCount > 0) {
                        // 优化未读消息徽章样式
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.error, // 使用错误颜色作为未读消息徽章
                                    shape = CircleShape // 使用圆形而不是圆角矩形
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (chatItem.unreadCount > 99) "99+" else chatItem.unreadCount.toString(),
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            },
            leadingContent = {
                // 使用新的圆形头像组件
                CircularAvatar(
                    imageUrl = chatItem.avatarUrl,
                    placeholderRes = com.example.chatapp.R.drawable.default_avatar,
                    size = 60.dp,
                    isOnline = chatItem.isOnline
                )
            }
        )
        
        // 长按菜单
        DropdownMenu(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.padding(vertical = 0.dp),
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("置顶聊天") },
                onClick = { /* TODO: 实现置顶功能 */ expanded = false }
            )
            DropdownMenuItem(
                text = { Text("标记已读") },
                onClick = { /* TODO: 实现标记已读功能 */ expanded = false }
            )
            DropdownMenuItem(
                text = { Text("删除聊天") },
                onClick = { /* TODO: 实现删除聊天功能 */ expanded = false }
            )
            DropdownMenuItem(
                text = { Text("消息免打扰") },
                onClick = { /* TODO: 实现免打扰功能 */ expanded = false }
            )
        }
    }
}