package com.kasuminotes.ui.app.clanBattle

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kasuminotes.R
import com.kasuminotes.data.ClanBattlePeriod
import com.kasuminotes.state.ClanBattleState
import com.kasuminotes.ui.components.BackButton
import com.kasuminotes.ui.components.TopBar

@Composable
fun ClanBattle(
    clanBattleState: ClanBattleState,
    onNavigateToMapList: (label: String, period: ClanBattlePeriod) -> Unit,
    onBack: () -> Unit
//    onNavigateToDungeon: () -> Unit,
//    onNavigateToTalentQuest: () -> Unit,
//    onNavigateToAbyssQuest: () -> Unit,
//    onNavigateToMirageQuest: () -> Unit,
//    onNavigateTo: (Int) -> Unit,
//    onDrawerOpen: () -> Unit
) {
    Scaffold(
        topBar = {
            TopBar(
                title = {
                    Text(stringResource(R.string.clan_battle))
                },
                navigationIcon = {
                    BackButton(onBack)
                }
            )
//            ClanBattleTopBar(
//                onNavigateToDungeon,
//                onNavigateToTalentQuest,
//                onNavigateToAbyssQuest,
//                onNavigateToMirageQuest
//            )
        },
//        bottomBar = { BottomBar(2, onNavigateTo, onDrawerOpen) },
        containerColor = MaterialTheme.colorScheme.surface,
        content = { contentPadding ->
            Box(Modifier.padding(contentPadding)) {
                ClanBattlePeriodList(clanBattleState, onNavigateToMapList)
            }
        }
    )
}

@Composable
private fun ClanBattleTopBar(
    onNavigateToDungeon: () -> Unit,
    onNavigateToTalentQuest: () -> Unit,
    onNavigateToAbyssQuest: () -> Unit,
    onNavigateToMirageQuest: () -> Unit
) {
    TopBar(
        title = {
            Text(stringResource(R.string.clan_battle))
        },
        navigationIcon = {
            Box(
                modifier = Modifier.size(48.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Security, null)
            }
        },
        actions = {
            val arr = arrayOf(
                R.string.dungeon to onNavigateToDungeon,
                R.string.talent_quest to onNavigateToTalentQuest,
                R.string.abyss_quest to onNavigateToAbyssQuest,
                R.string.mirage_quest to onNavigateToMirageQuest
            )
            var expanded by remember { mutableStateOf(false) }

            Box(modifier = Modifier.wrapContentSize(Alignment.TopStart)) {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Filled.Menu, null)
                }
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    arr.forEach { pair ->
                        DropdownMenuItem(
                            text = { Text(stringResource(pair.first)) },
                            onClick = {
                                expanded = false
                                pair.second.invoke()
                            },
                            leadingIcon = { Icon(Icons.AutoMirrored.Filled.ArrowForward, null) }
                        )
                    }
                }
            }
        }
    )
}
