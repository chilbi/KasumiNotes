package com.kasuminotes.ui.app.chara

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import com.kasuminotes.R
import com.kasuminotes.data.EquipData
import com.kasuminotes.data.UniqueData
import com.kasuminotes.data.UnitPromotion
import com.kasuminotes.data.UserData
import com.kasuminotes.ui.components.DiffBadgedBox
import com.kasuminotes.ui.components.ImageIcon
import com.kasuminotes.ui.theme.GrayFilter
import com.kasuminotes.utils.UrlUtil
import kotlin.math.absoluteValue

@ExperimentalCoilApi
@ExperimentalMaterialApi
@Composable
fun CharaEquipSlots(
    userData: UserData,
    originUserData: UserData,
    unitPromotion: UnitPromotion?,
    uniqueData: UniqueData?,
    onEquipClick: (equipData: EquipData, slot: Int) -> Unit,
    onUniqueClick: (UniqueData) -> Unit,
    onEquipChange: (equip: Boolean, slot: Int) -> Unit,
    onUniqueChange: (equip: Boolean) -> Unit
) {
    val spacerWidth = 24.dp
    val spacerHeight = 4.dp
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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

            Spacer(Modifier.width(spacerWidth))

            UniqueEquipIcon(
                uniqueLevel = userData.uniqueLevel,
                uniqueData = uniqueData,
                onClick = onUniqueClick,
                onChange = onUniqueChange
            )

            Spacer(Modifier.width(spacerWidth))

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

@ExperimentalCoilApi
@ExperimentalMaterialApi
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

        DiffBadgedBox(equipLevel, originEquipLevel) {
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

@ExperimentalCoilApi
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


@ExperimentalCoilApi
@Composable
private fun DraggableImageIcon(
    url: String,
    onClick: (() -> Unit)? = null,
    onDragged: (() -> Unit)? = null,
    enabled: Boolean = true,
    size: Dp = 48.dp,
    shape: Shape = MaterialTheme.shapes.small,
    colorFilter: ColorFilter? = null,
    content: @Composable BoxScope.() -> Unit = {}
) {
    var offsetX by remember { mutableStateOf(0f) }

    val sizePx = with(LocalDensity.current) { size.toPx() }

    ImageIcon(
        url,
        Modifier.draggable(
            state = rememberDraggableState { delta ->
                offsetX += delta
            },
            orientation = Orientation.Horizontal,
            enabled = enabled,
            onDragStopped = {
                if (offsetX.absoluteValue > sizePx) {
                    onDragged?.invoke()
                }
            }
        ),
        onClick,
        enabled,
        size,
        shape,
        colorFilter,
        content
    )
}
