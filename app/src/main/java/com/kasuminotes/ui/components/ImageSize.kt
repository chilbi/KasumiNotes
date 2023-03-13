package com.kasuminotes.ui.components

import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.LayoutModifier
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.unit.Constraints
import kotlin.math.roundToInt

class ImageSizeModifier(private val ratio: Float) : LayoutModifier {
    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints
    ): MeasureResult {
        val width = constraints.maxWidth
        val height = (width * ratio).roundToInt()

        val placeable = measurable.measure(
            Constraints(width, width, height, height)
        )

        return layout(placeable.width, placeable.height) {
            placeable.placeRelative(0, 0)
        }
    }

    override fun equals(other: Any?) =
        other is ImageSizeModifier && ratio == other.ratio

    override fun hashCode() = ratio.hashCode()
}

object ImageSize {
    val IconModifier by lazy { ImageSizeModifier(1f) }
    val PlateModifier by lazy { ImageSizeModifier(0.5f) }
    val StillModifier by lazy { ImageSizeModifier(0.5625f) }
}

fun Modifier.imageSize(ratio: Float): Modifier {
    return this.then(
        when (ratio) {
            1f -> ImageSize.IconModifier
            0.5f -> ImageSize.PlateModifier
            0.5625f -> ImageSize.StillModifier
            else -> ImageSizeModifier(ratio)
        }
    )
}
