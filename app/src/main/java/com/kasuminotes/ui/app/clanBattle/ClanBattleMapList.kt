package com.kasuminotes.ui.app.clanBattle

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kasuminotes.R
import com.kasuminotes.data.ClanBattleMapData
import com.kasuminotes.data.EnemyData
import com.kasuminotes.ui.app.state.ClanBattleState
import com.kasuminotes.ui.components.BackButton
import com.kasuminotes.ui.components.TopBar
import com.kasuminotes.ui.components.TabsPager
import com.kasuminotes.ui.theme.phaseColors

@Composable
fun ClanBattleMapList(
    clanBattleState: ClanBattleState,
    onNavigateToEnemy: (EnemyData) -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopBar(
                title = { Text(clanBattleState.title) },
                navigationIcon = { BackButton(onBack) }
            )
        },
        bottomBar = {
            Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
        },
        content = { contentPadding ->
            Box(Modifier.padding(contentPadding)) {
                MapTabsPanel(
                    mapDataList = clanBattleState.clanBattlePeriod?.mapDataList,
                    onEnemyClick = onNavigateToEnemy
                )
            }
        }
    )
}

@Composable
private fun MapTabsPanel(
    mapDataList: List<ClanBattleMapData>?,
    onEnemyClick: (EnemyData) -> Unit
) {
    if (mapDataList != null) {
        val pagerState = rememberPagerState()
        val unSelectedColor = MaterialTheme.colorScheme.onSurface.copy(0.5f)
        val style = MaterialTheme.typography.titleSmall
        TabsPager(
            scrollable = false,
            pageCount = mapDataList.size,
            pagerState = pagerState,
            containerColor = Color.Transparent,
            contentColor = phaseColors[mapDataList.size - pagerState.currentPage - 1],
            tabContent = { page ->
                Text(
                    text = stringResource(R.string.phase_d, mapDataList[page].phase),
                    modifier = Modifier.padding(vertical = 14.dp),
                    color = if (page == pagerState.currentPage) Color.Unspecified else unSelectedColor,
                    style = style
                )
            },
            pageContent = { page ->
                EnemyList(
                    mapData = mapDataList[page],
                    onEnemyClick = onEnemyClick
                )
            }
        )
    }
}
