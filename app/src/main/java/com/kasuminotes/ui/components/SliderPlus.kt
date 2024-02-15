package com.kasuminotes.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SliderPlus(
    value: Int,
    minValue: Int,
    maxValue: Int,
    onValueChange: (Int) -> Unit,
    startDecoration: (@Composable RowScope.(value: Int) -> Unit)? = null,
    endDecoration: (@Composable RowScope.() -> Unit)? = null
) {
    Row(
        modifier = Modifier.padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val ref = remember { SliderStateRef(null) }
        val sliderState = remember(value) {
            SliderState(
                value.toFloat(),
                0,
                { ref.state?.let { onValueChange(it.value.toInt()) } },
                minValue.toFloat()..maxValue.toFloat(),
            )
        }
        ref.state = sliderState

        if (startDecoration != null) {
            startDecoration(sliderState.value.toInt())
        }

        IconButton(
            onClick = { onValueChange(value - 1) },
            enabled = value > minValue
        ) {
            Icon(Icons.Filled.Remove, null)
        }

        Slider(sliderState, Modifier.weight(1f))

        IconButton(
            onClick = { onValueChange(value + 1) },
            enabled = value < maxValue
        ) {
            Icon(Icons.Filled.Add, null)
        }

        if (endDecoration != null) {
            endDecoration()
        }
    }
}

@Composable
fun SliderPlus(
    value: Int,
    minValue: Int,
    maxValue: Int,
    onValueChange: (Int) -> Unit,
    checked: Boolean? = null,
    onCheckedChange: ((Boolean) -> Unit)? = null,
    label: @Composable ColumnScope.() -> Unit
) {
    SliderPlus(
        value,
        minValue,
        maxValue,
        onValueChange,
        startDecoration = {
            Column(
                modifier = Modifier.size(64.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                label()

                Text(
                    text = it.toString(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        },
        endDecoration = {
            if (checked != null) {
                Checkbox(checked, onCheckedChange)
            }
        }
    )
}

@Stable
private class SliderStateRef(var state: SliderState?)
