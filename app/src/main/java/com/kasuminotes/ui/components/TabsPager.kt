package com.kasuminotes.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerScope
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun TabsPager(
    scrollable: Boolean,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    contentColor: Color = contentColorFor(containerColor),
    edgePadding: Dp = 52.dp,// ScrollableTabRowPadding
    onTabClick: ((page: Int) -> Unit)? = null,
    tabContent: @Composable ColumnScope.(page: Int) -> Unit,
    pageContent: @Composable PagerScope.(page: Int) -> Unit
) {
    Column {
        if (scrollable) {
            ScrollableTabRow(
                selectedTabIndex = pagerState.currentPage,
                modifier = modifier,
                containerColor = containerColor,
                contentColor = contentColor,
                edgePadding = edgePadding,
                indicator = { tabPositions ->
                    SecondaryIndicator(
                        Modifier.pagerTabIndicatorOffset(pagerState, tabPositions),
                        color = contentColor
                    )
                },
                tabs = { Tabs(pagerState, onTabClick, tabContent) }
            )
        } else {
            TabRow(
                selectedTabIndex = pagerState.currentPage,
                modifier = modifier,
                containerColor = containerColor,
                contentColor = contentColor,
                indicator = { tabPositions ->
                    SecondaryIndicator(
                        Modifier.pagerTabIndicatorOffset(pagerState, tabPositions),
                        color = contentColor
                    )
                },
                tabs = { Tabs(pagerState, onTabClick, tabContent) }
            )
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.Top,
            key = { it },
            pageContent = pageContent
        )
    }
}

@Composable
private fun Tabs(
    pagerState: PagerState,
    onTabClick: ((page: Int) -> Unit)?,
    tabContent: @Composable ColumnScope.(page: Int) -> Unit
) {
    val scope = rememberCoroutineScope()
    repeat(pagerState.pageCount) { page->
        val onClick = remember<() -> Unit>(page, onTabClick) {{
            if (onTabClick == null) {
                scope.launch { pagerState.animateScrollToPage(page) }
            } else {
                onTabClick(page)
            }
        }}
        Tab(
            selected = page == pagerState.currentPage,
            onClick = onClick,
        ) {
            tabContent(page)
        }
    }
}
