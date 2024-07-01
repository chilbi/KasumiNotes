package com.kasuminotes.ui.app.dungeon

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.kasuminotes.data.DungeonAreaData
import com.kasuminotes.ui.app.state.DungeonState
import com.kasuminotes.ui.components.FixedWidthLabel
import com.kasuminotes.ui.components.LabelContainer
import com.kasuminotes.ui.components.PlaceImage
import com.kasuminotes.ui.components.UnitElement
import com.kasuminotes.ui.theme.UnitImageShape
import com.kasuminotes.utils.UrlUtil

@Composable
fun DungeonAreaList(
    dungeonState: DungeonState,
    onNavigateToEnemy: (enemyId: Int, talentWeaknessList: List<Int>) -> Unit
) {
    Column(Modifier.verticalScroll(rememberScrollState())) {
        dungeonState.dungeonAreaDataGrouped.forEach { group ->
            LabelContainer(
                label = "${group.key - 31000} ${group.value[0].dungeonName}",
                color = MaterialTheme.colorScheme.primary
            ) {
                var floorNum = -1
                group.value.forEach { dungeonAreaData ->
                    Row(Modifier.padding(vertical = 4.dp)) {
                        if (dungeonAreaData.floorNum == floorNum) {
                            Spacer(Modifier.width(40.dp))
                        } else {
                            floorNum = dungeonAreaData.floorNum
                            FixedWidthLabel(
                                text = "${floorNum}F",
                                width = 32.dp,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }

                        DungeonAreaListItem(
                            dungeonAreaData,
                            dungeonState.enemyTalentWeaknessMap[dungeonAreaData.enemyId],
                            onNavigateToEnemy
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DungeonAreaListItem(
    dungeonAreaData: DungeonAreaData,
    weaknessList: List<Int>?,
    onItemClick: (enemyId: Int, talentWeaknessList: List<Int>) -> Unit
) {
    Row(Modifier.padding(4.dp).clickable { onItemClick(dungeonAreaData.enemyId, weaknessList ?: emptyList()) }) {
        Box(Modifier.size(56.dp)) {
            PlaceImage(
                url = UrlUtil.getBossUnitIconUrl(dungeonAreaData.unitId),
                shape = UnitImageShape
            )
        }
        Column(
            modifier = Modifier
                .height(56.dp)
                .padding(start = 8.dp),
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                text = dungeonAreaData.name,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = MaterialTheme.typography.titleMedium
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = dungeonAreaData.unitId.toString(),
                    color = LocalContentColor.current.copy(0.75f),
                    style = MaterialTheme.typography.titleSmall
                )
                var modeOrPattern = ""
                if (dungeonAreaData.mode != 0) {
                    modeOrPattern += "Mode${dungeonAreaData.mode}"
                }
                if (dungeonAreaData.pattern != 0) {
                    modeOrPattern += "Pattern${dungeonAreaData.pattern}"
                }
                if ( modeOrPattern != "") {
                    Text(
                        text =  modeOrPattern,
                        modifier = Modifier
                            .padding(start = 2.dp)
                            .background(MaterialTheme.colorScheme.tertiaryContainer, MaterialTheme.shapes.small)
                            .padding(horizontal = 4.dp),
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
                weaknessList?.forEachIndexed { index, weakness ->
                    if (weakness > 100) {
                        UnitElement(
                            padding = 2.dp,
                            talentId = index + 1,
                            elementSize = 14.dp
                        )
                    }
                }
            }
        }
    }
}
