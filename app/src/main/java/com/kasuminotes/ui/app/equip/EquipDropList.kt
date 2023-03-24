package com.kasuminotes.ui.app.equip

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.IconToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.kasuminotes.R
import com.kasuminotes.common.QuestType
import com.kasuminotes.data.QuestData
import com.kasuminotes.ui.app.quest.QuestDropList
import com.kasuminotes.ui.app.quest.QuestLabel
import com.kasuminotes.ui.components.FixedWidthLabel
import com.kasuminotes.ui.components.CenterText
import com.kasuminotes.ui.components.SortIconButton
import com.kasuminotes.ui.components.Toggle37Button

@Composable
fun EquipDropList(
    questTypes: Array<QuestType>,
    questDataList: List<QuestData>?,
    selectedList: List<Int>,
    sortDesc: Boolean,
    visibleMin37: Boolean,
    min37: Boolean,
    onQuestTypesChange: (QuestType) -> Unit,
    onSortDescToggle: () -> Unit,
    onMin37Toggle: () -> Unit
) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            FixedWidthLabel(stringResource(R.string.obtain_place))

            Spacer(Modifier.weight(1f))

            QuestType.values().forEach { type ->
                val checked = questTypes.contains(type)
                IconToggleButton(checked = checked, onCheckedChange = { onQuestTypesChange(type) }) {
                    QuestLabel(
                        questType = type,
                        checked = checked
                    )
                }
            }

            if (visibleMin37) {
                Toggle37Button(min37, onMin37Toggle)
            }

            SortIconButton(sortDesc, onSortDescToggle)
        }

        when {
            questDataList == null -> {
                CenterText(stringResource(R.string.searching))
            }
            questDataList.isEmpty() -> {
                CenterText(stringResource(R.string.no_data))
            }
            else -> {
                QuestDropList(
                    questDataList,
                    selectedList
                )
            }
        }
    }
}