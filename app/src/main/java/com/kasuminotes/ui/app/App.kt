package com.kasuminotes.ui.app

import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kasuminotes.ui.app.about.About
import com.kasuminotes.ui.app.chara.Chara
import com.kasuminotes.ui.app.clanBattle.ClanBattle
import com.kasuminotes.ui.app.equip.Equip
import com.kasuminotes.ui.app.exEquip.ExEquip
import com.kasuminotes.ui.app.home.Home
import com.kasuminotes.ui.app.quest.Quest
import com.kasuminotes.ui.app.summons.Summons
import com.kasuminotes.ui.app.usereditor.CharaEditor
import com.kasuminotes.ui.app.usereditor.UserImages
import com.kasuminotes.ui.theme.KasumiNotesTheme
import kotlinx.coroutines.launch

@Composable
fun App(appViewModel: AppViewModel = viewModel()) {
    val uiState = appViewModel.uiState
    val dbState = appViewModel.dbState
    val userState = dbState.userState
    val charaState = appViewModel.charaState
    val equipState = appViewModel.equipState
    val questState = appViewModel.questState
    val exEquipState = appViewModel.exEquipState
    val summonsState = appViewModel.summonsState

    KasumiNotesTheme(uiState.darkTheme) {
        val scaffoldState = rememberScaffoldState()
        val scope = rememberCoroutineScope()
        val openDrawer: () -> Unit = {
            scope.launch { scaffoldState.drawerState.open() }
        }
        val navigateToHomeAndOpenDrawer = {
            appViewModel.popBackStack()
            openDrawer()
        }

        NavHost(appViewModel.navController, "home") {
            composable("home") {
                Home(
                    scaffoldState,
                    uiState,
                    dbState,
                    appViewModel::navigateToImages,
                    appViewModel::navigateToEditor,
                    appViewModel::navigateToChara,
                    appViewModel::navigateToAbout,
                    appViewModel::navigateTo,
                    openDrawer
                )
            }
            composable("chara") {
                Chara(
                    charaState,
                    userState.maxUserData!!,
                    appViewModel::popBackStack,
                    appViewModel::navigateToEquip,
                    appViewModel::navigateToUnique,
                    appViewModel::navigateToExEquip,
                    appViewModel::navigateToSummons
                )
            }
            composable("equip") {
                Equip(
                    dbState,
                    equipState,
                    appViewModel::popBackStack
                )
            }
            composable("exEquip") {
                ExEquip(
                    exEquipState,
                    appViewModel::popBackStack
                )
            }
            composable("summons") {
                Summons(
                    summonsState,
                    appViewModel::popBackStack
                )
            }
            composable("quest") {
                Quest(
                    questState,
                    appViewModel::navigateToEquipById,
                    appViewModel::navigateTo,
                    navigateToHomeAndOpenDrawer
                )
            }
            composable("clanBattle") {
                ClanBattle(
                    appViewModel::navigateTo,
                    navigateToHomeAndOpenDrawer
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
