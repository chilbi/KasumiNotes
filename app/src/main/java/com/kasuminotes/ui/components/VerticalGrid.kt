package com.kasuminotes.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

sealed class VerticalGridCells {
    class Fixed(val count: Int) : VerticalGridCells()

    class Adaptive(val minSize: Dp) : VerticalGridCells()
}

@Composable
fun VerticalGrid(
    size: Int,
    cells: VerticalGridCells,
    modifier: Modifier = Modifier,
    contentFor: @Composable BoxScope.(index: Int) -> Unit
) {
    when (cells) {
        is VerticalGridCells.Fixed ->
            FixedGrid(
                size = size,
                cols = cells.count,
                modifier = modifier,
                contentFor = contentFor
            )
        is VerticalGridCells.Adaptive ->
            BoxWithConstraints(modifier) {
                val cols = maxOf((maxWidth / cells.minSize).toInt(), 1)
                FixedGrid(
                    size = size,
                    cols = cols,
                    contentFor = contentFor
                )
            }
    }
}

@Composable
private fun FixedGrid(
    size: Int,
    cols: Int,
    modifier: Modifier = Modifier,
    contentFor: @Composable BoxScope.(index: Int) -> Unit
) {
    Column(modifier) {
        if (cols > 1) {
            val rows = (size + cols - 1) / cols
            repeat(rows) { row ->
                Row {
                    repeat(cols) { col ->
                        val index = row * cols + col
                        if (index < size) {
                            Box(Modifier.weight(1f)) {
                                contentFor(index)
                            }
                        } else {
                            Spacer(Modifier.weight(1f))
                        }
                    }
                }
            }
        } else {
            repeat(size) { index ->
                Box {
                    contentFor(index)
                }
            }
        }
    }
}
