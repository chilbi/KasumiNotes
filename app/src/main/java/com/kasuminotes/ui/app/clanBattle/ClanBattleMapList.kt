package com.kasuminotes.ui.app.clanBattle

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kasuminotes.R
import com.kasuminotes.data.ClanBattleMapData
import com.kasuminotes.ui.app.state.ClanBattleState
import com.kasuminotes.ui.components.BackButton
import com.kasuminotes.ui.components.ImmersiveTopAppBar
import com.kasuminotes.ui.components.TabsPanel
import com.kasuminotes.ui.theme.phaseColors

@Composable
fun ClanBattleMapList(
    clanBattleState: ClanBattleState,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            ImmersiveTopAppBar(
                title = { Text(clanBattleState.title) },
                navigationIcon = { BackButton(onBack) }
            )
        },
        bottomBar = {
            Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
        },
        content = { contentPadding ->
            Box(Modifier.padding(contentPadding)) {
                MapTabsPanel(clanBattleState.clanBattlePeriod?.mapDataList)
            }
        }
    )
}

@Composable
private fun MapTabsPanel(
    mapDataList: List<ClanBattleMapData>?
) {
    if (mapDataList != null) {
        var selectedTabIndex by rememberSaveable { mutableStateOf(0) }
        val unSelectedColor = MaterialTheme.colors.onSurface.copy(0.5f)
        TabsPanel(
            size = mapDataList.size,
            scrollable = false,
            selectedTabIndex = selectedTabIndex,
            onTabIndexSelected = { selectedTabIndex = it },
            backgroundColor = Color.Transparent,
            contentColor = phaseColors[mapDataList.size - selectedTabIndex - 1],
            tabContentFor = { index ->
                Text(
                    text = stringResource(R.string.phase_d, mapDataList[index].phase),
                    modifier = Modifier.padding(vertical = 14.dp),
                    color = if (index == selectedTabIndex) Color.Unspecified else unSelectedColor,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            },
            panelContentFor = { index ->
                EnemyList(mapDataList[index])
            }
        )
    }
}
