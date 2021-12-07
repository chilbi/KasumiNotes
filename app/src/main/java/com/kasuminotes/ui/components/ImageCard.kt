package com.kasuminotes.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ImageCard(
    imageUrl: String,
    primaryText: String,
    secondaryText: String,
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(4.dp)
) {
    Row(modifier.padding(paddingValues)) {
        Box(Modifier.size(48.dp)) {
            PlaceImage(imageUrl)
        }
        Column(
            modifier = Modifier
                .height(48.dp)
                .padding(start = 8.dp),
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                text = primaryText,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = secondaryText,
                color = LocalContentColor.current.copy(0.6f),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
