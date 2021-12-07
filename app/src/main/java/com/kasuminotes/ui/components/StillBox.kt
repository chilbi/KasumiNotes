package com.kasuminotes.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import com.kasuminotes.utils.UrlUtil

@Composable
fun StillBox(
    unitId: Int,
    rarity: Int,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit = {}
) {
    Box(modifier) {
        PlaceImage(
            url = UrlUtil.getUnitStillUrl(unitId, rarity),
            shape = RectangleShape
        )

        content()
    }
}
