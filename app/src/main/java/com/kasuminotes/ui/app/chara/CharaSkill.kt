package com.kasuminotes.ui.app.chara

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kasuminotes.data.Property
import com.kasuminotes.data.UnitAttackPattern
import com.kasuminotes.data.UnitData
import com.kasuminotes.data.UnitSkillData
import com.kasuminotes.data.UserData
import com.kasuminotes.ui.components.AttackDetail
import com.kasuminotes.ui.components.AttackPattern
import com.kasuminotes.ui.components.SkillDetail
import com.kasuminotes.utils.UrlUtil

@Composable
fun CharaSkill(
    userData: UserData,
    unitData: UnitData,
    unitAttackPatternList: List<UnitAttackPattern>,
    unitSkillData: UnitSkillData?,
    property: Property,
    state: ScrollState,
    onSummonsClick: (summons: List<Int>, skillLevel: Int) -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(state)
            .padding(4.dp)
    ) {
        val skillList = remember(unitSkillData, userData) {
            unitSkillData?.getSkillList(
                userData.ubLevel,
                userData.skill1Level,
                userData.skill2Level,
                userData.exLevel
            )
        }

        if (skillList != null) {
            AttackPattern(
                hasUnique = unitData.hasUnique && userData.uniqueLevel > 0,
                atkType = unitData.atkType,
                unitAttackPatternList = unitAttackPatternList,
                unitSkillData = unitSkillData!!
            )

            AttackDetail(
                unitData.atkType,
                unitData.normalAtkCastTime,
                unitData.searchAreaWidth,
                property
            )

            skillList.forEach { item ->
                SkillDetail(
                    label = item.label,
                    isRfSkill = item.skillData.isRfSkill,
                    iconUrl = UrlUtil.getSkillIconUrl(item.skillData.iconType),
                    name = item.skillData.name,
                    castTime = item.skillData.skillCastTime,
                    description = item.skillData.description,
                    skillLevel = item.level,
                    property = property,
                    rawDepends = item.skillData.rawDepends,
                    actions = item.skillData.actions,
                    onSummonsClick = onSummonsClick
                )
            }
        }
    }
}
