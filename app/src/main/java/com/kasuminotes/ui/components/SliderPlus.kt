package com.kasuminotes.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SliderPlus(
    value: Int,
    minValue: Int,
    maxValue: Int,
    onValueChange: (Int) -> Unit,
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
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
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
    }
}