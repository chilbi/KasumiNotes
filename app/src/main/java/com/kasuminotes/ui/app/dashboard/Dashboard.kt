package com.kasuminotes.ui.app.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kasuminotes.R
import com.kasuminotes.ui.components.BottomBar
import com.kasuminotes.ui.components.VerticalGrid
import com.kasuminotes.ui.components.VerticalGridCells

@Composable
fun Dashboard(
    abyssLatestTalentId: Int,
    onNavigateToQuest: () -> Unit,
    onNavigateToClanBattle: () -> Unit,
    onNavigateToDungeon: () -> Unit,
    onNavigateToTalentQuest: () -> Unit,
    onNavigateToAbyssQuest: () -> Unit,
    onNavigateToMirageQuest: () -> Unit,
    onNavigateTo: (Int) -> Unit,
    onDrawerOpen: () -> Unit
) {
    val abyssQuestImageResId = when (abyssLatestTalentId) {
        1 -> R.drawable.abyss_quest_1
        2 -> R.drawable.abyss_quest_2
        3 -> R.drawable.abyss_quest_3
        4 -> R.drawable.abyss_quest_4
        else -> R.drawable.abyss_quest_5
    }
    val items = listOf(
        DashboardItem(R.drawable.quest, R.string.equip, onNavigateToQuest),
        DashboardItem(R.drawable.clan_battle, R.string.clan_battle, onNavigateToClanBattle),
        DashboardItem(R.drawable.dungeon, R.string.dungeon, onNavigateToDungeon),
        DashboardItem(R.drawable.talent_quest, R.string.talent_quest, onNavigateToTalentQuest),
        DashboardItem(abyssQuestImageResId, R.string.abyss_quest, onNavigateToAbyssQuest),
        DashboardItem(R.drawable.mirage_quest, R.string.mirage_quest, onNavigateToMirageQuest)
    )

    Scaffold(
        bottomBar = { BottomBar(1, onNavigateTo, onDrawerOpen) },
        containerColor = MaterialTheme.colorScheme.surface,
        content = { contentPadding ->
            VerticalGrid(
                size = items.size,
                cells = VerticalGridCells.Fixed(2),
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(contentPadding)
            ) { index ->
                val item = items[index]
                FilledTonalButton(
                    onClick = item.onNavigate,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(item.imageResId),
                            contentDescription = null,
                            modifier = Modifier.size(48.dp)
                        )
                        Text(stringResource(item.nameResId))
                    }
                }
            }
        }
    )
}

private data class DashboardItem(
    val imageResId: Int,
    val nameResId: Int,
    val onNavigate: () -> Unit
)
