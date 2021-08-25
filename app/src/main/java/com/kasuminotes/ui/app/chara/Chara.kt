package com.kasuminotes.ui.app.chara

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import coil.annotation.ExperimentalCoilApi
import com.kasuminotes.data.EquipData
import com.kasuminotes.data.MaxUserData
import com.kasuminotes.data.UniqueData
import com.kasuminotes.ui.app.state.CharaState

@ExperimentalCoilApi
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun Chara(
    charaState: CharaState,
    maxUserData: MaxUserData,
    onBack: () -> Unit,
    onEquipClick: (equipData: EquipData, slot: Int?) -> Unit,
    onUniqueClick: (UniqueData) -> Unit
) {
    CharaScaffold(
        charaState,
        maxUserData,
        onBack,
        onEquipClick,
        onUniqueClick
    )
}
