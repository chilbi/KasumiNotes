package com.kasuminotes.ui.app.mirageQuest

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kasuminotes.R
import com.kasuminotes.state.MirageQuestState
import com.kasuminotes.ui.components.AdaptiveWidthLabel
import com.kasuminotes.ui.components.CenterText
import com.kasuminotes.ui.components.LabelContainer
import com.kasuminotes.ui.components.PlaceImage
import com.kasuminotes.ui.theme.UnitImageShape
import com.kasuminotes.utils.UrlUtil

@Composable
fun MirageQuestAreaList(
    mirageQuestState: MirageQuestState,
    onEnemyClick: (enemyId: Int, waveGroupId: Int?) -> Unit
) {
    if (mirageQuestState.hasMirageQuest) {
        Row(Modifier.padding(start = 4.dp, top = 4.dp)) {
            Box(Modifier.weight(1f)) {
                AdaptiveWidthLabel(stringResource(R.string.mirage_floor_quest))
            }
            Box(Modifier.weight(1f)) {
                AdaptiveWidthLabel(stringResource(R.string.mirage_nemesis_quest))
            }
        }
        Row(Modifier.padding(4.dp)) {
            arrayOf(
                mirageQuestState.mirageQuestDataGrouped,
                mirageQuestState.nemesisQuestDataGrouped
            ).forEach { data ->
                Column(Modifier.weight(1f).verticalScroll(rememberScrollState())) {
                    data.forEach { group ->
                        LabelContainer(
                            label = group.value[0].questName,
                            color = MaterialTheme.colorScheme.primary
                        ) {
                            Row(Modifier.horizontalScroll(rememberScrollState())) {
                                group.value.forEach { data ->
                                    Box(
                                        Modifier
                                            .padding(4.dp)
                                            .size(56.dp)
                                            .clickable {
                                                onEnemyClick(
                                                    data.enemyId,
                                                    data.waveGroupId
                                                )
                                            }
                                    ) {
                                        PlaceImage(
                                            url = UrlUtil.getEnemyUnitIconUrl(data.unitId),
                                            shape = UnitImageShape
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    } else {
        CenterText(stringResource(R.string.no_data))
    }
}
