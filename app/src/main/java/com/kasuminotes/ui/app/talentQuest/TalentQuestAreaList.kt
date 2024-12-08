package com.kasuminotes.ui.app.talentQuest

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
import com.kasuminotes.ui.app.state.TalentQuestState
import com.kasuminotes.ui.components.CenterText
import com.kasuminotes.ui.components.LabelContainer
import com.kasuminotes.ui.components.PlaceImage
import com.kasuminotes.ui.theme.UnitImageShape
import com.kasuminotes.utils.UrlUtil

@Composable
fun TalentQuestAreaList(
    taLentQuestState: TalentQuestState,
    onNavigateToEnemy: (enemyId: Int) -> Unit
) {
    if (taLentQuestState.hasTalentQuest) {
        Column(Modifier.verticalScroll(rememberScrollState())) {
            taLentQuestState.talentQuestDataGrouped.forEach { group ->
                LabelContainer(
                    label = group.value[0].questName,
                    color= MaterialTheme.colorScheme.primary
                ) {
                    Row(Modifier.horizontalScroll(rememberScrollState())) {
                        group.value.forEach { data ->
                            Box(Modifier
                                .padding(4.dp)
                                .size(56.dp)
                                .clickable { onNavigateToEnemy(data.enemyId) }
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
    } else {
        CenterText(stringResource(R.string.no_data))
    }
}