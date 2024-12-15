package com.kasuminotes.ui.components

import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kasuminotes.R
import com.kasuminotes.state.CharaListState

@Composable
fun FilterCharaButton(charaListState: CharaListState) {
    var expanded by remember { mutableStateOf(false) }
    BadgedBox(
        badge = {
            if (charaListState.rarity6 || charaListState.unique1 || charaListState.unique2) {
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
            leadingIcon = { RadioButton(charaListState.rarity6, null) },
            onClick = {
                charaListState.toggleRarity6()
                expanded = false
            }
        )
        DropdownMenuItem(
            text = { Text(stringResource(R.string.unique_equip) + "1") },
            leadingIcon = { RadioButton(charaListState.unique1, null) },
            onClick = {
                charaListState.toggleUnique1()
                expanded = false
            }
        )
        DropdownMenuItem(
            text = { Text(stringResource(R.string.unique_equip) + "2") },
            leadingIcon = { RadioButton(charaListState.unique2, null) },
            onClick = {
                charaListState.toggleUnique2()
                expanded = false
            }
        )
    }
}