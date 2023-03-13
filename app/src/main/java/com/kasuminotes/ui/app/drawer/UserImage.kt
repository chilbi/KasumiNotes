package com.kasuminotes.ui.app.drawer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import com.kasuminotes.ui.components.ImageSize
import com.kasuminotes.ui.components.PlaceImage
import com.kasuminotes.utils.UrlUtil

@Composable
fun UserImage(
    userId: Int,
    backgroundColor: Color
) {
    Box(ImageSize.StillModifier) {
        PlaceImage(
            url = UrlUtil.getUserStillUrl(userId),
            shape = RectangleShape
        )
        Box(
            Modifier
                .matchParentSize()
                .background(
                    brush = Brush.verticalGradient(
                        0.5f to Color.Transparent,
                        1.0f to backgroundColor
                    )
                )
        )
    }
}
