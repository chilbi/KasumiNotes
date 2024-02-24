package com.kasuminotes.ui.app.drawer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

@Composable
fun MenuCaption(text: String) {
    Text(
        text = text,
        modifier = Modifier.padding(start = 16.dp),
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        style = MaterialTheme.typography.bodySmall
    )
}

@Composable
fun MenuItemText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyLarge
    )
}

@Composable
fun ListItemWithDropdownMenu(
    iconVector: ImageVector,
    text: String,
    content: @Composable ColumnScope.(onCollapse: () -> Unit) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ListItem(
        headlineContent = {
            Box(Modifier.fillMaxSize().wrapContentSize(Alignment.TopStart)) {
                Text(text)

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.width(160.dp),
                    offset = DpOffset((-12).dp, (-43).dp)
                ) {
                    content { expanded = false }
                }
            }
        },
        modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.TopStart).clickable { expanded = true },
        leadingContent = { Icon(iconVector, null) },
        trailingContent = { Icon(if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore, null) }
    )
}

@Composable
fun CheckIcon() {
    Icon(Icons.Filled.Check, null)
}
