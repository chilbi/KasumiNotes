package com.kasuminotes.ui.app.quest

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.kasuminotes.common.QuestType

@Composable
fun QuestLabel(
    questType: QuestType,
    modifier: Modifier = Modifier,
    checked: Boolean = true,
    shape: Shape = MaterialTheme.shapes.extraSmall,
    padding: PaddingValues = PaddingValues(horizontal = 4.dp),
    style: TextStyle = MaterialTheme.typography.labelLarge
) {
    Box(
        modifier
            .background(
                color = if (checked) questType.color else MaterialTheme.colorScheme.onSurface.copy(0.35f),
                shape = shape
            )
            .padding(padding),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = questType.name,
            color = Color.White,
            style = style
        )
    }
}
