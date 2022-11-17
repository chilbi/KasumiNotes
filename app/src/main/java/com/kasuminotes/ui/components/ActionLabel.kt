package com.kasuminotes.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kasuminotes.ui.theme.Diamond

@Composable
fun ActionLabel(actionNum: Int) {
    Box(
        Modifier
            .size(18.dp)
            .background(MaterialTheme.colors.primary, Diamond),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = actionNum.toString(),
            color = Color.White,
            fontSize = 11.sp
        )
    }
}
