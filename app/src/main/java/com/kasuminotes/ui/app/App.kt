package com.kasuminotes.ui.app

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.kasuminotes.ui.app.about.About
import com.kasuminotes.ui.app.chara.Chara
import com.kasuminotes.ui.app.clanBattle.ClanBattle
import com.kasuminotes.ui.app.clanBattle.ClanBattleEnemy
import com.kasuminotes.ui.app.clanBattle.ClanBattleMapList
import com.kasuminotes.ui.app.equip.Equip
import com.kasuminotes.ui.app.exEquip.ExEquip
import com.kasuminotes.ui.app.home.Home
import com.kasuminotes.ui.app.quest.Quest
import com.kasuminotes.ui.app.summons.Summons
import com.kasuminotes.ui.app.userEditor.CharaEditor
import com.kasuminotes.ui.app.userEditor.UserImages
import com.kasuminotes.ui.theme.KasumiNotesTheme
import kotlinx.coroutines.launch

@Composable
fun App(appViewModel: AppViewModel = viewModel()) {
    val uiState = appViewModel.uiState
    val dbState = appViewModel.dbState
    val userState = dbState.userState

    KasumiNotesTheme(uiState.themeIndex, uiState.darkTheme) {
        val drawerState = rememberDrawerState(DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        val openDrawer = remember<() -> Unit> {{
            scope.launch { drawerState.open() }
        }}
        val navigateToHomeAndOpenDrawer = remember {{
            appViewModel.navigateTo(0)
            openDrawer()
        }}

        AnimatedNavHost(
            navController = appViewModel.navController,
            startDestination = AppNavGraph.Home.route,
            enterTransition = NavTransitions.defaultEnterTransition,
            exitTransition = NavTransitions.defaultExitTransition
        ) {
            composable(
                route = AppNavGraph.Home.route,
                enterTransition = AppNavGraph.Home.enterTransition,
                exitTransition = AppNavGraph.Home.exitTransition
            ) {
                Home(
                    drawerState,
                    uiState,
                    dbState,
                    appViewModel::navigateToImagesForChangeUserImage,
                    appViewModel::navigateToEditorForChangeUserProfiles,
                    appViewModel::navigateToImagesForSignInUserImage,
                    appViewModel::navigateToEditorForSignInUserProfiles,
                    appViewModel::navigateToChara,
                    appViewModel::navigateToAbout,
                    appViewModel::navigateTo,
                    openDrawer
                )
            }
            composable(
                route = AppNavGraph.Chara.route,
                enterTransition = AppNavGraph.Chara.enterTransition,
                exitTransition = AppNavGraph.Chara.exitTransition
            ) {
                Chara(
                    appViewModel.charaState,
                    userState.maxUserData!!,
                    appViewModel::popBackStack,
                    appViewModel::navigateToEquip,
                    appViewModel::navigateToUnique,
                    appViewModel::navigateToExEquip,
                    appViewModel::navigateToSummons
                )
            }
            composable(
                route = AppNavGraph.Equip.route,
                enterTransition = AppNavGraph.Equip.enterTransition,
                exitTransition = AppNavGraph.Equip.exitTransition
            ) {
                Equip(
                    dbState,
                    appViewModel.equipState,
                    appViewModel::popBackStack
                )
            }
            composable(
                route = AppNavGraph.ExEquip.route,
                enterTransition = AppNavGraph.ExEquip.enterTransition,
                exitTransition = AppNavGraph.ExEquip.exitTransition
            ) {
                ExEquip(
                    appViewModel.exEquipState,
                    appViewModel::popBackStack
                )
            }
            composable(
                route = AppNavGraph.Summons.route,
                enterTransition = AppNavGraph.Summons.enterTransition,
                exitTransition = AppNavGraph.Summons.exitTransition
            ) {
                Summons(
                    appViewModel.summonsState,
                    appViewModel::popBackStack
                )
            }
            composable(
                route = AppNavGraph.Quest.route,
                enterTransition = AppNavGraph.Quest.enterTransition,
                exitTransition = AppNavGraph.Quest.exitTransition
            ) {
                Quest(
                    appViewModel.questState,
                    appViewModel::navigateToEquipById,
                    appViewModel::navigateTo,
                    navigateToHomeAndOpenDrawer
                )
            }
            composable(
                route = AppNavGraph.ClanBattle.route,
                enterTransition = AppNavGraph.ClanBattle.enterTransition,
                exitTransition = AppNavGraph.ClanBattle.exitTransition
            ) {
                ClanBattle(
                    appViewModel.clanBattleState,
                    appViewModel::navigateToMapList,
                    appViewModel::navigateTo,
                    navigateToHomeAndOpenDrawer
                )
            }
            composable(
                route = AppNavGraph.ClanBattleMapList.route,
                enterTransition = AppNavGraph.ClanBattleMapList.enterTransition,
                exitTransition = AppNavGraph.ClanBattleMapList.exitTransition
            ) {
                ClanBattleMapList(
                    appViewModel.clanBattleState,
                    appViewModel::navigateToEnemy,
                    appViewModel::popBackStack
                )
            }
            composable(
                route = AppNavGraph.ClanBattleEnemy.route,
                enterTransition = AppNavGraph.ClanBattleEnemy.enterTransition,
                exitTransition = AppNavGraph.ClanBattleEnemy.exitTransition
            ) {
                ClanBattleEnemy(
                    appViewModel.clanBattleState,
                    appViewModel::navigateToMinions,
                    appViewModel::popBackStack
                )
            }
            composable(
                route = AppNavGraph.About.route,
                enterTransition = AppNavGraph.About.enterTransition,
                exitTransition = AppNavGraph.About.exitTransition
            ) {
                About(
                    appViewModel::popBackStack,
                    appViewModel::linkTo
                )
            }
            composable(AppNavGraph.Images.route) {
                UserImages(
                    userState,
                    appViewModel::userEditorBack,
                    appViewModel::confirmNewImage
                )
            }
            composable(AppNavGraph.Editor.route) {
                CharaEditor(
                    userState,
                    appViewModel::userEditorBack,
                    appViewModel::confirmNewUserProfiles
                )
            }
        }
    }
}
