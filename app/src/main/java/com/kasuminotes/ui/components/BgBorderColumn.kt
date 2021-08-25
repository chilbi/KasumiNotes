package com.kasuminotes.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kasuminotes.ui.theme.BorderColor
import com.kasuminotes.ui.theme.Rounded8

fun Modifier.bgBorder(
    isLight: Boolean,
    color: Color = BorderColor,
    shape: Shape = Rounded8,
): Modifier = if (isLight) this.border(1.dp, color, shape)
else this.background(color, shape)

@Composable
fun BgBorderColumn(
    modifier: Modifier = Modifier,
    padding: Dp = 4.dp,
    color: Color = BorderColor,
    shape: Shape = Rounded8,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .padding(4.dp)
            .bgBorder(MaterialTheme.colors.isLight, color, shape)
            .padding(padding),
        content = content
    )
}
