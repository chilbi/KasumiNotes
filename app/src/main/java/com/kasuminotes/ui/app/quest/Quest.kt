package com.kasuminotes.ui.app.quest

import androidx.compose.runtime.Composable
import com.kasuminotes.state.QuestState
import com.kasuminotes.ui.components.BackButton
import com.kasuminotes.ui.components.SortIconButton

@Composable
fun Quest(
    questState: QuestState,
    onEquipClick: (Int) -> Unit,
    onBack: () -> Unit
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
    )
}
