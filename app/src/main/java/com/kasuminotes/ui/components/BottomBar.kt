package com.kasuminotes.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kasuminotes.R

@Composable
fun BottomBar(
    selectedIndex: Int,
    onNavigateTo: (Int) -> Unit,
    onDrawerOpen: () -> Unit
) {
//    BottomAppBar(Modifier.navigationBarsPadding()) {
    NavigationBar(tonalElevation = 6.dp) {
        NavigationBarItem(
            selected = selectedIndex == 0,
            onClick = { if (selectedIndex != 0) onNavigateTo(0) },
            icon = { Icon(Icons.Filled.People, null) },
            label = { Text(stringResource(R.string.chara)) }
        )
        NavigationBarItem(
            selected = selectedIndex == 1,
            onClick = { if (selectedIndex != 1) onNavigateTo(1) },
            icon = { Icon(Icons.Filled.Gavel, null) },
            label = { Text(stringResource(R.string.equip)) }
        )
//        Spacer(Modifier.weight(1f))
        NavigationBarItem(
            selected = selectedIndex == 2,
            onClick = { if (selectedIndex != 2) onNavigateTo(2) },
            icon = { Icon(Icons.Filled.Security, null) },
            label = { Text(stringResource(R.string.clan_battle)) }
        )
        NavigationBarItem(
            selected = false,
            onClick = onDrawerOpen,
            icon = { Icon(Icons.Filled.ManageAccounts, null) },
            label = { Text(stringResource(R.string.user)) }
        )
    }
//    }
}