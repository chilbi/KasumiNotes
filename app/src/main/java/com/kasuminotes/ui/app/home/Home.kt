package com.kasuminotes.ui.app.home

import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import com.kasuminotes.data.UserProfile
import com.kasuminotes.ui.app.state.DbState
import com.kasuminotes.ui.app.state.UiState

@Composable
fun Home(
    drawerState: DrawerState,
    uiState: UiState,
    dbState: DbState,
    onNavigateToImages: (allUserProfile: List<UserProfile>?, unlockedProfiles: List<UserProfile>) -> Unit,
    onNavigateToEditor: (allUserProfile: List<UserProfile>?, unlockedProfiles: List<UserProfile>) -> Unit,
    onNavigateToChara: (UserProfile) -> Unit,
    onNavigateToAbout: () -> Unit,
    onNavigateTo: (Int) -> Unit,
    onDrawerOpen: () -> Unit
) {
    val userState = dbState.userState

    val onImagesClick = {
        onNavigateToImages(null, userState.charaListState.profiles)
    }

    val onEditorClick = {
        onNavigateToEditor(null, userState.charaListState.profiles)
    }

    HomeScaffold(
        drawerState,
        userState,
        dbState,
        uiState,
        onImagesClick,
        onEditorClick,
        onNavigateToChara,
        onNavigateToAbout,
        onNavigateTo,
        onDrawerOpen
    )

    when {
        dbState.downloadState != null -> {
            DownloadDialog(
                dbState.downloadState!!,
                dbState::retryDownload,
                dbState::cancelDownload
            )
        }
        dbState.newDbVersion != null -> {
            UpdateDbDialog(
                dbState.newDbVersion!!,
                dbState::updateDb,
                dbState::cancelUpdateDb
            )
        }
        dbState.newAppReleaseInfo != null -> {
            UpdateAppDialog(
                dbState.newAppReleaseInfo!!,
                dbState::updateApp,
                dbState::cancelUpdateApp
            )
        }
        dbState.isLastDb -> {
            IsLastDbDialog(
                dbState::confirmIsLastDb,
                dbState::reDownload
            )
        }
        dbState.isLatestApp -> {
            IsLatestAppDialog(
                dbState::confirmIsLatestApp
            )
        }
        userState.allProfiles != null -> {
            SignInDialog(
                newUserId = userState.newUserId,
                newUserName = userState.newUserName,
                charaCount = userState.newProfiles?.size ?: 0,
                maxChara = userState.allProfiles?.size ?: 0,
                onImageSelect = {
                    onNavigateToImages(
                        userState.allProfiles,
                        emptyList()
                    )
                },
                onCharaEdit = {
                    onNavigateToEditor(
                        userState.allProfiles,
                        userState.newProfiles ?: emptyList()
                    )
                },
                onClose = userState::closeSignIn,
                onSignIn = userState::signIn
            )
        }
        userState.userList != null -> {
            LogInDialog(
                userList = userState.userList!!,
                maxChara = userState.maxUserData!!.maxChara,
                onClose = userState::closeLogIn,
                onSignOpen = userState::openSignIn,
                onLogIn = userState::logIn,
                onDelete = userState::deleteUser
            )
        }
    }
}
