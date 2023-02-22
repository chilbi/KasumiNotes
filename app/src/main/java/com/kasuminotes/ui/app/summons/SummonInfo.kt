package com.kasuminotes.ui.app.summons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kasuminotes.ui.components.PlaceImage
import com.kasuminotes.utils.UrlUtil

@Composable
fun SummonInfo(unitId: Int, unitName: String) {
    Row(Modifier.padding(4.dp)) {
        Box(Modifier.size(56.dp)) {
            PlaceImage(UrlUtil.summonIconUrl)
        }
        Column(
            modifier = Modifier
                .height(56.dp)
                .padding(start = 8.dp),
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                text = unitName,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = unitId.toString(),
                color = LocalContentColor.current.copy(0.6f),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
