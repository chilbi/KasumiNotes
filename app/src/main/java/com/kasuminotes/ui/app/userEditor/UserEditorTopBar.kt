package com.kasuminotes.ui.app.userEditor

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.kasuminotes.common.AtkType
import com.kasuminotes.common.Element
import com.kasuminotes.common.OrderBy
import com.kasuminotes.common.Position
import com.kasuminotes.ui.components.BackButton
import com.kasuminotes.ui.components.FilterMenu
import com.kasuminotes.ui.components.TopBar
import com.kasuminotes.ui.components.SearchBar

@Composable
fun UserEditorTopBar(
    title: String,
    searchText: String,
    atkType: AtkType,
    position: Position,
    element: Element,
    orderBy: OrderBy,
    sortDesc: Boolean,
    onSearchTextChange: (String) -> Unit,
    onAtkTypeChange: (AtkType) -> Unit,
    onPositionChange: (Position) -> Unit,
    onElementChange: (Element) -> Unit,
    onOrderByChange: (OrderBy) -> Unit,
    onBack: () -> Unit,
    content: @Composable ColumnScope.() -> Unit = {}
) {
    var visibleSearch by rememberSaveable { mutableStateOf(false) }

    TopBar(
        title = {
            if (visibleSearch) {
                SearchBar(searchText, onSearchTextChange)
            } else {
                Text(title)
            }
        },
        navigationIcon = {
            BackButton(onBack)
        },
        actions = {
            IconButton(onClick = {
                if (visibleSearch) {
                    visibleSearch = false
                    onSearchTextChange("")
                } else {
                    visibleSearch = true
                }
            }) {
                Icon(
                    if (visibleSearch) Icons.Filled.Close else Icons.Filled.Search,
                    null
                )
            }
        },
        content = {
            FilterMenu(
                atkType,
                position,
                element,
                orderBy,
                sortDesc,
                onAtkTypeChange,
                onPositionChange,
                onElementChange,
                onOrderByChange
            )

            content()
        }
    )
}