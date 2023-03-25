package com.kasuminotes.ui.app.clanBattle

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kasuminotes.R
import com.kasuminotes.data.ClanBattlePeriod
import com.kasuminotes.ui.components.PlaceImage
import com.kasuminotes.ui.components.LabelContainer
import com.kasuminotes.ui.theme.UnitImageShape
import com.kasuminotes.utils.UrlUtil

@Composable
fun ClanBattlePeriodList(
    clanBattlePeriodList: List<ClanBattlePeriod>,
    onPeriodClick: (label: String, period: ClanBattlePeriod) -> Unit,
) {
    val color = MaterialTheme.colorScheme.primary
    LazyVerticalGrid(
        columns = GridCells.Adaptive(320.dp),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(4.dp)
    ) {
        items(clanBattlePeriodList, { it.clanBattleId }) { clanBattlePeriod ->
            ClanBattlePeriodItem(clanBattlePeriod, color, onPeriodClick)
        }
    }
}

@Composable
private fun ClanBattlePeriodItem(
    clanBattlePeriod: ClanBattlePeriod,
    color: Color,
    onPeriodClick: (label: String, period: ClanBattlePeriod) -> Unit
) {
    val label = stringResource(
        R.string.clan_battle_period1_year2_month3_constellation4,
        clanBattlePeriod.periodNum,
        clanBattlePeriod.year,
        clanBattlePeriod.month,
        stringResource(clanBattlePeriod.constellation)
    )
    LabelContainer(label, color, onClick = { onPeriodClick(label, clanBattlePeriod) }) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            clanBattlePeriod.bossUnitIdList.forEach { unitId ->
                Box(Modifier.size(56.dp)) {
                    PlaceImage(
                        url = UrlUtil.getBossUnitIconUrl(unitId),
                        shape = UnitImageShape
                    )
                }
            }
        }
    }
}
