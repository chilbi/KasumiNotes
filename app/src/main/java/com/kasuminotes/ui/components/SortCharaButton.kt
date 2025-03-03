package com.kasuminotes.ui.components

import androidx.compose.foundation.layout.heightIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kasuminotes.R
import com.kasuminotes.common.OrderBy
import com.kasuminotes.state.CharaListState

@Composable
fun SortCharaButton(charaListState: CharaListState) {
    var expanded by remember { mutableStateOf(false) }
    IconButton(onClick = { expanded = true }) {
        Icon(Icons.AutoMirrored.Filled.Sort, null)
    }
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
        modifier = Modifier.heightIn(max = 420.dp)
    ) {
        val sortDescStr = stringResource(if (charaListState.sortDesc) R.string.desc else R.string.asc)
        OrderBy.entries.forEach { order ->
            val isActive = order == charaListState.orderBy
            DropdownMenuItem(
                text = {
                    Text(
                        text = stringResource(order.resId) + if (isActive) sortDescStr else "",
                        color = if (isActive) MaterialTheme.colorScheme.primary else Color.Unspecified
                    )
                },
                onClick = { charaListState.changeOrderBy(order).also { expanded = false } }
            )
        }
    }
}
