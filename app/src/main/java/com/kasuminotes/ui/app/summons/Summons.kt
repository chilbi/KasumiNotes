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
                val size = summonDataList.size
                if (size > 0) {
                    summonDataList.forEachIndexed { index, summonData ->
                        SummonDetail(summonData)
                        if (index != size - 1) {
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

