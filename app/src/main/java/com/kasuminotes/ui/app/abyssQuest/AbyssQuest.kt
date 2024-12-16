package com.kasuminotes.ui.app.abyssQuest

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.kasuminotes.data.AbyssSchedule
import com.kasuminotes.state.AbyssQuestState
import com.kasuminotes.ui.components.BackButton
import com.kasuminotes.ui.components.TopBar
import com.kasuminotes.ui.components.UnitElement

@Composable
fun AbyssQuest(
    abyssQuestState: AbyssQuestState,
    onNavigateToEnemy: (enemyId: Int, waveGroupId: Int?) -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopBar(
                title = { Text(stringResource(R.string.abyss_quest)) },
                navigationIcon = { BackButton(onBack) },
                actions = { SelectScheduleMenu(abyssQuestState) }
            )
        },
        bottomBar = {
            Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
        },
        content = { contentPadding ->
            Box(Modifier.padding(contentPadding)) {
                AbyssQuestAreaList(abyssQuestState, onNavigateToEnemy)
            }
        }
    )
}

@Composable
private fun SelectItem(schedule: AbyssSchedule) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            UnitElement(0.dp, schedule.talentId, 16.dp)
            Text(
                text = schedule.title,
                modifier = Modifier.padding(start = 2.dp),
                style = MaterialTheme.typography.labelMedium
            )
        }
        Text(
            text = schedule.startTimeText,
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Composable
private fun SelectScheduleMenu(abyssQuestState: AbyssQuestState) {
    if (abyssQuestState.selectedSchedule == null) return

    Box(Modifier.wrapContentSize()) {
        Box(Modifier.align(Alignment.CenterEnd)) {
            var expanded by remember { mutableStateOf(false) }
            TextButton(
                onClick = { expanded = true },
                colors = ButtonDefaults.textButtonColors(contentColor = LocalContentColor.current)
            ) {
                SelectItem(abyssQuestState.selectedSchedule!!)
                Icon(
                    imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = null
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                abyssQuestState.abyssScheduleList.forEach { schedule ->
                    DropdownMenuItem(
                        text= {
                            SelectItem(schedule)
                        },
                        onClick = {
                            abyssQuestState.selectSchedule(schedule)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
