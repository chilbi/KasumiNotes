package com.kasuminotes.ui.app.home

import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import com.kasuminotes.data.UserProfile
import com.kasuminotes.state.DbState
import com.kasuminotes.state.UiState

@Composable
fun Home(
    drawerState: DrawerState,
    uiState: UiState,
    dbState: DbState,
    onNavigateToImagesForChangeUserImage: () -> Unit,
    onNavigateToEditorForChangeUserProfiles: () -> Unit,
    onNavigateToImagesForSignInUserImage: () -> Unit,
    onNavigateToEditorForSignInUserProfiles: () -> Unit,
    onNavigateToChara: (UserProfile) -> Unit,
    onNavigateToAbout: () -> Unit,
    onNavigateTo: (Int) -> Unit,
    onDrawerOpen: () -> Unit
) {
    val userState = dbState.userState

    HomeScaffold(
        drawerState,
        userState,
        dbState,
        uiState,
        onNavigateToImagesForChangeUserImage,
        onNavigateToEditorForChangeUserProfiles,
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
                onImageClick = onNavigateToImagesForSignInUserImage,
                onEditClick = onNavigateToEditorForSignInUserProfiles,
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
