package com.kasuminotes.ui.app.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kasuminotes.data.UserProfile
import com.kasuminotes.ui.app.BottomBar
import com.kasuminotes.ui.app.state.DbState
import com.kasuminotes.ui.app.state.UiState
import com.kasuminotes.ui.app.state.UserState

@Composable
fun HomeScaffold(
    scaffoldState: ScaffoldState,
    userState: UserState,
    dbState: DbState,
    uiState: UiState,
    onImagesClick: () -> Unit,
    onEditorClick: () -> Unit,
    onNavigateToChara: (UserProfile) -> Unit,
    onNavigateToAbout: () -> Unit,
    onNavigateTo: (Int) -> Unit,
    onDrawerOpen: () -> Unit
) {
    val imageState = uiState.charaImageState
    val listState = userState.charaListState

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            HomeTopBar(
                userState.userId,
                imageState.vector,
                listState.searchText,
                listState.atkType,
                listState.position,
                listState.orderBy,
                listState.sortDesc,
                uiState::toggleImageVariant,
                listState::changeSearchText,
                listState::changeAtkType,
                listState::changePosition,
                listState::changeOrderBy,
                onDrawerOpen
            )
        },
        bottomBar = {
            BottomBar(0, onNavigateTo, onDrawerOpen)
        },
        floatingActionButton = {
            FloatingActionButton(onEditorClick) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = null,
                    tint = MaterialTheme.colors.onSecondary
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true,
        drawerContent = {
            HomeDrawer(
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
        content = { contentPadding ->
            Box(Modifier.padding(contentPadding)) {
                CharaList(
                    uiState.charaImageState,
                    listState.derivedProfiles,
                    listState.orderBy,
                    onNavigateToChara
                )
            }
        }
    )
}