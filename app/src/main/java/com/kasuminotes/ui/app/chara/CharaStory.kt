package com.kasuminotes.ui.app.chara

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import com.kasuminotes.R
import com.kasuminotes.data.CharaStoryStatus
import com.kasuminotes.data.Property
import com.kasuminotes.data.StoryItem
import com.kasuminotes.data.UnitData
import com.kasuminotes.data.UserData
import com.kasuminotes.data.UserProfile
import com.kasuminotes.ui.components.PropertyTable
import com.kasuminotes.ui.components.PlaceImage
import com.kasuminotes.ui.components.TabsPanel
import com.kasuminotes.ui.components.UnderlineLabelColumn
import com.kasuminotes.ui.components.UnderlineStyle

@ExperimentalCoilApi
@ExperimentalAnimationApi
@Composable
fun CharaStory(
    userData: UserData,
    unitData: UnitData,
    charaStoryStatus: CharaStoryStatus?,
    sharedProfiles: List<UserProfile>,
    selectedTabIndex: Int,
    onTabIndexSelected: (Int) -> Unit,
    state: ScrollState
) {
    Box(Modifier.fillMaxSize()) {
        val stories: List<StoryItem> = remember(userData, charaStoryStatus, sharedProfiles.size) {
            charaStoryStatus?.getStoryList(
                unitData.unitId,
                userData.rarity,
                userData.loveLevel,
                unitData.unitName,
                sharedProfiles
            ) ?: emptyList()
        }

        TabsPanel(
            size = stories.size,
            scrollable = stories.size > 4,
            selectedTabIndex = selectedTabIndex,
            onTabIndexSelected = onTabIndexSelected,
            backgroundColor = Color.Transparent,
            contentColor = MaterialTheme.colors.onSurface.copy(0.5f),
            edgePadding = 0.dp,
            tabContentFor = { index ->
                val story = stories[index]
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
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            },
            panelContentFor = { index ->
                val storyItem = stories[index]
                StoryProperties(
                    status = storyItem.status,
                    unlockCount = storyItem.unlockCount,
                    state = state
                )
            }
        )
    }
}

@Composable
private fun StoryProperties(
    status: List<Property>?,
    unlockCount: Int,
    state: ScrollState
) {
    if (status == null) return

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(state)
            .padding(4.dp)
    ) {
        status.forEachIndexed { index, property ->
            StoryProperty(
                label = stringResource(R.string.story_d, index + 2),
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
    UnderlineLabelColumn(
        label = label,
        color = if (unlock) MaterialTheme.colors.primaryVariant
        else MaterialTheme.colors.onSurface.copy(0.5f),
        style = if (unlock) UnderlineStyle.Solid else UnderlineStyle.Dotted
    ) {
        PropertyTable(
            property = property,
            indices = property.nonzeroIndices
        )
    }
}
