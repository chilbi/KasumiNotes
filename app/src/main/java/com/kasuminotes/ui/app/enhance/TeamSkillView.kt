package com.kasuminotes.ui.app.enhance

import android.content.Context
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.kasuminotes.R
import com.kasuminotes.common.Talent
import com.kasuminotes.data.SkillNode
import com.kasuminotes.data.TeamSkillNode
import com.kasuminotes.state.EnhanceState
import com.kasuminotes.ui.components.AdaptiveWidthLabel
import com.kasuminotes.ui.components.Infobar
import com.kasuminotes.ui.components.PlaceImage
import com.kasuminotes.utils.IconCache
import com.kasuminotes.utils.UrlUtil

@Composable
fun TeamSkillView(
    enhanceState: EnhanceState
) {
    val context = LocalContext.current
    val density = LocalDensity.current
    val iconCache = remember(context) { IconCache(context) }
    val nodes = enhanceState.teamSkillNodeList!!
    val enhancedNode = enhanceState.enhancedTeamSkillNode
    if (nodes.isEmpty()) {
        return
    }

    BoxWithConstraints(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        val listState by remember(nodes, density, maxWidth) {
            derivedStateOf {
                var minX = 2
                var maxX = 2
                var minY = 1
                var maxY = 1
                for (node in nodes) {
                    minX = minOf(minX, node.posX)
                    maxX = maxOf(maxX, node.posX)
                    minY = minOf(minY, node.posY)
                    maxY = maxOf(maxY, node.posY)
                }
                val gridX = minX + maxX
                val gridY = minY + maxY
                with(density) {
                    val gridXDp = maxWidth / gridX
                    var nodeSizeDp = 84.dp
                    if (nodeSizeDp > gridXDp * 2) {
                        nodeSizeDp = gridXDp * 2
                    }
                    val gridYDp = nodeSizeDp
                    val height = gridYDp * gridY
                    NodeSkillListState(
                        maxWidth,
                        height,
                        nodeSizeDp,
//                        nodeSizeDp.toPx(),
                        gridXDp,
                        gridXDp.toPx(),
                        gridYDp,
                        gridYDp.toPx(),
//                        gridX,
                        gridY
                    )
                }
            }
        }

        val viewedNode = remember { mutableStateOf<TeamSkillNode?>(null) }
        val changeViewedNode = remember { { node: TeamSkillNode -> viewedNode.value = node } }
        val closeDialog = remember { { viewedNode.value = null } }

        Box(Modifier.size(listState.width, listState.height)) {
            Canvas(Modifier.matchParentSize()) {
                var node = nodes[0]
                while (node.preNode != null) {
                    val preNode = node.preNode!!
                    val start = Offset(
                        listState.gridXPx * node.posX,
                        listState.gridYPx * (listState.gridY - node.posY)
                    )
                    val end = Offset(
                        listState.gridXPx * preNode.posX,
                        listState.gridYPx * (listState.gridY - preNode.posY)
                    )
                    drawConnection(
                        start,
                        end,
                        enhancedNode != null && node.nodeId <= enhancedNode.nodeId
                    )
                    node = preNode
                }
            }

            nodes.forEach { node ->
                TeamSkillNodeItem(
                    node,
                    enhancedNode,
                    listState,
                    iconCache,
                    changeViewedNode
                )
            }
        }

        if (viewedNode.value != null) {
            Dialog(closeDialog) {
                TeamNodeDetail(
                    viewedNode.value!!,
                    enhancedNode,
                    iconCache,
                    enhanceState::enhanceTeamNode,
                    closeDialog
                )
            }
        }
    }
}

@Composable
private fun TeamSkillNodeItem(
    node: TeamSkillNode,
    enhancedNode: TeamSkillNode?,
    listState: NodeSkillListState,
    iconCache: IconCache,
    onOpenNodeDetail: (TeamSkillNode) -> Unit
) {
    val enhanced = enhancedNode != null && node.nodeId <= enhancedNode.nodeId
    val x = listState.gridXDp * node.posX - listState.nodeSizeDp / 2
    val y = listState.gridYDp * (listState.gridY - node.posY) - listState.nodeSizeDp / 2
    Box(
        Modifier
            .offset(x, y)
            .size(listState.nodeSizeDp)
            .clickable(onClick = { onOpenNodeDetail(node) })
    ) {
        val iconResId = iconCache.getTeamNodeIconResId(node.iconId)
        if (iconResId != null) {
            PlaceImage(
                painter = painterResource(iconResId),
                loading = false,
                shape = CircleShape,
                colorFilter = if (enhanced) null else darkenColorFilter
            )
        } else {
            PlaceImage(
                painter = painterResource(0),
                loading = true,
                shape = CircleShape,
                colorFilter = if (enhanced) null else darkenColorFilter
            )
        }
    }
}

