package com.kasuminotes.ui.app.chara

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import com.kasuminotes.R
import com.kasuminotes.data.EquipData
import com.kasuminotes.data.MaxUserData
import com.kasuminotes.data.Property
import com.kasuminotes.data.UniqueData
import com.kasuminotes.data.UnitData
import com.kasuminotes.data.UnitPromotion
import com.kasuminotes.data.UserData
import com.kasuminotes.ui.app.DefaultUserId
import com.kasuminotes.ui.components.Alert
import com.kasuminotes.ui.components.PropertyTable
import com.kasuminotes.ui.components.ImmersiveBackButton
import com.kasuminotes.ui.components.Severity
import com.kasuminotes.ui.components.StillBox
import com.kasuminotes.ui.components.StillSizeModify
import com.kasuminotes.ui.components.bgBorder
import com.kasuminotes.ui.theme.ImmersiveSysUi

@ExperimentalCoilApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun CharaBackLayer(
    userData: UserData,
    unitData: UnitData,
    maxUserData: MaxUserData,
    unitPromotion: UnitPromotion?,
    uniqueData: UniqueData?,
    property: Property,
    originProperty: Property,
    originUserData: UserData,
    saveVisible: Boolean,
    statusBarHeight: Dp,
    onBack: () -> Unit,
    onEquipClick: (equipData: EquipData, slot: Int) -> Unit,
    onUniqueClick: (UniqueData) -> Unit,
    onEquipChange: (equip: Boolean, slot: Int) -> Unit,
    onUniqueChange: (equip: Boolean) -> Unit,
    onCharaLevelChange: (Int) -> Unit,
    onRarityChange: (Int) -> Unit,
    onUniqueLevelChange: (Int) -> Unit,
    onLoveLevelChange: (Int) -> Unit,
    onPromotionLevelChange: (Int) -> Unit,
    onSkillLevelChange: (value: Int, labelText: String) -> Unit,
    onLvLimitBreakChange: (maxCharaLevel: Int) -> Unit,
    onCancel: () -> Unit,
    onSave: () -> Unit
) {
    Column(Modifier.verticalScroll(rememberScrollState())) {
        StillBox(
            unitData.unitId,
            userData.rarity,
            StillSizeModify
        ) {
            ImmersiveBackButton(
                onBack,
                Modifier.padding(top = statusBarHeight)
            )

            CharaName(
                unitData.unitName,
                Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .height(36.dp)
            )
        }

        CharaUserData(
            userData,
            originUserData,
            maxUserData,
            unitData.maxRarity,
            unitData.hasUnique,
            unitPromotion,
            uniqueData,
            onEquipClick,
            onUniqueClick,
            onEquipChange,
            onUniqueChange,
            onCharaLevelChange,
            onRarityChange,
            onUniqueLevelChange,
            onLoveLevelChange,
            onPromotionLevelChange,
            onSkillLevelChange,
            onLvLimitBreakChange,
        )

        PropertyTable(
            property,
            modifier = Modifier
                .padding(8.dp)
                .bgBorder(MaterialTheme.colors.isLight)
                .padding(4.dp),
            originProperty = originProperty,
        )

        AlertMessage(userData.userId, saveVisible, onCancel, onSave)
    }
}

@ExperimentalAnimationApi
@Composable
private fun AlertMessage(
    userId: Int,
    saveVisible: Boolean,
    onCancel: () -> Unit,
    onSave: () -> Unit
) {
    var errorVisible by rememberSaveable { mutableStateOf(true) }

    var infoVisible by rememberSaveable { mutableStateOf(true) }

    AnimatedVisibility(visible = saveVisible) {
        Alert(
            severity = Severity.Warning,
            text = stringResource(R.string.save_to_database),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            padding = PaddingValues(vertical = 4.dp)
        ) {
            TextButton(
                onClick = onCancel,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colors.secondary
                )
            ) {
                Text(stringResource(R.string.cancel))
            }

            TextButton(onClick = onSave) {
                Text(stringResource(R.string.save))
            }
        }
    }

    AnimatedVisibility(
        visible = errorVisible && userId == DefaultUserId && saveVisible
    ) {
        Alert(
            severity = Severity.Error,
            text = stringResource(R.string.default_user_warning),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            IconButton(onClick = { errorVisible = false }) {
                Icon(Icons.Filled.Close, null)
            }
        }
    }

    AnimatedVisibility(visible = infoVisible) {
        Alert(
            severity = Severity.Info,
            text = stringResource(R.string.swipe_equip_info),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            IconButton(onClick = { infoVisible = false }) {
                Icon(Icons.Filled.Close, null)
            }
        }
    }
}

@Composable
private fun CharaName(
    unitName: String,
    modifier: Modifier = Modifier
) {
    Row(modifier) {
        Box(
            Modifier
                .fillMaxHeight()
                .background(ImmersiveSysUi)
        ) {
            Text(
                text = unitName,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 8.dp),
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(
            Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(
                    brush = Brush.horizontalGradient(
                        0.0f to ImmersiveSysUi,
                        0.2f to Color.Transparent
                    )
                )
        )
    }
}
