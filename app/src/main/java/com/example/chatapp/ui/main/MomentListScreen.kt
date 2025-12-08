package com.example.chatapp.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatapp.data.MomentItem
import com.example.chatapp.ui.components.CircularAvatar

@Composable
fun MomentListScreen(
    momentList: List<MomentItem>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
            .padding(16.dp)
    ) {
        items(momentList) { moment ->
            MomentItemComponent(
                momentItem = moment
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MomentItemComponent(
    momentItem: MomentItem,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 使用新的圆形头像组件
                CircularAvatar(
                    imageUrl = momentItem.avatarUrl,
                    size = 40.dp
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = momentItem.username,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = momentItem.timestamp,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = momentItem.content,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
            
            momentItem.imageUrl?.let { imageUrl ->
                Spacer(modifier = Modifier.height(8.dp))
                // 预留图片显示位置
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Text(
                        text = "图片占位符",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
            
            if (momentItem.likes.isNotEmpty() || momentItem.comments.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                
                // 点赞列表
                if (momentItem.likes.isNotEmpty()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle, // 实际应用中应使用点赞图标
                            contentDescription = "Like",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = momentItem.likes.joinToString(", ") { it },
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                
                // 评论列表
                if (momentItem.comments.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Column {
                        momentItem.comments.forEach { comment ->
                            Row {
                                Text(
                                    text = "${comment.username}: ",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = comment.content,
                                    fontSize = 14.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                    }
                }
            }
        }
    }
}