package com.kasuminotes.ui.app.clanBattle

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kasuminotes.data.EnemyData
import com.kasuminotes.data.SkillItem
import com.kasuminotes.data.UnitAttackPattern
import com.kasuminotes.data.UnitSkillData
import com.kasuminotes.ui.components.AttackDetail
import com.kasuminotes.ui.components.AttackPattern
import com.kasuminotes.ui.components.Container
import com.kasuminotes.ui.components.PropertyTable
import com.kasuminotes.ui.components.SkillDetail
import com.kasuminotes.ui.components.LabelContainer
import com.kasuminotes.ui.components.MultiLineText
import com.kasuminotes.utils.UrlUtil

private val clanBattleEnemyIndices = listOf(0, 16, 1, 3, 2, 4, 13, 15)
private val clanBattleEnemyPartIndices = listOf(0, 16, 1, 3, 2, 4)

@Composable
fun EnemyDetail(
    enemyData: EnemyData,
    enemyMultiParts: List<EnemyData>,
    unitAttackPatternList: List<UnitAttackPattern>,
    skillList: List<SkillItem>,
    unitSkillData: UnitSkillData?,
    onMinionsClick: (minions: List<Int>, skillLevel: Int) -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(4.dp)
    ) {
        EnemyComment(enemyData.comment)

        Container {
            PropertyTable(
                property = enemyData.property,
                indices = clanBattleEnemyIndices
            )
        }

        if (enemyMultiParts.isNotEmpty()) {
            enemyMultiParts.forEach { part ->
                LabelContainer(
                    label = part.name,
                    color = MaterialTheme.colorScheme.primary
                ) {
                    PropertyTable(
                        property = part.property,
                        indices = clanBattleEnemyPartIndices
                    )
                }
            }
        }

        if (unitSkillData != null) {
            AttackPattern(
                hasUnique1 = false,
                hasUnique2 = false,
                atkType = enemyData.atkType,
                unitAttackPatternList = unitAttackPatternList,
                unitSkillData = unitSkillData
            )

            val property = if (enemyMultiParts.isEmpty()) {
                enemyData.property
            } else {
                enemyMultiParts[0].property
            }

            AttackDetail(
                enemyData.atkType,
                enemyData.normalAtkCastTime,
                enemyData.searchAreaWidth,
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
                    onSummonsClick = onMinionsClick
                )
            }
        }
    }
}

@Composable
private fun EnemyComment(comment: String) {
    if (comment == "") return

    val comments = remember {
        val list = comment.split("\\n").toMutableList()
        if (list.size > 2) {
            listOf(list.removeAt(0), list.removeAt(0)) to list.toList()
        } else if (list.size > 1) {
            listOf(list.removeAt(0), "") to list
        } else {
            list to listOf("")
        }
    }

    Box {
        var expanded by remember { mutableStateOf(false) }

        Container {
            Box(Modifier.clickable { expanded = !expanded }) {
                Column {
                    MultiLineText(comments.first)
                }
                Icon(
                    imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }

            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn() + expandVertically(expandFrom = Alignment.Top),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column {
                    MultiLineText(comments.second)
                }
            }
        }
    }
}
