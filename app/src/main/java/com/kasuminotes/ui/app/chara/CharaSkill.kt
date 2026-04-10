package com.kasuminotes.ui.app.chara

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.kasuminotes.common.Talent
import com.kasuminotes.common.Role
import com.kasuminotes.data.MaxUserData
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
    maxUserData: MaxUserData,
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
            Box(Modifier.wrapContentSize()) {
                AttackPattern(
                    hasUnique1 = unitData.hasUnique1 && userData.unique1Level > 0,
                    hasUnique2 = unitData.hasUnique2 && userData.unique2Level > -1,
                    hasExUnique1 = maxUserData.isEquippedExUnique1(userData, unitData),
                    atkType = unitData.atkType,
                    unitAttackPatternList = unitAttackPatternList,
                    unitSkillData = unitSkillData!!
                )

                TalentAndRole(unitData.talentId, unitData.unitRoleId)
            }

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
                    coolTime = item.skillData.bossUbCoolTime,
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

@Composable
private fun BoxScope.TalentAndRole(talentId: Int, unitRoleId: Int) {
    if (talentId != 0) {
        Row(
            modifier = Modifier.padding(8.dp).align(Alignment.TopEnd),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val talent = Talent.fromId(talentId)
            Image(
                painter = painterResource(talent.imgId),
                contentDescription = null,
                modifier = Modifier.padding(end = 4.dp).size(18.dp)
            )
            Text(
                text = stringResource(talent.strId),
                textAlign = TextAlign.End,
                overflow = TextOverflow.Ellipsis,
                softWrap = false,
                maxLines = 1,
                style = MaterialTheme.typography.bodySmall
            )

            if (unitRoleId != 0) {
                val role = Role.fromId(unitRoleId)
                Spacer(Modifier.size(8.dp))
                Image(
                    painter = painterResource(role.imgId),
                    contentDescription = null,
                    modifier = Modifier.padding(end = 4.dp).size(18.dp)
                )
                Text(
                    text = stringResource(role.strId),
                    textAlign = TextAlign.End,
                    overflow = TextOverflow.Ellipsis,
                    softWrap = false,
                    maxLines = 1,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
