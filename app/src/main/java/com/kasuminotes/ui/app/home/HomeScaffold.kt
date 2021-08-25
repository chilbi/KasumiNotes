package com.kasuminotes.ui.app.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import coil.annotation.ExperimentalCoilApi
import com.kasuminotes.data.UserProfile
import com.kasuminotes.ui.app.BottomBar
import com.kasuminotes.ui.app.state.DbState
import com.kasuminotes.ui.app.state.UiState
import kotlinx.coroutines.launch

@ExperimentalCoilApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun HomeScaffold(
    uiState: UiState,
    dbState: DbState,
    onNavigateTo: (Int) -> Unit,
    onCharaClick: (UserProfile) -> Unit,
    onCharaEdit: () -> Unit,
    onImageChange: () -> Unit,
    onAboutClick: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()
    val imageState = uiState.charaImageState
    val userState = dbState.userState
    val listState = userState.charaListState

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            val openDrawer: () -> Unit = {
                scope.launch { scaffoldState.drawerState.open() }
            }
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
                openDrawer
            )
        },
        bottomBar = {
            BottomBar(0, onNavigateTo)
        },
        floatingActionButton = {
            FloatingActionButton(onCharaEdit) {
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
                dbState.latestAppURLFetching,
                uiState.language,
                uiState.darkTheme,
                onImageChange,
                userState::logOut,
                dbState::changeDbServer,
                dbState::fetchLastDbVersion,
                dbState::toggleDbAutoUpdate,
                uiState::changeLanguage,
                uiState::toggleDarkTheme,
                dbState::fetchLatestAppURL,
                dbState::toggleAppAutoUpdate,
                onAboutClick
            )
        },
        content = { contentPadding ->
            CharaList(
                uiState.charaImageState,
                listState.derivedProfiles,
                contentPadding,
                onCharaClick
            )
        }
    )
}
