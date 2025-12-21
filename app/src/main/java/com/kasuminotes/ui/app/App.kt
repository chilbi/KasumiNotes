package com.kasuminotes.ui.app

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kasuminotes.state.AppViewModel
import com.kasuminotes.ui.app.about.About
import com.kasuminotes.ui.app.abyssQuest.AbyssQuest
import com.kasuminotes.ui.app.chara.Chara
import com.kasuminotes.ui.app.clanBattle.ClanBattle
import com.kasuminotes.ui.app.enemy.Enemy
import com.kasuminotes.ui.app.clanBattle.ClanBattleMapList
import com.kasuminotes.ui.app.dungeon.Dungeon
import com.kasuminotes.ui.app.equip.Equip
import com.kasuminotes.ui.app.exEquip.ExEquip
import com.kasuminotes.ui.app.home.Home
import com.kasuminotes.ui.app.dashboard.Dashboard
import com.kasuminotes.ui.app.mirageQuest.MirageQuest
import com.kasuminotes.ui.app.quest.Quest
import com.kasuminotes.ui.app.summons.Summons
import com.kasuminotes.ui.app.talentQuest.TalentQuest
import com.kasuminotes.ui.app.userEditor.CharaEditor
import com.kasuminotes.ui.app.userEditor.UserImages
import com.kasuminotes.ui.theme.KasumiNotesTheme
import kotlinx.coroutines.launch