@Composable
private fun TeamNodeDetail(
    node: TeamSkillNode,
    enhancedNode: TeamSkillNode?,
    iconCache: IconCache,
    onEnhanceNode: (node: TeamSkillNode) -> Unit,
    onCloseDialog: () -> Unit
) {
    val context = LocalContext.current
    val title = getTeamTitle(node, context)
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        tonalElevation = AlertDialogDefaults.TonalElevation
    ) {
        Column(Modifier.padding(4.dp)) {
            AdaptiveWidthLabel(
                text = "#${node.nodeId}",
                margin = PaddingValues(start = 8.dp, top = 4.dp),
                padding = PaddingValues(horizontal = 2.dp),
                color = MaterialTheme.colorScheme.tertiary,
                style = MaterialTheme.typography.labelSmall
            )
            Row(
                modifier = Modifier.padding(horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val iconResId = iconCache.getTeamNodeIconResId(node.iconId)
                Box(Modifier.size(32.dp)) {
                    if (iconResId != null) {
                        PlaceImage(
                            painter = painterResource(iconResId),
                            loading = false,
                            shape = CircleShape
                        )
                    } else {
                        PlaceImage(
                            painter = painterResource(0),
                            loading = true,
                            shape = CircleShape
                        )
                    }
                }
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            TeamEnhanceDetail(
                node,
                enhancedNode,
                onEnhanceNode,
                onCloseDialog
            )
        }
    }
}


@Composable
private fun TeamEnhanceDetail(
    node: TeamSkillNode,
    enhancedNode: TeamSkillNode?,
    onEnhanceNode: (node: TeamSkillNode) -> Unit,
    onCloseDialog: () -> Unit
) {
    val skillEnhanceLevel = node.enhanceLevelList.getOrNull(0) ?: return
    skillEnhanceLevel.enhanceDataList.forEach { enhanceData ->
        val labelValue = getSkillNodeLabelValue(enhanceData.parameterType, enhanceData.enhanceValue)
        Infobar(
            label = labelValue.first,
            value = labelValue.second,
            width = 120.dp,
            margin = PaddingValues(horizontal = 16.dp, vertical = 4.dp)
        )
    }

    Row(
        modifier =  Modifier.padding(start = 16.dp, top = 4.dp, end = 12.dp, bottom = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(Modifier.size(36.dp)) {
            PlaceImage(UrlUtil.getItemIconUrl(skillEnhanceLevel.itemId))
        }
        Infobar(
            label = stringResource(R.string.required_number),
            value = skillEnhanceLevel.consumeNum.toString()
        )
    }

    Row(
        modifier = Modifier.fillMaxWidth().padding(end = 4.dp),
        horizontalArrangement = Arrangement.End
    ) {
        Button(
            onClick = {
                onCloseDialog()
                onEnhanceNode(node)
            }
        ) {
            val shouldCancel = enhancedNode != null && enhancedNode.nodeId == 1 && node.nodeId == 1
            Text(stringResource(if (shouldCancel) R.string.cancel_enhance else R.string.enhance))
        }
    }
}

private fun getTeamTitle(node: TeamSkillNode, context: Context): String {
    var chara: String
    val enhanceData = node.enhanceLevelList[0].enhanceDataList[0]
    val talentIdList = enhanceData.talentIdList.filter { it != 0 }
    if (talentIdList.size == 1) {
        val talent = Talent.fromId(talentIdList[0])
        chara = context.getString(R.string.chara_talent1, context.getString(talent.strId))
    } else {
        chara = context.getString(R.string.all_chara)
    }
//    else if (talentIdList.size > 1 && talentIdList.size < 5)
    val skillNode = SkillNode.fromId(node.titleId)
    val title = if (skillNode != null) context.getString(skillNode.titleStrId) else "???"
    return chara + title
}
