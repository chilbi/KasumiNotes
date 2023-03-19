package com.kasuminotes.ui.app.summons

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kasuminotes.ui.components.PlaceImage
import com.kasuminotes.utils.UrlUtil

private val textModifier = Modifier.padding(start = 8.dp, top = 0.dp, end = 8.dp, bottom = 8.dp)

@Composable
fun SummonHeader(
    unitId: Int,
    enemyId: Int?,
    name: String,
) {
    Box(
        Modifier
            .padding(8.dp)
            .size(48.dp)
    ) {
        PlaceImage(UrlUtil.summonIconUrl)
    }

    Text(
        text = name,
        modifier = textModifier,
        color = MaterialTheme.colorScheme.primary,
        style = MaterialTheme.typography.labelMedium
    )

    Text(
        text = "uid:$unitId",
        modifier = textModifier,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        style = MaterialTheme.typography.labelMedium
    )

    if (enemyId != null) {
        Text(
            text = "eid:$enemyId",
            modifier = textModifier,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.labelMedium
        )
    }
}
