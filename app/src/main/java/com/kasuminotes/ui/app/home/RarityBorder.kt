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
import com.kasuminotes.ui.theme.RarityColors
import com.kasuminotes.ui.theme.Rounded8

fun Modifier.rarityBorder(rankRarity: Int): Modifier {
    return rarityBorder(RaritiesColors.getRarityColors(rankRarity))
}

fun Modifier.rarityBorder(
    rarityColors: RarityColors,
    outlineWidth: Dp = 0.75f.dp,
    borderWidth: Dp = 2.5f.dp,
    inlineWidth: Dp = 0.25f.dp,
): Modifier {
    return this
        .fillMaxSize()
        .border(
            border = BorderStroke(
                width = outlineWidth,
                brush = SolidColor(rarityColors.deepDark)
            ),
            shape = Rounded8,
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
            shape = Rounded8.copy(CornerSize(7.25f.dp))//8-outline
        )
        .padding(borderWidth)
        .border(
            border = BorderStroke(
                width = inlineWidth,
                brush = SolidColor(rarityColors.deepDark)
            ),
            shape = Rounded8.copy(CornerSize(4.75f.dp))//8-outline-border
        )
}
