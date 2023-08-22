package com.kasuminotes.ui.app.chara

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgeDefaults
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.kasuminotes.R
import com.kasuminotes.data.ExEquipSlot
import com.kasuminotes.data.UserData
import com.kasuminotes.ui.components.ImageIcon
import com.kasuminotes.ui.theme.ShadowColor
import com.kasuminotes.utils.UrlUtil

@Composable
fun CharaExEquipSlots(
    userData: UserData,
    originUserData: UserData,
    exEquipSlots: List<ExEquipSlot>,
    onExEquipClick: (ExEquipSlot) -> Unit
) {
    Column(horizontalAlignment = Alignment.End) {
        val hasExEquipSlots = exEquipSlots.isNotEmpty()
        Box(Modifier.padding(end = 24.dp)) {
            ExEquipIcon(
                userData.exEquip1Level,
                originUserData.exEquip1Level,
                originUserData.exEquip1,
                if (hasExEquipSlots) exEquipSlots[0] else null,
                onExEquipClick
            )
        }
        Box(Modifier.padding(vertical = 4.dp)) {
            ExEquipIcon(
                userData.exEquip2Level,
                originUserData.exEquip2Level,
                originUserData.exEquip2,
                if (hasExEquipSlots) exEquipSlots[1] else null,
                onExEquipClick
            )
        }
        Box(Modifier.padding(end = 24.dp)) {
            ExEquipIcon(
                userData.exEquip3Level,
                originUserData.exEquip3Level,
                originUserData.exEquip3,
                if (hasExEquipSlots) exEquipSlots[2] else null,
                onExEquipClick
            )
        }
    }
}

@Composable
private fun ExEquipIcon(
    exEquipLevel: Int,
    originExEquipLevel: Int,
    originExEquipId: Int,
    exEquipSlot: ExEquipSlot?,
    onExEquipClick: (ExEquipSlot) -> Unit
) {
    if (exEquipSlot == null) {
        ImageIcon(
            painter = painterResource(R.drawable.item_00000),
            loading = false,
            enabled = false
        )
    } else {
        val exEquip = exEquipSlot.exEquipData
        val containerColor: Color
        val text: String
        if (
            exEquipLevel != originExEquipLevel ||
            (exEquip == null && originExEquipId != 0) ||
            (exEquip != null && originExEquipId == 0) ||
            (exEquip != null && exEquip.exEquipmentId != originExEquipId)
        ) {
            containerColor = BadgeDefaults.containerColor
            text = "M"
        } else {
            containerColor = Color.Transparent
            text = ""
        }
        BadgedBox({ Badge(containerColor = containerColor) { Text(text) } }) {
            if (exEquip == null) {
                ImageIcon(
                    painter = painterResource(R.drawable.item_00000),
                    loading = false,
                    onClick = { onExEquipClick(exEquipSlot) }
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(
                            UrlUtil.getExEquipCategoryUrl(
                                exEquipSlot.category
                            )
                        ),
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
            } else {
                ImageIcon(
                    url = UrlUtil.getExEquipUrl(exEquip.exEquipmentId),
                    onClick = { onExEquipClick(exEquipSlot) }
                ) {
                    Row(
                        Modifier
                            .align(Alignment.BottomStart)
                            .padding(2.dp)
                    ) {
                        repeat(exEquip.maxEnhanceLevel) { i ->
                            Image(
                                painter = painterResource(
                                    if (i < exEquipLevel) {
                                        if (i > 2) {
                                            R.drawable.star_small_6
                                        } else {
                                            R.drawable.star_small_1
                                        }
                                    } else {
                                        R.drawable.star_small_0
                                    }
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
