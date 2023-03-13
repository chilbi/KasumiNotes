package com.kasuminotes.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.selectedContainerColor(
    selected: Boolean,
    color: Color? = null,
    shape: Shape? = null
): Modifier = composed {
    if (selected) {
        this.background(
            color = color ?: MaterialTheme.colorScheme.tertiaryContainer,
            shape = shape ?: MaterialTheme.shapes.extraSmall
        )
    } else {
        this
    }
}

@Composable
fun BoxScope.SelectableItem(
    selected: Boolean,
    onClick: () -> Unit,
    size: Dp = 60.dp,
    margin: Dp = 2.dp,
    padding: Dp = 4.dp,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        Modifier
            .align(Alignment.Center)
            .size(size)
            .padding(margin)
            .selectedContainerColor(selected)
            .clickable(
                role = Role.Button,
                onClick = onClick
            )
            .padding(padding)
    ) {
        content()
    }
}
