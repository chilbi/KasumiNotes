package com.kasuminotes.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Pagination(
    count: Int,
    page: Int,
    onChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    minSize: Dp = 48.dp,
    selectedColor: Color = MaterialTheme.colorScheme.primary,
    style: TextStyle = MaterialTheme.typography.labelLarge
) {
    BoxWithConstraints(modifier) {
        val cols = maxOf((maxWidth / minSize).toInt(), 1)
        val items = mutableListOf<Int?>()
        if (cols < 5 || count <= cols) {
            for (value in 1..count) {
                items.add(value)
            }
        } else {
            val rightCount = cols / 2
            val leftCount = cols - rightCount
            var startPage = page - leftCount + 1
            var endPage = page + rightCount
            if (startPage < 1) {
                endPage -= startPage - 1
                startPage = 1
            } else if (endPage > count) {
                startPage -= endPage - count
                endPage = count
            }
            for (value in startPage..endPage) {
                items.add(value)
            }
            items[0] = 1
            items[cols - 1] = count
            if (items[1]!! > 2) {
                items[1] = null
            }
            if (items[cols - 2]!! < count - 1) {
                items[cols - 2] = null
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            repeat(cols) { i ->
                Box(
                    Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    if (i < count) {
                        val item = items[i]
                        if (item == null) {
                            val newPage  = if (i == 1) {
                                (items[2]!! + 1) / 2
                            } else {
                                (items[cols - 3]!! + count) / 2
                            }
                            IconButton(onClick = { onChange(newPage) }) {
                                Text(
                                    text = "…",
                                    style = style
                                )
                            }
                        } else {
                            IconButton(onClick = { onChange(item) }) {
                                Text(
                                    text = item.toString(),
                                    color = if (item == page) selectedColor else Color.Unspecified,
                                    style = style
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
