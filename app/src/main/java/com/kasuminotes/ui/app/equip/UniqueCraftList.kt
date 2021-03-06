package com.kasuminotes.ui.app.equip

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kasuminotes.R
import com.kasuminotes.data.UniqueCraft
import com.kasuminotes.ui.components.BgBorderColumn
import com.kasuminotes.ui.components.ColumnLabel
import com.kasuminotes.utils.UrlUtil

@Composable
fun UniqueCraftList(
    craftList: List<UniqueCraft>,
    onEnhanceLevelChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    BgBorderColumn(modifier, 0.dp) {
        ColumnLabel(stringResource(R.string.synthetic_material), 8.dp)

        Column(
            Modifier
                .verticalScroll(rememberScrollState())
                .padding(4.dp, 0.dp, 8.dp, 4.dp)
        ) {
            craftList.forEach { craftItem ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CraftItem(
                        imageUrl = UrlUtil.getEquipIconUrl(craftItem.heartId),
                        consumeSum = craftItem.heartSum,
                        selected = false,
                        enabled = false,
                        onClick = {}
                    )

                    Spacer(Modifier.width(8.dp))

                    CraftItem(
                        imageUrl = UrlUtil.getItemIconUrl(craftItem.memoryId),
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