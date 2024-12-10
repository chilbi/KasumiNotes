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
import com.kasuminotes.ui.app.state.CharaListState
import com.kasuminotes.ui.components.BackButton
import com.kasuminotes.ui.components.FilterCharaButton
import com.kasuminotes.ui.components.FilterCharaMenu
import com.kasuminotes.ui.components.TopBar
import com.kasuminotes.ui.components.SearchBar

@Composable
fun UserEditorTopBar(
    charaListState: CharaListState,
    title: String,
    onBack: () -> Unit,
    content: @Composable ColumnScope.() -> Unit = {}
) {
    var visibleSearch by rememberSaveable { mutableStateOf(false) }

    TopBar(
        title = {
            if (visibleSearch) {
                SearchBar(charaListState.searchText, charaListState::changeSearchText)
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
                    charaListState.changeSearchText("")
                } else {
                    visibleSearch = true
                }
            }) {
                Icon(
                    if (visibleSearch) Icons.Filled.Close else Icons.Filled.Search,
                    null
                )
            }
            FilterCharaButton(charaListState)
        },
        content = {
            FilterCharaMenu(charaListState)

            content()
        }
    )
}