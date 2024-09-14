package com.kasuminotes.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kasuminotes.R

@Composable
fun Rarities(
    highlightCount: Int,
    maxRarity: Int,
    rarity: Int,
    onRarityChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 20.dp,
    padding: PaddingValues = PaddingValues(4.dp)
) {
    val highlightIndex = maxRarity - highlightCount - 1
    Row(modifier) {
        repeat(maxRarity) { r ->
            Box(
                Modifier
                    .clickable(
                        role = Role.Button,
                        onClick = {
                            var newRarity = r + 1
                            if (newRarity == rarity) {
                                newRarity -= 1
                            }
                            onRarityChange(newRarity)
                        }
                    )
                    .padding(padding)
            ) {
                @DrawableRes
                val resId = if (r < rarity) {
                    if (r > highlightIndex) {
                        R.drawable.star_large_6
                    } else {
                        R.drawable.star_large_1
                    }
                } else {
                    R.drawable.star_large_0
                }

                Image(
                    painter = painterResource(resId),
                    contentDescription = null,
                    modifier = Modifier.size(size)
                )
            }
        }
    }
}
