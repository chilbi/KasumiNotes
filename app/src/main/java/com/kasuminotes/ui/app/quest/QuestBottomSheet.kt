package com.kasuminotes.ui.app.quest

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kasuminotes.R
import com.kasuminotes.common.QuestMode

@Composable
fun QuestBottomSheet(
    questMode: QuestMode,
    onQuestModeChange: (QuestMode) -> Unit
) {
    Column {
        QuestModeItem(R.string.equip_list, Icons.Filled.Gavel, QuestMode.Equip, questMode, onQuestModeChange)
        QuestModeItem(R.string.search_list, Icons.Filled.Search, QuestMode.Search, questMode, onQuestModeChange)
        QuestModeItem(R.string.map_list, Icons.Filled.Photo, QuestMode.Map, questMode, onQuestModeChange)
    }
}

@Composable
private fun QuestModeItem(
    @StringRes resId: Int,
    imageVector: ImageVector,
    mode: QuestMode,
    questMode: QuestMode,
    onQuestModeChange: (QuestMode) -> Unit
) {
    ListItem(
        headlineContent = {
            Text(
                text = stringResource(resId),
                style = MaterialTheme.typography.titleMedium
            )
        },
        modifier =  Modifier.clickable { onQuestModeChange(mode) },
        leadingContent = {
            Icon(imageVector, null)
        },
        trailingContent = {
            Icon(Icons.Filled.ArrowForwardIos, null)
        },
        tonalElevation = if (mode == questMode) 8.dp else 0.dp
    )
}
