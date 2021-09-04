package com.kasuminotes.ui.app.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.kasuminotes.common.AtkType
import com.kasuminotes.common.OrderBy
import com.kasuminotes.common.Position
import com.kasuminotes.ui.components.FilterMenu
import com.kasuminotes.ui.components.ImmersiveTopAppBar
import com.kasuminotes.ui.components.SearchBar
import com.kasuminotes.utils.UrlUtil

@ExperimentalCoilApi
@Composable
fun HomeTopBar(
    userId: Int,
    vector: ImageVector,
    searchText: String,
    atkType: AtkType,
    position: Position,
    orderBy: OrderBy,
    sortDesc: Boolean,
    onToggleImageVariant: () -> Unit,
    onSearchTextChange: (String) -> Unit,
    onAtkTypeChange: (AtkType) -> Unit,
    onPositionChange: (Position) -> Unit,
    onOrderByChange: (OrderBy) -> Unit,
    onDrawerOpen: () -> Unit
) {
    ImmersiveTopAppBar(
        title = {
            SearchBar(searchText, onSearchTextChange)
        },
        navigationIcon = {
            IconButton(onDrawerOpen) {
                Image(
                    painter = rememberImagePainter(UrlUtil.getUserIconUrl(userId)),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(4.dp)
                        .clip(CircleShape)
                )
            }
        },
        actions = {
            IconButton(onToggleImageVariant) {
                Icon(vector, null)
            }
        },
        content = {
            FilterMenu(
                atkType,
                position,
                orderBy,
                sortDesc,
                onAtkTypeChange,
                onPositionChange,
                onOrderByChange
            )
        }
    )
}
