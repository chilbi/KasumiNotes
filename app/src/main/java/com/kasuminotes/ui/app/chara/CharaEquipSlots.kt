package com.kasuminotes.ui.app.chara

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.kasuminotes.R
import com.kasuminotes.data.EquipData
import com.kasuminotes.data.UnitPromotion
import com.kasuminotes.data.UserData
import com.kasuminotes.ui.components.BadgedDiffBox
import com.kasuminotes.ui.components.DraggableImageIcon
import com.kasuminotes.ui.components.ImageIcon
import com.kasuminotes.ui.theme.GrayFilter
import com.kasuminotes.utils.UrlUtil

@Composable
fun CharaEquipSlots(
    userData: UserData,
    originUserData: UserData,
    unitPromotion: UnitPromotion?,
    onEquipClick: (equipData: EquipData, slot: Int) -> Unit,
    onEquipChange: (equip: Boolean, slot: Int) -> Unit
) {
    val spacerWidth = 24.dp
    val spacerHeight = 4.dp
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row {
            EquipIcon(
                equipLevel = userData.equip1Level,
                originEquipLevel = originUserData.equip1Level,
                equipData = unitPromotion?.equipSlot1,
                onClick = { onEquipClick(it, 1) },
                onChange = { onEquipChange(it, 1) }
            )

            Spacer(Modifier.width(spacerWidth))

            EquipIcon(
                equipLevel = userData.equip2Level,
                originEquipLevel = originUserData.equip2Level,
                equipData = unitPromotion?.equipSlot2,
                onClick = { onEquipClick(it, 2) },
                onChange = { onEquipChange(it, 2) }
            )
        }

        Spacer(Modifier.height(spacerHeight))

        Row {
            EquipIcon(
                equipLevel = userData.equip3Level,
                originEquipLevel = originUserData.equip3Level,
                equipData = unitPromotion?.equipSlot3,
                onClick = { onEquipClick(it, 3) },
                onChange = { onEquipChange(it, 3) }
            )

            Spacer(Modifier.width(spacerWidth + 48.dp))

            EquipIcon(
                equipLevel = userData.equip4Level,
                originEquipLevel = originUserData.equip4Level,
                equipData = unitPromotion?.equipSlot4,
                onClick = { onEquipClick(it, 4) },
                onChange = { onEquipChange(it, 4) }
            )
        }

        Spacer(Modifier.height(spacerHeight))

        Row {
            EquipIcon(
                equipLevel = userData.equip5Level,
                originEquipLevel = originUserData.equip5Level,
                equipData = unitPromotion?.equipSlot5,
                onClick = { onEquipClick(it, 5) },
                onChange = { onEquipChange(it, 5) }
            )

            Spacer(Modifier.width(spacerWidth))

            EquipIcon(
                equipLevel = userData.equip6Level,
                originEquipLevel = originUserData.equip6Level,
                equipData = unitPromotion?.equipSlot6,
                onClick = { onEquipClick(it, 6) },
                onChange = { onEquipChange(it, 6) }
            )
        }
    }
}

@Composable
private fun EquipIcon(
    equipLevel: Int,
    originEquipLevel: Int,
    equipData: EquipData?,
    onClick: (EquipData) -> Unit,
    onChange: (equip: Boolean) -> Unit
) {
    if (equipData == null) {
        ImageIcon(
            painter = painterResource(R.drawable.equip_999999),
            loading = false,
            enabled = false,
        )
    } else {
        val isEquipped = equipLevel > -1

        BadgedDiffBox(equipLevel, originEquipLevel) {
            DraggableImageIcon(
                url = UrlUtil.getEquipIconUrl(equipData.equipmentId),
                onClick = { if (isEquipped) onClick(equipData) else onChange(true) },
                onDragged = { onChange(!isEquipped) },
                colorFilter = if (isEquipped) null else GrayFilter
            ) {
                val maxEnhanceLevel = equipData.maxEnhanceLevel

                if (isEquipped && maxEnhanceLevel > 0) {
                    Row(
                        Modifier
                            .align(Alignment.BottomStart)
                            .fillMaxWidth()
                            .padding(2.dp)
                    ) {
                        repeat(maxEnhanceLevel) { i ->
                            Image(
                                painter = painterResource(
                                    if (i < equipLevel) R.drawable.star_small_1
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
