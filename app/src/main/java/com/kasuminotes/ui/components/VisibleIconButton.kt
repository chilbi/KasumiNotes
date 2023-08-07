package com.kasuminotes.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun VisibleIconButton(
    visible: Boolean,
    imageVector: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    visibleTint: Color = MaterialTheme.colorScheme.secondary,
    invisibleTint: Color = LocalContentColor.current.copy(0.5f)
) {
    IconButton(onClick, modifier) {
        Icon(
            imageVector = imageVector,
            contentDescription = null,
            tint = if (visible) visibleTint else invisibleTint
        )
    }
}
