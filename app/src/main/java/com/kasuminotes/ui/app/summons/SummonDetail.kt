package com.kasuminotes.ui.app.summons

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kasuminotes.data.Property
import com.kasuminotes.data.SkillItem
import com.kasuminotes.data.UnitAttackPattern
import com.kasuminotes.data.UnitSkillData
import com.kasuminotes.ui.components.AttackDetail
import com.kasuminotes.ui.components.AttackPattern
import com.kasuminotes.ui.components.BgBorderColumn
import com.kasuminotes.ui.components.ImageCard
import com.kasuminotes.ui.components.PropertyTable
import com.kasuminotes.ui.components.SkillDetail
import com.kasuminotes.utils.UrlUtil

@Composable
fun SummonDetail(
    id: Int,
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
    ImageCard(
        imageUrl = UrlUtil.summonIconUrl,
        primaryText = name,
        secondaryText = id.toString(),
        imageSize = 56.dp,
        primaryFontSize = 18.sp,
        secondaryFontSize = 16.sp
    )

    BgBorderColumn {
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
