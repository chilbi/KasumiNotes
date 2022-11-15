package com.kasuminotes.ui.app.chara

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.kasuminotes.R
import com.kasuminotes.data.EquipData
import com.kasuminotes.data.MaxUserData
import com.kasuminotes.data.UniqueData
import com.kasuminotes.data.ExEquipSlot
import com.kasuminotes.data.UnitPromotion
import com.kasuminotes.data.UserData
import com.kasuminotes.ui.components.DraggableImageIcon
import com.kasuminotes.ui.components.ImageIcon
import com.kasuminotes.ui.theme.GrayFilter
import com.kasuminotes.ui.theme.ShadowColor
import com.kasuminotes.utils.UrlUtil

@Composable
fun CharaUserData(
    userData: UserData,
    originUserData: UserData,
    maxUserData: MaxUserData,
    maxRarity: Int,
    hasUnique: Boolean,
    unitPromotion: UnitPromotion?,
    uniqueData: UniqueData?,
    exEquipSlots: List<ExEquipSlot>,
    onEquipClick: (equipData: EquipData, slot: Int) -> Unit,
    onUniqueClick: (UniqueData) -> Unit,
    onEquipChange: (equip: Boolean, slot: Int) -> Unit,
    onUniqueChange: (equip: Boolean) -> Unit,
    onCharaLevelChange: (Int) -> Unit,
    onRarityChange: (Int) -> Unit,
    onUniqueLevelChange: (Int) -> Unit,
    onLoveLevelChange: (Int) -> Unit,
    onPromotionLevelChange: (Int) -> Unit,
    onSkillLevelChange: (value: Int, labelText: String) -> Unit,
    onLvLimitBreakChange: (maxCharaLevel: Int) -> Unit
) {
    Column(Modifier.padding(8.dp)) {
        CharaStatus(
            userData,
            originUserData,
            maxUserData,
            maxRarity,
            hasUnique,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            onCharaLevelChange,
            onRarityChange,
            onUniqueLevelChange,
            onLoveLevelChange,
            onPromotionLevelChange,
            onSkillLevelChange
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.Start) {
                Box(Modifier.padding(start = 24.dp)) {
                    ImageIcon(
                        url = UrlUtil.getItemIconUrl(41000),
                        onClick = { onLvLimitBreakChange(maxUserData.maxCharaLevel) },
                        colorFilter = if (userData.lvLimitBreak > 0) null else GrayFilter
                    )
                }

                Spacer(Modifier.height(56.dp))

                Box(Modifier.padding(start = 24.dp)) {
                    UniqueEquipIcon(
                        uniqueLevel = userData.uniqueLevel,
                        uniqueData = uniqueData,
                        onClick = onUniqueClick,
                        onChange = onUniqueChange
                    )
                }
            }

            CharaEquipSlots(
                userData,
                originUserData,
                unitPromotion,
                onEquipClick,
                onEquipChange
            )
            
            Column(horizontalAlignment = Alignment.End) {
                val hasExEquipSlots = exEquipSlots.isNotEmpty()
                Box(Modifier.padding(end = 24.dp)) {
                    ExEquipIcon(if (hasExEquipSlots) exEquipSlots[0] else null)
                }
                Box(Modifier.padding(vertical = 4.dp)) {
                    ExEquipIcon(if (hasExEquipSlots) exEquipSlots[1] else null)
                }
                Box(Modifier.padding(end = 24.dp)) {
                    ExEquipIcon(if (hasExEquipSlots) exEquipSlots[2] else null)
                }
            }
        }
    }
}

@Composable
private fun UniqueEquipIcon(
    uniqueLevel: Int,
    uniqueData: UniqueData?,
    onClick: (UniqueData) -> Unit,
    onChange: (equip: Boolean) -> Unit
) {
    if (uniqueData == null) {
        ImageIcon(
            painter = painterResource(R.drawable.unique_0),
            loading = false,
            enabled = false,
        )
    } else {
        val isEquipped = uniqueLevel > 0

        DraggableImageIcon(
            url = UrlUtil.getEquipIconUrl(uniqueData.equipmentId),
            onClick = { if (isEquipped) onClick(uniqueData) else onChange(true) },
            onDragged = { onChange(!isEquipped) },
            colorFilter = if (isEquipped) null else GrayFilter
        )/* {
            if (isEquipped) {
                Box(
                    Modifier
                        .align(Alignment.BottomEnd)
                        .wrapContentSize()
                        .background(Color(0x66000000), MaterialTheme.shapes.small)
                        .padding(horizontal = 4.dp)
                ) {
                    Text(
                        text = uniqueLevel.toString(),
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }*/
    }
}

@Composable
private fun ExEquipIcon(
    exEquipSlot: ExEquipSlot?,
) {
    if (exEquipSlot == null) {
        ImageIcon(
            painter = painterResource(R.drawable.item_00000),
            loading = false,
            enabled = false
        )
    } else {
        ImageIcon(
            painter = painterResource(R.drawable.item_00000),
            loading = false
        ) {
            Image(
                painter = rememberAsyncImagePainter(UrlUtil.getExEquipCategoryUrl(exEquipSlot.category)),
                contentDescription = null,
                modifier = Modifier
                    .size(18.dp)
                    .align(Alignment.TopEnd)
            )
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = null,
                modifier = Modifier
                    .size(28.dp)
                    .align(Alignment.Center),
                tint = ShadowColor
            )
        }
    }
}
