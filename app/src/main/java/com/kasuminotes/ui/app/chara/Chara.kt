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
import com.kasuminotes.ui.app.state.DbState
import com.kasuminotes.ui.components.BackdropScaffold
import com.kasuminotes.ui.components.BackdropScaffoldDefaults
import com.kasuminotes.ui.components.BackdropValue
import com.kasuminotes.ui.components.rememberBackdropScaffoldState
import kotlinx.coroutines.launch

@Composable
fun Chara(
    charaState: CharaState,
    dbState: DbState,
    maxUserData: MaxUserData,
    onBack: () -> Unit,
    onEquipmentClick: (equipData: EquipData, slot: Int?) -> Unit,
    onUniqueClick: (uniqueData: UniqueData, slot: Int) -> Unit,
    onExEquipClick: (ExEquipSlot) -> Unit,
    onSummonsClick: (summons: List<Int>, skillLevel: Int) -> Unit
) {
    val userProfile = charaState.userProfile!!
    val userData = charaState.userData!!
    val unitData = userProfile.getRealUnitData(userData.rarity)
    val property = charaState.includeExEquipProperty
    val originProperty = remember(charaState.saveVisible, userProfile.includeExEquipProperty, property) {
        userProfile.includeExEquipProperty ?: property
    }

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
        onEquipmentClick(equipData, null)
    }}

    val onCharaChange = remember<(UserProfile) -> Unit>(userProfile) {{ otherChara ->
        charaState.initUserProfile(
            otherChara,
            dbState.userState.charaListState.profiles,
            maxUserData.maxCharaLevel
        )
    }}

    val onEquipChange = remember<(Boolean, Int) -> Unit>(userProfile) {{ equip, slot ->
        if (equip) {
            charaState.changeEquipLevel(userProfile.getEquipMaxLevel(slot), slot)
        } else {
            charaState.changeEquipLevel(-1, slot)
        }
    }}

    val onUniqueChange = remember<(Boolean, Int) -> Unit>(maxUserData.maxUniqueLevel) {{ equip, slot ->
        if (slot == 1) {
            if (equip) charaState.changeUniqueLevel(maxUserData.maxUniqueLevel, 1)
            else charaState.changeUniqueLevel(0, 1)
        } else {
            if (equip) charaState.changeUniqueLevel(5, 2)
            else charaState.changeUniqueLevel(-1, 2)
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
                userProfile.unique1Data,
                userProfile.unique2Data,
                userProfile.exEquipSlots,
                charaState.rankBonusProperty,
                property,
                originProperty,
                userProfile.userData,
                charaState.saveVisible,
                statusBarHeight,
                headerHeight,
                onBack,
                onEquipmentClick,
                onUniqueClick,
                onExEquipClick,
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
                userProfile.unique1Data,
                userProfile.promotions,
                userProfile.getRealUnitAttackPatternList(userData.rarity),
                userProfile.getRealUnitSkillData(userData.rarity),
                property,
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
