package com.kasuminotes.ui.app.exEquip

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Badge
import androidx.compose.material.BadgedBox
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    equippedExEquipId: Int,
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
            val backgroundColor: Color
            val badgeContent: @Composable (RowScope.() -> Unit)?
            if (exEquipId == equippedExEquipId) {
                backgroundColor = MaterialTheme.colors.secondary
                badgeContent = { Text(text = "E", fontSize = 10.sp) }
            } else {
                backgroundColor = Color.Transparent
                badgeContent = null
            }
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
                BadgedBox(
                    badge = {
                        Badge(
                            backgroundColor = backgroundColor,
                            contentColor = Color.White,
                            content = badgeContent
                        )
                    }
                ) {
                    ImageIcon(
                        url = UrlUtil.getExEquipUrl(exEquipId),
                        onClick = { onExEquipClick(exEquipId) }
                    )
                }
            }
        }
    }
}
