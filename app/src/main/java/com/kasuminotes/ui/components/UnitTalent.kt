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
import com.kasuminotes.common.Talent

@Composable
fun UnitTalent(
    padding: Dp,
    talentId: Int,
    talentSize: Dp,
    modifier: Modifier = Modifier
) {
    if (talentId == 0 ) return
    Box(
        Modifier
            .padding(start = padding, top = padding)
            .wrapContentSize()
            .then(modifier)
    ) {
        val talent = Talent.fromId(talentId)
        Image(
            painter = painterResource(talent.imgId),
            contentDescription = null,
            modifier = Modifier.size(talentSize)
        )
    }
}
