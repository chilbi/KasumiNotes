package com.kasuminotes.ui.app.quest

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
    var openSheet by rememberSaveable { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    val floatingActionButton: @Composable () -> Unit = {
        FloatingActionButton(onClick = { openSheet = !openSheet }) {
            Icon(Icons.Filled.Dashboard, null)
        }
    }

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

    if (openSheet) {
        ModalBottomSheet(
            onDismissRequest = { openSheet = false },
            sheetState = sheetState
        ) {
            QuestBottomSheet(
                questMode = questState.questMode,
                onQuestModeChange = { questMode ->
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) openSheet = false
                        questState.changeQuestMode(questMode)
                    }
                }
            )
        }
    }
}
