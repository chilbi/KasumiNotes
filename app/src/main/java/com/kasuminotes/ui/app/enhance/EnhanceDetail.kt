package com.kasuminotes.ui.app.enhance

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kasuminotes.R
import com.kasuminotes.common.Role
import com.kasuminotes.common.Talent
import com.kasuminotes.state.EnhanceState
import com.kasuminotes.ui.components.CenterText
import com.kasuminotes.ui.components.Container
import com.kasuminotes.ui.components.Infobar
import com.kasuminotes.ui.components.PlaceImage
import com.kasuminotes.ui.components.TabsPager
import com.kasuminotes.ui.components.UnderlineLabel
import com.kasuminotes.utils.UrlUtil

@Composable
fun EnhanceDetail(
    titles: List<Int>,
    pagerState: PagerState,
    enhanceState: EnhanceState
) {
    TabsPager(
        scrollable = true,
        pagerState = pagerState,
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.primary,
        edgePadding = 16.dp,
        tabContent = { page ->
            Text(
                text = stringResource(titles[page]),
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 14.dp),
                style = MaterialTheme.typography.titleSmall
            )
        },
        pageContent = { page ->
            Column(Modifier.padding(8.dp)) {
                when (page) {
                    0 -> {
                        TalentEnhanceDetail(
                            enhanceState.groupedTalentEnhancedMap,
                            enhanceState.talentConsumeMap
                        )
                    }
                    1 -> {
                        TalentEnhanceDetail(
                            enhanceState.groupedTalentLevelEnhancedMap,
                            enhanceState.talentLevelConsumeMap
                        )
                    }
                    2 -> {
                        TalentEnhanceDetail(
                            enhanceState.groupedTalentSkillEnhancedMap,
                            enhanceState.talentSkillConsumeMap
                        )
                    }
                    3 -> {
                        TalentEnhanceDetail(
                            enhanceState.groupedTeamSkillEnhancedMap,
                            enhanceState.teamSkillConsumeMap
                        )
                    }
                    4 -> {
                        RoleEnhanceDetail(
                            enhanceState.groupedRoleEnhancedMap,
                            enhanceState.roleConsumeMapEntries
                        )
                    }
                }
            }
        }
    )
}

@Composable
private fun TalentEnhanceDetail(
    groupedEnhancedMap: Map<Int, List<Pair<Int, Int>>>,
    consumeMap: Map<Int, Int>
) {
    var selectedTalentId by remember { mutableIntStateOf(Talent.talentIdList[0]) }

    Column(Modifier.verticalScroll(rememberScrollState())) {
        SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
            Talent.talentIdList.forEachIndexed { index, talentId ->
                SegmentedButton(
                    selected = selectedTalentId == talentId,
                    onClick = { selectedTalentId = talentId },
                    shape = SegmentedButtonDefaults.itemShape(index, Talent.talentIdList.size),
                    icon = {}
                ) {
                    Image(
                        painter = painterResource(Talent.fromId(talentId).imgId),
                        contentDescription = null
                    )
                }
            }
        }

        UnderlineLabel(
            label = stringResource(Talent.fromId(selectedTalentId).strId) +
                    stringResource(R.string.talent) +
                    stringResource(R.string.enhance_detail),
            color = MaterialTheme.colorScheme.primary
        )

        val enhancedList = groupedEnhancedMap[selectedTalentId]
        if (enhancedList == null) {
            CenterText(stringResource(R.string.no_data))
        } else {
            Container {
                enhancedList.forEach { item ->
                    TalentEnhanceValue(item.first, item.second)
                }
            }
        }

        UnderlineLabel(
            label = stringResource(R.string.consume_item),
            color = MaterialTheme.colorScheme.primary
        )

        if (consumeMap.isEmpty()) {
            CenterText(stringResource(R.string.no_data))
        } else {
            consumeMap.forEach { entry ->
                TalentConsumeDetail(entry.key, entry.value)
            }
        }
    }
}

@Composable
private fun RoleEnhanceDetail(
    groupedEnhancedMap: Map<Int, List<Pair<Int, Int>>>,
    consumeMapEntries: List<Map.Entry<Int, Int>>
) {
    var selectedRoleId by remember { mutableIntStateOf(Role.roleIdList[0]) }

    LazyVerticalGrid(GridCells.Adaptive(72.dp)) {
        item(
            key = "buttons",
            span = { GridItemSpan(maxLineSpan) }
        ) {
            SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
                Role.roleIdList.forEachIndexed { index, roleId ->
                    SegmentedButton(
                        selected = selectedRoleId == roleId,
                        onClick = { selectedRoleId = roleId },
                        shape = SegmentedButtonDefaults.itemShape(index, Role.roleIdList.size),
                        icon = {}
                    ) {
                        Image(
                            painter = painterResource(Role.fromId(roleId).imgId),
                            contentDescription = null
                        )
                    }
                }
            }
        }
        item(
            key = "label1",
            span = { GridItemSpan(maxLineSpan) }
        ) {
            UnderlineLabel(
                label = stringResource(Role.fromId(selectedRoleId).strId) +
                        stringResource(R.string.enhance_detail),
                color = MaterialTheme.colorScheme.primary
            )
        }
        item(
            key = "values1",
            span = { GridItemSpan(maxLineSpan) }
        ) {
            val enhancedList = groupedEnhancedMap[selectedRoleId]
            if (enhancedList == null) {
                CenterText(stringResource(R.string.no_data))
            } else {
                Container {
                    enhancedList.forEach { item ->
                        RoleEnhanceValue(item.first, item.second)
                    }
                }
            }
        }
        item(
            key = "label2",
            span = { GridItemSpan(maxLineSpan) }
        ) {
            UnderlineLabel(
                label = stringResource(R.string.consume_item),
                color = MaterialTheme.colorScheme.primary
            )
        }

        if (consumeMapEntries.isEmpty()) {
            item(
                key = "values2",
                span = { GridItemSpan(maxLineSpan) }
            ) {
                CenterText(stringResource(R.string.no_data))
            }
        } else {
            items(
                items = consumeMapEntries,
                key = { it.key }
            ) { entry ->
                RoleConsumeDetail(entry.key, entry.value)
            }
        }
    }
}


@Composable
private fun TalentConsumeDetail(
    itemId: Int,
    consumeNum: Int
) {
    Row(
        modifier = Modifier.padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(Modifier.size(36.dp)) {
            PlaceImage(UrlUtil.getItemIconUrl(itemId))
        }
        Infobar(
            label = stringResource(R.string.required_number),
            value = consumeNum.toString()
        )
    }
}


@Composable
private fun RoleConsumeDetail(
    itemId: Int,
    consumeNum: Int
) {
    Row(
        modifier = Modifier.width(72.dp).padding(4.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(Modifier.size(36.dp)) {
            PlaceImage(UrlUtil.getItemIconUrl(itemId))
        }
        Text(
            text = consumeNum.toString(),
            modifier = Modifier.width(32.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Composable
private fun TalentEnhanceValue(
    parameterType: Int,
    enhanceValue: Int
) {
    val labelValue = getSkillNodeLabelValue(parameterType, enhanceValue)
    Infobar(
        label = labelValue.first,
        value = labelValue.second,
        width = 120.dp
    )
}

@Composable
private fun RoleEnhanceValue(
    parameterType: Int,
    enhanceValue: Int
) {
    val labelValue = getRoleSlotLabelValue(parameterType, enhanceValue)
    Infobar(
        label = labelValue.first,
        value = labelValue.second,
        width = 120.dp
    )
}
