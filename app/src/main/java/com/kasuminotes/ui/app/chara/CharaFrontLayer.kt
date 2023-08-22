package com.kasuminotes.ui.app.chara

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kasuminotes.R
import com.kasuminotes.data.CharaStoryStatus
import com.kasuminotes.data.EquipData
import com.kasuminotes.data.Property
import com.kasuminotes.data.StoryItem
import com.kasuminotes.data.UniqueData
import com.kasuminotes.data.UnitAttackPattern
import com.kasuminotes.data.UnitData
import com.kasuminotes.data.UnitPromotion
import com.kasuminotes.data.UnitSkillData
import com.kasuminotes.data.UserData
import com.kasuminotes.data.UserProfile
import com.kasuminotes.ui.components.BackdropScaffoldDefaults
import com.kasuminotes.ui.components.PlaceImage
import com.kasuminotes.ui.components.TabsPager
import com.kasuminotes.utils.UrlUtil
import kotlinx.coroutines.launch

@Composable
fun CharaFrontLayer(
    userData: UserData,
    unitData: UnitData,
    charaStoryStatus: CharaStoryStatus?,
    sharedProfiles: List<UserProfile>?,
    unique1Data: UniqueData?,
    promotions: List<UnitPromotion>,
    unitAttackPatternList: List<UnitAttackPattern>,
    unitSkillData: UnitSkillData?,
    property: Property,
    onEquipClick: (EquipData) -> Unit,
    onUniqueClick: (uniqueData: UniqueData, slot: Int) -> Unit,
    onSummonsClick: (summons: List<Int>, skillLevel: Int) -> Unit,
    onCharaChange: (UserProfile) -> Unit,
    onToggle: () -> Unit
) {
    val titles = remember { listOf(R.string.profile, R.string.story, R.string.equipment, R.string.skill) }

    FrontLayerWrapper(
        titles.size,
        unitData.unitId,
        userData.rarity,
        onToggle
    ) {
        val stories: List<StoryItem> = remember(userData, charaStoryStatus, sharedProfiles) {
            if (charaStoryStatus == null || sharedProfiles == null) {
                emptyList()
            } else {
                charaStoryStatus.getStoryList(
                    unitData.unitId,
                    userData.rarity,
                    unitData.maxRarity,
                    userData.loveLevel,
                    unitData.unitName,
                    sharedProfiles
                )
            }
        }
        val style = MaterialTheme.typography.labelMedium
        val scope = rememberCoroutineScope()
        val profileScrollState = rememberScrollState()
        val storyScrollState = rememberScrollState()
        val equipmentGridState = rememberLazyGridState()
        val skillScrollState = rememberScrollState()
        val storyPagerState = rememberPagerState { stories.size }
        val onStoryTabClick = remember<(page: Int) -> Unit>(sharedProfiles) {
            { page ->
                if (storyPagerState.currentPage != page) {
                    scope.launch { storyPagerState.animateScrollToPage(page) }
                } else if (page > 0) {
                    scope.launch {
                        storyPagerState.scrollToPage(0)
                        onCharaChange(sharedProfiles!![page - 1])
                    }
                }
            }
        }

        TabsPager(
            scrollable = false,
            pagerState = rememberPagerState(3) { titles.size },
            modifier = Modifier.height(BackdropScaffoldDefaults.HeaderHeight),
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.primary,
            tabContent = { page ->
                Box(Modifier.height(BackdropScaffoldDefaults.HeaderHeight)) {
                    Text(
                        text = stringResource(titles[page]),
                        modifier = Modifier.align(Alignment.Center),
                        style = style
                    )
                }
            },
            pageContent = { page ->
                when (page) {
                    0 -> {
                        CharaProfile(unitData, profileScrollState)
                    }
                    1 -> {
                        CharaStory(
                            stories,
                            storyScrollState,
                            storyPagerState,
                            onStoryTabClick,
                        )
                    }
                    2 -> {
                        CharaEquips(
                            unique1Data,
                            promotions,
                            equipmentGridState,
                            onEquipClick,
                            onUniqueClick
                        )
                    }
                    3 -> {
                        CharaSkill(
                            userData,
                            unitData,
                            unitAttackPatternList,
                            unitSkillData,
                            property,
                            skillScrollState,
                            onSummonsClick
                        )
                    }
                }
            }
        )
    }
}

@Composable
private fun FrontLayerWrapper(
    titlesSize: Int,
    unitId: Int,
    rarity: Int,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    BoxWithConstraints {
        Surface(
            modifier = Modifier.padding(top = 28.dp),
            shape = BackdropScaffoldDefaults.frontLayerShape,
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = BackdropScaffoldDefaults.FrontLayerElevation,
            content = content
        )

        val offsetX = remember(maxWidth) { maxWidth / titlesSize * 3 - 28.dp }
        Box(
            Modifier
                .offset(offsetX)
                .size(56.dp)
                .clip(CircleShape)
                .clickable(onClick = onClick)
        ) {
            PlaceImage(
                url = UrlUtil.getUnitIconUrl(unitId, rarity),
                shape = CircleShape
            )
        }
    }
}
