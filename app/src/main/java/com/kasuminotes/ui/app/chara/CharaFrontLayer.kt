package com.kasuminotes.ui.app.chara

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.kasuminotes.data.UniqueData
import com.kasuminotes.data.UnitAttackPattern
import com.kasuminotes.data.UnitData
import com.kasuminotes.data.UnitPromotion
import com.kasuminotes.data.UnitSkillData
import com.kasuminotes.data.UserData
import com.kasuminotes.data.UserProfile
import com.kasuminotes.ui.components.BackdropScaffoldDefaults
import com.kasuminotes.ui.components.PlaceImage
import com.kasuminotes.ui.components.TabsPanel
import com.kasuminotes.utils.UrlUtil

@Composable
fun CharaFrontLayer(
    userData: UserData,
    unitData: UnitData,
    charaStoryStatus: CharaStoryStatus?,
    sharedProfiles: List<UserProfile>,
    uniqueData: UniqueData?,
    promotions: List<UnitPromotion>,
    unitAttackPatternList: List<UnitAttackPattern>,
    unitSkillData: UnitSkillData?,
    property: Property,
    onEquipClick: (EquipData) -> Unit,
    onUniqueClick: (UniqueData) -> Unit,
    onSummonsClick: (summons: List<Int>, skillLevel: Int) -> Unit,
    onCharaClick: (UserProfile) -> Unit,
    onToggle: () -> Unit
) {
    val titles = remember { listOf(R.string.profile, R.string.story, R.string.equipment, R.string.skill) }
    val titlesSize = titles.size
    val style = MaterialTheme.typography.labelMedium

    FrontLayerWrapper(
        titlesSize,
        unitData.unitId,
        userData.rarity,
        onToggle
    ) {
        var storySelectedTabIndex by rememberSaveable { mutableStateOf(0) }
        val profileScrollState = rememberScrollState()
        val storyScrollState = rememberScrollState()
        val equipmentGridState = rememberLazyGridState()
        val skillScrollState = rememberScrollState()
        val onTabIndexSelected = remember<(Int) -> Unit>(storySelectedTabIndex, sharedProfiles) {
            { index ->
                if (storySelectedTabIndex != index) {
                    storySelectedTabIndex = index
                } else if (index > 0) {
                    storySelectedTabIndex = 0
                    onCharaClick(sharedProfiles[index - 1])
                }
            }
        }

        TabsPanel(
            size = titlesSize,
            scrollable = false,
            initIndex = 3,
            modifier = Modifier.height(BackdropScaffoldDefaults.HeaderHeight),
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.primary,
            tabContentFor = { index ->
                Box(Modifier.height(BackdropScaffoldDefaults.HeaderHeight)) {
                    Text(
                        text = stringResource(titles[index]),
                        modifier = Modifier.align(Alignment.Center),
                        style = style
                    )
                }
            },
            panelContentFor = { index ->
                when (index) {
                    0 -> {
                        CharaProfile(unitData, profileScrollState)
                    }
                    1 -> {
                        CharaStory(
                            userData,
                            unitData,
                            charaStoryStatus,
                            sharedProfiles,
                            storySelectedTabIndex,
                            onTabIndexSelected,
                            storyScrollState
                        )
                    }
                    2 -> {
                        CharaEquips(
                            uniqueData,
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
