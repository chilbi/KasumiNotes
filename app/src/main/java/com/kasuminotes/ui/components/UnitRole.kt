package com.kasuminotes.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import com.kasuminotes.common.Role

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
            .then(modifier)
    ) {
        val role = Role.fromId(unitRoleId)
        Image(
            painter = painterResource(role.imgId),
            contentDescription = null,
            modifier = Modifier.size(roleSize)
        )
    }
}
