package com.kasuminotes.ui.app.quest

import androidx.compose.runtime.Composable
import com.kasuminotes.ui.components.BottomBar
import com.kasuminotes.state.QuestState
import com.kasuminotes.ui.components.BackButton
import com.kasuminotes.ui.components.SortIconButton

@Composable
fun Quest(
    questState: QuestState,
    onEquipClick: (Int) -> Unit,
    onBack: () -> Unit
//    onNavigateTo: (Int) -> Unit,
//    onDrawerOpen: () -> Unit
) {
    QuestScaffold(
        questState,
        onEquipClick,
        sortButton = {
            SortIconButton(questState.sortDesc, questState::toggleSortDesc)
        },
        backButton = {
            BackButton(onBack)
        }
//        bottomBar = {
//            BottomBar(1, onNavigateTo, onDrawerOpen)
//        }
    )
}
