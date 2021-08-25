package com.kasuminotes.ui.app.state

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ViewAgenda
import androidx.compose.material.icons.filled.ViewComfy
import androidx.compose.material.icons.filled.ViewDay
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kasuminotes.R
import com.kasuminotes.common.ImageVariant
import com.kasuminotes.utils.UrlUtil

@Immutable
class CharaImageState(
    private val imageVariant: ImageVariant
) {
    val getImageUrl: (Int, Int) -> String
    val ratio: Float
    val cellCount: Int
    val vector: ImageVector
    val isIcon: Boolean
    val isPlate: Boolean
    val positionSize: Dp
    val starSize: Dp
    val uniqueSize: Dp
    @DrawableRes var star0Id: Int
    @DrawableRes var star1Id: Int
    @DrawableRes var star6Id: Int
    @DrawableRes var uniqueId: Int

    val nextVariant: ImageVariant
        get() = when (imageVariant) {
            ImageVariant.Icon -> ImageVariant.Plate
            ImageVariant.Plate -> ImageVariant.Still
            ImageVariant.Still -> ImageVariant.Icon
        }

    init {
        when (imageVariant) {
            ImageVariant.Icon -> {
                getImageUrl = UrlUtil::getUnitIconUrl
                ratio = 1f
                cellCount = 4
                vector = Icons.Filled.ViewComfy
                isIcon = true
                isPlate = false
                positionSize = 16.dp
                starSize = 12.dp
                uniqueSize = 12.dp
                star0Id = R.drawable.star_small_0
                star1Id = R.drawable.star_small_1
                star6Id = R.drawable.star_small_6
                uniqueId = R.drawable.unique_small
            }
            ImageVariant.Plate -> {
                getImageUrl = UrlUtil::getUnitPlateUrl
                ratio = 0.5f
                cellCount = 2
                vector = Icons.Filled.ViewAgenda
                isIcon = false
                isPlate = true
                positionSize = 20.dp
                starSize = 14.dp
                uniqueSize = 20.dp
                star0Id = R.drawable.star_large_0
                star1Id = R.drawable.star_large_1
                star6Id = R.drawable.star_large_6
                uniqueId = R.drawable.unique_large
            }
            ImageVariant.Still -> {
                getImageUrl = UrlUtil::getUnitStillUrl
                ratio = 0.5625f
                cellCount = 1
                vector = Icons.Filled.ViewDay
                isIcon = false
                isPlate = false
                positionSize = 26.dp
                starSize = 20.dp
                uniqueSize = 26.dp
                star0Id = R.drawable.star_large_0
                star1Id = R.drawable.star_large_1
                star6Id = R.drawable.star_large_6
                uniqueId = R.drawable.unique_large
            }
        }
    }
}
