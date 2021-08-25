package com.kasuminotes.ui.components

import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.absoluteValue
import kotlin.math.min

@Immutable
class UnderlineStyle private constructor(@Suppress("unused") private val value: Int) {
    companion object {
        val Solid = UnderlineStyle(0)
        val Dotted = UnderlineStyle(1)
        val Dashed = UnderlineStyle(2)
    }

    override fun toString() = when (this) {
        Solid -> "Solid"
        Dotted -> "Dotted"
        Dashed -> "Dashed"
        else -> "Unknown"
    }
}

fun Modifier.underline(
    width: Dp,
    color: Color,
    style: UnderlineStyle = UnderlineStyle.Solid,
    startPointX: Dp = 0.dp,
): Modifier {
    return this.then(Modifier.drawWithCache {
        val sizeWidth = size.width.absoluteValue
        val sizeHeight = size.height.absoluteValue
        val strokeWidth = min(
            if (width == Dp.Hairline) 1f else width.toPx(), sizeHeight
        )
        val startX = startPointX.toPx()
        val y = sizeHeight - (strokeWidth / 2)
        val points: List<Offset> = if (style == UnderlineStyle.Solid) {
            listOf(
                Offset(startX, y),
                Offset(sizeWidth, y)
            )
        } else {
            val strokeLength: Float
            val gapLength: Float
            if (style == UnderlineStyle.Dotted) {
                strokeLength = strokeWidth
                gapLength = strokeWidth
            } else {
                strokeLength = strokeWidth * 4
                gapLength = strokeWidth * 2
            }
            val list = mutableListOf<Offset>()
            var start = startX
            var end: Float
            while (start < sizeWidth) {
                list.add(Offset(start, y))
                end = min(start + strokeLength, sizeWidth)
                list.add(Offset(end, y))
                start = end + gapLength
            }
            list
        }
        onDrawWithContent {
            drawContent()
            drawPoints(points, PointMode.Lines, color, strokeWidth)
        }
    })
}