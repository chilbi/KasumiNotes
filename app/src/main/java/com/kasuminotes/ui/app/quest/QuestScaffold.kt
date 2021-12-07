package com.kasuminotes.ui.app.quest

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomDrawer
import androidx.compose.material.BottomDrawerValue
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.rememberBottomDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.dp
import com.kasuminotes.common.QuestMode
import com.kasuminotes.ui.app.state.QuestState
import kotlinx.coroutines.launch

@Composable
fun QuestScaffold(
    questState: QuestState,
    onEquipClick: (Int) -> Unit,
    sortButton: @Composable () -> Unit,
    bottomBar: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()
    val drawerState = rememberBottomDrawerState(BottomDrawerValue.Closed)

    val floatingActionButton: @Composable () -> Unit = {
        FloatingActionButton(onClick = {
            scope.launch { drawerState.open() }
        }) {
            Icon(
                imageVector = Icons.Filled.Dashboard,
                contentDescription = null,
                tint = MaterialTheme.colors.onSecondary
            )
        }
    }

    BottomDrawer(
        drawerContent = {
            QuestDrawer(
                questMode = questState.questMode,
                onQuestModeChange = { questMode ->
                    scope.launch {
                        scope.launch { drawerState.close() }
                        questState.changeQuestMode(questMode)
                    }
                }
            )
        },
        drawerState = drawerState,
        drawerShape = RoundedCornerShape(8.dp, 8.dp),
        gesturesEnabled = false,
        content = {
            when (questState.questMode) {
                QuestMode.Equip -> {
                    QuestEquip(
                        questState.equipmentPairList,
                        questState.typePairList,
                        questState.equipTypes,
                        questState::changeEquipTypes,
                        questState::changeAllEquipTypes,
                        onEquipClick,
                        bottomBar,
                        floatingActionButton
                    )
                }
                QuestMode.Search -> {
                    QuestSearch(
                        questState.visitIndex,
                        questState.questDataList,
                        questState.equipMaterialPairList,
                        questState.memoryPieces,
                        questState.searchId,
                        questState.searchSet,
                        questState.searches,
                        questState.searchedList,
                        questState.searchTypes,
                        questState.maxArea > 36,
                        questState.min37,
                        questState::search,
                        questState::addSearches,
                        questState::delSearches,
                        questState::changeSearchId,
                        questState::changeSearchTypes,
                        questState::changeSearches,
                        questState::toggleMin37,
                        questState::toggleVisitIndex,
                        sortButton,
                        bottomBar,
                        floatingActionButton
                    )
                }
                QuestMode.Map -> {
                    QuestMap(
                        questState.questDataList,
                        questState.highlightList,
                        questState.questType,
                        questState.maxArea,
                        questState.area,
                        questState::changeQuestType,
                        questState::changeHighlightList,
                        questState::changeArea,
                        sortButton,
                        bottomBar,
                        floatingActionButton
                    )
                }
            }
        }
    )
}
