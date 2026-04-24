package com.kasuminotes.ui.app.enhance

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kasuminotes.R
import com.kasuminotes.common.Role
import com.kasuminotes.data.RoleMasteryData
import com.kasuminotes.data.RoleSlot
import com.kasuminotes.data.RoleSlotData
import com.kasuminotes.state.EnhanceState
import com.kasuminotes.ui.components.AdaptiveWidthLabel
import com.kasuminotes.ui.components.Container
import com.kasuminotes.ui.components.Infobar
import com.kasuminotes.ui.components.PlaceImage
import com.kasuminotes.ui.components.Rarities
import com.kasuminotes.ui.components.SliderPlus
import com.kasuminotes.ui.components.UnderlineLabel
import com.kasuminotes.utils.UrlUtil
import kotlin.collections.component1
import kotlin.collections.component2

@Composable
fun RoleMasteryView(
    enhanceState: EnhanceState
) {
    val roleMasteryDataMap = enhanceState.roleMasteryDataMap!!
    val roleEnhanceMap = enhanceState.roleEnhanceMap!!

    Column(
        Modifier
            .verticalScroll(rememberScrollState())
            .padding(4.dp)
    ) {
        SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
            enhanceState.roleIdList.forEachIndexed { index, roleId ->
                SegmentedButton(
                    selected = enhanceState.selectedRoleId == roleId,
                    onClick = { enhanceState.selectRoleId(roleId) },
                    shape = SegmentedButtonDefaults.itemShape(index, enhanceState.roleIdList.size),
                    icon = {}
                ) {
                    Image(
                        painter = painterResource(Role.fromId(roleId).imgId),
                        contentDescription = null
                    )
                }
            }
        }

        val roleMasteryData = roleMasteryDataMap[enhanceState.selectedRoleId]!!
        val slotEnhanceMap = roleEnhanceMap[enhanceState.selectedRoleId]!!

        RoleSlotsDetail(
            enhanceState,
            roleMasteryData,
            slotEnhanceMap,
        )
    }
}

@Composable
private fun RoleSlotsDetail(
    enhanceState: EnhanceState,
    roleMasteryData: RoleMasteryData,
    slotEnhanceMap: Map<Int, Pair<Int, Int>>,
) {
    val role = Role.fromId(enhanceState.selectedRoleId)

    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(role.imgId),
            contentDescription = null
        )
        AdaptiveWidthLabel(
            text = stringResource(role.strId)
        )
    }
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        roleMasteryData.slotIdMap.forEach { (slotId, roleSlotData) ->
            val slotEnhance = slotEnhanceMap[slotId]!!
            var roleSlotLevel = roleSlotData.slotLevelMap[slotEnhance.first]
            var enhanceLevel = slotEnhance.second
            var isEquipped = true
            if (roleSlotLevel == null) {
                roleSlotLevel = roleSlotData.slotLevelMap[1]!!
                enhanceLevel = 0
                isEquipped = false
            }
            RoleSlot(
                slotId,
                roleSlotLevel.iconId,
                enhanceLevel,
                roleSlotLevel.maxEnhanceLevel,
                isEquipped,
                enhanceState.selectedSlotId == slotId,
                enhanceState::selectSlotId
            )
        }
    }


    val selectedRoleSlotData = roleMasteryData.slotIdMap[enhanceState.selectedSlotId]!!
    val selectedSlotEnhance = slotEnhanceMap[enhanceState.selectedSlotId]!!

    RoleSlotEnhanceDetail(
        enhanceState,
        role,
        selectedRoleSlotData,
        selectedSlotEnhance

    )
}

