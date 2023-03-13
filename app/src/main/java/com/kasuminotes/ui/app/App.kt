package com.kasuminotes.ui.app

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
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

    KasumiNotesTheme(uiState.darkTheme) {
        val drawerState = rememberDrawerState(DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        val openDrawer: () -> Unit = {
            scope.launch { drawerState.open() }
        }
        val navigateToHomeAndOpenDrawer = {
            appViewModel.popBackStack()
            openDrawer()
        }

        AnimatedNavHost(
            navController = appViewModel.navController,
            startDestination = NavGraph.Home.route,
            enterTransition = NavTransitions.defaultEnterTransition,
            exitTransition = NavTransitions.defaultExitTransition
        ) {
            composable(
                route = NavGraph.Home.route,
                enterTransition = NavGraph.Home.enterTransition,
                exitTransition = NavGraph.Home.exitTransition
            ) {
                Home(
                    drawerState,
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
            composable(
                route = NavGraph.Chara.route,
                enterTransition = NavGraph.Chara.enterTransition,
                exitTransition = NavGraph.Chara.exitTransition
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
                route = NavGraph.Equip.route,
                enterTransition = NavGraph.Equip.enterTransition,
                exitTransition = NavGraph.Equip.exitTransition
            ) {
                Equip(
                    dbState,
                    appViewModel.equipState,
                    appViewModel::popBackStack
                )
            }
            composable(
                route = NavGraph.ExEquip.route,
                enterTransition = NavGraph.ExEquip.enterTransition,
                exitTransition = NavGraph.ExEquip.exitTransition
            ) {
                ExEquip(
                    appViewModel.exEquipState,
                    appViewModel::popBackStack
                )
            }
            composable(
                route = NavGraph.Summons.route,
                enterTransition = NavGraph.Summons.enterTransition,
                exitTransition = NavGraph.Summons.exitTransition
            ) {
                Summons(
                    appViewModel.summonsState,
                    appViewModel::popBackStack
                )
            }
            composable(
                route = NavGraph.Quest.route,
                enterTransition = NavGraph.Quest.enterTransition,
                exitTransition = NavGraph.Quest.exitTransition
            ) {
                Quest(
                    appViewModel.questState,
                    appViewModel::navigateToEquipById,
                    appViewModel::navigateTo,
                    navigateToHomeAndOpenDrawer
                )
            }
            composable(
                route = NavGraph.ClanBattle.route,
                enterTransition = NavGraph.ClanBattle.enterTransition,
                exitTransition = NavGraph.ClanBattle.exitTransition
            ) {
                ClanBattle(
                    appViewModel.clanBattleState,
                    appViewModel::navigateToMapList,
                    appViewModel::navigateTo,
                    navigateToHomeAndOpenDrawer
                )
            }
            composable(
                route = NavGraph.ClanBattleMapList.route,
                enterTransition = NavGraph.ClanBattleMapList.enterTransition,
                exitTransition = NavGraph.ClanBattleMapList.exitTransition
            ) {
                ClanBattleMapList(
                    appViewModel.clanBattleState,
                    appViewModel::navigateToEnemy,
                    appViewModel::popBackStack
                )
            }
            composable(
                route = NavGraph.ClanBattleEnemy.route,
                enterTransition = NavGraph.ClanBattleEnemy.enterTransition,
                exitTransition = NavGraph.ClanBattleEnemy.exitTransition
            ) {
                ClanBattleEnemy(
                    appViewModel.clanBattleState,
                    appViewModel::navigateToMinions,
                    appViewModel::popBackStack
                )
            }
            composable(
                route = NavGraph.About.route,
                enterTransition = NavGraph.About.enterTransition,
                exitTransition = NavGraph.About.exitTransition
            ) {
                About(
                    appViewModel::popBackStack,
                    appViewModel::linkTo
                )
            }
            composable(NavGraph.Images.route) {
                UserImages(
                    userState,
                    appViewModel::userEditorBack,
                    appViewModel::confirmNewImage
                )
            }
            composable(NavGraph.Editor.route) {
                CharaEditor(
                    userState,
                    appViewModel::userEditorBack,
                    appViewModel::confirmNewUserProfiles
                )
            }
        }
    }
}
