package com.kasuminotes.ui.app.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import coil.compose.rememberAsyncImagePainter
import com.kasuminotes.ui.app.state.UiState
import com.kasuminotes.ui.app.state.UserState
import com.kasuminotes.ui.components.FilterCharaButton
import com.kasuminotes.ui.components.FilterCharaMenu
import com.kasuminotes.ui.components.TopBar
import com.kasuminotes.ui.components.SearchBar
import com.kasuminotes.utils.UrlUtil

@Composable
fun HomeTopBar(
    userState: UserState,
    uiState: UiState,
    onDrawerOpen: () -> Unit
) {
    TopBar(
        title = {
            val onClear = remember {{ userState.charaListState.changeSearchText("") }}
            SearchBar(
                userState.charaListState.searchText,
                userState.charaListState::changeSearchText,
                onClear
            )
        },
        navigationIcon = {
            IconButton(onDrawerOpen) {
                Image(
                    painter = rememberAsyncImagePainter(UrlUtil.getUserIconUrl(userState.userId)),
                    contentDescription = null,
                    modifier = Modifier.clip(CircleShape)
                )
            }
        },
        actions = {
            IconButton(uiState::toggleImageVariant) {
                Icon(uiState.charaImageState.vector, null)
            }
            FilterCharaButton(userState.charaListState)
        },
        content = {
            FilterCharaMenu(userState.charaListState)
        }
    )
}

