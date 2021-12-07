package com.kasuminotes.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.kasuminotes.ui.theme.place

@Composable
fun BoxScope.PlaceImage(
    painter: Painter,
    loading: Boolean,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.place,
    shape: Shape = MaterialTheme.shapes.small,
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
    color: Color = MaterialTheme.colors.place,
    shape: Shape = MaterialTheme.shapes.small,
    colorFilter: ColorFilter? = null
) {
    val painter = rememberImagePainter(url)
    val loading = painter.state !is ImagePainter.State.Success

    PlaceImage(
        painter,
        loading,
        modifier,
        color,
        shape,
        colorFilter
    )
}
