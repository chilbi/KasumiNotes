package com.kasuminotes.ui.app.talentQuest

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.heightIn
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
import com.kasuminotes.ui.app.state.TalentQuestState
import com.kasuminotes.ui.components.BackButton
import com.kasuminotes.ui.components.TopBar
import com.kasuminotes.ui.components.UnitElement

@Composable
fun TalentQuest(
    taLentQuestState: TalentQuestState,
    onNavigateToEnemy: (enemyId: Int) -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopBar(
                title = { Text(stringResource(R.string.talent_quest)) },
                navigationIcon = { BackButton(onBack) },
                actions = {
                    if (taLentQuestState.hasTalentQuest) {
                        SelectAreaMenu(taLentQuestState)
                        SelectNum(taLentQuestState)
                    }
                }
            )
        },
        bottomBar = {
            Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
        },
        content = { contentPadding ->
            Box(Modifier.padding(contentPadding)) {
                TalentQuestAreaList(taLentQuestState, onNavigateToEnemy)
            }
        }
    )
}

@Composable
private fun SelectAreaMenu(taLentQuestState: TalentQuestState) {
    if (taLentQuestState.selectedArea == null) return

    Box(Modifier.wrapContentSize()) {
        Box(Modifier.align(Alignment.CenterEnd)) {
            var expanded by remember { mutableStateOf(false) }
            TextButton(
                onClick = { expanded = true },
                colors = ButtonDefaults.textButtonColors(contentColor = LocalContentColor.current)
            ) {
                UnitElement(0.dp, taLentQuestState.selectedArea!!.talentId, 16.dp)
                Text(
                    taLentQuestState.selectedArea!!.areaName,
                    Modifier.padding(start = 2.dp)
                )
                Icon(
                    imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = null
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                taLentQuestState.talentQuestAreaDataList.forEach { areaData ->
                    DropdownMenuItem(
                        text= {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                UnitElement(0.dp, areaData.talentId, 16.dp)
                                Text(
                                    areaData.areaName,
                                    Modifier.padding(start = 2.dp)
                                )
                            }
                        },
                        onClick = {
                            taLentQuestState.selectArea(areaData)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun SelectNum(taLentQuestState: TalentQuestState) {
    Box(Modifier.wrapContentSize()) {
        Box(Modifier.align(Alignment.CenterEnd)) {
            var expanded by remember { mutableStateOf(false) }
            TextButton(
                onClick = { expanded = true },
                colors = ButtonDefaults.textButtonColors(contentColor = LocalContentColor.current)
            ) {
                Text(taLentQuestState.selectedNum.toString())
                Icon(
                    imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = null
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.heightIn(max = 420.dp)
            ) {
                (1..taLentQuestState.maxNum).forEach { num ->
                    DropdownMenuItem(
                        text= { Text(num.toString()) },
                        onClick = {
                            taLentQuestState.selectNum(num)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
