package com.kasuminotes.ui.app.chara

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import com.kasuminotes.data.EquipData
import com.kasuminotes.data.MaxUserData
import com.kasuminotes.data.UniqueData
import com.kasuminotes.data.UnitPromotion
import com.kasuminotes.data.UserData
import com.kasuminotes.ui.components.BadgedButtonDialog
import com.kasuminotes.ui.components.LabelText
import com.kasuminotes.ui.components.SliderPlus

@ExperimentalCoilApi
@ExperimentalMaterialApi
@Composable
fun CharaUserData(
    userData: UserData,
    originUserData: UserData,
    maxUserData: MaxUserData,
    maxRarity: Int,
    hasUnique: Boolean,
    unitPromotion: UnitPromotion?,
    uniqueData: UniqueData?,
    onEquipClick: (equipData: EquipData, slot: Int) -> Unit,
    onUniqueClick: (UniqueData) -> Unit,
    onEquipChange: (equip: Boolean, slot: Int) -> Unit,
    onUniqueChange: (equip: Boolean) -> Unit,
    onCharaLevelChange: (Int) -> Unit,
    onRarityChange: (Int) -> Unit,
    onUniqueLevelChange: (Int) -> Unit,
    onLoveLevelChange: (Int) -> Unit,
    onPromotionLevelChange: (Int) -> Unit,
    onSkillLevelChange: (value: Int, labelText: String) -> Unit
) {
    Column(Modifier.padding(8.dp)) {
        CharaStatus(
            userData,
            originUserData,
            maxUserData,
            maxRarity,
            hasUnique,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            onCharaLevelChange,
            onRarityChange,
            onUniqueLevelChange,
            onLoveLevelChange,
            onPromotionLevelChange
        )

        Row(
            Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            SkillColumn(Alignment.Start) {
                SkillBadgeButtonDialog(
                    labelText = "UB",
                    level = userData.ubLevel,
                    originLevel = originUserData.ubLevel,
                    maxLevel = userData.charaLevel,
                    onLevelChange = onSkillLevelChange
                )

                SkillBadgeButtonDialog(
                    labelText = "EX",
                    level = userData.exLevel,
                    originLevel = originUserData.exLevel,
                    maxLevel = userData.charaLevel,
                    onLevelChange = onSkillLevelChange
                )
            }

            CharaEquipSlots(
                userData,
                originUserData,
                unitPromotion,
                uniqueData,
                onEquipClick,
                onUniqueClick,
                onEquipChange,
                onUniqueChange
            )

            SkillColumn(Alignment.End) {
                SkillBadgeButtonDialog(
                    labelText = "Main 1",
                    level = userData.skill1Level,
                    originLevel = originUserData.skill1Level,
                    maxLevel = userData.charaLevel,
                    onLevelChange = onSkillLevelChange
                )

                SkillBadgeButtonDialog(
                    labelText = "Main 2",
                    level = userData.skill2Level,
                    originLevel = originUserData.skill2Level,
                    maxLevel = userData.charaLevel,
                    onLevelChange = onSkillLevelChange
                )
            }
        }
    }
}

@Composable
private fun RowScope.SkillColumn(
    horizontalAlignment: Alignment.Horizontal,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight(),
        horizontalAlignment = horizontalAlignment,
        verticalArrangement = Arrangement.SpaceBetween,
        content = content
    )
}

@ExperimentalMaterialApi
@Composable
private fun SkillBadgeButtonDialog(
    labelText: String,
    level: Int,
    originLevel: Int,
    maxLevel: Int,
    onLevelChange: (value: Int, labelText: String) -> Unit
) {
    BadgedButtonDialog(
        value = level,
        originValue = originLevel,
        label = {
            LabelText(
                text = labelText,
                background = MaterialTheme.colors.secondary
            )
        }
    ) {
        Surface(
            shape = CircleShape,
            elevation = 16.dp
        ) {
            SliderPlus(
                value = level,
                minValue = 1,
                maxValue = maxLevel,
                onValueChange = { onLevelChange(it, labelText) }
            ) {
                LabelText(
                    text = labelText,
                    background = MaterialTheme.colors.secondary
                )
            }
        }
    }
}
