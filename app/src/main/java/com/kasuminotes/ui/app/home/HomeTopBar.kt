package com.kasuminotes.ui.app.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.kasuminotes.R
import com.kasuminotes.common.AtkType
import com.kasuminotes.common.Element
import com.kasuminotes.common.OrderBy
import com.kasuminotes.common.Position
import com.kasuminotes.ui.components.FilterMenu
import com.kasuminotes.ui.components.TopBar
import com.kasuminotes.ui.components.SearchBar
import com.kasuminotes.utils.UrlUtil

@Composable
fun HomeTopBar(
    userId: Int,
    vector: ImageVector,
    searchText: String,
    atkType: AtkType,
    position: Position,
    element: Element,
    orderBy: OrderBy,
    sortDesc: Boolean,
    rarity6: Boolean,
    unique1: Boolean,
    unique2: Boolean,
    onRarity6Toggle: () -> Unit,
    onUnique1Toggle: () -> Unit,
    onUnique2Toggle: () -> Unit,
    onToggleImageVariant: () -> Unit,
    onSearchTextChange: (String) -> Unit,
    onAtkTypeChange: (AtkType) -> Unit,
    onPositionChange: (Position) -> Unit,
    onElementChange: (Element) -> Unit,
    onOrderByChange: (OrderBy) -> Unit,
    onDrawerOpen: () -> Unit
) {
    TopBar(
        title = {
            val onClear = remember {{ onSearchTextChange("") }}
            SearchBar(searchText, onSearchTextChange, onClear)
        },
        navigationIcon = {
            IconButton(onDrawerOpen) {
                Image(
                    painter = rememberAsyncImagePainter(UrlUtil.getUserIconUrl(userId)),
                    contentDescription = null,
                    modifier = Modifier.clip(CircleShape)
                )
            }
        },
        actions = {
            IconButton(onToggleImageVariant) {
                Icon(vector, null)
            }
            FilterButton(
                rarity6,
                unique1,
                unique2,
                onRarity6Toggle,
                onUnique1Toggle,
                onUnique2Toggle
            )
        },
        content = {
            FilterMenu(
                atkType,
                position,
                element,
                orderBy,
                sortDesc,
                onAtkTypeChange,
                onPositionChange,
                onElementChange,
                onOrderByChange
            )
        }
    )
}

@Composable
private fun FilterButton(
    rarity6: Boolean,
    unique1: Boolean,
    unique2: Boolean,
    onRarity6Toggle: () -> Unit,
    onUnique1Toggle: () -> Unit,
    onUnique2Toggle: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    BadgedBox(
        badge = {
            if (rarity6 || unique1 || unique2) {
                Badge(Modifier.offset((-4).dp, 4.dp)) {
                    Text("A")
                }
            }
        }
    ) {
        IconButton(onClick = { expanded = true }) {
            Icon(Icons.Filled.FilterList, null)
        }
    }
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
        modifier = Modifier.heightIn(max = 420.dp)
    ) {
        DropdownMenuItem(
            text = { Text(stringResource(R.string.rarity_6)) },
            leadingIcon = { RadioButton(rarity6, null) },
            onClick = {
                onRarity6Toggle()
                expanded = false
            }
        )
        DropdownMenuItem(
            text = { Text(stringResource(R.string.unique_equip) + "1") },
            leadingIcon = { RadioButton(unique1, null) },
            onClick = {
                onUnique1Toggle()
                expanded = false
            }
        )
        DropdownMenuItem(
            text = { Text(stringResource(R.string.unique_equip) + "2") },
            leadingIcon = { RadioButton(unique2, null) },
            onClick = {
                onUnique2Toggle()
                expanded = false
            }
        )
    }
}
