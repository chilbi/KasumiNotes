package com.kasuminotes.ui.app.chara

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kasuminotes.R
import com.kasuminotes.common.Element
import com.kasuminotes.data.Property
import com.kasuminotes.data.UnitAttackPattern
import com.kasuminotes.data.UnitData
import com.kasuminotes.data.UnitSkillData
import com.kasuminotes.data.UserData
import com.kasuminotes.ui.components.AttackDetail
import com.kasuminotes.ui.components.AttackPattern
import com.kasuminotes.ui.components.Infobar
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
            Box(Modifier.wrapContentSize()) {
                AttackPattern(
                    hasUnique1 = unitData.hasUnique1 && userData.unique1Level > 0,
                    hasUnique2 = unitData.hasUnique2 && userData.unique2Level > -1,
                    atkType = unitData.atkType,
                    unitAttackPatternList = unitAttackPatternList,
                    unitSkillData = unitSkillData!!
                )

                if (unitData.talentId != 0) {
                    val imgId: Int
                    val strId: Int
                    when (unitData.talentId) {
                        1 -> {
                            imgId = R.drawable.fire
                            strId = Element.Fire.resId
                        }
                        2 -> {
                            imgId = R.drawable.water
                            strId = Element.Water.resId
                        }
                        3 -> {
                            imgId = R.drawable.wind
                            strId = Element.Wind.resId
                        }
                        4 -> {
                            imgId = R.drawable.light
                            strId = Element.Light.resId
                        }
                        else -> {
                            imgId = R.drawable.dark
                            strId = Element.Dark.resId
                        }
                    }
                    Infobar(
                        label = stringResource(R.string.element),
                        value = stringResource(strId),
                        modifier = Modifier.padding(8.dp).width(110.dp).align(Alignment.TopEnd),
                        endDecoration = {
                            Image(
                                painter = painterResource(imgId),
                                contentDescription = null,
                                modifier = Modifier.padding(start = 4.dp).size(18.dp)
                            )
                        },
                        width = 48.dp,
                        color = MaterialTheme.colorScheme.secondary,
                        textAlign = TextAlign.Center
                    )
                }
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
