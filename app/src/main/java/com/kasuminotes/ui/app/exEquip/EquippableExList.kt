package com.kasuminotes.ui.app.exEquip

import androidx.compose.material3.Badge
import androidx.compose.material3.BadgeDefaults
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kasuminotes.R
import com.kasuminotes.ui.components.LabelContainer
import com.kasuminotes.ui.components.PlaceImage
import com.kasuminotes.ui.components.SelectableItem
import com.kasuminotes.ui.components.VerticalGrid
import com.kasuminotes.ui.components.VerticalGridCells
import com.kasuminotes.utils.UrlUtil

@Composable
fun EquippableExList(
    equippableExList: List<Int>,
    selectedExEquipId: Int,
    equippedExEquipId: Int,
    onExEquipClick: (exEquipId: Int) -> Unit
) {
    LabelContainer(
        label = stringResource(R.string.same_category_equip),
        color = MaterialTheme.colorScheme.primary
    ) {
        if (equippableExList.isNotEmpty()) {
            VerticalGrid(
                size = equippableExList.size,
                cells = VerticalGridCells.Adaptive(60.dp)
            ) { index ->
                val exEquipId = equippableExList[index]
                val containerColor: Color
                val text: String
                if (exEquipId == equippedExEquipId) {
                    containerColor = BadgeDefaults.containerColor
                    text = "E"
                } else {
                    containerColor = Color.Transparent
                    text = ""
                }
                SelectableItem(
                    selected = exEquipId == selectedExEquipId,
                    onClick = { onExEquipClick(exEquipId) }
                ) {
                    BadgedBox({ Badge(containerColor = containerColor) { Text(text) } }) {
                        PlaceImage(UrlUtil.getExEquipUrl(exEquipId))
                    }
                }
            }
        }
    }
}
