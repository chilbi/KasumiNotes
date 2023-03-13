package com.kasuminotes.ui.components

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun BadgedDiffBox(
    value: Int,
    originValue: Int,
    content: @Composable BoxScope.() -> Unit
) {
    val diff = value - originValue
    val containerColor: Color
    val badgeContent: @Composable (RowScope.() -> Unit)?
    when {
        diff == 0 -> {
            containerColor = Color.Transparent
            badgeContent = null
        }
        diff > 0 -> {
            containerColor = MaterialTheme.colorScheme.primaryContainer
            badgeContent = { Text("+$diff") }
        }
        else -> {
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
            badgeContent = { Text(diff.toString()) }
        }
    }

    BadgedBox(
        badge = {
            Badge(
                containerColor = containerColor,
                content = badgeContent
            )
        },
        content = content
    )
}
