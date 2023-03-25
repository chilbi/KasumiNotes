package com.kasuminotes.ui.app.chara

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.rememberLazyListState
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
    FrontLayerWrapper(
        unitId = unitData.unitId,
        rarity = userData.rarity,
        onClick = onToggle
    ) {
        val titles = remember {
            listOf(R.string.profile, R.string.story, R.string.equipment, R.string.skill)
        }

        var storySelectedTabIndex by rememberSaveable { mutableStateOf(0) }
        val profileScrollState = rememberScrollState()
        val storyScrollState = rememberScrollState()
        val equipmentScrollState = rememberLazyListState()
        val skillScrollState = rememberScrollState()

        val style = MaterialTheme.typography.labelMedium

        TabsPanel(
            size = titles.size,
            scrollable = false,
            initIndex = 3,
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
                    0 ->
                        CharaProfile(
                            unitData,
                            profileScrollState
                        )
                    1 ->
                        CharaStory(
                            userData,
                            unitData,
                            charaStoryStatus,
                            sharedProfiles,
                            selectedTabIndex = storySelectedTabIndex,
                            onTabIndexSelected = {
                                if (storySelectedTabIndex != it) {
                                    storySelectedTabIndex = it
                                } else if (it > 0) {
                                    storySelectedTabIndex = 0
                                    onCharaClick(sharedProfiles[it - 1])
                                }
                            },
                            state = storyScrollState
                        )
                    2 ->
                        CharaEquips(
                            uniqueData,
                            promotions,
                            state = equipmentScrollState,
                            onEquipClick = onEquipClick,
                            onUniqueClick = onUniqueClick
                        )
                    3 ->
                        CharaSkill(
                            userData,
                            unitData,
                            unitAttackPatternList,
                            unitSkillData,
                            property,
                            state = skillScrollState,
                            onSummonsClick = onSummonsClick
                        )
                }
            }
        )
    }
}

@Composable
private fun FrontLayerWrapper(
    unitId: Int,
    rarity: Int,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Box {
        Surface(
            modifier = Modifier.padding(top = 28.dp),
            shape = BackdropScaffoldDefaults.frontLayerShape,
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = BackdropScaffoldDefaults.FrontLayerElevation,
            content = content
        )

        Box(
            Modifier
                .align(Alignment.TopEnd)
                .offset((-70.181816).dp)
        ) {
            Box(Modifier.size(56.dp).clip(CircleShape).clickable(onClick = onClick)) {
                PlaceImage(
                    url = UrlUtil.getUnitIconUrl(unitId, rarity),
                    shape = CircleShape
                )
            }
        }
    }
}
