package com.kasuminotes.ui.app.exEquip

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kasuminotes.R
import com.kasuminotes.action.toNumStr
import com.kasuminotes.data.Property
import com.kasuminotes.ui.components.ColumnLabel
import com.kasuminotes.ui.components.Infobar
import com.kasuminotes.ui.components.Rarities
import com.kasuminotes.ui.components.UnderlineLabelColumn
import com.kasuminotes.ui.components.VerticalGrid
import com.kasuminotes.ui.components.VerticalGridCells
import kotlin.math.roundToInt

@Composable
fun ExEquipProperty(
    percentProperty: Property,
    baseProperty: Property,
    maxEnhanceLevel: Int,
    enhanceLevel: Int,
    onEnhanceLevelChange: (Int) -> Unit
) {
    UnderlineLabelColumn(
        label = stringResource(R.string.equip_property),
        color = MaterialTheme.colors.primary
    ) {
        val indices = percentProperty.nonzeroIndices
        VerticalGrid(indices.size, VerticalGridCells.Fixed(2)) { i ->
            val index = indices[i]
            val label = stringResource(Property.getStrRes(index))
            val value = percentProperty[index]

            Infobar(
                label = label,
                value = if (index < 7) {
                    "${(value / 100).toNumStr()}%(+${(baseProperty[index] * value / 10000).roundToInt()})"
                } else {
                    value.roundToInt().toString()
                }
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            ColumnLabel(stringResource(R.string.promotion_level))
            Spacer(Modifier.width(8.dp))
            Rarities(
                highlightCount = if (maxEnhanceLevel > 4) 2 else if (maxEnhanceLevel > 3) 1 else 0,
                maxRarity = maxEnhanceLevel,
                rarity = enhanceLevel,
                onRarityChange = onEnhanceLevelChange
            )
        }
    }
}
