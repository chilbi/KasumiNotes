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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kasuminotes.data.EnemyData
import com.kasuminotes.ui.components.AttackDetail
import com.kasuminotes.ui.components.AttackPattern
import com.kasuminotes.ui.components.BgBorderColumn
import com.kasuminotes.ui.components.PropertyTable
import com.kasuminotes.ui.components.SkillDetail
import com.kasuminotes.ui.components.UnderlineLabelColumn
import com.kasuminotes.utils.UrlUtil

private val clanBattleEnemyIndices = listOf(0, 16, 1, 3, 2, 4, 13, 15)
private val clanBattleEnemyPartIndices = listOf(0, 16, 1, 3, 2, 4)

@Composable
fun EnemyDetail(
    enemyData: EnemyData,
    onMinionsClick: (minions: List<Int>, skillLevel: Int) -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(4.dp)
    ) {
        EnemyComment(enemyData.comment)

        BgBorderColumn {
            PropertyTable(
                property = enemyData.property,
                indices = clanBattleEnemyIndices
            )
        }

        if (enemyData.enemyMultiParts.isNotEmpty()) {
            enemyData.enemyMultiParts.forEach { part ->
                UnderlineLabelColumn(
                    label = part.name,
                    color = MaterialTheme.colors.primary
                ) {
                    PropertyTable(
                        property = part.property,
                        indices = clanBattleEnemyPartIndices
                    )
                }
            }
        }

        if (enemyData.unitSkillData != null) {
            AttackPattern(
                hasUnique = false,
                atkType = enemyData.atkType,
                unitAttackPatternList = enemyData.unitAttackPatternList,
                unitSkillData = enemyData.unitSkillData!!
            )

            val property = if (enemyData.enemyMultiParts.isEmpty()) {
                enemyData.property
            } else {
                enemyData.enemyMultiParts[0].property
            }

            AttackDetail(
                enemyData.atkType,
                enemyData.normalAtkCastTime,
                enemyData.searchAreaWidth,
                property
            )

            enemyData.skillList.forEach { item ->
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
    val comments = remember {
        val list = comment.split("\\n").toMutableList()
        listOf(list.removeAt(0), list.removeAt(0)) to list.toList()
    }

    Box {
        var expanded by remember { mutableStateOf(false) }

        BgBorderColumn {
            Box(Modifier.clickable { expanded = !expanded }) {
                Column {
                    comments.first.forEach { text ->
                        CommentText(text)
                    }
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
                    comments.second.forEach { text ->
                        CommentText(text)
                    }
                }
            }
        }
    }
}


@Composable
private fun CommentText(text: String) {
    Text(
        text = text,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 2.dp),
        fontSize = 14.sp
    )
}
