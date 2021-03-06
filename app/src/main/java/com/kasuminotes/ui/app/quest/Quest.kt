package com.kasuminotes.ui.app.quest

import androidx.compose.runtime.Composable
import com.kasuminotes.ui.app.BottomBar
import com.kasuminotes.ui.app.state.QuestState
import com.kasuminotes.ui.components.SortIconButton

@Composable
fun Quest(
    questState: QuestState,
    onNavigateTo: (Int) -> Unit,
    onEquipClick: (Int) -> Unit
) {
    QuestScaffold(
        questState,
        onEquipClick,
        sortButton = {
            SortIconButton(questState.sortDesc, questState::toggleSortDesc)
        },
        bottomBar = {
            BottomBar(1, onNavigateTo)
        }
    )
}
