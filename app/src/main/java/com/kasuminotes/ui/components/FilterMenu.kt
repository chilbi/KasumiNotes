package com.kasuminotes.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.kasuminotes.common.Element
import com.kasuminotes.common.OrderBy
import com.kasuminotes.common.Position
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

@Composable
fun FilterMenu(
    atkType: AtkType,
    position: Position,
    element: Element,
    orderBy: OrderBy,
    sortDesc: Boolean,
    onAtkTypeChange: (AtkType) -> Unit,
    onPositionChange: (Position) -> Unit,
    onElementChange: (Element) -> Unit,
    onOrderByChange: (OrderBy) -> Unit
) {
    Row(
        Modifier.fillMaxWidth(),
        Arrangement.SpaceBetween
    ) {
        ElementMenu(element, onElementChange)
        AtkTypeMenu(atkType, onAtkTypeChange)
        PositionMenu(position, onPositionChange)
        OrderByMenu(orderBy, sortDesc, onOrderByChange)
    }
}

@Composable
private fun RowScope.ElementMenu(
    element: Element,
    onElementChange: (Element) -> Unit
) {
    FilterMenuItem(
        alignment = Alignment.CenterStart,
        label = stringResource(element.resId)
    ) { onCollapse ->
        Element.entries.forEach { ele ->
            DropdownMenuItem(
                text = { Text(stringResource(ele.resId)) },
                onClick = { onElementChange(ele).also { onCollapse() } }
            )
        }
    }
}

@Composable
private fun RowScope.AtkTypeMenu(
    atkType: AtkType,
    onAtkTypeChange: (AtkType) -> Unit
) {
    FilterMenuItem(
        alignment = Alignment.Center,
        label = stringResource(atkType.resId)
    ) { onCollapse ->
        AtkType.entries.forEach { type ->
            DropdownMenuItem(
                text = { Text(stringResource(type.resId)) },
                onClick = { onAtkTypeChange(type).also { onCollapse() } }
            )
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
        Position.entries.forEach { pos ->
            DropdownMenuItem(
                text = { Text(stringResource(pos.resId)) },
                onClick = { onPositionChange(pos).also { onCollapse() } }
            )
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
        OrderBy.entries.forEach { order ->
            DropdownMenuItem(
                text = { Text(stringResource(order.resId)) },
                onClick = { onOrderByChange(order).also { onCollapse() } }
            )
        }
    }
}

@Composable
private fun RowScope.FilterMenuItem(
    alignment: Alignment,
    label: String,
    content: @Composable ColumnScope.(onCollapse: () -> Unit) -> Unit
) {
    Box(Modifier.wrapContentSize()) {
        Box(Modifier.align(alignment)) {
            var expanded by remember { mutableStateOf(false) }
            TextButton(
                onClick = { expanded = true },
                colors = ButtonDefaults.textButtonColors(contentColor = LocalContentColor.current)
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
