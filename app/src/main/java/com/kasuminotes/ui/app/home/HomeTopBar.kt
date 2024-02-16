package com.kasuminotes.ui.app.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import coil.compose.rememberAsyncImagePainter
import com.kasuminotes.common.AtkType
import com.kasuminotes.common.Element
import com.kasuminotes.common.OrderBy
import com.kasuminotes.common.Position
import com.kasuminotes.ui.components.FilterMenu
import com.kasuminotes.ui.components.TopBar
import com.kasuminotes.ui.components.SearchBar
import com.kasuminotes.utils.UrlUtil

@Composable
fun HomeTopBar(
    userId: Int,
    vector: ImageVector,
    searchText: String,
    atkType: AtkType,
    position: Position,
    element: Element,
    orderBy: OrderBy,
    sortDesc: Boolean,
    onToggleImageVariant: () -> Unit,
    onSearchTextChange: (String) -> Unit,
    onAtkTypeChange: (AtkType) -> Unit,
    onPositionChange: (Position) -> Unit,
    onElementChange: (Element) -> Unit,
    onOrderByChange: (OrderBy) -> Unit,
    onDrawerOpen: () -> Unit
) {
    TopBar(
        title = {
            val onClear = remember {{ onSearchTextChange("") }}
            SearchBar(searchText, onSearchTextChange, onClear)
        },
        navigationIcon = {
            IconButton(onDrawerOpen) {
                Image(
                    painter = rememberAsyncImagePainter(UrlUtil.getUserIconUrl(userId)),
                    contentDescription = null,
                    modifier = Modifier.clip(CircleShape)
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
                element,
                orderBy,
                sortDesc,
                onAtkTypeChange,
                onPositionChange,
                onElementChange,
                onOrderByChange
            )
        }
    )
}
