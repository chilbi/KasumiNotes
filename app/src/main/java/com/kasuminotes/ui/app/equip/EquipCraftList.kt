package com.kasuminotes.ui.app.equip

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kasuminotes.R
import com.kasuminotes.data.EquipCraft
import com.kasuminotes.ui.components.FixedWidthLabel
import com.kasuminotes.ui.components.Container
import com.kasuminotes.ui.components.VerticalGrid
import com.kasuminotes.ui.components.VerticalGridCells
import com.kasuminotes.utils.UrlUtil

@Composable
fun EquipCraftList(
    craftList: List<EquipCraft>,
    searchList: List<Int>,
    enabledSearch: Boolean,
    onSearchListChange: (materialId: Int) -> Unit
) {
    Container {
        FixedWidthLabel(stringResource(R.string.synthetic_material))

        VerticalGrid(
            size = craftList.size,
            cells = VerticalGridCells.Adaptive(84.dp)
        ) { index ->
            val craftItem = craftList[index]
            CraftItem(
                imageUrl = UrlUtil.getEquipIconUrl(craftItem.equipmentId),
                consumeSum = craftItem.consumeSum,
                selected = searchList.contains(craftItem.equipmentId),
                enabled = enabledSearch,
                onClick = { onSearchListChange(craftItem.equipmentId) }
            )
        }
    }
}