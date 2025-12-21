package com.kasuminotes.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter

@Composable
fun BoxScope.PlaceImage(
    painter: Painter,
    loading: Boolean,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.surfaceVariant,
    shape: Shape = MaterialTheme.shapes.extraSmall,
    colorFilter: ColorFilter? = null
) {
    if (loading) {
        Spacer(
            Modifier
                .matchParentSize()
                .background(color, shape)
        )
    }

    Image(
        painter = painter,
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .then(modifier),
        contentScale = ContentScale.FillWidth,
        colorFilter = colorFilter
    )
}

@Composable
fun BoxScope.PlaceImage(
    url: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.surfaceVariant,
    shape: Shape = MaterialTheme.shapes.extraSmall,
    colorFilter: ColorFilter? = null
) {
    val painter = rememberAsyncImagePainter(url)
    val state = painter.state.collectAsState()
    val loading = state.value !is AsyncImagePainter.State.Success

    PlaceImage(
        painter,
        loading,
        modifier,
        color,
        shape,
        colorFilter
    )
}
