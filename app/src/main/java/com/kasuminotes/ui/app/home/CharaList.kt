package com.kasuminotes.ui.app.home

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.kasuminotes.data.UserProfile
import com.kasuminotes.ui.app.state.CharaImageState

@Composable
fun CharaList(
    charaImageState: CharaImageState,
    derivedProfiles: List<UserProfile>,
    onCharaClick: (UserProfile) -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition()
    val layerAlpha = infiniteTransition.animateFloat(
        initialValue = 0.0f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 4500
                0.0f at 0 with FastOutSlowInEasing
                0.0f at 1750 with FastOutSlowInEasing
                1.0f at 2750 with FastOutSlowInEasing
                1.0f at 4500 with FastOutSlowInEasing
            },
            repeatMode = RepeatMode.Reverse
        )
    )

    LazyVerticalGrid(
        cells = GridCells.Fixed(charaImageState.cellCount),
        contentPadding = PaddingValues(4.dp)
    ) {
        items(derivedProfiles) { userProfile ->
            CharaItem(
                userProfile,
                charaImageState,
                layerAlpha,
                onCharaClick
            )
        }
    }
}