@Composable
private fun RoleSlot(
    slotId: Int,
    iconId: Int,
    enhanceLevel: Int,
    maxEnhanceLevel: Int,
    isEquipped: Boolean,
    selected: Boolean,
    onSlotIdSelect: (slotId: Int) -> Unit
) {
    Box(
        Modifier
            .size(64.dp)
            .background(
                if (selected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
                MaterialTheme.shapes.small
            )
            .clip(MaterialTheme.shapes.small)
            .clickable(
                onClick = { onSlotIdSelect(slotId) }
            )
            .padding(8.dp)
    ) {
        PlaceImage(
            url = UrlUtil.getItemIconUrl(iconId),
            colorFilter = if (isEquipped) null else darkenColorFilter
        )
        Row(
            Modifier
                .align(Alignment.BottomEnd)
                .offset(x = 2.dp)
                .padding(bottom = 2.dp)
        ) {
            repeat(maxEnhanceLevel) { i ->
                Image(
                    painter = painterResource(
                        if (i < enhanceLevel) R.drawable.star_small_1
                        else R.drawable.star_small_0
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .size((7.2f).dp)
                        .offset(x = (-1f * i).dp)
                )
            }
        }
    }
}

@Composable
private fun RoleSlotEnhanceDetail(
    enhanceState: EnhanceState,
    role: Role,
    roleSlotData: RoleSlotData,
    slotEnhance: Pair<Int, Int>
) {
    val (slotLevel, enhanceLevel) = slotEnhance
    var roleSlotLevel = roleSlotData.slotLevelMap[slotLevel]
    var isEquipped = true
    if (roleSlotLevel == null) {
        roleSlotLevel = roleSlotData.slotLevelMap[1]!!
        isEquipped = false
    }
    val roleEnhanceLevel = roleSlotLevel.enhanceLevelMap[enhanceLevel]!!
    val roleSlot = roleEnhanceLevel.typeValuePairList.getOrNull(0)?.let {
        RoleSlot.fromId(it.first)
    }

    Container {
        var name = if (roleSlot == null) "???" else stringResource(roleSlot.nameStrId)
        name = "【${stringResource(role.strId)}】${name}Lv${maxOf(slotLevel, 1)}"
        Text(
            text = name,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleSmall
        )

        Row(
            modifier = Modifier.padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(Modifier.size(64.dp)) {
                PlaceImage(
                    url = UrlUtil.getItemIconUrl(roleSlotLevel.iconId),
                    colorFilter = if (isEquipped) null else darkenColorFilter
                )
                Row(
                    Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = 2.dp)
                        .padding(bottom = 2.dp)
                ) {
                    repeat(roleSlotLevel.maxEnhanceLevel) { i ->
                        Image(
                            painter = painterResource(
                                if (i < enhanceLevel) R.drawable.star_small_1
                                else R.drawable.star_small_0
                            ),
                            contentDescription = null,
                            modifier = Modifier
                                .size((10.2f).dp)
                                .offset(x = (-1.2f * i).dp)
                        )
                    }
                }
            }

            Column(Modifier.padding(start = 8.dp)) {
                SliderPlus(
                    value = slotLevel,
                    minValue = 0,
                    maxValue = roleSlotData.maxSlotLevel,
                    onValueChange = { value ->
                        enhanceState.enhanceRole(
                            value,
                            roleSlotData.slotLevelMap[value]?.maxEnhanceLevel ?: 0
                        )
                    }
                )

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Rarities(
                        highlightCount = 0,
                        maxRarity = roleSlotLevel.maxEnhanceLevel,
                        rarity = enhanceLevel,
                        onRarityChange = { level ->
                            if (isEquipped) {
                                enhanceState.enhanceRole(
                                    slotLevel,
                                    level
                                )
                            }
                        }
                    )
                }
            }
        }

        Column(Modifier.padding(4.dp)) {
            UnderlineLabel(
                label = stringResource(R.string.role_property),
                color = MaterialTheme.colorScheme.primary
            )
            roleEnhanceLevel.typeValuePairList.forEach { item ->
                val labelValue = getRoleSlotLabelValue(item.first, item.second)
                Infobar(
                    label = labelValue.first,
                    value = labelValue.second,
                    width = 120.dp
                )
            }
        }

        Column(Modifier.padding(4.dp)) {
            UnderlineLabel(
                label = stringResource(R.string.consume_item),
                color = MaterialTheme.colorScheme.primary
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(Modifier.size(36.dp)) {
                    PlaceImage(UrlUtil.getItemIconUrl(roleEnhanceLevel.itemId))
                }
                Infobar(
                    label = stringResource(R.string.required_number),
                    value = roleEnhanceLevel.num.toString()
                )
            }
        }
    }
}
