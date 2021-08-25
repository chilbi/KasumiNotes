package com.kasuminotes.ui.components

import androidx.compose.material.IconToggleButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun Toggle37Button(
    checked: Boolean,
    onToggle: () -> Unit,
    color: Color = MaterialTheme.colors.onSurface,
    checkedColor: Color = color.copy(0.35f)
) {
    IconToggleButton(
        checked = checked,
        onCheckedChange = { onToggle()  }
    ) {
        Text(
            text = "37↑",
            color = if (checked) color else checkedColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
