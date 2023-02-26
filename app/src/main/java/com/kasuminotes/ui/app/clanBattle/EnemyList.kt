package com.kasuminotes.ui.app.clanBattle

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kasuminotes.R
import com.kasuminotes.action.toNumStr
import com.kasuminotes.data.ClanBattleMapData
import com.kasuminotes.data.EnemyData
import com.kasuminotes.data.Property
import com.kasuminotes.ui.components.BgBorderColumn
import com.kasuminotes.ui.components.ImageCard
import com.kasuminotes.ui.components.Infobar
import com.kasuminotes.utils.UrlUtil

@Composable
fun EnemyList(
    mapData: ClanBattleMapData
) {
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(4.dp)
    ) {
        mapData.enemyDataList.forEachIndexed { i, enemyData ->
            EnemyListItem(
                enemyData = enemyData,
                scoreCoefficient = mapData.scoreCoefficientList[i]
            )
        }
    }
}

@Composable
private fun EnemyListItem(
    enemyData: EnemyData,
    scoreCoefficient: Float,
) {
    BgBorderColumn(onClick = {}) {
        ImageCard(
            imageUrl = UrlUtil.getBossUnitIconUrl(enemyData.unitId),
            primaryText = enemyData.name,
            secondaryText = "Lv${enemyData.level}",
            imageSize = 56.dp,
            primaryFontSize = 18.sp,
            secondaryFontSize = 16.sp
        )

        Row {
            Box(Modifier.weight(1f)) {
                Infobar(
                    label = stringResource(Property.getStrRes(0)),
                    value = enemyData.property.hp.toNumStr()
                )
            }
            Box(Modifier.weight(1f)) {
                Infobar(
                    label = stringResource(R.string.score_coefficient),
                    value = scoreCoefficient.toString()
                )
            }
        }

        Row {
            Box(Modifier.weight(1f)) {
                Infobar(
                    label = stringResource(Property.getStrRes(2)),
                    value = enemyData.property.def.toNumStr()
                )
            }
            Box(Modifier.weight(1f)) {
                Infobar(
                    label = stringResource(Property.getStrRes(4)),
                    value = enemyData.property.magicDef.toNumStr()
                )
            }
        }
    }
}
