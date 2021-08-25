package com.kasuminotes.ui.app.equip

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import com.kasuminotes.R
import com.kasuminotes.data.EquipCraft
import com.kasuminotes.ui.components.BgBorderColumn
import com.kasuminotes.ui.components.ColumnLabel
import com.kasuminotes.ui.components.VerticalGrid
import com.kasuminotes.ui.components.VerticalGridCells
import com.kasuminotes.utils.UrlUtil

@ExperimentalCoilApi
@Composable
fun EquipCraftList(
    craftList: List<EquipCraft>,
    searchList: List<Int>,
    enabledSearch: Boolean,
    onSearchListChange: (materialId: Int) -> Unit
) {
    BgBorderColumn {
        ColumnLabel(stringResource(R.string.synthetic_material))

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