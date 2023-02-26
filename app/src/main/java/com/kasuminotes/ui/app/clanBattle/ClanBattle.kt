package com.kasuminotes.ui.app.clanBattle

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kasuminotes.data.ClanBattlePeriod
import com.kasuminotes.ui.app.BottomBar
import com.kasuminotes.ui.app.state.ClanBattleState

@Composable
fun ClanBattle(
    clanBattleState: ClanBattleState,
    onNavigateToMapList: (label: String, period: ClanBattlePeriod) -> Unit,
    onNavigateTo: (Int) -> Unit,
    onDrawerOpen: () -> Unit
) {
    Scaffold(
        topBar = { ClanBattleTopBar() },
        bottomBar = { BottomBar(2, onNavigateTo, onDrawerOpen) },
        floatingActionButton = {
            FloatingActionButton({}) {
                Icon(
                    imageVector = Icons.Filled.Dashboard,
                    contentDescription = null,
                    tint = MaterialTheme.colors.onSecondary
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true,
        content = { contentPadding ->
            Box(Modifier.padding(contentPadding)) {
                ClanBattlePeriodList(clanBattleState.clanBattlePeriodList, onNavigateToMapList)
            }
        }
    )
}
