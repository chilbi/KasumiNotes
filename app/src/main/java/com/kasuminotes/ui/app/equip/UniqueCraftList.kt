package com.kasuminotes.ui.app.equip

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kasuminotes.R
import com.kasuminotes.data.UniqueCraft
import com.kasuminotes.ui.components.FixedWidthLabel
import com.kasuminotes.ui.components.Container
import com.kasuminotes.utils.UrlUtil

@Composable
fun UniqueCraftList(
    craftList: List<UniqueCraft>,
    onEnhanceLevelChange: (Int) -> Unit
) {
    Container {
        FixedWidthLabel(
            text = stringResource(R.string.enhance_material)
        )
        Column(Modifier.padding(4.dp)) {
            craftList.forEach { craftItem ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (craftItem.heartId != -1) {
                        CraftItem(
                            itemIdList = listOf(craftItem.heartId),
                            imageUrl = UrlUtil::getEquipIconUrl,
                            consumeSum = craftItem.heartSum,
                            selected = false,
                            enabled = false,
                            onClick = {}
                        )
                        Spacer(Modifier.width(8.dp))
                    }
                    CraftItem(
                        itemIdList = craftItem.memoryIdList,
                        imageUrl = UrlUtil::getItemIconUrl,
                        consumeSum = craftItem.memorySum,
                        selected = false,
                        enabled = false,
                        onClick = {}
                    )
                    Spacer(Modifier.weight(1f))
                    Button(onClick = { onEnhanceLevelChange(craftItem.unlockLevel) }) {
                        Text(text = craftItem.unlockLevel.toString())
                    }
                }
            }
        }
    }
}