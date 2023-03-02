package com.kasuminotes.ui.app.summons

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kasuminotes.ui.app.state.SummonsState

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
        content = { contentPadding ->
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(4.dp)
            ) {
                val summonDataList = summonsState.summonDataList
                val summonSize = summonDataList.size
                if (summonSize > 0) {
                    summonDataList.forEachIndexed { index, summonData ->
                        SummonDetail(
                            summonData.unitId,
                            null,
                            summonData.unitName,
                            summonData.searchAreaWidth,
                            summonData.atkType,
                            summonData.normalAtkCastTime,
                            summonData.property,
                            summonPropertyIndices,
                            summonData.unitAttackPatternList,
                            summonData.unitSkillData,
                            summonData.skillList
                        )
                        if (index != summonSize - 1) {
                            Divider(
                                modifier = Modifier.padding(vertical = 32.dp),
                                thickness = 2.dp
                            )
                        }
                    }
                }

                val minionDataList = summonsState.minionDataList
                val minionSize = minionDataList.size
                if (minionSize > 0) {
                    minionDataList.forEachIndexed { index, minionData ->
                        SummonDetail(
                            minionData.unitId,
                            minionData.enemyId,
                            minionData.name,
                            minionData.searchAreaWidth,
                            minionData.atkType,
                            minionData.normalAtkCastTime,
                            minionData.property,
                            minionPropertyIndices,
                            minionData.unitAttackPatternList,
                            minionData.unitSkillData,
                            minionData.skillList
                        )
                        if (index != minionSize - 1) {
                            Divider(
                                modifier = Modifier.padding(vertical = 32.dp),
                                thickness = 2.dp
                            )
                        }
                    }
                }
            }
        }
    )
}

