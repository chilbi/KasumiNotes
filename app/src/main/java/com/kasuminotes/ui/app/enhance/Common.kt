package com.kasuminotes.ui.app.enhance

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import com.kasuminotes.data.RoleSlot
import com.kasuminotes.data.SkillNode

@Composable
fun getSkillNodeLabelValue(parameterType: Int, enhanceValue: Int): Pair<String, String> {
    val label: String
    val value: String
    val skillNode = SkillNode.fromId(parameterType)
    if (skillNode == null) {
        label = "type${parameterType}"
        value = enhanceValue.toString()
    } else {
        label = stringResource(skillNode.titleStrId)
        value = skillNode.valueDisplay(enhanceValue)
    }
    return label to value
}

@Composable
fun getRoleSlotLabelValue(parameterType: Int, enhanceValue: Int): Pair<String, String> {
    val label: String
    val value: String
    val roleSlot = RoleSlot.fromId(parameterType)
    if (roleSlot == null) {
        label = "type${parameterType}"
        value = enhanceValue.toString()
    } else {
        label = stringResource(roleSlot.titleStrId)
        value = roleSlot.valueDisplay(enhanceValue)
    }
    return label to value
}

data class NodeSkillListState(
    val width: Dp,
    val height: Dp,
    val nodeSizeDp: Dp,
//    val nodeSizePx: Float,
    val gridXDp: Dp,
    val gridXPx: Float,
    val gridYDp: Dp,
    val gridYPx: Float,
//    val gridX: Int,
    val gridY: Int,
)

fun DrawScope.drawConnection(start: Offset, end: Offset, highlight: Boolean) {
    if (highlight) {
        drawLine(
            highlightConnectColor.copy(alpha = 0.2f),
            start,
            end,
            30f
        )
        drawLine(
            highlightConnectColor.copy(alpha = 0.5f),
            start,
            end,
            25f
        )
        drawLine(
            highlightConnectColor,
            start,
            end,
            20f
        )
        drawLine(
            Color.Companion.White,
            start,
            end,
            10f
        )
    } else {
        drawLine(
            connectionEdgeColor,
            start,
            end,
            14f
        )
        drawLine(
            connectionColor,
            start,
            end,
            10f
        )
    }
}

val darkenColorFilter = ColorFilter.tint(
    Color.Black.copy(alpha = 0.6f),
    BlendMode.SrcAtop
)

private val connectionColor = Color(0xFF6A719B)
private val connectionEdgeColor = Color(0xFF202F58)
private val highlightConnectColor = Color(0xFFFAD972)
