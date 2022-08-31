package com.kasuminotes.ui.app

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.People
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kasuminotes.R

@Composable
fun BottomBar(
    selectedIndex: Int,
    onNavigateTo: (Int) -> Unit
) {
    BottomAppBar(Modifier.navigationBarsPadding()) {
        BottomNavigation(elevation = 0.dp) {
            BottomNavigationItem(
                selected = selectedIndex == 0,
                onClick = { onNavigateTo(0) },
                enabled = selectedIndex != 0,
                icon = { Icon(Icons.Filled.People, null) },
                label = { Text(stringResource(R.string.chara)) }
            )
            Spacer(Modifier.weight(1f))
            BottomNavigationItem(
                selected = selectedIndex == 1,
                onClick = { onNavigateTo(1) },
                enabled = selectedIndex != 1,
                icon = { Icon(Icons.Filled.Gavel, null) },
                label = { Text(stringResource(R.string.equip)) }
            )
        }
    }
}