package com.kasuminotes.ui.app.summons

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
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
import com.kasuminotes.common.SummonMinion
import com.kasuminotes.ui.app.state.SummonsState
import com.kasuminotes.ui.components.BackButton
import com.kasuminotes.ui.components.TabsPager
import com.kasuminotes.ui.components.TopBar

private val summonPropertyIndices: List<Int> = listOf(
    1, 3, 5, 6, 2, 4, 16, 7, 0, 8, 9, 10, 13, 15, 14
)

private val minionPropertyIndices: List<Int> = listOf(0, 16, 1, 3, 2, 4, 13, 15)

@Composable
fun Summons(
    summonsState: SummonsState,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = { SummonsTopBar(onBack) },
        bottomBar = { Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars)) },
        containerColor = MaterialTheme.colorScheme.surface,
        content = { contentPadding ->
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
            ) {
                var propertyIndices = summonPropertyIndices
                val summonMinionList: List<SummonMinion> = summonsState.summonDataList.ifEmpty {
                    propertyIndices = minionPropertyIndices
                    summonsState.minionDataList
                }

                SummonsTabsPanel(
                    summonMinionList,
                    propertyIndices
                )
            }
        }
    )
}

@Composable
private fun SummonsTopBar(onBack: () -> Unit) {
    TopBar(
        title = { Text(stringResource(R.string.summons_info)) },
        navigationIcon = { BackButton(onBack) }
    )
}

@Composable
private fun SummonsTabsPanel(
    summonMinionList: List<SummonMinion>,
    propertyIndices: List<Int>
) {
    val size = summonMinionList.size
    if (size > 0) {
        TabsPager(
            scrollable = size > 3,
            pageCount = size,
            pagerState = rememberPagerState(),
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.primary,
            edgePadding = 0.dp,
            tabContent = { page ->
                val summonMinion = summonMinionList[page]
                SummonHeader(
                    summonMinion.unitId,
                    summonMinion.enemyId,
                    summonMinion.name
                )
            },
            pageContent = { page ->
                val summonMinion = summonMinionList[page]
                SummonDetail(
                    summonMinion.searchAreaWidth,
                    summonMinion.atkType,
                    summonMinion.normalAtkCastTime,
                    summonMinion.property,
                    summonMinion.unitAttackPatternList,
                    summonMinion.unitSkillData,
                    summonMinion.skillList,
                    propertyIndices
                )
            }
        )
    }
}
