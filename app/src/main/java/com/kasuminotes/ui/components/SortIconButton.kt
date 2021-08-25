package com.kasuminotes.ui.components

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sort
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate

@Composable
fun SortIconButton(
    sortDesc: Boolean,
    onToggle: () -> Unit
) {
    IconButton(onClick = onToggle) {
        Icon(
            imageVector = Icons.Filled.Sort,
            contentDescription = null,
            modifier = Modifier.rotate(if (sortDesc) 0f else 180f)
        )
    }
}
