package com.kasuminotes.ui.app.summons

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kasuminotes.data.Property
import com.kasuminotes.data.SkillItem
import com.kasuminotes.data.UnitAttackPattern
import com.kasuminotes.data.UnitSkillData
import com.kasuminotes.ui.components.AttackDetail
import com.kasuminotes.ui.components.AttackPattern
import com.kasuminotes.ui.components.Container
import com.kasuminotes.ui.components.ImageCard
import com.kasuminotes.ui.components.PropertyTable
import com.kasuminotes.ui.components.SkillDetail
import com.kasuminotes.utils.UrlUtil

@Composable
fun SummonDetail(
    unitId: Int,
    enemyId: Int?,
    name: String,
    searchAreaWidth: Int,
    atkType: Int,
    normalAtkCastTime: Float,
    property: Property,
    propertyIndices: List<Int>,
    unitAttackPatternList: List<UnitAttackPattern>,
    unitSkillData: UnitSkillData?,
    skillList: List<SkillItem>
) {
    var secondaryText = "uid:$unitId"
    if (enemyId != null) {
        secondaryText += "; eid:$enemyId"
    }
    ImageCard(
        imageUrl = UrlUtil.summonIconUrl,
        primaryText = name,
        secondaryText = secondaryText,
        imageSize = 56.dp
    )

    Container {
        PropertyTable(
            property = property,
            indices = propertyIndices
        )
    }

    if (unitSkillData != null) {
        AttackPattern(
            hasUnique = false,
            atkType = atkType,
            unitAttackPatternList = unitAttackPatternList,
            unitSkillData = unitSkillData
        )

        AttackDetail(
            atkType,
            normalAtkCastTime,
            searchAreaWidth,
            property
        )

        skillList.forEach { item ->
            SkillDetail(
                label = item.label,
                isRfSkill = item.skillData.isRfSkill,
                iconUrl = UrlUtil.getSkillIconUrl(item.skillData.iconType),
                name = item.skillData.name,
                coolTime = item.skillData.bossUbCoolTime,
                castTime = item.skillData.skillCastTime,
                description = item.skillData.description,
                skillLevel = item.level,
                property = property,
                rawDepends = item.skillData.rawDepends,
                actions = item.skillData.actions
            )
        }
    }
}
