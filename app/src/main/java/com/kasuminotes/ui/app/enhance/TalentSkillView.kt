package com.kasuminotes.ui.app.enhance

import android.content.Context
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.kasuminotes.R
import com.kasuminotes.common.Talent
import com.kasuminotes.data.SkillNode
import com.kasuminotes.data.TalentSkillNode
import com.kasuminotes.state.ConnectionNode
import com.kasuminotes.state.EnhanceState
import com.kasuminotes.ui.components.AdaptiveWidthLabel
import com.kasuminotes.ui.components.Infobar
import com.kasuminotes.ui.components.PlaceImage
import com.kasuminotes.utils.IconCache
import com.kasuminotes.utils.UrlUtil

@Composable
fun TalentSkillView(
    enhanceState: EnhanceState
) {
    val context = LocalContext.current
    val density = LocalDensity.current
    val iconCache = remember(context) { IconCache(context) }
    val nodes = enhanceState.currentPageNodeList!!

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
                    var nodeSizeDp = 48.dp
                    if (nodeSizeDp > gridXDp * 2) {
                        nodeSizeDp = gridXDp * 2
                    }
                    val gridYDp = nodeSizeDp * 5 / 4
                    val height = gridYDp * gridY + 56.dp
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

        val viewedNode = remember { mutableStateOf<ConnectionNode?>(null) }
        val changeViewedNode = remember { { node: ConnectionNode -> viewedNode.value = node } }
        val closeDialog = remember { { viewedNode.value = null } }

        Box(Modifier.size(listState.width, listState.height)) {
            Canvas(Modifier.matchParentSize()) {
                enhanceState.connectionList!!.forEach { connectionNode ->
                    if (connectionNode.preNode != null) {
                        val start = Offset(
                            listState.gridXPx * connectionNode.node.posX,
                            listState.gridYPx * (listState.gridY - connectionNode.node.posY)
                        )
                        val end = Offset(
                            listState.gridXPx * connectionNode.preNode.posX,
                            listState.gridYPx * (listState.gridY - connectionNode.preNode.posY)
                        )
                        drawConnection(
                            start,
                            end,
                            connectionNode.enhanceLevel == connectionNode.node.maxLevel
                        )
                    }
                }
            }

            enhanceState.enhanceNodeList!!.forEach { connectionNode ->
                TalentSkillNodeItem(
                    connectionNode,
                    listState,
                    iconCache,
                    changeViewedNode
                )
            }
        }

        if (viewedNode.value != null) {
            Dialog(closeDialog) {
                TalentNodeDetail(
                    viewedNode.value!!,
                    iconCache,
                    enhanceState::enhanceTalentNode,
                    closeDialog
                )
            }
        }
    }
}

@Composable
private fun TalentSkillNodeItem(
    connectionNode: ConnectionNode,
    listState: NodeSkillListState,
    iconCache: IconCache,
    onOpenNodeDetail: (ConnectionNode) -> Unit
) {
    val node = connectionNode.node
    val enhanced = connectionNode.enhanceLevel == node.maxLevel
    val color = getTalentColor(node)
    val x = listState.gridXDp * node.posX - listState.nodeSizeDp / 2
    val y = listState.gridYDp * (listState.gridY - node.posY) - listState.nodeSizeDp / 2
    Box(
        Modifier
            .offset(x, y)
            .size(listState.nodeSizeDp)
            .clickable(onClick = { onOpenNodeDetail(connectionNode) })
    ) {
        val iconResId = iconCache.getNodeIconResId(node.iconId)
        if (iconResId != null) {
            PlaceImage(
                painter = painterResource(iconResId),
                loading = false,
                modifier = Modifier.padding(6.dp),
                shape = CircleShape,
                colorFilter = if (connectionNode.enhanceLevel == 0) darkenColorFilter else null
            )
        } else {
            PlaceImage(
                painter = painterResource(0),
                loading = true,
                modifier = Modifier.padding(6.dp),
                shape = CircleShape,
                colorFilter = if (connectionNode.enhanceLevel == 0) darkenColorFilter else null
            )
        }
        if (enhanced) {
            Image(
                painter = painterResource(R.drawable.node_icon_enhanced_frame),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth().offset(y = (-1.5).dp),
                contentScale = ContentScale.FillWidth
            )
        }
        Text(
            text = "${connectionNode.enhanceLevel}/${node.maxLevel}",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = 3.dp)
                .background(levelBgBrush)
                .padding(horizontal = 4.dp),
            color = color,
            fontSize = 10.sp,
            lineHeight = 10.sp
        )
    }
}

