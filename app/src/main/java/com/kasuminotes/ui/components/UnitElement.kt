package com.kasuminotes.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import com.kasuminotes.R

@Composable
fun UnitElement(
    padding: Dp,
    talentId: Int,
    elementSize: Dp,
    modifier: Modifier = Modifier
) {
    if (talentId == 0 ) return
    Box(
        Modifier
            .padding(start = padding, top = padding)
            .wrapContentSize()
            .then(modifier)
    ) {
        val id = when (talentId) {
            1 -> R.drawable.fire
            2 -> R.drawable.water
            3 -> R.drawable.wind
            4 -> R.drawable.light
            else -> R.drawable.dark
        }
        Image(
            painter = painterResource(id),
            contentDescription = null,
            modifier = Modifier.size(elementSize)
        )
    }
}