package com.kasuminotes.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kasuminotes.ui.theme.BorderColor
import com.kasuminotes.ui.theme.Rounded8

fun Modifier.bgBorder(
    isLight: Boolean,
    color: Color = BorderColor,
    shape: Shape = Rounded8
): Modifier = if (isLight) this.border(1.dp, color, shape)
else this.background(color, shape)

private fun Modifier.click(shape: Shape, onClick: (() -> Unit)?) =
    if (onClick == null) this else this.clip(shape).clickable(onClick = onClick)

@Composable
fun BgBorderColumn(
    modifier: Modifier = Modifier,
    padding: Dp = 4.dp,
    color: Color = BorderColor,
    shape: Shape = Rounded8,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .padding(4.dp)
            .bgBorder(MaterialTheme.colors.isLight, color, shape)
            .click(shape, onClick)
            .padding(padding),
        content = content
    )
}
