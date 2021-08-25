package com.kasuminotes.ui.app.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import coil.annotation.ExperimentalCoilApi
import com.kasuminotes.data.UserProfile
import com.kasuminotes.ui.app.state.DbState
import com.kasuminotes.ui.app.state.UiState

@ExperimentalCoilApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun Home(
    uiState: UiState,
    dbState: DbState,
    onNavigateTo: (Int) -> Unit,
    onNavigateToChara: (UserProfile) -> Unit,
    onNavigateToImages: (allUserProfile: List<UserProfile>?, unlockedProfiles: List<UserProfile>) -> Unit,
    onNavigateToEditor: (allUserProfile: List<UserProfile>?, unlockedProfiles: List<UserProfile>) -> Unit,
    onAboutClick: () -> Unit
) {
    HomeScaffold(
        uiState,
        dbState,
        onNavigateTo,
        onNavigateToChara,
        onCharaEdit = {
            onNavigateToEditor(null, dbState.userState.charaListState.profiles)
        },
        onImageChange = {
            onNavigateToImages(null, dbState.userState.charaListState.profiles)
        },
        onAboutClick = onAboutClick
    )

    val userState = dbState.userState

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
