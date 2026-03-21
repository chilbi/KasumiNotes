package com.kasuminotes.ui.app.chara

import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import com.kasuminotes.R
import com.kasuminotes.action.toNumStr
import com.kasuminotes.data.ConnectRankBonusItem
import com.kasuminotes.data.ConnectRankData
import com.kasuminotes.data.MaxUserData
import com.kasuminotes.data.UnitData
import com.kasuminotes.data.UserData
import com.kasuminotes.ui.components.Infobar
import com.kasuminotes.ui.components.LabelContainer
import com.kasuminotes.ui.components.UnderlineStyle
import com.kasuminotes.ui.components.VerticalGrid
import com.kasuminotes.ui.components.VerticalGridCells
import com.kasuminotes.ui.components.underline

@Composable
fun CharaConnectRank(
    userData: UserData,
    unitData: UnitData,
    connectRankData: ConnectRankData
) {
    Column(Modifier.verticalScroll(rememberScrollState())) {
        LabelContainer(
            label = stringResource(R.string.connect_rank_status),
            color = MaterialTheme.colorScheme.primary
        ) {
            val statusList = connectRankData.getStatusList(userData.connectRank, unitData.unitRoleId)
            VerticalGrid(
                size = statusList.size,
                cells = VerticalGridCells.Fixed(2),
            ) { index ->
                val pair = statusList[index]
                val label = stringResource(
                    when (pair.first) {
                        1 -> R.string.hp
                        2 -> if (unitData.atkType == 1) R.string.atk else R.string.magic_str
                        3 -> R.string.def
                        4 -> R.string.magic_def
                        5 -> R.string.damage_increase
                        6 -> R.string.damage_reduction
                        else -> R.string.unknown_connect_rank_status
                    }
                )
                Infobar(
                    label = label,
                    value = "${(pair.second.toDouble() / 100.0).toNumStr()}%",
                    width = 86.dp
                )
            }
        }

        LabelContainer(
            label = stringResource(R.string.connect_rank_bonus),
            color = MaterialTheme.colorScheme.primary
        ) {
            val context = LocalContext.current
            val bonusList = connectRankData.getBonusList(userData.connectRank, context)
            bonusList.forEach { item ->
                BonusItem(item)
            }
        }
    }
}

@Composable
private fun BonusItem(item: ConnectRankBonusItem) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = item.unlock,
            onCheckedChange = null
        )
        Column(Modifier.padding(4.dp)) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
                    .underline(1f.dp, MaterialTheme.colorScheme.primary, UnderlineStyle.Dashed)
                    .padding(bottom = 4.dp)
            ) {
                Text(
                    text = item.label,
                    style = MaterialTheme.typography.labelMedium
                )
            }
            Text(
                text = item.value,
                modifier = Modifier.fillMaxWidth(),
                color= MaterialTheme.colorScheme.tertiary,
                textAlign = TextAlign.End,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}
