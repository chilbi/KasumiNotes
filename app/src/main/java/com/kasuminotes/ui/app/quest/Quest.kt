package com.kasuminotes.ui.app.quest

import androidx.compose.runtime.Composable
import com.kasuminotes.ui.components.BottomBar
import com.kasuminotes.ui.app.state.QuestState
import com.kasuminotes.ui.components.SortIconButton

@Composable
fun Quest(
    questState: QuestState,
    onEquipClick: (Int) -> Unit,
    onNavigateTo: (Int) -> Unit,
    onDrawerOpen: () -> Unit
) {
    QuestScaffold(
        questState,
        onEquipClick,
        sortButton = {
            SortIconButton(questState.sortDesc, questState::toggleSortDesc)
        },
        bottomBar = {
            BottomBar(1, onNavigateTo, onDrawerOpen)
        }
    )
}
