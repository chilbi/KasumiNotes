package com.kasuminotes.ui.app.summons

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kasuminotes.R
import com.kasuminotes.common.SummonMinion
import com.kasuminotes.data.ExtraEffectData
import com.kasuminotes.state.SummonsState
import com.kasuminotes.ui.components.BackButton
import com.kasuminotes.ui.components.TabsPager
import com.kasuminotes.ui.components.TopBar

private val summonPropertyIndices: List<Int> = listOf(1, 3, 5, 6, 2, 4, 16, 7, 0, 8, 13, 15, 14)
private val minionPropertyIndices: List<Int> = listOf(0, 16, 1, 3, 2, 4, 13, 15)

@Composable
fun Summons(
    summonsState: SummonsState,
    onBack: () -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    val toggle = { visible = !visible }

    Scaffold(
        topBar = { SummonsTopBar(summonsState.extraEffectData != null, visible, toggle, onBack) },
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
                    visible,
                    summonsState.extraEffectData,
                    summonMinionList,
                    propertyIndices
                )
            }
        }
    )
}

@Composable
private fun SummonsTopBar(
    isExtraEffect: Boolean,
    visible: Boolean,
    onToggle: () -> Unit,
    onBack: () -> Unit
) {
    TopBar(
        title = {
            Text(stringResource(if (isExtraEffect) R.string.extra_effect else R.string.summons_info))
        },
        navigationIcon = {
            BackButton(onBack)
        },
        actions = {
            if (isExtraEffect) {
                IconButton(onToggle) {
                    Icon(if (visible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff, null)
                }
            }
        }
    )
}

@Composable
private fun SummonsTabsPanel(
    visible: Boolean,
    extraEffectData: ExtraEffectData?,
    summonMinionList: List<SummonMinion>,
    propertyIndices: List<Int>
) {
    val isExtraEffect = extraEffectData != null
    val size = summonMinionList.size
    if (size > 0) {
        TabsPager(
            scrollable = size > 3,
            pagerState = rememberPagerState { size },
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.primary,
            edgePadding = 0.dp,
            tabContent = { page ->
                val summonMinion = summonMinionList[page]
                SummonHeader(
                    summonMinion.unitId,
                    summonMinion.enemyId,
                    summonMinion.name,
                    extraEffectData
                )
            },
            pageContent = { page ->
                val summonMinion = summonMinionList[page]
                SummonDetail(
                    isExtraEffect,
                    visible,
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
