package com.kasuminotes.ui.app.clanBattle

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kasuminotes.data.EnemyData
import com.kasuminotes.ui.app.state.ClanBattleState
import com.kasuminotes.ui.components.BackButton
import com.kasuminotes.ui.components.ImageCard
import com.kasuminotes.ui.components.ImmersiveTopAppBar
import com.kasuminotes.utils.UrlUtil

@Composable
fun ClanBattleEnemy(
    clanBattleState: ClanBattleState,
    onMinionsClick: (minions: List<Int>, skillLevel: Int) -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = { EnemyTopBar(clanBattleState.enemyData!!, onBack) },
        bottomBar = { Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars)) },
        content = { contentPadding ->
            Box(Modifier.padding(contentPadding)) {
                EnemyDetail(
                    enemyData = clanBattleState.enemyData!!,
                    enemyMultiParts = clanBattleState.enemyMultiParts,
                    unitAttackPatternList = clanBattleState.unitAttackPatternList,
                    skillList = clanBattleState.skillList,
                    unitSkillData = clanBattleState.unitSkillData,
                    onMinionsClick = onMinionsClick
                )
            }
        }
    )
}

@Composable
private fun EnemyTopBar(
    enemyData: EnemyData,
    onBack: () -> Unit
) {
    ImmersiveTopAppBar(
        title = {
            ImageCard(
                imageUrl = UrlUtil.getBossUnitIconUrl(enemyData.unitId),
                primaryText = enemyData.name,
                secondaryText = "lv${enemyData.level}"
            )
        },
        navigationIcon = { BackButton(onBack) }
    )
}
