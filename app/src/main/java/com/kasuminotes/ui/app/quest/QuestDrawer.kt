package com.kasuminotes.ui.app.quest

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.Icon
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.kasuminotes.R
import com.kasuminotes.common.QuestMode

@Composable
fun QuestDrawer(
    questMode: QuestMode,
    onQuestModeChange: (QuestMode) -> Unit
) {
    val color = MaterialTheme.colors.primary

    val selectable: (QuestMode) -> Modifier = { mode ->
        val selected = mode == questMode
        Modifier
            .then(
                if (selected) Modifier.background(color)
                else Modifier
            )
            .selectable(selected) {
                onQuestModeChange(mode)
            }
    }
    Column(Modifier.selectableGroup()) {
        ListItem(
            modifier = selectable(QuestMode.Equip),
            icon = {
                Icon(Icons.Filled.Gavel, null)
            },
            trailing = {
                Icon(Icons.Filled.ArrowForwardIos, null)
            },
            text = {
                Text(
                    text = stringResource(R.string.equip_list),
                    fontWeight = FontWeight.Bold
                )
            }
        )

        ListItem(
            modifier = selectable(QuestMode.Search),
            icon = {
                Icon(Icons.Filled.Search, null)
            },
            trailing = {
                Icon(Icons.Filled.ArrowForwardIos, null)
            },
            text = {
                Text(
                    text = stringResource(R.string.search_list),
                    fontWeight = FontWeight.Bold
                )
            }
        )

        ListItem(
            modifier = selectable(QuestMode.Map),
            icon = {
                Icon(Icons.Filled.Photo, null)
            },
            trailing = {
                Icon(Icons.Filled.ArrowForwardIos, null)
            },
            text = {
                Text(
                    text = stringResource(R.string.map_list),
                    fontWeight = FontWeight.Bold
                )
            }
        )
    }
}
