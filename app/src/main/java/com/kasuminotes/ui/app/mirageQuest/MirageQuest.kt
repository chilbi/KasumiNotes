package com.kasuminotes.ui.app.mirageQuest

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.kasuminotes.R
import com.kasuminotes.state.MirageQuestState
import com.kasuminotes.ui.components.BackButton
import com.kasuminotes.ui.components.TopBar

@Composable
fun MirageQuest(
    mirageQuestState: MirageQuestState,
    onNavigateToEnemy: (enemyId: Int, waveGroupId: Int?) -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopBar(
                title = { Text(stringResource(R.string.mirage_quest)) },
                navigationIcon = { BackButton(onBack) },
//                actions = { SelectQuestMenu(mirageQuestState) }
            )
        },
        bottomBar = {
            Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
        },
        content = { contentPadding ->
            Column(Modifier.padding(contentPadding)) {
                MirageQuestAreaList(mirageQuestState, onNavigateToEnemy)
            }
        }
    )
}

//@Composable
//private fun SelectQuestMenu(mirageQuestState: MirageQuestState) {
//    val mirageQuestTitleList = listOf(
//        stringResource(R.string.mirage_nemesis_quest),
//        stringResource(R.string.mirage_floor_quest)
//    )
//
//    Box(Modifier.wrapContentSize()) {
//        Box(Modifier.align(Alignment.CenterEnd)) {
//            var expanded by remember { mutableStateOf(false) }
//            TextButton(
//                onClick = { expanded = true },
//                colors = ButtonDefaults.textButtonColors(contentColor = LocalContentColor.current)
//            ) {
//                Text(
//                    mirageQuestTitleList[mirageQuestState.selectedQuest],
//                    Modifier.padding(start = 2.dp)
//                )
//                Icon(
//                    imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
//                    contentDescription = null
//                )
//            }
//            DropdownMenu(
//                expanded = expanded,
//                onDismissRequest = { expanded = false }
//            ) {
//                mirageQuestTitleList.forEachIndexed { index, title ->
//                    DropdownMenuItem(
//                        text= {
//                            Text(title)
//                        },
//                        onClick = {
//                            mirageQuestState.selectQuest(index)
//                            expanded = false
//                        }
//                    )
//                }
//            }
//        }
//    }
//}
