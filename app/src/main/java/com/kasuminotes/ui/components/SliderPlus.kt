package com.kasuminotes.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SliderPlus(
    value: Int,
    minValue: Int,
    maxValue: Int,
    onValueChange: (Int) -> Unit,
    checked: Boolean? = null,
    onCheckedChange: ((Boolean) -> Unit)? = null,
    label: @Composable () -> Unit
) {
    Row(
        modifier = Modifier.padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        var floatValue by rememberSaveable(value) { mutableStateOf(value.toFloat()) }

        val intValue = floatValue.toInt()

        Column(
            modifier = Modifier.size(64.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            label()

            Text(
                text = intValue.toString(),
                style = MaterialTheme.typography.bodyMedium
            )
        }

        IconButton(
            onClick = { onValueChange(value - 1) },
            enabled = value > minValue
        ) {
            Icon(Icons.Filled.Remove, null)
        }

        Slider(
            value = floatValue,
            onValueChange = { floatValue = it },
            modifier = Modifier.weight(1f),
            valueRange = minValue.toFloat()..maxValue.toFloat(),
            onValueChangeFinished = { onValueChange(intValue) }
        )

        IconButton(
            onClick = { onValueChange(value + 1) },
            enabled = value < maxValue
        ) {
            Icon(Icons.Filled.Add, null)
        }

        if (checked != null) {
            Checkbox(checked, onCheckedChange)
        }
    }
}