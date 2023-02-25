package com.kasuminotes.ui.app.chara

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kasuminotes.R
import com.kasuminotes.data.EquipData
import com.kasuminotes.data.UniqueData
import com.kasuminotes.data.UnitPromotion
import com.kasuminotes.ui.components.ImageIcon
import com.kasuminotes.ui.components.UnderlineLabelColumn
import com.kasuminotes.ui.theme.RaritiesColors
import com.kasuminotes.ui.theme.UniqueColor
import com.kasuminotes.ui.theme.rankRarity
import com.kasuminotes.utils.UrlUtil

@Composable
fun CharaEquips(
    uniqueData: UniqueData?,
    promotions: List<UnitPromotion>,
    state: LazyListState,
    onEquipClick: (EquipData) -> Unit,
    onUniqueClick: (UniqueData) -> Unit,
) {
    val maxPromotionLevel = promotions.size

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 4.dp, end = 4.dp, bottom = 4.dp),
        state = state
    ) {
        item {
            Spacer(Modifier.height(4.dp))
        }
        
        item {
            UniqueEquipItem(uniqueData, onUniqueClick)
        }
        
        items(maxPromotionLevel, { i -> i }) { index ->
            val unitPromotion = promotions[index]
            val promotionLevel = maxPromotionLevel - index

            EquipItem(
                unitPromotion,
                promotionLevel,
                onEquipClick = onEquipClick
            )
        }
    }
}

@Composable
private fun EquipItem(
    unitPromotion: UnitPromotion,
    promotionLevel: Int,
    color: Color = RaritiesColors.getRarityColors(promotionLevel.rankRarity).middle,
    onEquipClick: (EquipData) -> Unit
) {
    UnderlineLabelColumn(
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
                EquipIcon(
                    equipData,
                    onEquipClick = onEquipClick
                )
            }
        }
    }
}

@Composable
private fun UniqueEquipItem(
    uniqueData: UniqueData?,
    onEquipClick: (UniqueData) -> Unit,
) {
    UnderlineLabelColumn(
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
                color = MaterialTheme.colors.onSurface.copy(0.35f)
            } else {
                text = uniqueData.equipmentName
                color = UniqueColor
            }

            Text(
                text = text,
                modifier = Modifier.padding(start = 8.dp),
                color = color,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
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
