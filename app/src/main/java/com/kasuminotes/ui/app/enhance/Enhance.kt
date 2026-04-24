package com.kasuminotes.ui.app.enhance

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kasuminotes.R
import com.kasuminotes.state.EnhanceState
import com.kasuminotes.ui.components.BackButton
import com.kasuminotes.ui.components.SliderPlus
import com.kasuminotes.ui.components.TabsPager
import com.kasuminotes.ui.components.TopBar

@Composable
fun Enhance(
    enhanceState: EnhanceState,
    onBack: () -> Unit
) {
    val scaffoldTitles = remember { listOf(R.string.talent_level, R.string.talent_skill, R.string.team_skill, R.string.role_mastery) }
    val scaffoldPagerState = rememberPagerState { scaffoldTitles.size }
    val bottomSheetTitles = remember { listOf(R.string.all, R.string.talent_level, R.string.talent_skill, R.string.team_skill, R.string.role_mastery) }
    val bottomSheetPagerState = rememberPagerState { bottomSheetTitles.size }
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopBar(
                title = {
                    Text(stringResource(R.string.knight_enhance))
                },
                navigationIcon = {
                    BackButton(onBack)
                }
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .clickable { showBottomSheet = true }
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.enhance_detail),
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.titleLarge
                )
            }
            Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
        },
        floatingActionButton = {
            when (scaffoldPagerState.currentPage) {
                1 -> TalentSkillFAB(enhanceState)
                2 -> TeamSkillFAB(enhanceState)
            }
        },
        content = { contentPadding ->
            Column(Modifier.padding(contentPadding)) {
                EnhanceTabsPager(scaffoldTitles, scaffoldPagerState, enhanceState)
            }
        }
    )

    if (showBottomSheet) {
        ModalBottomSheet(
            sheetState = rememberModalBottomSheetState(true),
            onDismissRequest = { showBottomSheet = false }
        ) {
            EnhanceDetail(
                bottomSheetTitles,
                bottomSheetPagerState,
                enhanceState
            )
        }
    }
}

@Composable
private fun EnhanceTabsPager(
    titles: List<Int>,
    pagerState: PagerState,
    enhanceState: EnhanceState
) {
    val userScrollEnabled = remember { mutableStateOf(true) }
    val changeUserScrollEnabled = remember { { value: Boolean -> userScrollEnabled.value = value } }

    TabsPager(
        scrollable = true,
        pagerState = pagerState,
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.primary,
        edgePadding = 16.dp,
        userScrollEnabled = userScrollEnabled.value,
        tabContent = { page ->
            Text(
                text = stringResource(titles[page]),
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 14.dp),
                style = MaterialTheme.typography.titleSmall
            )
        },
        pageContent = { page ->
            when (page) {
                0 -> {
                    if (enhanceState.talentLevelMap != null) {
                        TalentLevelView(enhanceState, changeUserScrollEnabled)
                    }
                }
                1 -> {
                    if (enhanceState.currentPageNodeList != null) {
                        TalentSkillView(enhanceState)
                    }
                }
                2 -> {
                    if (enhanceState.teamSkillNodeList != null) {
                        TeamSkillView(enhanceState)
                    }
                }
                3 -> {
                    if (enhanceState.roleEnhanceMap != null) {
                        RoleMasteryView(enhanceState)
                    }
                }
            }
        }
    )
}

@Composable
private fun TalentSkillFAB(
    enhanceState: EnhanceState
) {
    if (enhanceState.currentPageNodeList == null) {
        return
    }
    var expanded by remember { mutableStateOf(false) }
    FloatingActionButton(
        onClick = { expanded = true }
    ) {
        Text("${enhanceState.currentPageNum}/${enhanceState.maxPageNum}")
    }
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        DropdownMenuItem(
            text= { Text(stringResource(R.string.prev_page)) },
            onClick = enhanceState::prevPage,
            enabled = enhanceState.currentPageNum > 1
        )
        DropdownMenuItem(
            text= { Text(stringResource(R.string.next_page)) },
            onClick = enhanceState::nextPage,
            enabled = enhanceState.currentPageNum < enhanceState.maxPageNum
        )
    }
}

@Composable
private fun TeamSkillFAB(
    enhanceState: EnhanceState
) {
    if (enhanceState.teamSkillNodeList == null) {
        return
    }
    var expanded by remember { mutableStateOf(false) }
    FloatingActionButton(
        onClick = { expanded = true }
    ) {
        val enhancedPoint = if (enhanceState.enhancedTeamSkillNode == null) 0 else enhanceState.enhancedTeamSkillNode!!.nodeId
        val maxPoint = enhanceState.teamSkillNodeList!![0].nodeId
        Text("$enhancedPoint/$maxPoint")
    }
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
        modifier = Modifier.fillMaxWidth()
    ) {
        SliderPlus(
            value = enhanceState.enhancedTeamSkillNode?.nodeId ?: 0,
            minValue = 0,
            maxValue = enhanceState.teamSkillNodeList!!.size,
            onValueChange = { value ->
                if (value == 0) {
                    enhanceState.enhanceTeamNode(null)
                } else {
                    val node = enhanceState.teamSkillNodeList!!.find { it.nodeId == value }
                    enhanceState.enhanceTeamNode(node)
                }
            },
            startDecoration = { value ->
                Text(
                    text = value.toString(),
                    modifier = Modifier.width(32.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        )
    }
}
