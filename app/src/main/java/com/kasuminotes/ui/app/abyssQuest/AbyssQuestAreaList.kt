package com.kasuminotes.ui.app.abyssQuest

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kasuminotes.R
import com.kasuminotes.data.QuestWaveGroupEnemy
import com.kasuminotes.state.AbyssQuestState
import com.kasuminotes.ui.components.CenterText
import com.kasuminotes.ui.components.LabelContainer
import com.kasuminotes.ui.components.PlaceImage
import com.kasuminotes.ui.theme.UnitImageShape
import com.kasuminotes.utils.UrlUtil

@Composable
fun AbyssQuestAreaList(
    abyssQuestState: AbyssQuestState,
    onEnemyClick: (enemyId: Int, waveGroupId: Int?) -> Unit
) {
    if (abyssQuestState.hasAbyssQuest) {
        Column(Modifier.verticalScroll(rememberScrollState())) {
            abyssQuestState.abyssQuestDataGrouped.forEach { group ->
                val label = group.value[0].questName
                LabelContainer(
                    label = label,
                    color= MaterialTheme.colorScheme.primary
                ) {
                    if (label == "BOSS") {
                        val bossMap = group.value.groupBy { it.enemyName }
                        val grades = AbyssGrade.entries.toTypedArray()
                        val gradeStyle = MaterialTheme.typography.labelMedium
                        val shape = MaterialTheme.shapes.extraSmall
                        Column(Modifier.horizontalScroll(rememberScrollState())) {
                            bossMap.forEach { entry ->
                                Row {
                                    entry.value.forEachIndexed { index, enemy ->
                                        AbyssEnemyItem(enemy, onEnemyClick) {
                                            val grade = grades.getOrElse(index) { AbyssGrade.EX }
                                            Text(
                                                text = grade.name,
                                                modifier = Modifier
                                                    .align(Alignment.BottomEnd)
                                                    .background(grade.color, shape)
                                                    .padding(4.dp, 1.dp),
                                                color = Color.White,
                                                style = gradeStyle
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        Row(Modifier.horizontalScroll(rememberScrollState())) {
                            group.value.forEach { enemy ->
                                AbyssEnemyItem(enemy, onEnemyClick)
                            }
                        }
                    }
                }
            }
        }
    } else {
        CenterText(stringResource(R.string.no_data))
    }
}

@Composable
private fun AbyssEnemyItem(
    enemy: QuestWaveGroupEnemy,
    onEnemyClick: (enemyId: Int, waveGroupId: Int?) -> Unit,
    content: @Composable BoxScope.() -> Unit = {}
) {
    Box(
        Modifier
            .padding(4.dp)
            .size(56.dp)
            .clickable { onEnemyClick(enemy.enemyId, enemy.waveGroupId) }
    ) {
        PlaceImage(
            url = UrlUtil.getEnemyUnitIconUrl(enemy.unitId),
            shape = UnitImageShape
        )
        content()
    }
}

private enum class AbyssGrade(val color: Color) {
    N(Color(0xFF69B4F3)),
    H(Color(0xFFE35875)),
    VH(Color(0xFFA456B9)),
    EX(Color(0xFF6545A8))
}
