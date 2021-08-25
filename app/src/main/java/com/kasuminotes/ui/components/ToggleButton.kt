package com.kasuminotes.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TextToggleButton(
    leftText: String,
    rightText: String,
    leftChecked: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    color: Color = MaterialTheme.colors.secondary,
    contentColor: Color = contentColorFor(color),
    shape: Shape = MaterialTheme.shapes.small,
    padding: PaddingValues = PaddingValues(4.dp),
    fontSize: TextUnit = 14.sp,
    fontWeight: FontWeight = FontWeight.Bold
) {
    Row(
        modifier
            .clip(shape)
            .clickable(
                enabled = enabled,
                role = Role.Button,
                onClick = onToggle
            )
    ) {
        Text(
            text = leftText,
            modifier = if (leftChecked) Modifier.background(color).padding(padding)
            else Modifier.background(Color.White).padding(padding),
            color = if (leftChecked) contentColor else Color.Gray,
            fontSize = fontSize,
            fontWeight = fontWeight
        )
        Text(
            text = rightText,
            modifier = if (!leftChecked) Modifier.background(color).padding(padding)
            else Modifier.background(Color.White).padding(padding),
            color = if (!leftChecked) contentColor else Color.Gray,
            fontSize = fontSize,
            fontWeight = fontWeight
        )
    }
}