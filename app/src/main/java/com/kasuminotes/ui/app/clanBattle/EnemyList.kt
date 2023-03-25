package com.kasuminotes.ui.app.clanBattle

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kasuminotes.R
import com.kasuminotes.action.toNumStr
import com.kasuminotes.data.ClanBattleMapData
import com.kasuminotes.data.EnemyData
import com.kasuminotes.data.Property
import com.kasuminotes.ui.components.Container
import com.kasuminotes.ui.components.ImageCard
import com.kasuminotes.ui.components.Infobar
import com.kasuminotes.ui.components.VerticalGrid
import com.kasuminotes.ui.components.VerticalGridCells
import com.kasuminotes.ui.theme.phaseColors
import com.kasuminotes.utils.UrlUtil

@Composable
fun EnemyList(
    mapData: ClanBattleMapData,
    onEnemyClick: (EnemyData) -> Unit
) {
    Column(Modifier.fillMaxSize().padding(4.dp)) {
        ListLabel(
            from = mapData.from,
            to = mapData.lapNumTo,
            color = phaseColors[mapData.phase - 1]
        )

        val list = mapData.enemyDataList
        VerticalGrid(
            size = list.size,
            cells = VerticalGridCells.Adaptive(350.dp),
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) { index ->
            val enemyData = list[index]
            EnemyListItem(
                enemyData = enemyData,
                scoreCoefficient = mapData.scoreCoefficientList[index],
                onEnemyClick = onEnemyClick
            )
        }
    }
}

@Composable
private fun ListLabel(
    from: Int,
    to: Int,
    color: Color
) {
    val label: String = if (to == -1) {
        stringResource(R.string.round_after_d, from)
    } else if (from == to) {
        stringResource(R.string.round_d, from)
    } else {
        stringResource(R.string.round_from1_to2, from, to)
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        LabelDivider()
        Text(
            text = label,
            color = color,
            fontSize = 14.sp
        )
        LabelDivider()
    }
}

@Composable
private fun RowScope.LabelDivider() {
    Box(
        Modifier
            .weight(1f)
            .padding(horizontal = 8.dp)
            .height(1.dp)
            .background(MaterialTheme.colorScheme.outlineVariant)
    )
}


@Composable
private fun EnemyListItem(
    enemyData: EnemyData,
    scoreCoefficient: Float,
    onEnemyClick: (EnemyData) -> Unit
) {
    Container(onClick = { onEnemyClick(enemyData) }) {
        val atkTypeStr = stringResource(
            if (enemyData.atkType == 1) R.string.physical
            else R.string.magic
        )
        ImageCard(
            imageUrl = UrlUtil.getBossUnitIconUrl(enemyData.unitId),
            primaryText = enemyData.name,
            secondaryText = "【$atkTypeStr】",
            imageSize = 56.dp
        ) {
            if (enemyData.multiParts.isNotEmpty()) {
                Text(
                    text = stringResource(R.string.multi_target_d, enemyData.multiParts.size),
                    modifier = Modifier.padding(horizontal = 4.dp),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelLarge
                )
            }
            Spacer(Modifier.weight(1f))
            Infobar(
                label = "Lv",
                value = enemyData.level.toString(),
                modifier = Modifier.width(72.dp),
                width = 26.dp,
                color = MaterialTheme.colorScheme.primaryContainer,
                textAlign = TextAlign.Center
            )
        }

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
                    value = scoreCoefficient.toString(),
                    color = MaterialTheme.colorScheme.secondary
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
