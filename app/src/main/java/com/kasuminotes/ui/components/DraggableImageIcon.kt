package com.kasuminotes.ui.components

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.absoluteValue

@Composable
fun DraggableImageIcon(
    url: String,
    onClick: (() -> Unit)? = null,
    onDragged: (() -> Unit)? = null,
    enabled: Boolean = true,
    size: Dp = 48.dp,
    shape: Shape = MaterialTheme.shapes.small,
    colorFilter: ColorFilter? = null,
    content: @Composable BoxScope.() -> Unit = {}
) {
    var offsetX by remember { mutableStateOf(0f) }

    val sizePx = with(LocalDensity.current) { size.toPx() }

    ImageIcon(
        url,
        Modifier.draggable(
            state = rememberDraggableState { delta ->
                offsetX += delta
            },
            orientation = Orientation.Horizontal,
            enabled = enabled,
            onDragStopped = {
                if (offsetX.absoluteValue > sizePx) {
                    onDragged?.invoke()
                }
            }
        ),
        onClick,
        enabled,
        size,
        shape,
        colorFilter,
        content
    )
}