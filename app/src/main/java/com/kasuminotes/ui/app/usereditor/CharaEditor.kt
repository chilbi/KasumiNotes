package com.kasuminotes.ui.app.usereditor

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.kasuminotes.R
import com.kasuminotes.common.ImageVariant
import com.kasuminotes.data.UserProfile
import com.kasuminotes.ui.app.home.CharaItem
import com.kasuminotes.ui.app.state.CharaImageState
import com.kasuminotes.ui.app.state.UserState
import com.kasuminotes.ui.components.LabelImage
import com.kasuminotes.ui.components.LabelText
import com.kasuminotes.ui.components.PlaceImage
import com.kasuminotes.ui.components.SizedBox
import com.kasuminotes.ui.components.SliderPlus
import com.kasuminotes.ui.components.imageSize
import com.kasuminotes.ui.theme.DarkWarning
import com.kasuminotes.ui.theme.RaritiesColors
import com.kasuminotes.ui.theme.rankRarity
import com.kasuminotes.utils.UrlUtil

@Composable
fun CharaEditor(
    userState: UserState,
    onBack: () -> Unit,
    onConfirm: () -> Unit
) {
    val listState = userState.charaListState
    val lockedChara = listState.lockedChara
    val selectedChara = listState.selectedChara
    val isEmpty = selectedChara.isEmpty()

    var visibleUnlock by remember { mutableStateOf(true) }

    var visibleLock by remember { mutableStateOf(true) }

    var visibleEditor by remember { mutableStateOf(false) }

    val toggleVisibleEditor: () -> Unit = { visibleEditor = !visibleEditor }

    val profiles: List<UserProfile> by remember(
        visibleUnlock,
        visibleLock,
        listState.derivedProfiles,
        listState.lockedChara
    ) {
        val locked = listState.lockedChara
        derivedStateOf {
            if (visibleUnlock && visibleLock) {
                listState.derivedProfiles
            } else if (visibleUnlock) {
                listState.derivedProfiles.filter { userProfile ->
                    !locked.contains(userProfile.unitData.unitId)
                }
            } else if (visibleLock) {
                listState.derivedProfiles.filter { userProfile ->
                    locked.contains(userProfile.unitData.unitId)
                }
            } else {
                emptyList()
            }
        }
    }

    UserEditorScaffold(
        title = stringResource(R.string.edit_chara),
        searchText = listState.searchText,
        atkType = listState.atkType,
        position = listState.position,
        orderBy = listState.orderBy,
        sortDesc = listState.sortDesc,
        onSearchTextChange = listState::changeSearchText,
        onAtkTypeChange = listState::changeAtkType,
        onPositionChange = listState::changePosition,
        onOrderByChange = listState::changeOrderBy,
        onBack = onBack,
        floatingActionButton = {
            FloatingActionButton(
                onClick = if (isEmpty) onConfirm else toggleVisibleEditor,
                backgroundColor = if (isEmpty) MaterialTheme.colors.primary else MaterialTheme.colors.secondary,
                contentColor = Color.White
            ) {
                Icon(
                    imageVector = if (isEmpty) Icons.Filled.Check else Icons.Filled.Edit,
                    contentDescription = null
                )
            }
        },
        topBarContent = {
            val count = listState.profiles.size
            val lockCount = listState.lockedChara.size
            val unlockCount = count - lockCount
            val allSelected = if (visibleUnlock && visibleLock) {
                selectedChara.size == count
            } else if (visibleUnlock) {
                selectedChara.size == unlockCount
            } else if (visibleLock) {
                selectedChara.size == lockCount
            } else {
                true
            }

            SelectMenu(
                unlockCount,
                lockCount,
                visibleUnlock,
                visibleLock,
                allSelected,
                onUnlockToggle = {
                    visibleUnlock = !visibleUnlock
                    listState.clearSelected()
                },
                onLockToggle = {
                    visibleLock = !visibleLock
                    listState.clearSelected()
                },
                onAllSelect = listState::selectAllChara,
                onUnlockSelect = listState::selectUnlockedChara,
                onLockSelect = listState::selectLockedChara,
                onClearSelect = listState::clearSelected
            )
        },
        content = { contentPadding ->
            val charaImageState = remember { CharaImageState(ImageVariant.Icon) }
            val infiniteTransition = rememberInfiniteTransition()
            val layerAlpha = infiniteTransition.animateFloat(
                initialValue = 0.0f,
                targetValue = 1.0f,
                animationSpec = infiniteRepeatable(
                    animation = keyframes {
                        durationMillis = 4500
                        0.0f at 0 with FastOutSlowInEasing
                        0.0f at 1850 with FastOutSlowInEasing
                        1.0f at 2650 with FastOutSlowInEasing
                        1.0f at 4500 with FastOutSlowInEasing
                    },
                    repeatMode = RepeatMode.Reverse
                )
            )

            Box(Modifier.padding(contentPadding)) {
                LazyVerticalGrid(
                    cells = GridCells.Fixed(4),
                    contentPadding = PaddingValues(4.dp)
                ) {
                    items(profiles) { userProfile ->
                        val unitId = userProfile.unitData.unitId
                        val locked = lockedChara.contains(unitId)
                        val selected = selectedChara.contains(unitId)

                        SelectableCharaItem(
                            unitId,
                            userProfile,
                            layerAlpha,
                            locked,
                            selected,
                            charaImageState,
                            listState::selectChara
                        )
                    }
                }
            }
        }
    )

    if (visibleEditor) {
        EditorDialog(
            userState.maxUserData!!.maxCharaLevel,
            userState.maxUserData!!.maxUniqueLevel,
            userState.maxUserData!!.maxPromotionLevel,
            selectedChara,
            toggleVisibleEditor,
            listState::deleteProfiles,
            listState::modifyProfiles,
        )
    }
}

