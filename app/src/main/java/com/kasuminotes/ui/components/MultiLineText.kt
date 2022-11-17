package com.kasuminotes.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MultiLineText(
    text: String,
    fontSize: TextUnit = 13.sp,
    textAlign: TextAlign = TextAlign.Center
) {
    text.split("\\n").forEach { line ->
        Text(
            text = line,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 2.dp),
            fontSize = fontSize,
            textAlign = textAlign
        )
    }
}
