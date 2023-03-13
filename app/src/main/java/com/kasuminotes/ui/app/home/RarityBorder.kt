package com.kasuminotes.ui.app.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kasuminotes.ui.theme.RaritiesColors
import com.kasuminotes.ui.theme.UnitImageShape

private val outlineWidth: Dp = 0.8f.dp
private val borderWidth: Dp = 2.7f.dp
private val inlineWidth: Dp = 0.5f.dp
private val borderShape = UnitImageShape.copy(CornerSize(8.dp - outlineWidth))
private val inlineShape = UnitImageShape.copy(CornerSize(8.dp - outlineWidth - borderWidth))

fun Modifier.rarityBorder(rankRarity: Int): Modifier {
    val rarityColors = RaritiesColors.getRarityColors(rankRarity)
    return this
        .fillMaxSize()
        .border(
            border = BorderStroke(
                width = outlineWidth,
                brush = SolidColor(rarityColors.deepDark)
            ),
            shape = UnitImageShape,
        )
        .padding(outlineWidth)
        .border(
            border = BorderStroke(
                width = borderWidth,
                brush = Brush.verticalGradient(
                    0.0f to rarityColors.middle,
                    0.5f to rarityColors.highLight,
                    0.5f to rarityColors.dark,
                    1.0f to rarityColors.light
                )
            ),
            shape = borderShape
        )
        .padding(borderWidth)
        .border(
            border = BorderStroke(
                width = inlineWidth,
                brush = SolidColor(rarityColors.deepDark)
            ),
            shape = inlineShape
        )
}
