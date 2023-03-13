package com.kasuminotes.ui.app.equip

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kasuminotes.R
import com.kasuminotes.ui.components.PlaceImage
import com.kasuminotes.ui.components.selectedContainerColor

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
            .selectedContainerColor(selected)
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
            style = MaterialTheme.typography.bodyMedium
        )
    }
}