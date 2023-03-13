package com.kasuminotes.ui.app.drawer

import androidx.activity.compose.BackHandler
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.kasuminotes.ui.app.state.DbState
import com.kasuminotes.ui.app.state.UiState
import com.kasuminotes.ui.app.state.UserState
import kotlinx.coroutines.launch

@Composable
fun ModalDrawer(
    drawerState: DrawerState,
    userState: UserState,
    dbState: DbState,
    uiState: UiState,
    onImagesClick: () -> Unit,
    onNavigateToAbout: () -> Unit,
    content: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()

    BackHandler(drawerState.isOpen) {
        scope.launch { drawerState.close() }
    }

    ModalNavigationDrawer(
        drawerContent = {
            DrawerContent(
                userState.userId,
                userState.userName,
                userState.maxUserData,
                dbState.dbServer,
                dbState.dbVersion,
                dbState.appAutoUpdate,
                dbState.dbAutoUpdate,
                dbState.lastVersionFetching,
                dbState.latestAppReleaseInfoFetching,
                uiState.language,
                uiState.darkTheme,
                onImagesClick,
                userState::logOut,
                dbState::changeDbServer,
                dbState::fetchLastDbVersion,
                dbState::toggleDbAutoUpdate,
                uiState::changeLanguage,
                uiState::toggleDarkTheme,
                dbState::fetchLatestAppReleaseInfo,
                dbState::toggleAppAutoUpdate,
                onNavigateToAbout
            )
        },
        drawerState = drawerState,
        content = content
    )
}
