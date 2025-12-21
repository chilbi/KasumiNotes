package com.kasuminotes.ui.app.drawer

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.kasuminotes.ui.app.DefaultUserId
import com.kasuminotes.utils.UrlUtil

@Composable
fun UserHeader(
    userId: Int,
    userName: String,
    backgroundColor: Color,
    onImageClick: () -> Unit,
    onLogOut: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 16.dp, end = 16.dp)
            .height(56.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(UrlUtil.getUserIconUrl(userId)),
            contentDescription = null,
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .clickable(
                    enabled = userId != DefaultUserId,
                    onClick = onImageClick
                )
                .border(
                    border = BorderStroke(
                        width = 2.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.tertiary
                            )
                        )
                    ),
                    shape = CircleShape
                )
                .border(
                    border = BorderStroke(
                        width = 4.dp,
                        brush = SolidColor(backgroundColor)
                    ),
                    shape = CircleShape
                )
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .height(56.dp)
                .padding(start = 16.dp),
            verticalArrangement = Arrangement.SpaceAround,
        ) {
            Text(
                text = userName,
                color = MaterialTheme.colorScheme.primary,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = "uid:$userId",
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.75f),
                style = MaterialTheme.typography.bodyMedium
            )
        }

        IconButton(
            onClick = onLogOut,
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            Icon(Icons.AutoMirrored.Filled.Logout, null)
        }
    }
}
