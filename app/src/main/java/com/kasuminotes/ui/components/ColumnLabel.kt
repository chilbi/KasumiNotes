package com.kasuminotes.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ColumnLabel(
    text: String,
    padding: Dp = 4.dp
) {
    Text(
        text = text,
        modifier = Modifier
            .padding(padding)
            .width(72.dp)
            .background(
                color = MaterialTheme.colors.primary,
                shape = MaterialTheme.shapes.small
            )
            .padding(vertical = 2.dp),
        color = MaterialTheme.colors.onPrimary,
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        textAlign = TextAlign.Center
    )
}