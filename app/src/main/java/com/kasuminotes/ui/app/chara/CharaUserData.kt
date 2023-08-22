package com.kasuminotes.ui.app.chara

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.kasuminotes.R
import com.kasuminotes.data.EquipData
import com.kasuminotes.data.MaxUserData
import com.kasuminotes.data.UniqueData
import com.kasuminotes.data.ExEquipSlot
import com.kasuminotes.data.UnitPromotion
import com.kasuminotes.data.UserData
import com.kasuminotes.ui.components.BadgedDiffBox
import com.kasuminotes.ui.components.DraggableImageIcon
import com.kasuminotes.ui.components.ImageIcon
import com.kasuminotes.ui.theme.GrayFilter
import com.kasuminotes.utils.UrlUtil

@Composable
fun CharaUserData(
    userData: UserData,
    originUserData: UserData,
    maxUserData: MaxUserData,
    maxRarity: Int,
    hasUnique1: Boolean,
    unitPromotion: UnitPromotion?,
    unique1Data: UniqueData?,
    unique2Data: UniqueData?,
    exEquipSlots: List<ExEquipSlot>,
    onEquipClick: (equipData: EquipData, slot: Int) -> Unit,
    onUniqueClick: (uniqueData: UniqueData, slot: Int) -> Unit,
    onExEquipClick: (ExEquipSlot) -> Unit,
    onEquipChange: (equip: Boolean, slot: Int) -> Unit,
    onUniqueChange: (equip: Boolean, slot: Int) -> Unit,
    onCharaLevelChange: (Int) -> Unit,
    onRarityChange: (Int) -> Unit,
    onUniqueLevelChange: (value: Int, slot: Int) -> Unit,
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
            hasUnique1,
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
                    Unique1Icon(
                        userData.unique1Level,
                        unique1Data,
                        onUniqueClick,
                        onUniqueChange
                    )
                }
                Box(Modifier.padding(vertical = 4.dp)) {
                    Unique2Icon(
                        userData.unique2Level,
                        originUserData.unique2Level,
                        unique2Data,
                        onUniqueClick,
                        onUniqueChange
                    )
                }
                Box(Modifier.padding(start = 24.dp)) {
                    ImageIcon(
                        url = UrlUtil.getItemIconUrl(41000),
                        onClick = { onLvLimitBreakChange(maxUserData.maxCharaLevel) },
                        colorFilter = if (userData.lvLimitBreak > 0) null else GrayFilter
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
            
            CharaExEquipSlots(
                userData,
                originUserData,
                exEquipSlots,
                onExEquipClick
            )
        }
    }
}

@Composable
private fun Unique1Icon(
    unique1Level: Int,
    unique1Data: UniqueData?,
    onClick: (UniqueData, Int) -> Unit,
    onChange: (Boolean, Int) -> Unit
) {
    if (unique1Data == null) {
        ImageIcon(
            painter = painterResource(R.drawable.unique_0),
            loading = false,
            enabled = false,
        )
    } else {
        val isEquipped = unique1Level > 0
        DraggableImageIcon(
            url = UrlUtil.getEquipIconUrl(unique1Data.equipmentId),
            onClick = { if (isEquipped) onClick(unique1Data, 1) else onChange(true, 1) },
            onDragged = { onChange(!isEquipped, 1) },
            colorFilter = if (isEquipped) null else GrayFilter
        )
    }
}

@Composable
private fun Unique2Icon(
    unique2Level: Int,
    originUnique2Level: Int,
    unique2Data: UniqueData?,
    onClick: (UniqueData, Int) -> Unit,
    onChange: (Boolean, Int) -> Unit
) {
    if (unique2Data == null) {
        ImageIcon(
            painter = painterResource(R.drawable.unique_0),
            loading = false,
            enabled = false,
        )
    } else {
        val isEquipped = unique2Level > -1
        BadgedDiffBox(unique2Level, originUnique2Level) {
            DraggableImageIcon(
                url = UrlUtil.getEquipIconUrl(unique2Data.equipmentId),
                onClick = { if (isEquipped) onClick(unique2Data, 2) else onChange(true, 2) },
                onDragged = { onChange(!isEquipped, 2) },
                colorFilter = if (isEquipped) null else GrayFilter
            ) {
                if (isEquipped) {
                    Row(
                        Modifier
                            .align(Alignment.BottomStart)
                            .fillMaxWidth()
                            .padding(2.dp)
                    ) {
                        repeat(5) { i ->
                            Image(
                                painter = painterResource(
                                    if (i < unique2Level) R.drawable.star_small_1
                                    else R.drawable.star_small_0
                                ),
                                contentDescription = null,
                                modifier = Modifier.size((8.8f).dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