@Composable
fun App(appViewModel: AppViewModel = viewModel()) {
    val uiState = appViewModel.uiState
    val dbState = appViewModel.dbState
    val userState = dbState.userState

    val navController = rememberNavController()
    
    LaunchedEffect(appViewModel) {
        dbState.init()
        appViewModel.navController = navController
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.route) {
                AppNavData.Home.route -> {
                    appViewModel.charaState.destroy()
                    dbState.userState.charaListState.destroy()
                }
            }
        }
    }

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

        NavHost(
            navController = navController,
            startDestination = AppNavData.Home.route,
            enterTransition = AppNavTransitions.enterTransition,
            exitTransition = AppNavTransitions.exitTransition
        ) {
            composable(
                route = AppNavData.Home.route,
                enterTransition = AppNavData.Home.enterTransition,
                exitTransition = AppNavData.Home.exitTransition
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
                route = AppNavData.Dashboard.route,
                enterTransition = AppNavData.Dashboard.enterTransition,
                exitTransition = AppNavData.Dashboard.exitTransition
            ) {
                Dashboard(
                    appViewModel.abyssQuestState.abyssLatestTalentId,
                    appViewModel::navigateToQuest,
                    appViewModel::navigateToClanBattle,
                    appViewModel::navigateToDungeon,
                    appViewModel::navigateToTalentQuest,
                    appViewModel::navigateToAbyssQuest,
                    appViewModel::navigateToMirageQuest,
                    appViewModel::navigateTo,
                    navigateToHomeAndOpenDrawer
                )
            }
            composable(
                route = AppNavData.Chara.route,
                enterTransition = AppNavData.Chara.enterTransition,
                exitTransition = AppNavData.Chara.exitTransition
            ) {
                Chara(
                    appViewModel.charaState,
                    appViewModel.dbState,
                    userState.maxUserData!!,
                    appViewModel::popBackStack,
                    appViewModel::navigateToEquip,
                    appViewModel::navigateToUnique,
                    appViewModel::navigateToExEquip,
                    appViewModel::navigateToSummons
                )
            }
            composable(
                route = AppNavData.Equip.route,
                enterTransition = AppNavData.Equip.enterTransition,
                exitTransition = AppNavData.Equip.exitTransition
            ) {
                Equip(
                    dbState,
                    appViewModel.equipState,
                    appViewModel::popBackStack
                )
            }
            composable(
                route = AppNavData.ExEquip.route,
                enterTransition = AppNavData.ExEquip.enterTransition,
                exitTransition = AppNavData.ExEquip.exitTransition
            ) {
                ExEquip(
                    appViewModel.exEquipState,
                    appViewModel::popBackStack
                )
            }
            composable(
                route = AppNavData.Summons.route,
                enterTransition = AppNavData.Summons.enterTransition,
                exitTransition = AppNavData.Summons.exitTransition
            ) {
                Summons(
                    appViewModel.summonsState,
                    appViewModel::popBackStack
                )
            }
            composable(
                route = AppNavData.Quest.route,
                enterTransition = AppNavData.Quest.enterTransition,
                exitTransition = AppNavData.Quest.exitTransition
            ) {
                Quest(
                    appViewModel.questState,
                    appViewModel::navigateToEquipById,
                    appViewModel::popBackStack
//                    appViewModel::navigateTo,
//                    navigateToHomeAndOpenDrawer
                )
            }
            composable(
                route = AppNavData.ClanBattle.route,
                enterTransition = AppNavData.ClanBattle.enterTransition,
                exitTransition = AppNavData.ClanBattle.exitTransition
            ) {
                ClanBattle(
                    appViewModel.clanBattleState,
                    appViewModel::navigateToMapList,
                    appViewModel::popBackStack
//                    appViewModel::navigateToDungeon,
//                    appViewModel::navigateToTalentQuest,
//                    appViewModel::navigateToAbyssQuest,
//                    appViewModel::navigateToMirageQuest,
//                    appViewModel::navigateTo,
//                    navigateToHomeAndOpenDrawer
                )
            }
            composable(
                route = AppNavData.ClanBattleMapList.route,
                enterTransition = AppNavData.ClanBattleMapList.enterTransition,
                exitTransition = AppNavData.ClanBattleMapList.exitTransition
            ) {
                ClanBattleMapList(
                    appViewModel.clanBattleState,
                    appViewModel::navigateToClanBattleEnemy,
                    appViewModel::popBackStack
                )
            }
            composable(
                route = AppNavData.Enemy.route,
                enterTransition = AppNavData.Enemy.enterTransition,
                exitTransition = AppNavData.Enemy.exitTransition
            ) {
                Enemy(
                    appViewModel.enemyState,
                    appViewModel::navigateToExtraEffect,
                    appViewModel::navigateToMinions,
                    appViewModel::popBackStack
                )
            }
            composable(
                route = AppNavData.Dungeon.route,
                enterTransition = AppNavData.Dungeon.enterTransition,
                exitTransition = AppNavData.Dungeon.exitTransition
            ) {
                Dungeon(
                    appViewModel.dungeonState,
                    appViewModel::navigateToDungeonEnemy,
                    appViewModel::popBackStack
                )
            }
            composable(
                route = AppNavData.TalentQuest.route,
                enterTransition = AppNavData.TalentQuest.enterTransition,
                exitTransition = AppNavData.TalentQuest.exitTransition
            ) {
                TalentQuest(
                    appViewModel.talentQuestState,
                    appViewModel::navigateToTalentQuestEnemy,
                    appViewModel::popBackStack
                )
            }
            composable(
                route = AppNavData.AbyssQuest.route,
                enterTransition = AppNavData.AbyssQuest.enterTransition,
                exitTransition = AppNavData.AbyssQuest.exitTransition
            ) {
                AbyssQuest(
                    appViewModel.abyssQuestState,
                    appViewModel::navigateToAbyssQuestEnemy,
                    appViewModel::popBackStack
                )
            }
            composable(
                route = AppNavData.MirageQuest.route,
                enterTransition = AppNavData.MirageQuest.enterTransition,
                exitTransition = AppNavData.MirageQuest.exitTransition
            ) {
                MirageQuest(
                    appViewModel.mirageQuestState,
                    appViewModel::navigateToMirageQuestEnemy,
                    appViewModel::popBackStack
                )
            }
            composable(
                route = AppNavData.About.route,
                enterTransition = AppNavData.About.enterTransition,
                exitTransition = AppNavData.About.exitTransition
            ) {
                About(
                    appViewModel::popBackStack,
                    appViewModel::linkTo
                )
            }
            composable(AppNavData.Images.route) {
                UserImages(
                    userState,
                    appViewModel::userEditorBack,
                    appViewModel::confirmNewImage
                )
            }
            composable(AppNavData.Editor.route) {
                CharaEditor(
                    userState,
                    appViewModel::userEditorBack,
                    appViewModel::confirmNewUserProfiles
                )
            }
        }
    }
}
