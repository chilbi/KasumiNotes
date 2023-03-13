package com.kasuminotes.ui.app.drawer

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Sync
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
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
            Text(text)

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.width(160.dp),
                offset = DpOffset((-12).dp, (-43).dp)
            ) {
                content { expanded = false }
            }
        },
        modifier = Modifier.clickable { expanded = true },
        leadingContent = { Icon(iconVector, null) },
        trailingContent = { Icon(if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore, null) }
    )
}

@Composable
fun CheckIcon() {
    Icon(Icons.Filled.Check, null)
}

@Composable
fun SyncIcon(enable: Boolean) {
    if (enable) {
        val infiniteTransition = rememberInfiniteTransition()
        val degrees by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = -360f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 1000,
                    easing = LinearEasing
                )
            )
        )
        Icon(
            imageVector = Icons.Filled.Sync,
            contentDescription = null,
            modifier = Modifier.rotate(degrees),
            tint = MaterialTheme.colorScheme.primary
        )
    } else {
        Icon(Icons.Filled.Sync, null)
    }
}
