package com.kasuminotes.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kasuminotes.R
import com.kasuminotes.data.UnitAttackPattern
import com.kasuminotes.data.UnitSkillData

@Composable
fun AttackPattern(
    hasUnique: Boolean,
    atkType: Int,
    unitAttackPatternList: List<UnitAttackPattern>,
    unitSkillData: UnitSkillData,
    fontSize: TextUnit = 10.sp,
    loopLabelColor: Color = MaterialTheme.colors.secondary,
    atkLabelColor: Color = MaterialTheme.colors.primary
) {
    val size = unitAttackPatternList.size
    if (size == 0) return
    val hasMultiPattern = size > 1

    unitAttackPatternList.forEachIndexed { i, pattern ->
        var text = stringResource(R.string.pattern)
        if (hasMultiPattern) {
            text += i + 1
        }

        BgBorderColumn {
            SkillLabel(text)

            VerticalGrid(
                size = pattern.atkPatternList.size,
                cells = VerticalGridCells.Adaptive(56.dp)
            ) { index ->
                val atkPattern = pattern.getAtkPattern(
                    index,
                    atkType,
                    hasUnique,
                    unitSkillData
                )

                PatternItem(
                    atkPattern.loopLabel,
                    atkPattern.iconUrl,
                    atkPattern.atkLabel,
                    fontSize,
                    loopLabelColor,
                    atkLabelColor
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
    fontSize: TextUnit,
    loopLabelColor: Color,
    atkLabelColor: Color
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
                    fontSize = fontSize,
                    fontWeight = FontWeight.SemiBold
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
                fontSize = fontSize,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
