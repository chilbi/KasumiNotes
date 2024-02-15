package com.kasuminotes.ui.app.userEditor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.kasuminotes.R
import com.kasuminotes.ui.components.LabelImage
import com.kasuminotes.ui.components.LabelText
import com.kasuminotes.ui.components.PlaceImage
import com.kasuminotes.ui.components.SliderPlus
import com.kasuminotes.ui.theme.RaritiesColors
import com.kasuminotes.ui.theme.rankRarity
import com.kasuminotes.utils.UrlUtil

@Composable
fun CharaEditorDialog(
    maxCharaLevel: Int,
    maxUniqueLevel: Int,
    maxPromotionLevel: Int,
    selectedChara: List<Int>,
    onClose: () -> Unit,
    onDelete: () -> Unit,
    onConfirm: (
        rarity: Int?,
        charaLevel: Int?,
        loveLevel: Int?,
        uniqueLevel: Int?,
        promotionLevel: Int?,
        unlockSlot: Int?
    ) -> Unit
) {
    Dialog(
        onDismissRequest = onClose,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 24.dp),
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = AlertDialogDefaults.TonalElevation
        ) {
            Column(
                Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 8.dp, vertical = 24.dp)
            ) {
                EditorDialogTitle(selectedChara.size) {
                    onClose()
                    onDelete()
                }

                SelectedCharaList(selectedChara)

                EditorDialogContent(
                    maxCharaLevel,
                    maxUniqueLevel,
                    maxPromotionLevel,
                    onClose,
                    onConfirm
                )
            }
        }
    }
}

@Composable
private fun EditorDialogTitle(
    selectedSize: Int,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(stringResource(R.string.edit_selected_d, selectedSize))

        IconButton(onClick) {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.tertiary
            )
        }
    }
}

@Composable
private fun SelectedCharaList(selectedChara: List<Int>) {
    LazyRow(contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)) {
        items(selectedChara, { it }) { unitId ->
            Box(
                Modifier
                    .size(48.dp)
                    .padding(4.dp)
            ) {
                PlaceImage(UrlUtil.getUnitIconUrl(unitId, 5))
            }
        }
    }
}

@Composable
private fun EditorDialogContent(
    maxCharaLevel: Int,
    maxUniqueLevel: Int,
    maxPromotionLevel: Int,
    onClose: () -> Unit,
    onConfirm: (
        rarity: Int?,
        charaLevel: Int?,
        loveLevel: Int?,
        uniqueLevel: Int?,
        promotionLevel: Int?,
        unlockSlot: Int?
    ) -> Unit
) {
    var rarity by remember { mutableStateOf(6) }
    var charaLevel by remember { mutableStateOf(maxCharaLevel) }
    var loveLevel by remember { mutableStateOf(12) }
    var uniqueLevel by remember { mutableStateOf(maxUniqueLevel) }
    var promotionLevel by remember { mutableStateOf(maxPromotionLevel) }
    var unlockSlot by remember { mutableStateOf(6) }

    var rarityChecked by remember { mutableStateOf(true) }
    var charaLevelChecked by remember { mutableStateOf(true) }
    var loveLevelChecked by remember { mutableStateOf(true) }
    var uniqueLevelChecked by remember { mutableStateOf(true) }
    var promotionLevelChecked by remember { mutableStateOf(true) }
    var unlockSlotChecked by remember { mutableStateOf(true) }

    Column(Modifier.padding(4.dp)) {
        SliderPlus(
            value = rarity,
            minValue = 1,
            maxValue = 6,
            onValueChange = { rarity = it },
            checked = rarityChecked,
            onCheckedChange = { rarityChecked = it },
            label = {
                LabelImage(
                    if (rarity > 5) R.drawable.star_large_6
                    else R.drawable.star_large_1
                )
            }
        )

        SliderPlus(
            value = loveLevel,
            minValue = 0,
            maxValue = 12,
            onValueChange = { loveLevel = it },
            checked = loveLevelChecked,
            onCheckedChange = { loveLevelChecked = it},
            label = {
                LabelImage(R.drawable.love_level)
            }
        )

        SliderPlus(
            value = uniqueLevel,
            minValue = 0,
            maxValue = maxUniqueLevel,
            onValueChange = { uniqueLevel = it },
            checked = uniqueLevelChecked,
            onCheckedChange = { uniqueLevelChecked = it },
            label = {
                LabelImage(R.drawable.unique_large)
            }
        )

        SliderPlus(
            value = charaLevel,
            minValue = 1,
            maxValue = maxCharaLevel + 10,
            onValueChange = { charaLevel = it },
            checked = charaLevelChecked,
            onCheckedChange = { charaLevelChecked = it },
            label = {
                LabelText(stringResource(R.string.level))
            }
        )

        val color = RaritiesColors.getRarityColors(promotionLevel.rankRarity).middle

        SliderPlus(
            value = promotionLevel,
            minValue = 1,
            maxValue = maxPromotionLevel,
            onValueChange = { promotionLevel = it },
            checked = promotionLevelChecked,
            onCheckedChange = { promotionLevelChecked = it },
            label = {
                LabelText(
                    text = stringResource(R.string.rank),
                    color = color
                )
            }
        )

        SliderPlus(
            value = unlockSlot,
            minValue = 0,
            maxValue = 6,
            onValueChange = { unlockSlot = it },
            checked = unlockSlotChecked,
            onCheckedChange = { unlockSlotChecked = it },
            label = {
                LabelText(
                    text = stringResource(R.string.slot),
                    color = color
                )
            }
        )
    }

    Row(Modifier.padding(8.dp)) {
        Spacer(Modifier.weight(1f))

        TextButton(onClose) {
            Text(stringResource(R.string.cancel))
        }

        Spacer(Modifier.width(8.dp))

        Button(
            onClick = {
                onClose()
                onConfirm(
                    if (rarityChecked) rarity else null,
                    if (charaLevelChecked) charaLevel else null,
                    if (loveLevelChecked) loveLevel else null,
                    if (uniqueLevelChecked) uniqueLevel else null,
                    if (promotionLevelChecked) promotionLevel else null,
                    if (unlockSlotChecked) unlockSlot else null
                )
            }
        ) {
            Text(stringResource(R.string.confirm))
        }
    }
}
