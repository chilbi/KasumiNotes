package com.kasuminotes.ui.app.quest

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kasuminotes.R
import com.kasuminotes.data.EquipInfo
import com.kasuminotes.ui.components.ImageIcon
import com.kasuminotes.ui.components.TopBar
import com.kasuminotes.ui.components.LabelContainer
import com.kasuminotes.ui.components.VerticalGrid
import com.kasuminotes.ui.components.VerticalGridCells
import com.kasuminotes.ui.theme.RaritiesColors
import com.kasuminotes.utils.UrlUtil

@Composable
fun QuestEquip(
    equipmentPairList: List<Pair<Int, List<EquipInfo>>>?,
    typePairList: List<Pair<Int, String>>?,
    equipTypes: Set<Int>,
    onEquipTypesChange: (checked: Boolean, type: Int) -> Unit,
    onAllEquipTypesChange: (allSelected: Boolean) -> Unit,
    onEquipClick: (Int) -> Unit,
    bottomBar: @Composable () -> Unit,
    floatingActionButton: @Composable () -> Unit
) {
    Scaffold(
        topBar = {
            TopBar(
                title = {
                    Text(stringResource(R.string.equip_list))
                },
                navigationIcon = {
                    Box(
                        modifier = Modifier.size(48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.Gavel, null)
                    }
                },
                actions = {
                    FilterButton(
                        typePairList,
                        equipTypes,
                        onEquipTypesChange,
                        onAllEquipTypesChange
                    )
                }
            )
        },
        bottomBar = bottomBar,
        floatingActionButton = floatingActionButton,
        containerColor = MaterialTheme.colorScheme.surface,
        content = { contentPadding ->
            Box(Modifier.padding(contentPadding)) {
                LazyColumn(contentPadding = PaddingValues(4.dp)) {
                    items(equipmentPairList ?: emptyList(), { it.first }) { pair ->
                        val equips = pair.second.filter { equipTypes.contains(it.type) }
                        if (equips.isNotEmpty()) {
                            EquipmentPairItem(
                                rarity = pair.first,
                                equips = equips,
                                onEquipClick = onEquipClick
                            )
                        }
                    }
                }
            }
        }
    )
}

@Composable
private fun FilterButton(
    typePairList: List<Pair<Int, String>>?,
    equipTypes: Set<Int>,
    onEquipTypesChange: (checked: Boolean, type: Int) -> Unit,
    onAllEquipTypesChange: (allSelected: Boolean) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    IconButton(onClick = { expanded = true }) {
        Icon(Icons.Filled.FilterList, null)
    }
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
        modifier = Modifier.heightIn(max = 420.dp)
    ) {
        val allSelected = typePairList?.size == equipTypes.size

        DropdownMenuItem(
            text = { Text(stringResource(R.string.all)) },
            leadingIcon = { Checkbox(allSelected, null) },
            onClick = { onAllEquipTypesChange(allSelected) }
        )

        typePairList?.forEach { pair ->
            val checked = equipTypes.contains(pair.first)
            DropdownMenuItem(
                text = { Text(pair.second) },
                leadingIcon = { Checkbox(checked, null) },
                onClick = { onEquipTypesChange(checked, pair.first) }
            )
        }
    }
}

@Composable
fun EquipmentPairItem(
    rarity: Int,
    equips: List<EquipInfo>,
    onEquipClick: (Int) -> Unit
) {
    LabelContainer(
        label = stringResource(R.string.rare) + rarity,
        color = RaritiesColors.getRarityColors(rarity / 10).middle
    ) {
        VerticalGrid(
            size = equips.size,
            cells = VerticalGridCells.Adaptive(48.dp)
        ) { index ->
            val equipId = equips[index].equipmentId
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
                ImageIcon(
                    url = UrlUtil.getEquipIconUrl(equipId),
                    onClick = { onEquipClick(equipId) }
                )
            }
        }
    }
}
