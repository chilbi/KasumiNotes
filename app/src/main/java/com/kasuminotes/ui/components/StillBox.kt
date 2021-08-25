package com.kasuminotes.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import coil.annotation.ExperimentalCoilApi
import com.kasuminotes.utils.UrlUtil

@ExperimentalCoilApi
@Composable
fun StillBox(
    unitId: Int,
    rarity: Int,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit = {}
) {
    Box(modifier) {
        PlaceImage(url = UrlUtil.getUnitStillUrl(unitId, rarity))

        content()
    }
}
