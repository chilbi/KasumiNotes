package com.kasuminotes.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter

@Composable
fun ImageIcon(
    painter: Painter,
    loading: Boolean,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    enabled: Boolean = true,
    size: Dp = 48.dp,
    shape: Shape = MaterialTheme.shapes.small,
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
    shape: Shape = MaterialTheme.shapes.small,
    colorFilter: ColorFilter? = null,
    content: @Composable BoxScope.() -> Unit = {}
) {
    val painter = rememberImagePainter(url)
    val loading = painter.state !is ImagePainter.State.Success

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
