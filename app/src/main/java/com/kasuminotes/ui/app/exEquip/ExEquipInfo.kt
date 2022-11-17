package com.kasuminotes.ui.app.exEquip

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kasuminotes.ui.components.BgBorderColumn
import com.kasuminotes.ui.components.MultiLineText
import com.kasuminotes.ui.components.PlaceImage
import com.kasuminotes.utils.UrlUtil

@Composable
fun ExEquipInfo(
    exEquipId: Int,
    name: String,
    description: String
) {
    BgBorderColumn {
        Row(Modifier.padding(4.dp)) {
            Box(Modifier.size(56.dp)) {
                PlaceImage(UrlUtil.getExEquipUrl(exEquipId))
            }

            Column(Modifier.padding(start = 8.dp)) {
                Text(
                    text = name,
                    fontSize = 16.sp
                )

                MultiLineText(
                    text = description,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Start
                )
            }
        }
    }
}
