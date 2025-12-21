package com.kasuminotes.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import kotlin.math.absoluteValue

@Composable
fun ImageIcon(
    painter: Painter,
    loading: Boolean,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    enabled: Boolean = true,
    size: Dp = 48.dp,
    shape: Shape = MaterialTheme.shapes.extraSmall,
    colorFilter: ColorFilter? = null,
    content: @Composable BoxScope.() -> Unit = {}
) {
    Box(
        Modifier
            .size(size)
            .clip(shape)
            .clickable(
                enabled = enabled,
                role = Role.Button,
                onClick = { onClick?.invoke() }
            )
            .then(modifier)
    ) {
        PlaceImage(
            painter = painter,
            loading = loading,
            shape = RectangleShape,
            colorFilter = colorFilter
        )
        content()
    }
}

@Composable
fun ImageIcon(
    url: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    enabled: Boolean = true,
    size: Dp = 48.dp,
    shape: Shape = MaterialTheme.shapes.extraSmall,
    colorFilter: ColorFilter? = null,
    content: @Composable BoxScope.() -> Unit = {}
) {
    val painter = rememberAsyncImagePainter(url)
    val state = painter.state.collectAsState()
    val loading = state.value !is AsyncImagePainter.State.Success

    ImageIcon(
        painter = painter,
        loading = loading,
        modifier,
        onClick,
        enabled,
        size,
        shape,
        colorFilter,
        content
    )
}

@Composable
fun DraggableImageIcon(
    url: String,
    onClick: (() -> Unit)? = null,
    onDragged: (() -> Unit)? = null,
    enabled: Boolean = true,
    size: Dp = 48.dp,
    shape: Shape = MaterialTheme.shapes.extraSmall,
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
