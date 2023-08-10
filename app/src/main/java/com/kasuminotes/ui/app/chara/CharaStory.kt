package com.kasuminotes.ui.app.chara

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kasuminotes.R
import com.kasuminotes.data.Property
import com.kasuminotes.data.StoryItem
import com.kasuminotes.ui.components.PropertyTable
import com.kasuminotes.ui.components.PlaceImage
import com.kasuminotes.ui.components.TabsPager
import com.kasuminotes.ui.components.LabelContainer
import com.kasuminotes.ui.components.UnderlineStyle
import com.kasuminotes.ui.components.VerticalGrid
import com.kasuminotes.ui.components.VerticalGridCells

@Composable
fun CharaStory(
    stories: List<StoryItem>,
    scrollState: ScrollState,
    pagerState: PagerState,
    onTabClick: (page: Int) -> Unit
) {
    // TODO tabPage状态不能持久保存
    Box(Modifier.fillMaxSize().verticalScroll(scrollState)) {
        if (stories.isNotEmpty()) {
            TabsPager(
                scrollable = stories.size > 4,
                pagerState = pagerState,
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.secondary,
                edgePadding = 0.dp,
                onTabClick = onTabClick,
                tabContent = { page ->
                    val story = stories[page]
                    Box(
                        Modifier
                            .padding(top = 8.dp)
                            .size(48.dp)
                    ) {
                        PlaceImage(story.iconUrl)
                    }

                    Text(
                        text = story.label,
                        modifier = Modifier.padding(vertical = 4.dp),
                        style = MaterialTheme.typography.labelMedium
                    )
                },
                pageContent = { page ->
                    val storyItem = stories[page]
                    StoryProperties(
                        status = storyItem.status,
                        diffCount = storyItem.diffCount,
                        unlockCount = storyItem.unlockCount
                    )
                }
            )
        }
    }
}

@Composable
private fun StoryProperties(
    status: List<Property>?,
    diffCount: Int,
    unlockCount: Int
) {
    if (status != null) {
        VerticalGrid(
            size = status.size,
            cells = VerticalGridCells.Adaptive(400.dp),
            modifier = Modifier.fillMaxSize().padding(4.dp)// TODO 滚动修饰符在这无效
        ) { index ->
            val property = status[index]
            StoryProperty(
                label = stringResource(R.string.story_d, index + 1 + diffCount),
                unlock = index < unlockCount,
                property = property
            )
        }
    }
}

@Composable
private fun StoryProperty(
    label: String,
    unlock: Boolean,
    property: Property
) {
    LabelContainer(
        label = label,
        color = if (unlock) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.onSurface.copy(0.5f),
        style = if (unlock) UnderlineStyle.Solid else UnderlineStyle.Dotted
    ) {
        PropertyTable(
            property = property,
            indices = property.nonzeroIndices
        )
    }
}
