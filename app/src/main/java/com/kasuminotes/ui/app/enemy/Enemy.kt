package com.kasuminotes.ui.app.enemy

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kasuminotes.R
import com.kasuminotes.data.EnemyData
import com.kasuminotes.state.EnemyState
import com.kasuminotes.ui.components.BackButton
import com.kasuminotes.ui.components.ImageCard
import com.kasuminotes.ui.components.Infobar
import com.kasuminotes.ui.components.TopBar
import com.kasuminotes.ui.components.UnitElement
import com.kasuminotes.utils.Helper
import com.kasuminotes.utils.UrlUtil

@Composable
fun Enemy(
    enemyState: EnemyState,
    onExtraEffectClick: (enemyIdList: List<Int>, epTableName: String) -> Unit,
    onMinionsClick: (minions: List<Int>, skillLevel: Int, enemyData: EnemyData, epTableName: String) -> Unit,
    onBack: () -> Unit
) {
    val enemyData = enemyState.enemyData!!
    val isShadowChara = Helper.isShadowChara(enemyData.unitId)

    Scaffold(
        topBar = {
            EnemyTopBar(enemyData, isShadowChara, enemyState.talentWeaknessList, onBack)
        },
        bottomBar = {
            Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
        },
        content = { contentPadding ->
            Box(Modifier.padding(contentPadding)) {
                EnemyDetail(
                    enemyState,
                    isShadowChara,
                    onExtraEffectClick,
                    onMinionsClick
                )
            }
        }
    )
}

@Composable
private fun EnemyTopBar(
    enemyData: EnemyData,
    isShadowChara: Boolean,
    talentWeaknessList: List<Int>,
    onBack: () -> Unit
) {
    TopBar(
        title = {
            Box(Modifier.wrapContentSize()) {
                var secondaryText = "lv${enemyData.level}"
                if (isShadowChara) {
                    if (enemyData.uniqueEquipmentFlag1 > 0) {
                        val uniqueEquip = stringResource(R.string.unique_equip)
                        secondaryText += "; ${uniqueEquip}1"
                        if (enemyData.uniqueEquipmentFlag1 > 1) {
                            secondaryText += "; ${uniqueEquip}2"
                        }
                    }
                    if (enemyData.rarity > 5) {
                        secondaryText += "; ${stringResource(R.string.rarity_6)}"
                    }
                }
                ImageCard(
                    imageUrl = UrlUtil.getEnemyUnitIconUrl(enemyData.unitId),
                    primaryText = enemyData.name,
                    secondaryText = secondaryText
                )
                if (talentWeaknessList.any { weakness -> weakness > 100 }) {
                    Infobar(
                        label = stringResource(R.string.weakness),
                        value = "",
                        modifier = Modifier.width(130.dp).align(Alignment.BottomEnd),
                        endDecoration = {
                            Row {
                                talentWeaknessList.forEachIndexed { index, weakness ->
                                    if (weakness > 100) {
                                        UnitElement(
                                            padding = 1.dp,
                                            talentId = index + 1,
                                            elementSize = 14.dp
                                        )
                                    }
                                }
                            }
                        },
                        width = 36.dp,
                        margin = PaddingValues(8.dp),
                        padding = PaddingValues(horizontal = 2.dp, vertical = 1.dp),
                        color = MaterialTheme.colorScheme.secondary,
                        textAlign = TextAlign.Center,
                        labelStyle = MaterialTheme.typography.labelSmall
                    )
                }
            }
        },
        navigationIcon = { BackButton(onBack) }
    )
}
