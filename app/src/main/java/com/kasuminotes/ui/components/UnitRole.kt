package com.kasuminotes.ui.components

import androidx.annotation.DrawableRes
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
fun UnitRole(
    padding: Dp,
    unitRoleId: Int,
    roleSize: Dp,
    modifier: Modifier = Modifier
) {
    if (unitRoleId == 0 ) return
    Box(
        Modifier
            .padding(start = padding, top = padding)
            .wrapContentSize()
            .then(modifier)
    ) {
        @DrawableRes
        val resId = when (unitRoleId) {
            1 -> R.drawable.attacker
            2 -> R.drawable.breaker
            3 -> R.drawable.buffer
            4 -> R.drawable.debuffer
            5 -> R.drawable.booster
            6 -> R.drawable.healer
            7 -> R.drawable.tank
            else -> R.drawable.jammer
        }
        Image(
            painter = painterResource(resId),
            contentDescription = null,
            modifier = Modifier.size(roleSize)
        )
    }
}
