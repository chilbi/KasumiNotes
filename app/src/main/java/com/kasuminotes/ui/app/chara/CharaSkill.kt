package com.kasuminotes.ui.app.chara

import androidx.annotation.StringRes
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kasuminotes.BuildConfig
import com.kasuminotes.R
import com.kasuminotes.data.Property
import com.kasuminotes.data.SkillAction
import com.kasuminotes.action.ActionBuilder
import com.kasuminotes.action.D
import com.kasuminotes.action.getUnknown
import com.kasuminotes.action.stringDescription
import com.kasuminotes.data.UnitAttackPattern
import com.kasuminotes.data.UnitData
import com.kasuminotes.data.UnitSkillData
import com.kasuminotes.data.UserData
import com.kasuminotes.ui.components.ActionLabel
import com.kasuminotes.ui.components.BgBorderColumn
import com.kasuminotes.ui.components.ImageCard
import com.kasuminotes.ui.components.Infobar
import com.kasuminotes.ui.components.PlaceImage
import com.kasuminotes.ui.components.UnderlineLabel
import com.kasuminotes.ui.components.VerticalGrid
import com.kasuminotes.ui.components.VerticalGridCells
import com.kasuminotes.ui.theme.LightInfo
import com.kasuminotes.ui.theme.LightWarning
import com.kasuminotes.utils.UrlUtil
import kotlin.math.roundToInt

@Composable
fun CharaSkill(
    userData: UserData,
    unitData: UnitData,
    unitAttackPatternList: List<UnitAttackPattern>,
    unitSkillData: UnitSkillData?,
    property: Property,
    state: ScrollState
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

            val name: String
            val damage: Double
            val atkTypeText: String

            if (unitData.atkType == 1) {
                name = stringResource(R.string.physical_atk)
                damage = property.atk
                atkTypeText = stringResource(R.string.physical)
            } else {
                name = stringResource(R.string.magic_atk)
                damage = property.magicStr
                atkTypeText = stringResource(R.string.magic)
            }

            val description = stringResource(
                R.string.action_damage_target1_formula2_content3,
                stringResource(R.string.target_one_content1, stringResource(R.string.target_enemy)),
                damage.roundToInt().toString(),
                atkTypeText
            )

            SkillDetail(
                label = "A",
                isRfSkill = false,
                iconUrl = UrlUtil.getAtkIconUrl(unitData.atkType),
                name = name,
                castTime = unitData.normalAtkCastTime,
                description = description,
                searchAreaWidth = unitData.searchAreaWidth
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
                    actions = item.skillData.actions
                )
            }
        }
    }
}

