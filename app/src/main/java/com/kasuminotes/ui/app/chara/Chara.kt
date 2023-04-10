package com.kasuminotes.ui.app.chara

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import com.kasuminotes.data.EquipData
import com.kasuminotes.data.ExEquipSlot
import com.kasuminotes.data.MaxUserData
import com.kasuminotes.data.UniqueData
import com.kasuminotes.data.UserProfile
import com.kasuminotes.ui.app.state.CharaState
import com.kasuminotes.ui.components.BackdropScaffold
import com.kasuminotes.ui.components.BackdropScaffoldDefaults
import com.kasuminotes.ui.components.BackdropValue
import com.kasuminotes.ui.components.rememberBackdropScaffoldState
import kotlinx.coroutines.launch

@Composable
fun Chara(
    charaState: CharaState,
    maxUserData: MaxUserData,
    onBack: () -> Unit,
    onEquipSlotClick: (equipData: EquipData, slot: Int?) -> Unit,
    onUniqueClick: (UniqueData) -> Unit,
    onExEquipSlotClick: (ExEquipSlot) -> Unit,
    onSummonsClick: (summons: List<Int>, skillLevel: Int) -> Unit
) {
    val userProfile = charaState.userProfile!!
    val userData = charaState.userData!!
    val unitData = userProfile.getRealUnitData(userData.rarity)
    val originProperty = userProfile.includeExEquipProperty ?: charaState.includeExEquipProperty

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

    val calcBackLayerConstraints = remember<(Constraints) -> Constraints> {{ constraints ->
        constraints.copy(minWidth = 0, minHeight = 0)
    }}

    val onToggle = remember<() -> Unit> {{
        scope.launch {
            if (scaffoldState.isConcealed) scaffoldState.reveal()
            else scaffoldState.conceal()
        }
    }}

    val onEquipClick = remember<(EquipData) -> Unit> {{ equipData ->
        onEquipSlotClick(equipData, null)
    }}

    val onCharaChange = remember<(UserProfile) -> Unit>(userProfile) {{ otherChara ->
        charaState.initUserProfile(
            otherChara,
            userProfile.sharedProfiles!!.plus(userProfile),
            maxUserData.maxCharaLevel
        )
    }}

    val onEquipChange = remember<(Boolean, Int) -> Unit>(userProfile) {{ equip, slot ->
        if (equip) {
            charaState.changeEquipLevel(slot, userProfile.getEquipMaxLevel(slot))
        } else {
            charaState.changeEquipLevel(slot, -1)
        }
    }}

    val onUniqueChange = remember<(Boolean) -> Unit> {{ equip ->
        if (equip) {
            charaState.changeUniqueLevel(maxUserData.maxUniqueLevel)
        } else {
            charaState.changeUniqueLevel(0)
        }
    }}

    BackdropScaffold(
        appBar = {
            CharaTopBar(
                unitData.unitId,
                userData.rarity,
                unitData.unitName,
                statusBarHeight,
                onBack
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
                charaState.rankBonusProperty,
                charaState.includeExEquipProperty,
                originProperty,
                userProfile.userData,
                charaState.saveVisible,
                statusBarHeight,
                headerHeight,
                onBack,
                onEquipSlotClick,
                onUniqueClick,
                onExEquipSlotClick,
                onEquipChange,
                onUniqueChange,
                charaState::changeCharaLevel,
                charaState::changeRarity,
                charaState::changeUniqueLevel,
                charaState::changeLoveLevel,
                charaState::changePromotionLevel,
                charaState::changeSkillLevel,
                charaState::changeLvLimitBreak,
                charaState::cancel,
                charaState::save
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
                onEquipClick,
                onUniqueClick,
                onSummonsClick,
                onCharaChange,
                onToggle
            )
        },
        modifier = Modifier.navigationBarsPadding(),
        scaffoldState = scaffoldState,
        peekHeight = peekHeight,
        headerHeight = headerHeight,
        persistentAppBar = false,
        backLayerBackgroundColor = MaterialTheme.colorScheme.surface,
        backLayerContentColor = MaterialTheme.colorScheme.onSurface,
        frontLayerShape = RectangleShape,
        frontLayerElevation = 0.dp,
        frontLayerBackgroundColor = Color.Transparent,
        frontLayerContentColor = MaterialTheme.colorScheme.onSurface,
        frontLayerScrimColor = Color.Transparent,
        calcBackLayerConstraints = calcBackLayerConstraints
    )
}
