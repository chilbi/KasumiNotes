package com.kasuminotes.ui.app.summons

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kasuminotes.data.ExtraEffectData

private val textModifier = Modifier.padding(start = 8.dp, top = 0.dp, end = 8.dp, bottom = 8.dp)

@Composable
fun SummonHeader(
    unitId: Int,
    enemyId: Int?,
    name: String,
    extraEffectData: ExtraEffectData?
) {
    val primary: String
    val secondary: String?
    if (extraEffectData == null) {
        primary = "uid:$unitId"
        secondary = if (enemyId == null) null else "eid:$enemyId"
    } else {
        val enemyIdIndex = extraEffectData.enemyIdList.indexOf(enemyId)
        if (enemyIdIndex > -1) {
            primary = "eid:$enemyId"
            secondary = "exec_timing:${extraEffectData.execTimingList[enemyIdIndex]}"
        } else {
            primary = "uid:$unitId"
            secondary = if (enemyId == null) null else "eid:$enemyId"
        }
    }
    /*Box(
        Modifier
            .padding(8.dp)
            .size(48.dp)
    ) {
        PlaceImage(UrlUtil.summonIconUrl)
    }*/
    Spacer(Modifier.height(8.dp))

    Text(
        text = name,
        modifier = textModifier,
        color = MaterialTheme.colorScheme.primary,
        style = MaterialTheme.typography.labelMedium
    )

    Text(
        text = primary,
        modifier = textModifier,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        style = MaterialTheme.typography.labelMedium
    )

    if (secondary != null) {
        Text(
            text = secondary,
            modifier = textModifier,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.labelMedium
        )
    }
}
