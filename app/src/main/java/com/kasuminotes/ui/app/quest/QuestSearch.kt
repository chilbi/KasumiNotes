package com.kasuminotes.ui.app.quest

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.FabPosition
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.IconToggleButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlaylistAdd
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.kasuminotes.R
import com.kasuminotes.common.QuestType
import com.kasuminotes.data.EquipInfo
import com.kasuminotes.data.QuestData
import com.kasuminotes.ui.components.ImageIcon
import com.kasuminotes.ui.components.ImmersiveTopAppBar
import com.kasuminotes.ui.components.NoDataText
import com.kasuminotes.ui.components.PlaceImage
import com.kasuminotes.ui.components.TextToggleButton
import com.kasuminotes.ui.components.Toggle37Button
import com.kasuminotes.ui.components.UnderlineLabelColumn
import com.kasuminotes.ui.components.VerticalGrid
import com.kasuminotes.ui.components.VerticalGridCells
import com.kasuminotes.ui.components.selectedBg
import com.kasuminotes.ui.theme.RaritiesColors
import com.kasuminotes.ui.theme.place
import com.kasuminotes.ui.theme.selected
import com.kasuminotes.utils.UrlUtil

@Composable
fun QuestSearch(
    visitIndex: Int,
    questDataList: List<QuestData>?,
    equipMaterialPairList: List<Pair<Int, List<EquipInfo>>>?,
    memoryPieces: Array<List<Int>>?,
    searchId: Int,
    searchSet: List<Pair<Int, Array<Int>>>,
    searches: Array<Int>,
    searchedList: List<Int>,
    searchTypes: Array<QuestType>,
    visibleMin37: Boolean,
    min37: Boolean,
    onSearch: () -> Unit,
    onSearchesAdd: () -> Unit,
    onSearchesDel: (Int) -> Unit,
    onSearchIdChange: (Int) -> Unit,
    onSearchTypesChange: (QuestType) -> Unit,
    onSearchesChange: (Int) -> Unit,
    onToggleMin37: () -> Unit,
    onToggleVisitIndex: () -> Unit,
    sortButton: @Composable () -> Unit,
    bottomBar: @Composable () -> Unit,
    floatingActionButton: @Composable () -> Unit
) {
    Scaffold(
        topBar = {
            ImmersiveTopAppBar(
                title = {
                    Text(stringResource(R.string.search_list))
                },
                navigationIcon = {
                    IconButton(onClick = onSearch) {
                        Icon(Icons.Filled.Search, null)
                    }
                },
                actions = {
                    QuestType.values().forEach { type ->
                        val checked = searchTypes.contains(type)
                        IconToggleButton(checked = checked, onCheckedChange = { onSearchTypesChange(type) }) {
                            QuestLabel(
                                questType = type,
                                checked = checked
                            )
                        }
                    }
                },
                content = {
                    Row(
                        modifier = Modifier.padding(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SearchSet(visitIndex, searches, onSearchesChange)

                        SearchSetMenu(
                            visitIndex,
                            searchId,
                            searchSet,
                            onSearch,
                            onSearchesAdd,
                            onSearchesDel,
                            onSearchIdChange
                        )

                        Spacer(Modifier.weight(1f))

                        if (visitIndex == 0) {
                            if (visibleMin37) {
                                Toggle37Button(min37, onToggleMin37, Color.White)
                            }

                            sortButton()
                        } else {
                            TextToggleButton(
                                leftText = stringResource(R.string.equip_material),
                                rightText = stringResource(R.string.piece),
                                leftChecked = visitIndex == 1,
                                onToggle = onToggleVisitIndex,
                                modifier = Modifier.padding(4.dp)
                            )
                        }
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
                Crossfade(targetState = visitIndex) { state ->
                    when (state) {
                        0 -> {
                            when {
                                questDataList == null -> {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 4.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator()
                                    }
                                }
                                questDataList.isEmpty() -> {
                                    NoDataText()
                                }
                                else -> {
                                    LazyColumn(contentPadding = PaddingValues(4.dp)) {
                                        items(questDataList) { questData ->
                                            QuestDropItem(
                                                questData = questData,
                                                selectedList = searchedList,
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        1 -> {
                            LazyColumn(contentPadding = PaddingValues(4.dp)) {
                                items(equipMaterialPairList ?: emptyList()) { pair ->
                                    EquipPairItem(
                                        rarity = pair.first,
                                        equips = pair.second,
                                        searches = searches,
                                        onSearchesChange = onSearchesChange
                                    )
                                }
                            }
                        }
                        2 -> {
                            Column(
                                modifier = Modifier
                                    .verticalScroll(rememberScrollState())
                                    .padding(4.dp)
                            ) {
                                if (memoryPieces != null) {
                                    val normalPieces = memoryPieces[0]
                                    val purePieces = memoryPieces[1]

                                    PiecesItem(
                                        label = stringResource(R.string.memory_piece),
                                        color = RaritiesColors.getRarityColors(3).middle,
                                        pieces = normalPieces,
                                        searches = searches,
                                        onSearchesChange = onSearchesChange
                                    )

                                    if (purePieces.isNotEmpty()) {
                                        PiecesItem(
                                            label = stringResource(R.string.pure_memory_piece),
                                            color = RaritiesColors.getRarityColors(4).middle,
                                            pieces = purePieces,
                                            searches = searches,
                                            onSearchesChange = onSearchesChange
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
private fun SearchSet(
    visitIndex: Int,
    searches: Array<Int>,
    onSearchesChange: (Int) -> Unit
) {
    Row {
        searches.forEach { materialId ->
            Box(Modifier.padding(4.dp)) {
                if (materialId == 0) {
                    Spacer(
                        Modifier
                            .size(48.dp)
                            .background(
                                MaterialTheme.colors.place,
                                MaterialTheme.shapes.small
                            )
                            .clickable(enabled = visitIndex == 0) {
                                onSearchesChange(0)
                            }
                    )
                } else {
                    ImageIcon(
                        url = if (materialId < 100000) UrlUtil.getItemIconUrl(materialId)
                        else UrlUtil.getEquipIconUrl(materialId),
                        onClick = { onSearchesChange(materialId) }
                    )
                }
            }
        }
    }
}

@Composable
private fun SearchSetMenu(
    visitIndex: Int,
    searchId: Int,
    searchSet: List<Pair<Int, Array<Int>>>,
    onSearch: () -> Unit,
    onSearchesAdd: () -> Unit,
    onSearchesDel: (Int) -> Unit,
    onSearchIdChange: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    IconButton(onClick = {
        if (visitIndex == 0) {
            if (searchSet.size > 1) {
                expanded = true
            } else {
                onSearchesAdd()
            }
        } else {
            onSearch()
        }
    }) {
        Icon(
            imageVector = if (visitIndex == 0) Icons.Filled.PlaylistAdd
            else Icons.Filled.Search,
            contentDescription = null
        )
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
        modifier = Modifier.heightIn(max = 420.dp),
        offset = DpOffset((-4).dp, 4.dp)
    ) {
        searchSet.forEach { pair ->
            if (pair.first != searchId) {
                DropdownMenuItem(
                    onClick = {
                        expanded = false
                        onSearchIdChange(pair.first)
                    },
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    SearchSet(
                        visitIndex = 0,
                        searches = pair.second,
                        onSearchesChange = {
                            expanded = false
                            onSearchIdChange(pair.first)
                        }
                    )

                    IconButton(onClick = {
                        if (searchSet.size == 2) {
                            expanded = false
                        }
                        onSearchesDel(pair.first)
                    }) {
                        Icon(Icons.Filled.Delete, null)
                    }
                }
            }
        }
        DropdownMenuItem(
            onClick = {
                expanded = false
                onSearchesAdd()
            },
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            Spacer(Modifier.weight(1f))
            Box(
                modifier = Modifier.size(48.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Add, null)
            }
        }
    }
}

@Composable
fun EquipPairItem(
    rarity: Int,
    equips: List<EquipInfo>,
    searches: Array<Int>,
    onSearchesChange: (Int) -> Unit
) {
    UnderlineLabelColumn(
        label = stringResource(R.string.rare) + rarity,
        color = RaritiesColors.getRarityColors(rarity / 10).middle
    ) {
        VerticalGrid(
            size = equips.size,
            cells = VerticalGridCells.Adaptive(60.dp)
        ) { index ->
            val equipId = equips[index].equipmentId
            SelectableItem(
                selected = searches.contains(equipId),
                url = UrlUtil.getEquipIconUrl(equipId),
                onClick = { onSearchesChange(equipId) }
            )
        }
    }
}

@Composable
private fun PiecesItem(
    label: String,
    color: Color,
    pieces: List<Int>,
    searches: Array<Int>,
    onSearchesChange: (Int) -> Unit
) {
    UnderlineLabelColumn(label, color) {
        VerticalGrid(
            size = pieces.size,
            cells = VerticalGridCells.Adaptive(60.dp)
        ) { index ->
            val id = pieces[index]
            SelectableItem(
                selected = searches.contains(id),
                url = UrlUtil.getItemIconUrl(id),
                onClick = { onSearchesChange(id) }
            )
        }
    }
}

@Composable
private fun BoxScope.SelectableItem(
    selected: Boolean,
    url: String,
    onClick: () -> Unit
) {
    Box(
        Modifier
            .align(Alignment.Center)
            .size(60.dp)
            .padding(2.dp)
            .selectedBg(
                selected,
                MaterialTheme.colors.selected,
                MaterialTheme.shapes.small
            )
            .clickable(
                role = Role.Button,
                onClick = onClick
            )
            .padding(4.dp)
    ) {
        PlaceImage(url)
    }
}
