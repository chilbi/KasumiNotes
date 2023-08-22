package com.kasuminotes.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.kasuminotes.R
import com.kasuminotes.data.UnitAttackPattern
import com.kasuminotes.data.UnitSkillData

@Composable
fun AttackPattern(
    hasUnique1: Boolean,
    hasUnique2: Boolean,
    atkType: Int,
    unitAttackPatternList: List<UnitAttackPattern>,
    unitSkillData: UnitSkillData,
    loopLabelColor: Color = MaterialTheme.colorScheme.onSurface,
    atkLabelColor: Color = MaterialTheme.colorScheme.primary,
    style: TextStyle = MaterialTheme.typography.labelMedium
) {
    val size = unitAttackPatternList.size
    if (size == 0) return
    val hasMultiPattern = size > 1

    unitAttackPatternList.forEachIndexed { i, pattern ->
        Container {
            var text = stringResource(R.string.pattern)
            if (hasMultiPattern) {
                text += i + 1
            }
            AdaptiveWidthLabel(text)

            VerticalGrid(
                size = pattern.atkPatternList.size,
                cells = VerticalGridCells.Adaptive(56.dp)
            ) { index ->
                val atkPattern = pattern.getAtkPattern(
                    index,
                    atkType,
                    hasUnique1,
                    hasUnique2,
                    unitSkillData
                )

                PatternItem(
                    atkPattern.loopLabel,
                    atkPattern.iconUrl,
                    atkPattern.atkLabel,
                    loopLabelColor,
                    atkLabelColor,
                    style
                )
            }
        }
    }
}

@Composable
private fun PatternItem(
    loopLabel: String?,
    iconUrl: String,
    atkLabel: String,
    loopLabelColor: Color,
    atkLabelColor: Color,
    style: TextStyle
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(Modifier.height(20.dp)) {
            if (loopLabel != null) {
                Text(
                    text = loopLabel,
                    modifier = Modifier.align(Alignment.BottomCenter),
                    color = loopLabelColor,
                    style = style
                )
            }
        }

        Box(
            Modifier
                .padding(vertical = 2.dp)
                .size(48.dp)
        ) {
            PlaceImage(iconUrl)
        }

        Box(Modifier.height(20.dp)) {
            Text(
                text = atkLabel,
                modifier = Modifier.align(Alignment.TopCenter),
                color = atkLabelColor,
                style = style
            )
        }
    }
}
