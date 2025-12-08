package com.example.chatapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.chatapp.R

@Composable
fun CircularAvatar(
    modifier: Modifier = Modifier,
    imageUrl: String? = null,
    placeholderRes: Int? = null,
    size: Dp = 56.dp,
    contentDescription: String? = null,
    isOnline: Boolean = false

) {
    Box(
        modifier = modifier.size(size)
    ) {
        if (imageUrl != null) {
            // 使用 Coil 加载网络图片
            val painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .placeholder(placeholderRes ?: R.drawable.default_avatar)
                    .error(placeholderRes ?: R.drawable.default_avatar)
                    .build()
            )

            Image(
                painter = painter,
                contentDescription = contentDescription,
                modifier = Modifier
                    .size(size)
                    .clip(CircleShape)
            )
        } else {
            // 使用占位图片
            Box(
                modifier = Modifier
                    .size(size)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.default_avatar),
                    contentDescription = contentDescription,
                    modifier = Modifier.size(size)
                )
            }
        }

        // 在线状态指示器
        if (isOnline) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(Color.Green)
            )
        }
    }
}