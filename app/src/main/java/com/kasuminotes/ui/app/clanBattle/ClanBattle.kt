package com.kasuminotes.ui.app.clanBattle

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Security
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kasuminotes.R
import com.kasuminotes.ui.app.BottomBar
import com.kasuminotes.ui.components.ImmersiveTopAppBar

@Composable
fun ClanBattle(
    onNavigateTo: (Int) -> Unit,
    onDrawerOpen: () -> Unit
) {
    Scaffold(
        topBar = {
            ImmersiveTopAppBar(
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
                }
            )
        },
        bottomBar = {
            BottomBar(2, onNavigateTo, onDrawerOpen)
        },
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
            Box(Modifier.padding(contentPadding)) {}
        }
    )
}