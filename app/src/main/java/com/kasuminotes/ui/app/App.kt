package com.kasuminotes.ui.app

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material.rememberScaffoldState
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
import com.kasuminotes.ui.app.usereditor.CharaEditor
import com.kasuminotes.ui.app.usereditor.UserImages
import com.kasuminotes.ui.theme.KasumiNotesTheme
import kotlinx.coroutines.launch

private const val durationMillis = 500

@Composable
fun App(appViewModel: AppViewModel = viewModel()) {
    val uiState = appViewModel.uiState
    val dbState = appViewModel.dbState
    val userState = dbState.userState

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

        AnimatedNavHost(
            navController = appViewModel.navController,
            startDestination = "home",
            enterTransition = { fadeIn(tween(durationMillis)) },
            exitTransition = { fadeOut(tween(durationMillis)) }
        ) {
            composable(
                route = "home",
                enterTransition = {
                    if (
                        initialState.destination.route == "chara" ||
                        initialState.destination.route == "about"
                    ) {
                        slideInHorizontally(tween(durationMillis)) { -it }
                    } else {
                        null
                    }
                },
                exitTransition = {
                    if (
                        targetState.destination.route == "chara" ||
                        targetState.destination.route == "about"
                    ) {
                        slideOutHorizontally(tween(durationMillis)) {-it }
                    } else {
                        null
                    }
                }
            ) {
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
            composable(
                route = "chara",
                enterTransition = {
                    if (initialState.destination.route == "home") {
                        slideInHorizontally(tween(durationMillis)) { it }
                    } else {
                        slideInHorizontally(tween(durationMillis)) { -it }
                    }
                },
                exitTransition = {
                    if (targetState.destination.route == "home") {
                        slideOutHorizontally(tween(durationMillis)) { it }
                    } else {
                        slideOutHorizontally(tween(durationMillis)) { -it }
                    }
                }
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
                route = "equip",
                enterTransition = {
                    slideInHorizontally(tween(durationMillis)) { it }
                },
                exitTransition = {
                    slideOutHorizontally(tween(durationMillis)) { it }
                }
            ) {
                Equip(
                    dbState,
                    appViewModel.equipState,
                    appViewModel::popBackStack
                )
            }
            composable(
                route = "exEquip",
                enterTransition = {
                    slideInHorizontally(tween(durationMillis)) { it }
                },
                exitTransition = {
                    slideOutHorizontally(tween(durationMillis)) { it }
                }
            ) {
                ExEquip(
                    appViewModel.exEquipState,
                    appViewModel::popBackStack
                )
            }
            composable(
                route = "summons",
                enterTransition = {
                    slideInHorizontally(tween(durationMillis)) { it }
                },
                exitTransition = {
                    slideOutHorizontally(tween(durationMillis)) { it }
                }
            ) {
                Summons(
                    appViewModel.summonsState,
                    appViewModel::popBackStack
                )
            }
            composable(
                route = "quest",
                enterTransition = {
                    if (initialState.destination.route == "equip") {
                        slideInHorizontally(tween(durationMillis)) { -it }
                    } else {
                        null
                    }
                },
                exitTransition = {
                    if (targetState.destination.route == "equip") {
                        slideOutHorizontally(tween(durationMillis)) {-it }
                    } else {
                        null
                    }
                }
            ) {
                Quest(
                    appViewModel.questState,
                    appViewModel::navigateToEquipById,
                    appViewModel::navigateTo,
                    navigateToHomeAndOpenDrawer
                )
            }
            composable(
                route = "clanBattle",
                enterTransition = {
                    if (initialState.destination.route == "clanBattleMapList") {
                        slideInHorizontally(tween(durationMillis)) { -it }
                    } else {
                        null
                    }
                },
                exitTransition = {
                    if (targetState.destination.route == "clanBattleMapList") {
                        slideOutHorizontally(tween(durationMillis)) {-it }
                    } else {
                        null
                    }
                }
            ) {
                ClanBattle(
                    appViewModel.clanBattleState,
                    appViewModel::navigateToMapList,
                    appViewModel::navigateTo,
                    navigateToHomeAndOpenDrawer
                )
            }
            composable(
                route = "clanBattleMapList",
                enterTransition = {
                    if (initialState.destination.route == "clanBattle") {
                        slideInHorizontally(tween(durationMillis)) { it }
                    } else {
                        slideInHorizontally(tween(durationMillis)) { -it }
                    }
                },
                exitTransition = {
                    if (targetState.destination.route == "clanBattle") {
                        slideOutHorizontally(tween(durationMillis)) { it }
                    } else {
                        slideOutHorizontally(tween(durationMillis)) { -it }
                    }
                }
            ) {
                ClanBattleMapList(
                    appViewModel.clanBattleState,
                    appViewModel::navigateToEnemy,
                    appViewModel::popBackStack
                )
            }
            composable(
                route = "clanBattleEnemy",
                enterTransition = {
                    if (initialState.destination.route == "clanBattleMapList") {
                        slideInHorizontally(tween(durationMillis)) { it }
                    } else {
                        slideInHorizontally(tween(durationMillis)) { -it }
                    }
                },
                exitTransition = {
                    if (targetState.destination.route == "clanBattleMapList") {
                        slideOutHorizontally(tween(durationMillis)) { it }
                    } else {
                        slideOutHorizontally(tween(durationMillis)) { -it }
                    }
                }
            ) {
                ClanBattleEnemy(
                    appViewModel.clanBattleState,
                    appViewModel::navigateToMinions,
                    appViewModel::popBackStack
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
            composable(
                route = "about",
                enterTransition = {
                    slideInHorizontally(tween(durationMillis)) { it }
                },
                exitTransition = {
                    slideOutHorizontally(tween(durationMillis)) { it }
                }
            ) {
                About(
                    appViewModel::popBackStack,
                    appViewModel::linkTo
                )
            }
        }
    }
}
