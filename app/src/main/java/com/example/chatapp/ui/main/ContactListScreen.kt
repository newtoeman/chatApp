package com.example.chatapp.ui.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatapp.data.ContactItem
import com.example.chatapp.ui.components.CircularAvatar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactListScreen(
    contactList: List<ContactItem>,
    onItemClick: (ContactItem) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        items(contactList) { contact ->
            ContactItemComponent(
                contactItem = contact,
                onItemClick = onItemClick
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactItemComponent(
    contactItem: ContactItem,
    onItemClick: (ContactItem) -> Unit,
    modifier: Modifier = Modifier
) {
    ListItem(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onItemClick(contactItem) }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        headlineContent = {
            Text(
                text = contactItem.username,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        },
        supportingContent = {
            contactItem.signature?.let { signature ->
                Text(
                    text = signature,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        leadingContent = {
            // 使用新的圆形头像组件
            CircularAvatar(
                imageUrl = contactItem.avatarUrl,
                size = 56.dp
            )
        }
    )
}