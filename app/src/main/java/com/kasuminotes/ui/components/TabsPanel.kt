package com.kasuminotes.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun TabsPanel(
    size: Int,
    scrollable: Boolean,
    initIndex: Int,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = contentColorFor(containerColor),
    edgePadding: Dp = 52.dp,// ScrollableTabRowPadding
    tabContentFor: @Composable ColumnScope.(index: Int) -> Unit,
    panelContentFor: @Composable (index: Int) -> Unit
) {
    var selectedTabIndex by rememberSaveable { mutableStateOf(initIndex) }

    TabsPanel(
        size = size,
        scrollable = scrollable,
        selectedTabIndex = selectedTabIndex,
        onTabIndexSelected = { selectedTabIndex = it },
        modifier = modifier,
        containerColor = containerColor,
        contentColor = contentColor,
        edgePadding = edgePadding,
        tabContentFor = tabContentFor,
        panelContentFor = panelContentFor
    )
}

@Composable
fun TabsPanel(
    size: Int,
    scrollable: Boolean,
    selectedTabIndex: Int,
    onTabIndexSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    contentColor: Color = contentColorFor(containerColor),
    edgePadding: Dp = 52.dp,// ScrollableTabRowPadding
    tabContentFor: @Composable ColumnScope.(index: Int) -> Unit,
    panelContentFor: @Composable (index: Int) -> Unit
) {
    Column {
        var prevSelectedTabIndex by remember { mutableStateOf(selectedTabIndex) }

        val tabs: @Composable () -> Unit = {
            repeat(size) { index->
                Tab(
                    selected = index == selectedTabIndex,
                    onClick = {
                        prevSelectedTabIndex = selectedTabIndex
                        onTabIndexSelected(index)
                    },
                ) {
                    tabContentFor(index)
                }
            }
        }

        if (scrollable) {
            ScrollableTabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = modifier,
                containerColor = containerColor,
                contentColor = contentColor,
                edgePadding = edgePadding,
                tabs = tabs
            )
        } else {
            TabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = modifier,
                containerColor = containerColor,
                contentColor = contentColor,
                tabs = tabs
            )
        }

        Box(Modifier.fillMaxSize()) {
            repeat(size) { index->
                TabPanel(
                    index,
                    from = prevSelectedTabIndex,
                    to = selectedTabIndex
                ) {
                    panelContentFor(index)
                }
            }
        }
    }
}

@Composable
private fun TabPanel(
    index: Int,
    from: Int,
    to: Int,
    content: @Composable AnimatedVisibilityScope.() -> Unit
) {
    val x = if (from < to) 1 else -1

    AnimatedVisibility(
        visible = index == to,
        enter = slideInHorizontally(initialOffsetX = { x * it }),
        exit = slideOutHorizontally(targetOffsetX = { -x * it }),
        content = content
    )
}
