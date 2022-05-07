package com.kasuminotes.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
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
import com.kasuminotes.common.AtkType
import com.kasuminotes.common.OrderBy
import com.kasuminotes.common.Position

@Composable
fun FilterMenu(
    atkType: AtkType,
    position: Position,
    orderBy: OrderBy,
    sortDesc: Boolean,
    onAtkTypeChange: (AtkType) -> Unit,
    onPositionChange: (Position) -> Unit,
    onOrderByChange: (OrderBy) -> Unit
) {
    Row(Modifier.fillMaxWidth()) {
        AtkTypeMenu(atkType, onAtkTypeChange)

        PositionMenu(position, onPositionChange)

        OrderByMenu(orderBy, sortDesc, onOrderByChange)
    }
}

@Composable
private fun RowScope.AtkTypeMenu(
    atkType: AtkType,
    onAtkTypeChange: (AtkType) -> Unit
) {
    FilterMenuItem(
        alignment = Alignment.CenterStart,
        label = stringResource(atkType.resId)
    ) { onCollapse ->
        AtkType.values().forEach { type ->
            DropdownMenuItem(onClick = { onAtkTypeChange(type).also { onCollapse() } }) {
                Text(stringResource(type.resId))
            }
        }
    }
}

@Composable
private fun RowScope.PositionMenu(
    position: Position,
    onPositionChange: (Position) -> Unit
) {
    FilterMenuItem(
        alignment = Alignment.Center,
        label = stringResource(position.resId)
    ) { onCollapse ->
        Position.values().forEach { pos ->
            DropdownMenuItem(onClick = { onPositionChange(pos).also { onCollapse() } }) {
                Text(stringResource(pos.resId))
            }
        }
    }
}

@Composable
private fun RowScope.OrderByMenu(
    orderBy: OrderBy,
    sortDesc: Boolean,
    onOrderByChange: (OrderBy) -> Unit
) {
    FilterMenuItem(
        alignment = Alignment.CenterEnd,
        label = stringResource(orderBy.resId) + stringResource(if (sortDesc) R.string.desc else R.string.asc)
    ) { onCollapse ->
        OrderBy.values().forEach { order ->
            DropdownMenuItem(onClick = { onOrderByChange(order).also { onCollapse() } }) {
                Text(stringResource(order.resId))
            }
        }
    }
}

@Composable
private fun RowScope.FilterMenuItem(
    alignment: Alignment,
    label: String,
    content: @Composable ColumnScope.(onCollapse: () -> Unit) -> Unit
) {
    Box(Modifier.weight(1f)) {
        Box(Modifier.align(alignment)) {
            var expanded by remember { mutableStateOf(false) }
            TextButton(
                onClick = { expanded = true },
                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colors.onPrimary)
            ) {
                Text(label)
                Icon(
                    imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = null
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.heightIn(max = 420.dp)
            ) {
                content { expanded = false }
            }
        }
    }
}
