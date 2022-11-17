package com.kasuminotes.ui.app.exEquip

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kasuminotes.R
import com.kasuminotes.ui.components.ImageIcon
import com.kasuminotes.ui.components.UnderlineLabelColumn
import com.kasuminotes.ui.components.VerticalGrid
import com.kasuminotes.ui.components.VerticalGridCells
import com.kasuminotes.ui.components.selectedBg
import com.kasuminotes.ui.theme.selected
import com.kasuminotes.utils.UrlUtil

@Composable
fun EquippableExList(
    equippableExList: List<Int>,
    selectedExEquipId: Int,
    onExEquipClick: (exEquipId: Int) -> Unit
) {
    UnderlineLabelColumn(
        label = stringResource(R.string.same_category_equip),
        color = MaterialTheme.colors.secondary
    ) {
        VerticalGrid(
            size = equippableExList.size,
            cells = VerticalGridCells.Adaptive(56.dp)
        ) { index ->
            val exEquipId = equippableExList[index]
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
                    .selectedBg(
                        exEquipId == selectedExEquipId,
                        MaterialTheme.colors.selected,
                        MaterialTheme.shapes.small
                    )
                    .padding(start = 4.dp, top = 4.dp, end = 4.dp)
            ) {
                ImageIcon(
                    url = UrlUtil.getExEquipUrl(exEquipId),
                    onClick = { onExEquipClick(exEquipId) }
                )
            }
        }
    }
}
