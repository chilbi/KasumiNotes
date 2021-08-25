package com.kasuminotes.ui.app.quest

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kasuminotes.common.QuestType

@Composable
fun QuestLabel(
    questType: QuestType,
    modifier: Modifier = Modifier,
    checked: Boolean = true,
    shape: Shape = MaterialTheme.shapes.small,
    padding: PaddingValues = PaddingValues(horizontal = 4.dp),
    fontSize: TextUnit = 14.sp
) {
    Box(
        modifier
            .background(
                color = if (checked) questType.color else Color.White.copy(0.35f),
                shape = shape
            )
            .padding(padding),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = questType.name,
            color = Color.White,
            fontSize = fontSize,
            fontWeight = FontWeight.Bold
        )
    }
}
