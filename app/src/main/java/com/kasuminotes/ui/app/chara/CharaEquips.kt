package com.kasuminotes.ui.app.chara

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kasuminotes.R
import com.kasuminotes.data.EquipData
import com.kasuminotes.data.UniqueData
import com.kasuminotes.data.UnitPromotion
import com.kasuminotes.ui.components.ImageIcon
import com.kasuminotes.ui.components.LabelContainer
import com.kasuminotes.ui.theme.RaritiesColors
import com.kasuminotes.ui.theme.UniqueColor
import com.kasuminotes.ui.theme.rankRarity
import com.kasuminotes.utils.UrlUtil

@Composable
fun CharaEquips(
    uniqueData: UniqueData?,
    promotions: List<UnitPromotion>,
    state: LazyGridState,
    onEquipClick: (EquipData) -> Unit,
    onUniqueClick: (UniqueData) -> Unit,
) {
    val maxPromotionLevel = promotions.size

    LazyVerticalGrid(
        columns = GridCells.Adaptive(336.dp),
        modifier = Modifier.fillMaxSize(),
        state = state,
        contentPadding = PaddingValues(4.dp),
    ) {
        item("unique") {
            UniqueEquipItem(uniqueData, onUniqueClick)
        }
        
        items(maxPromotionLevel, { it }) { index ->
            val unitPromotion = promotions[index]
            val promotionLevel = maxPromotionLevel - index

            EquipItem(unitPromotion, promotionLevel, onEquipClick)
        }
    }
}

@Composable
private fun EquipItem(
    unitPromotion: UnitPromotion,
    promotionLevel: Int,
    onEquipClick: (EquipData) -> Unit,
    color: Color = RaritiesColors.getRarityColors(promotionLevel.rankRarity).middle
) {
    LabelContainer(
        label = stringResource(R.string.rank) + " $promotionLevel",
        color = color
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            unitPromotion.equipSlots.forEach { equipData ->
                EquipIcon(equipData, onEquipClick)
            }
        }
    }
}

@Composable
private fun UniqueEquipItem(
    uniqueData: UniqueData?,
    onEquipClick: (UniqueData) -> Unit,
) {
    LabelContainer(
        label = stringResource(R.string.unique_equip),
        color = UniqueColor
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            UniqueEquipIcon(
                uniqueData,
                onEquipClick
            )

            val text: String
            val color: Color

            if (uniqueData == null) {
                text = stringResource(R.string.no_unique)
                color = MaterialTheme.colorScheme.onSurface.copy(0.35f)
            } else {
                text = uniqueData.equipmentName
                color = UniqueColor
            }

            Text(
                text = text,
                modifier = Modifier.padding(start = 8.dp),
                color = color,
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}

@Composable
private fun EquipIcon(
    equipData: EquipData?,
    onEquipClick: (EquipData) -> Unit
) {
    if (equipData == null) {
        ImageIcon(
            painter = painterResource(R.drawable.equip_999999),
            loading = false,
            enabled = false,
        )
    } else {
        ImageIcon(
            url = UrlUtil.getEquipIconUrl(equipData.equipmentId),
            onClick = { onEquipClick(equipData) }
        )
    }
}

@Composable
private fun UniqueEquipIcon(
    uniqueData: UniqueData?,
    onEquipClick: (UniqueData) -> Unit,
) {
    if (uniqueData == null) {
        ImageIcon(
            painter = painterResource(R.drawable.unique_0),
            loading = false,
            enabled = false,
        )
    } else {
        ImageIcon(
            url = UrlUtil.getEquipIconUrl(uniqueData.equipmentId),
            onClick = { onEquipClick(uniqueData) }
        )
    }
}
