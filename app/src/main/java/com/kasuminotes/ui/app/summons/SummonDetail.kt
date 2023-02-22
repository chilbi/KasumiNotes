package com.kasuminotes.ui.app.summons

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kasuminotes.data.SummonData
import com.kasuminotes.ui.components.AttackDetail
import com.kasuminotes.ui.components.AttackPattern
import com.kasuminotes.ui.components.PropertyTable
import com.kasuminotes.ui.components.SkillDetail
import com.kasuminotes.ui.components.bgBorder
import com.kasuminotes.utils.UrlUtil

@Composable
fun SummonDetail(summonData: SummonData) {
    SummonInfo(summonData.unitId, summonData.unitName)

    PropertyTable(
        property = summonData.property,
        modifier = Modifier
            .padding(4.dp)
            .bgBorder(MaterialTheme.colors.isLight)
            .padding(4.dp)
    )

    if (summonData.unitSkillData != null) {
        AttackPattern(
            hasUnique = false,
            atkType = summonData.atkType,
            unitAttackPatternList = summonData.unitAttackPatternList,
            unitSkillData = summonData.unitSkillData!!
        )
    }

    AttackDetail(
        summonData.atkType,
        summonData.normalAtkCastTime,
        summonData.searchAreaWidth,
        summonData.property
    )

    summonData.skillList.forEach { item ->
        SkillDetail(
            label = item.label,
            isRfSkill = item.skillData.isRfSkill,
            iconUrl = UrlUtil.getSkillIconUrl(item.skillData.iconType),
            name = item.skillData.name,
            castTime = item.skillData.skillCastTime,
            description = item.skillData.description,
            skillLevel = item.level,
            property = summonData.property,
            rawDepends = item.skillData.rawDepends,
            actions = item.skillData.actions
        )
    }
}
