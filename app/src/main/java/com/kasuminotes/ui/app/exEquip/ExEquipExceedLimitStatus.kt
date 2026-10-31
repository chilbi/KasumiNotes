package com.kasuminotes.ui.app.exEquip

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kasuminotes.R
import com.kasuminotes.data.Property
import com.kasuminotes.ui.components.FixedWidthLabel
import com.kasuminotes.ui.components.Infobar
import com.kasuminotes.ui.components.Rarities
import com.kasuminotes.ui.components.LabelContainer
import com.kasuminotes.ui.components.VerticalGrid
import com.kasuminotes.ui.components.VerticalGridCells

@Composable
fun ExEquipExceedLimitStatus(
    status: Property,
    maxRank: Int,
    rank: Int,
    valueDisplay: (index: Int, value: Double) -> String,
    onRankChange: (Int) -> Unit
) {
    LabelContainer(
        label = stringResource(R.string.exceed_limit),
        color = MaterialTheme.colorScheme.primary
    ) {
        val indices = status.nonzeroIndices
        VerticalGrid(indices.size, VerticalGridCells.Fixed(2)) { i ->
            val index = indices[i]
            val label = stringResource(Property.getStrRes(index))
            val value = status[index]

            Infobar(label, valueDisplay(index, value))
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            FixedWidthLabel(stringResource(R.string.exceed_limit_rank))
            Spacer(Modifier.width(8.dp))
            Rarities(
                highlightCount = maxRank,
                maxRarity = maxRank,
                rarity = rank,
                onRarityChange = onRankChange
            )
        }
    }
}
