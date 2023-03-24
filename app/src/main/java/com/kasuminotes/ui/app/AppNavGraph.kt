package com.kasuminotes.ui.app

import androidx.compose.animation.AnimatedContentScope
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

sealed class AppNavGraph(
    val route: String,
    val parent: List<String> = emptyList(),
    val child: List<String> = emptyList()
) {
    object Home : AppNavGraph(
        route = "home",
        child = listOf("chara", "about")
    )

    object Chara : AppNavGraph(
        route = "chara",
        parent = listOf("home"),
        child = listOf("equip", "exEquip", "summons")
    )

    object Equip : AppNavGraph(
        route = "equip",
        parent = listOf("chara", "quest")
    )

    object ExEquip : AppNavGraph(
        route = "exEquip",
        parent = listOf("chara")
    )

    object Summons : AppNavGraph(
        route = "summons",
        parent = listOf("chara", "clanBattleEnemy")
    )

    object Quest : AppNavGraph(
        route = "quest",
        child = listOf("equip")
    )

    object ClanBattle : AppNavGraph(
        route = "clanBattle",
        child = listOf("clanBattleMapList")
    )

    object ClanBattleMapList : AppNavGraph(
        route = "clanBattleMapList",
        parent = listOf("clanBattle"),
        child = listOf("clanBattleEnemy")
    )

    object ClanBattleEnemy : AppNavGraph(
        route = "clanBattleEnemy",
        parent = listOf("clanBattleMapList"),
        child = listOf("summons")
    )

    object About : AppNavGraph(
        route = "about",
        parent = listOf("home")
    )

    object Images : AppNavGraph(
        route = "images"
    )

    object Editor : AppNavGraph(
        route = "editor"
    )

    private val isRoot = parent.isEmpty()
    private val isLeaf = child.isEmpty()

    val enterTransition: AnimatedContentScope<NavBackStackEntry>.() -> EnterTransition? = {
        if (isRoot) {
            if (child.contains(initialState.destination.route)) {
                NavTransitions.slideInLeft
            } else {
                null
            }
        } else if (isLeaf) {
            NavTransitions.slideInRight
        } else {
            if (parent.contains(initialState.destination.route)) {
                NavTransitions.slideInRight
            } else {
                NavTransitions.slideInLeft
            }
        }
    }

    val exitTransition: AnimatedContentScope<NavBackStackEntry>.() -> ExitTransition? = {
        if (isRoot) {
            if (child.contains(targetState.destination.route)) {
                NavTransitions.slideOutLeft
            } else {
                null
            }
        } else if (isLeaf) {
            NavTransitions.slideOutRight
        } else {
            if (parent.contains(targetState.destination.route)) {
                NavTransitions.slideOutRight
            } else {
                NavTransitions.slideOutLeft
            }
        }
    }
}

object NavTransitions {
    private const val durationMillis = 500
    private val intOffsetSpec: FiniteAnimationSpec<IntOffset> = tween(durationMillis)
    private val floatSpec: FiniteAnimationSpec<Float> = tween(durationMillis)

    val slideInLeft = slideInHorizontally(intOffsetSpec) { -it }
    val slideInRight = slideInHorizontally(intOffsetSpec) { it }

    val slideOutLeft = slideOutHorizontally(intOffsetSpec) { -it }
    val slideOutRight = slideOutHorizontally(intOffsetSpec) { it }

    private val fadeIn = fadeIn(floatSpec)
    private val fadeOut = fadeOut(floatSpec)

    val defaultEnterTransition: AnimatedContentScope<NavBackStackEntry>.() -> EnterTransition = {
        fadeIn
    }
    val defaultExitTransition: AnimatedContentScope<NavBackStackEntry>.() -> ExitTransition = {
        fadeOut
    }
}
