package com.kasuminotes.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color

@Composable
fun SyncIcon(
    enable: Boolean,
    tint: Color = MaterialTheme.colorScheme.primary
) {
    if (enable) {
        val infiniteTransition = rememberInfiniteTransition(label = "SyncInfiniteTransition")
        val degrees by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = -360f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 1000,
                    easing = LinearEasing
                )
            ),
            label = "SyncFloatAnimation"
        )
        Icon(
            imageVector = Icons.Filled.Sync,
            contentDescription = null,
            modifier = Modifier.rotate(degrees),
            tint = tint
        )
    } else {
        Icon(Icons.Filled.Sync, null)
    }
}