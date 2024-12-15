package com.kasuminotes.ui.app.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.DrawerState
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kasuminotes.data.UserProfile
import com.kasuminotes.ui.components.BottomBar
import com.kasuminotes.ui.app.drawer.ModalDrawer
import com.kasuminotes.state.DbState
import com.kasuminotes.state.UiState
import com.kasuminotes.state.UserState

@Composable
fun HomeScaffold(
    drawerState: DrawerState,
    userState: UserState,
    dbState: DbState,
    uiState: UiState,
    onImageClick: () -> Unit,
    onEditClick: () -> Unit,
    onNavigateToChara: (UserProfile) -> Unit,
    onNavigateToAbout: () -> Unit,
    onNavigateTo: (Int) -> Unit,
    onDrawerOpen: () -> Unit
) {
    ModalDrawer(drawerState, userState, dbState, uiState, onImageClick, onNavigateToAbout) {
        Scaffold(
            topBar = {
                HomeTopBar(
                    userState,
                    uiState,
                    onDrawerOpen
                )
            },
            bottomBar = {
                BottomBar(0, onNavigateTo, onDrawerOpen)
            },
            floatingActionButton = {
                FloatingActionButton(onEditClick) {
                    Icon(Icons.Filled.Edit, null)
                }
            },
            containerColor = MaterialTheme.colorScheme.surface,
            content = { contentPadding ->
                Box(Modifier.padding(contentPadding)) {
                    CharaList(
                        uiState.charaImageState,
                        userState.charaListState.derivedProfiles,
                        userState.charaListState.orderBy,
                        onNavigateToChara
                    )
                }
            }
        )
    }
}
