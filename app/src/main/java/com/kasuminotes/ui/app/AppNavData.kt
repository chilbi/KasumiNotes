package com.kasuminotes.ui.app

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavBackStackEntry

sealed class AppNavData(
    val route: String,
    val parent: List<String> = emptyList(),
    val child: List<String> = emptyList()
) {
    data object Home : AppNavData(
        route = "home",
        child = listOf("chara", "about")
    )

    data object Chara : AppNavData(
        route = "chara",
        parent = listOf("home"),
        child = listOf("equip", "exEquip", "summons")
    )

    data object Equip : AppNavData(
        route = "equip",
        parent = listOf("chara", "quest")
    )

    data object ExEquip : AppNavData(
        route = "exEquip",
        parent = listOf("chara")
    )

    data object Summons : AppNavData(
        route = "summons",
        parent = listOf("chara", "enemy")
    )

    data object Quest : AppNavData(
        route = "quest",
        child = listOf("equip")
    )

    data object ClanBattle : AppNavData(
        route = "clanBattle",
        child = listOf("clanBattleMapList", "dungeon", "talentQuest", "abyssQuest", "mirageQuest")
    )

    data object ClanBattleMapList : AppNavData(
        route = "clanBattleMapList",
        parent = listOf("clanBattle"),
        child = listOf("enemy")
    )

    data object Enemy : AppNavData(
        route = "enemy",
        parent = listOf("clanBattleMapList", "dungeon", "talentQuest", "abyssQuest", "mirageQuest"),
        child = listOf("summons")
    )

    data object Dungeon : AppNavData(
        route = "dungeon",
        parent = listOf("clanBattle"),
        child = listOf("enemy")
    )

    data object TalentQuest : AppNavData(
        route = "talentQuest",
        parent = listOf("clanBattle"),
        child = listOf("enemy")
    )

    data object AbyssQuest : AppNavData(
        route = "abyssQuest",
        parent = listOf("clanBattle"),
        child = listOf("enemy")
    )

    data object MirageQuest : AppNavData(
        route = "mirageQuest",
        parent = listOf("clanBattle"),
        child = listOf("enemy")
    )

    data object About : AppNavData(
        route = "about",
        parent = listOf("home")
    )

    data object Images : AppNavData(
        route = "images"
    )

    data object Editor : AppNavData(
        route = "editor"
    )

    private val isRoot = parent.isEmpty()
    private val isLeaf = child.isEmpty()

    val enterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition? = {
        if (isRoot) {
            if (child.contains(initialState.destination.route)) {
                AppNavTransitions.slideInLeft
            } else {
                null
            }
        } else if (isLeaf) {
            AppNavTransitions.slideInRight
        } else {
            if (parent.contains(initialState.destination.route)) {
                AppNavTransitions.slideInRight
            } else {
                AppNavTransitions.slideInLeft
            }
        }
    }

    val exitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition? = {
        if (isRoot) {
            if (child.contains(targetState.destination.route)) {
                AppNavTransitions.slideOutLeft
            } else {
                null
            }
        } else if (isLeaf) {
            AppNavTransitions.slideOutRight
        } else {
            if (parent.contains(targetState.destination.route)) {
                AppNavTransitions.slideOutRight
            } else {
                AppNavTransitions.slideOutLeft
            }
        }
    }
}

object AppNavTransitions {
    private const val durationMillis = 500
    private val intOffsetSpec: FiniteAnimationSpec<IntOffset> = tween(durationMillis)
    private val floatSpec: FiniteAnimationSpec<Float> = tween(durationMillis)
    private val fadeIn = fadeIn(floatSpec)
    private val fadeOut = fadeOut(floatSpec)

    val slideInLeft = slideInHorizontally(intOffsetSpec) { -it }
    val slideInRight = slideInHorizontally(intOffsetSpec) { it }
    val slideOutLeft = slideOutHorizontally(intOffsetSpec) { -it }
    val slideOutRight = slideOutHorizontally(intOffsetSpec) { it }
    val enterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = { fadeIn }
    val exitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = { fadeOut }
}