@Composable
private fun SelectableCharaItem(
    unitId: Int,
    userProfile: UserProfile,
    layerAlpha: State<Float>,
    locked: Boolean,
    selected: Boolean,
    charaImageState: CharaImageState,
    onSelect: (Int) -> Unit
) {
    Box(Modifier.imageSize(1f)) {
        if (locked) {
            SizedBox(charaImageState.ratio, charaImageState.isIcon) {
                PlaceImage(
                    url = charaImageState.getImageUrl(unitId, 3),
                    modifier = Modifier.clickable {
                        onSelect(unitId)
                    }
                )
            }
        } else {
            CharaItem(
                userProfile,
                charaImageState,
                layerAlpha,
                onCharaClick = { onSelect(unitId) }
            )
        }

        if (selected) {
            Box(
                Modifier
                    .align(Alignment.TopEnd)
                    .size(32.dp)
                    .padding(4.dp)
                    .background(
                        DarkWarning,
                        MaterialTheme.shapes.small
                    )
            ) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.Center),
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
private fun SelectMenu(
    unlockCount: Int,
    lockCount: Int,
    visibleUnlock: Boolean,
    visibleLock: Boolean,
    allSelected: Boolean,
    onUnlockToggle: () -> Unit,
    onLockToggle: () -> Unit,
    onAllSelect: () -> Unit,
    onUnlockSelect: () -> Unit,
    onLockSelect: () -> Unit,
    onClearSelect: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        ToggleButton(
            alignment = Alignment.CenterStart,
            text = stringResource(R.string.unlock_chara) + unlockCount,
            checked = visibleUnlock,
            onToggle = onUnlockToggle
        )

        ToggleButton(
            alignment = Alignment.Center,
            text = stringResource(R.string.lock_chara) + lockCount,
            checked = visibleLock,
            onToggle = onLockToggle
        )

        ToggleButton(
            alignment = Alignment.CenterEnd,
            text = stringResource(R.string.all_select),
            checked = allSelected,
            onToggle = {
                if (allSelected) {
                    onClearSelect()
                } else {
                    if (visibleUnlock && visibleLock) {
                        onAllSelect()
                    } else if (visibleUnlock) {
                        onUnlockSelect()
                    } else if (visibleLock) {
                        onLockSelect()
                    }
                }
            }
        )
    }
}

@Composable
private fun RowScope.ToggleButton(
    alignment: Alignment,
    text: String,
    checked: Boolean,
    onToggle: () -> Unit
) {
    Box(Modifier.weight(1f)) {
        Row(
            modifier = Modifier
                .align(alignment)
                .selectable(
                    selected = checked,
                    role = Role.Checkbox,
                    onClick = onToggle
                )
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Checkbox(checked, null)
            Text(
                text = text,
                modifier = Modifier.padding(start = 4.dp),
                fontSize = 14.sp
            )
        }
    }
}

@Composable
private fun EditorDialog(
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            shape = MaterialTheme.shapes.medium,
            elevation = 16.dp
        ) {
            Column(
                Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 8.dp, vertical = 4.dp)
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
                tint = MaterialTheme.colors.secondary
            )
        }
    }
}

@Composable
private fun SelectedCharaList(selectedChara: List<Int>) {
    LazyRow(contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)) {
        items(selectedChara) { unitId ->
            Box(
                Modifier
                    .size(48.dp)
                    .padding(4.dp)
            ) {
                PlaceImage(UrlUtil.getUnitIconUrl(unitId, 3))
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
            onCheckedChange = { rarityChecked = it }
        ) {
            LabelImage(
                if (rarity > 5) R.drawable.star_large_6
                else R.drawable.star_large_1
            )
        }

        SliderPlus(
            value = loveLevel,
            minValue = 0,
            maxValue = 12,
            onValueChange = { loveLevel = it },
            checked = loveLevelChecked,
            onCheckedChange = { loveLevelChecked = it}
        ) {
            LabelImage(R.drawable.love_level)
        }

        SliderPlus(
            value = uniqueLevel,
            minValue = 0,
            maxValue = maxUniqueLevel,
            onValueChange = { uniqueLevel = it },
            checked = uniqueLevelChecked,
            onCheckedChange = { uniqueLevelChecked = it}
        ) {
            LabelImage(R.drawable.unique_large)
        }

        SliderPlus(
            value = charaLevel,
            minValue = 1,
            maxValue = maxCharaLevel,
            onValueChange = { charaLevel = it },
            checked = charaLevelChecked,
            onCheckedChange = { charaLevelChecked = it },
        ) {
            LabelText(stringResource(R.string.level))
        }

        val bgColor = RaritiesColors.getRarityColors(promotionLevel.rankRarity).middle

        SliderPlus(
            value = promotionLevel,
            minValue = 1,
            maxValue = maxPromotionLevel,
            onValueChange = { promotionLevel = it },
            checked = promotionLevelChecked,
            onCheckedChange = { promotionLevelChecked = it }
        ) {
            LabelText(
                text = stringResource(R.string.rank),
                background = bgColor
            )
        }

        SliderPlus(
            value = unlockSlot,
            minValue = 0,
            maxValue = 6,
            onValueChange = { unlockSlot = it },
            checked = unlockSlotChecked,
            onCheckedChange = { unlockSlotChecked = it }
        ) {
            LabelText(
                text = stringResource(R.string.slot),
                background = bgColor
            )
        }
    }

    Row(Modifier.padding(vertical = 4.dp)) {
        Spacer(Modifier.weight(1f))

        TextButton(onClose) {
            Text(stringResource(R.string.cancel))
        }

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
            },
        ) {
            Text(stringResource(R.string.confirm))
        }
    }
}
