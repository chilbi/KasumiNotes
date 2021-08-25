package com.kasuminotes.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SizedBox(
    ratio: Float,
    isIcon: Boolean,
    content: @Composable BoxScope.() -> Unit
) {
    BoxWithConstraints(Modifier.imageSize(ratio)) {
        if (isIcon && maxWidth > 84.dp) {
            Box(
                modifier = Modifier
                    .size(76.dp)
                    .align(Alignment.Center),
                content = content
            )
        } else {
            Box(
                modifier = Modifier.padding(4.dp),
                content = content
            )
        }
    }
}
