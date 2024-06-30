package com.kasuminotes.ui.app.dungeon

import androidx.compose.foundation.layout.Box
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
import com.kasuminotes.data.DungeonAreaData
import com.kasuminotes.ui.app.state.DungeonState
import com.kasuminotes.ui.components.BackButton
import com.kasuminotes.ui.components.TopBar

@Composable
fun Dungeon(
    dungeonState: DungeonState,
    onNavigateToEnemy: (enemyId: Int, talentWeaknessList: List<Int>) -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopBar(
                title = { Text(stringResource(R.string.dungeon)) },
                navigationIcon = { BackButton(onBack) }
            )
        },
        bottomBar = {
            Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
        },
        content = { contentPadding ->
            Box(Modifier.padding(contentPadding)) {
                DungeonAreaList(dungeonState, onNavigateToEnemy)
            }
        }
    )
}
