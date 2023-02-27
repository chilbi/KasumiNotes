package com.kasuminotes.ui.app.exEquip

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kasuminotes.ui.components.MultiLineText
import com.kasuminotes.ui.components.PlaceImage
import com.kasuminotes.ui.components.UnderlineLabelColumn
import com.kasuminotes.utils.UrlUtil

@Composable
fun ExEquipInfo(
    exEquipId: Int,
    name: String,
    description: String
) {
    UnderlineLabelColumn(
        label = name,
        color = MaterialTheme.colors.primary
    ) {
        Row(Modifier.padding(4.dp)) {
            Box(Modifier.size(56.dp)) {
                PlaceImage(UrlUtil.getExEquipUrl(exEquipId))
            }

            Column(Modifier.padding(start = 8.dp)) {
                MultiLineText(
                    text = description,
                    textAlign = TextAlign.Start
                )
            }
        }
    }
}
