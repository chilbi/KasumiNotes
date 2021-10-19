package com.kasuminotes.ui.app.quest

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.FabPosition
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import com.kasuminotes.R
import com.kasuminotes.common.QuestType
import com.kasuminotes.data.QuestData
import com.kasuminotes.ui.components.ImmersiveTopAppBar
import com.kasuminotes.ui.components.Pagination
import com.kasuminotes.ui.components.SortIconButton

@ExperimentalCoilApi
@Composable
fun QuestMap(
    questDataList: List<QuestData>?,
    highlightList: List<Int>,
    questType: QuestType,
    maxArea: Int,
    area: Int,
    onQuestTypeChange: (QuestType) -> Unit,
    onHighlightListChange: (Int) -> Unit,
    onAreaChange: (Int) -> Unit,
    sortButton: @Composable () -> Unit,
    bottomBar: @Composable () -> Unit,
    floatingActionButton: @Composable () -> Unit
) {
    Scaffold(
        topBar = {
            ImmersiveTopAppBar(
                title = {
                    Text(stringResource(R.string.map_list))
                },
                navigationIcon = {
                    var expanded by remember { mutableStateOf(false) }
                    IconButton(onClick = { expanded = true }) {
                        QuestLabel(
                            questType = questType,
                            modifier = Modifier.size(40.dp),
                            shape = CircleShape,
                            fontSize = 16.sp
                        )
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        QuestType.values().forEach { type ->
                            DropdownMenuItem(onClick = {
                                onQuestTypeChange(type)
                                expanded = false
                            }) {
                                QuestLabel(type)
                            }
                        }
                    }
                },
                actions = {
                    sortButton()
                },
                content = {
                    if (
                        questType == QuestType.N ||
                        questType == QuestType.H
                    ) {
                        Pagination(
                            count = maxArea,
                            page = area,
                            onChange = onAreaChange
                        )
                    }
                }
            )
        },
        bottomBar = bottomBar,
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true,
        content = { contentPadding ->
            Box(Modifier.padding(contentPadding)) {
                LazyColumn(contentPadding = PaddingValues(4.dp)) {
                    items(questDataList ?: emptyList()) { questData ->
                        QuestDropItem(
                            questData = questData,
                            selectedList = highlightList,
                            onSelected = onHighlightListChange
                        )
                    }
                }
            }
        }
    )
}
