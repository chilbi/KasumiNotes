package com.kasuminotes.ui.app

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import coil.annotation.ExperimentalCoilApi
import com.kasuminotes.ui.app.about.About
import com.kasuminotes.ui.app.chara.Chara
import com.kasuminotes.ui.app.equip.Equip
import com.kasuminotes.ui.app.home.Home
import com.kasuminotes.ui.app.quest.Quest
import com.kasuminotes.ui.app.usereditor.CharaEditor
import com.kasuminotes.ui.app.usereditor.UserImages
import com.kasuminotes.ui.theme.KasumiNotesTheme

@ExperimentalComposeUiApi
@ExperimentalCoilApi
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun App(appViewModel: AppViewModel = viewModel()) {
    val uiState = appViewModel.uiState
    val dbState = appViewModel.dbState
    val userState = dbState.userState
    val charaState = appViewModel.charaState
    val equipState = appViewModel.equipState
    val questState = appViewModel.questState

    KasumiNotesTheme(uiState.darkTheme) {
        NavHost(appViewModel.navController, "home") {
            composable("home") {
                Home(
                    uiState,
                    dbState,
                    appViewModel::navigateTo,
                    appViewModel::navigateToChara,
                    appViewModel::navigateToImages,
                    appViewModel::navigateToEditor,
                    appViewModel::navigateToAbout
                )
            }
            composable("chara") {
                Chara(
                    charaState,
                    userState.maxUserData!!,
                    appViewModel::popBackStack,
                    appViewModel::navigateToEquip,
                    appViewModel::navigateToUnique
                )
            }
            composable("equip") {
                Equip(
                    dbState,
                    equipState,
                    appViewModel::popBackStack
                )
            }
            composable("quest") {
                Quest(
                    questState,
                    appViewModel::navigateTo,
                    appViewModel::navigateToEquipById
                )
            }
            composable("images") {
                UserImages(
                    userState,
                    appViewModel::userEditorBack,
                    appViewModel::confirmNewImage
                )
            }
            composable("editor") {
                CharaEditor(
                    userState,
                    appViewModel::userEditorBack,
                    appViewModel::confirmNewUserProfiles
                )
            }
            composable("about") {
                About(
                    appViewModel::popBackStack,
                    appViewModel::linkTo
                )
            }
        }
    }
}
