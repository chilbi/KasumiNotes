package com.kasuminotes.ui.app.exEquip

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.kasuminotes.R
import com.kasuminotes.action.toNumStr
import com.kasuminotes.data.Property
import com.kasuminotes.ui.components.Infobar
import com.kasuminotes.ui.components.UnderlineLabelColumn
import com.kasuminotes.ui.components.VerticalGrid
import com.kasuminotes.ui.components.VerticalGridCells
import kotlin.math.roundToInt

@Composable
fun ExEquipProperty(
    percentProperty: Property,
    baseProperty: Property
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
                value = if (value < 100.0) {
                    value.roundToInt().toString()
                } else {
                    "${(value / 100).toNumStr()}%(+${(baseProperty[index] * value / 10000).roundToInt()})"
                }
            )
        }
    }
}