@Composable
private fun SkillLabel(
    text: String,
    color: Color = MaterialTheme.colors.onPrimary,
    bgColor: Color = MaterialTheme.colors.primaryVariant,
) {
    Text(
        text = text,
        modifier = Modifier
            .padding(4.dp)
            .background(
                color = bgColor,
                shape = MaterialTheme.shapes.small
            )
            .padding(horizontal = 8.dp),
        fontSize = 14.sp,
        color = color,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun AttackPattern(
    hasUnique: Boolean,
    atkType: Int,
    unitAttackPatternList: List<UnitAttackPattern>,
    unitSkillData: UnitSkillData,
    fontSize: TextUnit = 10.sp,
    loopLabelColor: Color = MaterialTheme.colors.secondary,
    atkLabelColor: Color = MaterialTheme.colors.primary
) {
    val size = unitAttackPatternList.size
    if (size == 0) return
    val hasMultiPattern = size > 1

    unitAttackPatternList.forEachIndexed { i, pattern ->
        var text = stringResource(R.string.pattern)
        if (hasMultiPattern) {
            text += i + 1
        }

        BgBorderColumn {
            SkillLabel(text)

            VerticalGrid(
                size = pattern.atkPatternList.size,
                cells = VerticalGridCells.Adaptive(56.dp)
            ) { index ->
                val atkPattern = pattern.getAtkPattern(
                    index,
                    atkType,
                    hasUnique,
                    unitSkillData
                )

                PatternItem(
                    atkPattern.loopLabel,
                    atkPattern.iconUrl,
                    atkPattern.atkLabel,
                    fontSize,
                    loopLabelColor,
                    atkLabelColor
                )
            }
        }
    }
}

@Composable
private fun PatternItem(
    loopLabel: String?,
    iconUrl: String,
    atkLabel: String,
    fontSize: TextUnit,
    loopLabelColor: Color,
    atkLabelColor: Color
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(Modifier.height(20.dp)) {
            if (loopLabel != null) {
                Text(
                    text = loopLabel,
                    modifier = Modifier.align(Alignment.BottomCenter),
                    color = loopLabelColor,
                    fontSize = fontSize,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Box(
            Modifier
                .padding(vertical = 2.dp)
                .size(48.dp)
        ) {
            PlaceImage(iconUrl)
        }

        Box(Modifier.height(20.dp)) {
            Text(
                text = atkLabel,
                modifier = Modifier.align(Alignment.TopCenter),
                color = atkLabelColor,
                fontSize = fontSize,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun SkillDetail(
    label: String,
    isRfSkill: Boolean,
    iconUrl: String,
    name: String,
    castTime: Float,
    description: String,
    searchAreaWidth: Int = 0,
    skillLevel: Int = 0,
    property: Property? = null,
    rawDepends: List<Int>? = null,
    actions: List<SkillAction>? = null
) {
    val visible = remember { mutableStateOf(false) }

    BgBorderColumn(Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SkillLabel(label)
            if (isRfSkill) {
                SkillLabel(
                    text = "RF",
                    color = MaterialTheme.colors.onSecondary,
                    bgColor = MaterialTheme.colors.secondary
                )
            }
            Spacer(Modifier.weight(1f))
            if (BuildConfig.DEBUG && actions != null) {
                IconButton(onClick = { visible.value = !visible.value }) {
                    Icon(
                        imageVector = Icons.Filled.Code,
                        contentDescription = null,
                        tint = if (visible.value) MaterialTheme.colors.secondary
                        else LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
                    )
                }
            }
            @StringRes
            val labelId: Int
            val value: Int
            val color: Color
            if (searchAreaWidth == 0) {
                labelId = R.string.skill_level
                value = skillLevel
                color = LightInfo
            } else {
                labelId = R.string.search_area_width
                value = searchAreaWidth
                color = LightWarning
            }
            Infobar(
                label = stringResource(labelId),
                value = value.toString(),
                modifier = Modifier.width(100.dp),
                width = 57.dp,
                color = color,
                contentColor = Color.Black
            )
        }

        ImageCard(
            imageUrl = iconUrl,
            primaryText = name,
            secondaryText = stringResource(R.string.cast_time_s, castTime)
        )

        Text(
            text = description,
            modifier = Modifier.padding(4.dp),
            fontSize = 14.sp,
            lineHeight = 28.sp
        )

        val descriptionList: List<D>? by remember(skillLevel, property, rawDepends, actions) {
            derivedStateOf {
                if (property != null && rawDepends != null && actions != null) {
                    ActionBuilder(rawDepends, actions, false).buildDescriptionList(
                        skillLevel,
                        property
                    )
                } else {
                    null
                }
            }
        }

        if (actions != null) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
                UnderlineLabel(
                    label = stringResource(R.string.skill_effect),
                    color = MaterialTheme.colors.primary
                )
            }
        }

        if (!visible.value && descriptionList != null) {
            descriptionList!!.forEachIndexed { index, d ->
                Row(Modifier.padding(4.dp)) {
                    ActionLabel(index + 1)

                    Text(
                        text = stringDescription(d),
                        modifier = Modifier.padding(start = 4.dp),
                        fontSize = 14.sp,
                        lineHeight = 28.sp
                    )
                }
            }
        }

        if (visible.value && BuildConfig.DEBUG && actions != null) {
            actions.forEachIndexed { index, action ->
                Row(Modifier.padding(4.dp)) {
                    ActionLabel(index + 1)

                    Text(
                        text = stringDescription(action.getUnknown()),
                        modifier = Modifier.padding(start = 4.dp),
                        fontSize = 14.sp,
                        lineHeight = 28.sp
                    )
                }
            }
        }
    }
}