@Composable
private fun TalentNodeDetail(
    connectionNode: ConnectionNode,
    iconCache: IconCache,
    onEnhanceNode: (node: TalentSkillNode, level: Int) -> Unit,
    onCloseDialog: () -> Unit
) {
    val context = LocalContext.current
    val node = connectionNode.node
    val (title, color) = getTalentTitleAndColor(node, context)
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
                val iconResId = iconCache.getNodeIconResId(node.iconId)
                Box(Modifier.size(32.dp)) {
                    if (iconResId != null) {
                        PlaceImage(
                            painter = painterResource(iconResId),
                            loading = false,
                            modifier = Modifier.padding(4.dp),
                            shape = CircleShape
                        )
                    } else {
                        PlaceImage(
                            painter = painterResource(0),
                            loading = true,
                            modifier = Modifier.padding(4.dp),
                            shape = CircleShape
                        )
                    }
                }
                Text(
                    text = title,
                    color = color,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            TalentEnhanceDetail(
                node,
                if (connectionNode.enhanceLevel > 0) connectionNode.enhanceLevel else node.enhanceLevelList.size,
                onEnhanceNode,
                onCloseDialog
            )
        }
    }
}

@Composable
private fun TalentEnhanceDetail(
    node: TalentSkillNode,
    enhanceLevel: Int,
    onEnhanceNode: (node: TalentSkillNode, level: Int) -> Unit,
    onCloseDialog: () -> Unit
) {
    val level = remember { mutableIntStateOf(enhanceLevel) }
    val skillEnhanceLevel = node.enhanceLevelList.getOrNull(level.intValue - 1) ?: return
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
        modifier = Modifier.padding(start = 16.dp, top = 4.dp, end = 12.dp, bottom = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(Modifier.size(36.dp)) {
            PlaceImage(UrlUtil.getItemIconUrl(skillEnhanceLevel.itemId))
        }
        Column {
            Infobar(
                label = stringResource(R.string.promotion_level),
                value = level.intValue.toString()
            )
            var totalConsumeNum = 0
            node.enhanceLevelList.forEachIndexed { index, item ->
                if (index < level.intValue) {
                    totalConsumeNum += item.consumeNum
                }
            }
            Infobar(
                label = stringResource(R.string.required_number),
                value = totalConsumeNum.toString()
            )
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth().padding(end = 4.dp),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { level.intValue -= 1 },
            enabled = level.intValue > 1
        ) {
            Icon(Icons.Filled.Remove, null)
        }
        IconButton(
            onClick = { level.intValue += 1 },
            enabled = level.intValue < node.enhanceLevelList.size
        ) {
            Icon(Icons.Filled.Add, null)
        }
        Button(
            onClick = {
                onCloseDialog()
                onEnhanceNode(node, level.intValue)
            }
        ) {
            Text(stringResource(R.string.enhance))
        }
    }
}

private fun getTalentColor(node: TalentSkillNode): Color {
    val talentIdList = node.enhanceLevelList[0].enhanceDataList[0].talentIdList.filter { it != 0 }
    return if (talentIdList.size == 1) {
        Talent.fromId(talentIdList[0]).color
    } else {
        Talent.All.color
    }
}

private fun getTalentTitleAndColor(node: TalentSkillNode, context: Context): Pair<String, Color> {
    var color: Color
    var chara: String
    val enhanceData = node.enhanceLevelList[0].enhanceDataList[0]
    val talentIdList = enhanceData.talentIdList.filter { it != 0 }
    if (talentIdList.size == 1) {
        val talent = Talent.fromId(talentIdList[0])
        color = talent.color
        chara = context.getString(R.string.chara_talent1, context.getString(talent.strId))
    } else {
        color = Talent.All.color
        chara = context.getString(R.string.all_chara)
    }
//    else if (talentIdList.size > 1 && talentIdList.size < 5)
    val skillNode = SkillNode.fromId(node.titleId)
    val title = if (skillNode != null) context.getString(skillNode.titleStrId) else "???"
    return chara + title to color
}

private val levelBgBrush = Brush.horizontalGradient(
    0f to Color.Transparent,
    0.2f to Color.White.copy(0.7f),
    0.5f to Color.White.copy(0.9f),
    0.8f to Color.White.copy(0.7f),
    1f to Color.Transparent
)
