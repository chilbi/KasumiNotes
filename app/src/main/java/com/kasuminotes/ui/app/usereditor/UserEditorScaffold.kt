package com.kasuminotes.ui.app.usereditor

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.insets.navigationBarsHeight
import com.kasuminotes.common.AtkType
import com.kasuminotes.common.OrderBy
import com.kasuminotes.common.Position

@Composable
fun UserEditorScaffold(
    title: String,
    searchText: String,
    atkType: AtkType,
    position: Position,
    orderBy: OrderBy,
    sortDesc: Boolean,
    onSearchTextChange: (String) -> Unit,
    onAtkTypeChange: (AtkType) -> Unit,
    onPositionChange: (Position) -> Unit,
    onOrderByChange: (OrderBy) -> Unit,
    onBack: () -> Unit,
    floatingActionButton: @Composable () -> Unit = {},
    topBarContent: @Composable ColumnScope.() -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            UserEditorTopBar(
                title,
                searchText,
                atkType,
                position,
                orderBy,
                sortDesc,
                onSearchTextChange,
                onAtkTypeChange,
                onPositionChange,
                onOrderByChange,
                onBack,
                topBarContent
            )
        },
        bottomBar = {
            Spacer(Modifier.navigationBarsHeight())
        },
        floatingActionButton = floatingActionButton,
        content = content
    )
}
