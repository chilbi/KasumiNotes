package com.kasuminotes.ui.app.exEquip

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kasuminotes.ui.components.MultiLineText
import com.kasuminotes.ui.components.PlaceImage
import com.kasuminotes.ui.components.LabelContainer
import com.kasuminotes.utils.UrlUtil

@Composable
fun ExEquipInfo(
    exEquipId: Int,
    name: String,
    description: String
) {
    LabelContainer(
        label = name,
        color = MaterialTheme.colorScheme.primary
    ) {
        Row(Modifier.padding(4.dp)) {
            Box(Modifier.size(56.dp)) {
                PlaceImage(UrlUtil.getExEquipUrl(exEquipId))
            }

            Column(Modifier.padding(start = 8.dp)) {
                MultiLineText(description)
            }
        }
    }
}
