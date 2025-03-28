package com.kasuminotes.ui.app.home

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kasuminotes.common.OrderBy
import com.kasuminotes.data.UserProfile
import com.kasuminotes.state.CharaImageState
import com.kasuminotes.ui.components.imageSize

@Composable
fun CharaList(
    charaImageState: CharaImageState,
    derivedProfiles: List<UserProfile>,
    orderBy: OrderBy,
    onCharaClick: (UserProfile) -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition("CharaListInfiniteTransition")
    val layerAlpha = infiniteTransition.animateFloat(
        initialValue = 0.0f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 4500
                0.0f at 0 using FastOutSlowInEasing
                0.0f at 1750 using FastOutSlowInEasing
                1.0f at 2750 using FastOutSlowInEasing
                1.0f at 4500 using FastOutSlowInEasing
            },
            repeatMode = RepeatMode.Reverse
        ),
        label = "CharaListFloatAnimation"
    )

    LazyVerticalGrid(
        columns = charaImageState.gridCells,
        contentPadding = PaddingValues(4.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        items(derivedProfiles) { userProfile ->
            CharaItem(
                layerAlpha,
                orderBy,
                userProfile,
                charaImageState,
                onCharaClick
            )
        }
        if (derivedProfiles.isNotEmpty()) {
            item("box1") {
                Box(Modifier.imageSize(1f))
            }
            item("box2") {
                Box(Modifier.imageSize(1f))
            }
        }
    }
}
