package com.kasuminotes.ui.components

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.Badge
import androidx.compose.material.BadgedBox
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.kasuminotes.ui.theme.Negative
import com.kasuminotes.ui.theme.Positive

@Composable
fun DiffBadgedBox(
    value: Int,
    originValue: Int,
    content: @Composable BoxScope.() -> Unit
) {
    val diff = value - originValue
    val backgroundColor: Color
    val badgeContent: @Composable (RowScope.() -> Unit)?
    when {
        diff == 0 -> {
            backgroundColor = Color.Transparent
            badgeContent = null
        }
        diff > 0 -> {
            backgroundColor = Positive
            badgeContent = { Text(text = "+$diff", fontSize = 8.sp) }
        }
        else -> {
            backgroundColor = Negative
            badgeContent = { Text(diff.toString(), fontSize = 8.sp) }
        }
    }

    BadgedBox(
        badge = {
            Badge(
                backgroundColor = backgroundColor,
                contentColor = Color.White,
                content = badgeContent
            )
        },
        content = content
    )
}
