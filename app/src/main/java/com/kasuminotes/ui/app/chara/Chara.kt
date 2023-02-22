package com.kasuminotes.ui.app.chara

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material.BackdropScaffold
import androidx.compose.material.BackdropScaffoldDefaults
import androidx.compose.material.BackdropValue
import androidx.compose.material.MaterialTheme
import androidx.compose.material.rememberBackdropScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.kasuminotes.data.EquipData
import com.kasuminotes.data.ExEquipSlot
import com.kasuminotes.data.MaxUserData
import com.kasuminotes.data.UniqueData
import com.kasuminotes.ui.app.state.CharaState
import kotlinx.coroutines.launch

@Composable
fun Chara(
    charaState: CharaState,
    maxUserData: MaxUserData,
    onBack: () -> Unit,
    onEquipClick: (equipData: EquipData, slot: Int?) -> Unit,
    onUniqueClick: (UniqueData) -> Unit,
    onExEquipSlotClick: (ExEquipSlot) -> Unit,
    onSummonsClick: (summons: List<Int>, skillLevel: Int) -> Unit
) {
    val userProfile = charaState.userProfile!!
    val userData = charaState.userData!!
    val unitData = userProfile.getRealUnitData(userData.rarity)

    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBackdropScaffoldState(BackdropValue.Concealed)
    val density = LocalDensity.current
    val statusBarTop =  WindowInsets.statusBars.getTop(density)

    val statusBarHeight = remember(statusBarTop, density) {
        with(density) { statusBarTop.toDp() }
    }

    val peekHeight = remember(statusBarTop, density) {
        statusBarHeight + 56.dp
    }

    val headerHeight = remember {
        BackdropScaffoldDefaults.HeaderHeight + 28.dp
    }

    BackdropScaffold(
        appBar = {
            CharaTopBar(
                unitData.unitId,
                userData.rarity,
                unitData.unitName,
                statusBarHeight,
                onBack = { if (scaffoldState.isConcealed) onBack() }
            )
        },
        backLayerContent = {
            CharaBackLayer(
                userData,
                unitData,
                maxUserData,
                userProfile.unitPromotion,
                userProfile.uniqueData,
                userProfile.exEquipSlots,
                charaState.includeExEquipProperty,
                userProfile.includeExEquipProperty ?: charaState.includeExEquipProperty,
                userProfile.userData,
                charaState.saveVisible,
                statusBarHeight,
                onBack = { if (scaffoldState.isRevealed) onBack() },
                onEquipClick = onEquipClick,
                onUniqueClick = onUniqueClick,
                onExEquipSlotClick,
                onEquipChange = { equip, slot ->
                    if (equip) {
                        charaState.changeEquipLevel(slot, userProfile.getEquipMaxLevel(slot))
                    } else {
                        charaState.changeEquipLevel(slot, -1)
                    }
                },
                onUniqueChange = { equip ->
                    if (equip) {
                        charaState.changeUniqueLevel(maxUserData.maxUniqueLevel)
                    } else {
                        charaState.changeUniqueLevel(0)
                    }
                },

                onCharaLevelChange = charaState::changeCharaLevel,
                onRarityChange = charaState::changeRarity,
                onUniqueLevelChange = charaState::changeUniqueLevel,
                onLoveLevelChange = charaState::changeLoveLevel,
                onPromotionLevelChange = charaState::changePromotionLevel,
                onSkillLevelChange = charaState::changeSkillLevel,
                onLvLimitBreakChange = charaState::changeLvLimitBreak,

                onCancel = charaState::cancel,
                onSave = charaState::save
            )
        },
        frontLayerContent = {
            CharaFrontLayer(
                userData,
                unitData,
                userProfile.charaStoryStatus,
                userProfile.sharedProfiles,
                userProfile.uniqueData,
                userProfile.promotions,
                userProfile.getRealUnitAttackPatternList(userData.rarity),
                userProfile.getRealUnitSkillData(userData.rarity),
                charaState.includeExEquipProperty,
                onEquipClick = { onEquipClick(it, null) },
                onUniqueClick = onUniqueClick,
                onSummonsClick = onSummonsClick,
                onCharaClick = {
                    charaState.initUserProfile(
                        it,
                        userProfile.sharedProfiles.plus(userProfile),
                        maxUserData.maxCharaLevel
                    )
                },
                onToggle = {
                    scope.launch {
                        if (scaffoldState.isConcealed) scaffoldState.reveal()
                        else scaffoldState.conceal()
                    }
                }
            )
        },
        modifier = Modifier.navigationBarsPadding(),
        scaffoldState = scaffoldState,
        peekHeight = peekHeight,
        headerHeight = headerHeight,
        persistentAppBar = false,
        backLayerBackgroundColor = MaterialTheme.colors.surface,
        backLayerContentColor = MaterialTheme.colors.onSurface,
        frontLayerShape = RectangleShape,
        frontLayerElevation = 0.dp,
        frontLayerBackgroundColor = Color.Transparent,
        frontLayerContentColor = MaterialTheme.colors.onSurface,
        frontLayerScrimColor = Color.Transparent
    )
}
