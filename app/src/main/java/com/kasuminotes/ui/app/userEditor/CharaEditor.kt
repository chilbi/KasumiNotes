package com.kasuminotes.ui.app.userEditor

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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.kasuminotes.R
import com.kasuminotes.common.ImageVariant
import com.kasuminotes.common.OrderBy
import com.kasuminotes.data.UserProfile
import com.kasuminotes.ui.app.home.CharaItem
import com.kasuminotes.state.CharaImageState
import com.kasuminotes.state.UserState
import com.kasuminotes.ui.components.PlaceImage
import com.kasuminotes.ui.components.SizedBox
import com.kasuminotes.ui.components.imageSize

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
        listState,
        title = stringResource(R.string.edit_chara),
        onBack = onBack,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text(stringResource(if (isEmpty) R.string.save_edit else R.string.edit)) },
                icon = { Icon(if (isEmpty) Icons.Filled.Check else Icons.Filled.Edit, null) },
                onClick = if (isEmpty) onConfirm else toggleVisibleEditor
            )
        },
        topBarContent = {
            val derivedCount = listState.derivedProfiles.size
            val derivedLockCount = listState.derivedLockedChara.size
            val derivedUnlockCount = derivedCount - derivedLockCount
            val allSelected = if (visibleUnlock && visibleLock) {
                selectedChara.size == derivedCount
            } else if (visibleUnlock) {
                selectedChara.size == derivedUnlockCount
            } else if (visibleLock) {
                selectedChara.size == derivedLockCount
            } else {
                true
            }

            SelectMenu(
                derivedUnlockCount,
                derivedLockCount,
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
            val infiniteTransition = rememberInfiniteTransition(label = "CharaEditorInfiniteTransition")
            val layerAlpha = infiniteTransition.animateFloat(
                initialValue = 0.0f,
                targetValue = 1.0f,
                animationSpec = infiniteRepeatable(
                    animation = keyframes {
                        durationMillis = 4500
                        0.0f at 0 using FastOutSlowInEasing
                        0.0f at 1750 using FastOutSlowInEasing
                        1.0f at 2750 using FastOutSlowInEasing
                        1.0f at 4500 using FastOutSlowInEasing
                    },
                    repeatMode = RepeatMode.Reverse
                ),
                label = "CharaEditorFloatAnimation"
            )

            Box(Modifier.padding(contentPadding)) {
                LazyVerticalGrid(
                    columns = charaImageState.gridCells,
                    contentPadding = PaddingValues(4.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    items(profiles) { userProfile ->
                        val unitId = userProfile.unitData.unitId
                        val locked = lockedChara.contains(unitId)
                        val selected = selectedChara.contains(unitId)

                        SelectableCharaItem(
                            layerAlpha,
                            listState.orderBy,
                            unitId,
                            userProfile,
                            locked,
                            selected,
                            charaImageState,
                            listState::selectChara
                        )
                    }
                    item("box1") {
                        Box(Modifier.imageSize(1f))
                    }
                    item("box2") {
                        Box(Modifier.imageSize(1f))
                    }
                }
            }
        }
    )

    if (visibleEditor) {
        CharaEditorDialog(
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
    layerAlpha: State<Float>,
    orderBy: OrderBy,
    unitId: Int,
    userProfile: UserProfile,
    locked: Boolean,
    selected: Boolean,
    charaImageState: CharaImageState,
    onSelect: (Int) -> Unit
) {
    Box(Modifier.imageSize(1f)) {
        if (locked) {
            SizedBox(charaImageState.ratio) {
                PlaceImage(
                    url = charaImageState.getImageUrl(unitId, 3),
                    modifier = Modifier.clickable { onSelect(unitId) }
                )
            }
        } else {
            CharaItem(
                layerAlpha,
                orderBy,
                userProfile,
                charaImageState,
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
                        MaterialTheme.colorScheme.secondary,
                        MaterialTheme.shapes.extraSmall
                    )
            ) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.Center),
                    tint = MaterialTheme.colorScheme.onSecondary
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
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
