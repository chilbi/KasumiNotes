package com.kasuminotes.ui.app.equip

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import com.kasuminotes.R
import com.kasuminotes.ui.components.PlaceImage
import com.kasuminotes.ui.components.selectedBg
import com.kasuminotes.ui.theme.selected

@ExperimentalCoilApi
@Composable
fun CraftItem(
    imageUrl: String,
    consumeSum: Int,
    selected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(4.dp)
            .selectedBg(
                selected,
                MaterialTheme.colors.selected,
                MaterialTheme.shapes.small
            )
            .clickable(enabled, onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(Modifier.size(44.dp)) {
            PlaceImage(imageUrl)
        }

        Text(
            text = stringResource(R.string.times_d, consumeSum),
            modifier = Modifier
                .width(32.dp)
                .padding(start = 4.dp),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}