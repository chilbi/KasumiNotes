package com.kasuminotes.ui.app.enemy

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kasuminotes.R
import com.kasuminotes.data.EnemyData
import com.kasuminotes.state.EnemyState
import com.kasuminotes.ui.components.AttackDetail
import com.kasuminotes.ui.components.AttackPattern
import com.kasuminotes.ui.components.Container
import com.kasuminotes.ui.components.Infobar
import com.kasuminotes.ui.components.PropertyTable
import com.kasuminotes.ui.components.SkillDetail
import com.kasuminotes.ui.components.LabelContainer
import com.kasuminotes.ui.components.MultiLineText
import com.kasuminotes.utils.UrlUtil

private val shadowCharaIndices: List<Int> = listOf(1, 3, 5, 6, 2, 4, 16, 7, 0, 8, 13, 15, 14)
private val enemyIndices = listOf(0, 16, 1, 3, 2, 4, 13, 15)
private val enemyPartIndices = listOf(0, 16, 1, 3, 2, 4)

@Composable
fun EnemyDetail(
    enemyState: EnemyState,
    isShadowChara: Boolean,
    onExtraEffectClick: (enemyIdList: List<Int>, epTableName: String) -> Unit,
    onMinionsClick: (minions: List<Int>, skillLevel: Int, enemyData: EnemyData, epTableName: String) -> Unit
) {
    val enemyData = enemyState.enemyData!!

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
                indices = if (isShadowChara) shadowCharaIndices else enemyIndices
            )
        }

        if (enemyState.enemyMultiParts.isNotEmpty()) {
            enemyState.enemyMultiParts.forEach { part ->
                LabelContainer(
                    label = part.name,
                    color = MaterialTheme.colorScheme.primary
                ) {
                    PropertyTable(
                        property = part.property,
                        indices = enemyPartIndices
                    )
                }
            }
        }

        if (enemyData.extraEffectData != null) {
            Container {
                Row {
                    Infobar(
                        label = "content_type",
                        value = enemyData.extraEffectData!!.contentType.toString(),
                        modifier = Modifier.weight(1f),
                        width = 95.dp,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Infobar(
                        label = "exec_timing",
                        value = enemyData.extraEffectData!!.execTimingList.joinToString(","),
                        modifier = Modifier.weight(1f),
                        width =  88.dp,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                Button(
                    onClick = {
                        onExtraEffectClick(
                            enemyData.extraEffectData!!.enemyIdList,
                            enemyState.epTableName
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp)
                ) {
                    Text(stringResource(R.string.extra_effect))
                }
            }
        }

        if (enemyState.unitSkillData != null) {
            AttackPattern(
                hasUnique1 = false,
                hasUnique2 = false,
                atkType = enemyData.atkType,
                unitAttackPatternList = enemyState.unitAttackPatternList,
                unitSkillData = enemyState.unitSkillData!!
            )

            val property = if (enemyState.enemyMultiParts.isEmpty()) {
                enemyData.property
            } else {
                enemyState.enemyMultiParts[0].property
            }

            AttackDetail(
                enemyData.atkType,
                enemyData.normalAtkCastTime,
                enemyData.searchAreaWidth,
                property
            )

            enemyState.skillList.forEach { item ->
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
                    onSummonsClick = { summons, skillLevel ->
                        onMinionsClick(summons, skillLevel, enemyData, enemyState.epTableName)
                    }
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
