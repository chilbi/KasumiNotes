package com.kasuminotes.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SkillLabel(
    text: String,
    color: Color = MaterialTheme.colors.onPrimary,
    bgColor: Color = MaterialTheme.colors.primaryVariant,
) {
    Text(
        text = text,
        modifier = Modifier
            .padding(4.dp)
            .background(
                color = bgColor,
                shape = MaterialTheme.shapes.small
            )
            .padding(horizontal = 8.dp),
        fontSize = 14.sp,
        color = color,
        fontWeight = FontWeight.Bold
    )
}
