package com.kasuminotes.ui.app.enhance

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.kasuminotes.R
import com.kasuminotes.common.Talent
import com.kasuminotes.data.ExperienceTalentLevel
import com.kasuminotes.state.EnhanceState
import com.kasuminotes.ui.components.Container
import com.kasuminotes.ui.components.Infobar

@Composable
fun TalentLevelView(
    enhanceState: EnhanceState,
    onUserScrollEnabledChange: (Boolean) -> Unit
) {
    val talentLevelMap = enhanceState.talentLevelMap!!
    Column(
        Modifier
            .fillMaxSize()
            .padding(4.dp)
            .verticalScroll(rememberScrollState())
    ) {
        talentLevelMap.forEach { (talentId, pair) ->
            TalentLevelItem(
                enhanceState.maxTalentLevel!!,
                talentId,
                pair.first,
                pair.second,
                enhanceState::changeTalentLevel,
                onUserScrollEnabledChange
            )
        }
    }
}

@Composable
private fun TalentLevelItem(
    maxTalentLevel: Int,
    talentId: Int,
    expTalentLevel: ExperienceTalentLevel,
    toExpTalentLevel: ExperienceTalentLevel,
    onTalentLevelChange: (talentInt: Int, talentLevel: Int, toTalentLevel: Int) -> Unit,
    onUserScrollEnabledChange: (Boolean) -> Unit
) {
    val talent = Talent.fromId(talentId)
    Container {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.width(56.dp).padding(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(talent.imgId),
                    contentDescription = null,
                    modifier = Modifier.size(36.dp).padding(4.dp)
                )
                Text(
                    text = expTalentLevel.talentLevel.toString(),
                    color = talent.color,
                    style = MaterialTheme.typography.labelMedium
                )
                HorizontalDivider()
                Text(
                    text = maxTalentLevel.toString(),
                    style = MaterialTheme.typography.labelMedium
                )
            }
            Column {
                Infobar(
                    label = stringResource(talent.strId) + stringResource(R.string.talent_atk),
                    value = expTalentLevel.totalEnhanceValue.toString(),
                    width = 100.dp,
                    valueStyle = MaterialTheme.typography.bodySmall.copy(color = talent.color)
                )
                Infobar(
                    label = "${stringResource(R.string.total_number)} | ${stringResource(R.string.required_number)}",
                    value = buildAnnotatedString {
                        append("${expTalentLevel.totalPoint} | ")
                        withStyle(SpanStyle(color = talent.color)) {
                            append((toExpTalentLevel.totalPoint - expTalentLevel.totalPoint).toString())
                        }
                    },
                    width = 100.dp
                )
            }
        }

        var value by remember { mutableStateOf(expTalentLevel.talentLevel.toFloat()..toExpTalentLevel.talentLevel.toFloat()) }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    value = (value.start - 1)..value.endInclusive
                    onTalentLevelChange(
                        talentId,
                        value.start.toInt(),
                        value.endInclusive.toInt()
                    )
                },
                modifier = Modifier.size(24.dp),
                enabled = value.start > 1
            ) {
                Icon(Icons.Filled.Remove, null)
            }
            Text(
                text = value.start.toInt().toString(),
                modifier = Modifier.width(30.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelMedium
            )
            IconButton(
                onClick = {
                    value = (value.start + 1)..value.endInclusive
                    onTalentLevelChange(
                        talentId,
                        value.start.toInt(),
                        value.endInclusive.toInt()
                    )
                },
                modifier = Modifier.size(24.dp),
                enabled = value.start < value.endInclusive
            ) {
                Icon(Icons.Filled.Add, null)
            }

            RangeSlider(
                value = value,
                onValueChange = { newValue ->
                    value = newValue
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp)
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = { onUserScrollEnabledChange(false) },
                            onDragEnd = { onUserScrollEnabledChange(true) },
                            onDrag = { _, _ -> }
                        )
                    },
                valueRange = 1f..maxTalentLevel.toFloat(),
                steps = maxTalentLevel - 2,
                onValueChangeFinished = {
                    onUserScrollEnabledChange(true)
                    onTalentLevelChange(
                        talentId,
                        value.start.toInt(),
                        value.endInclusive.toInt()
                    )
                }
            )

            IconButton(
                onClick = {
                    value = value.start..(value.endInclusive - 1)
                    onTalentLevelChange(
                        talentId,
                        value.start.toInt(),
                        value.endInclusive.toInt()
                    )
                },
                modifier = Modifier.size(24.dp),
                enabled = value.endInclusive > value.start
            ) {
                Icon(Icons.Filled.Remove, null)
            }
            Text(
                text = value.endInclusive.toInt().toString(),
                modifier = Modifier.width(30.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelMedium
            )
            IconButton(
                onClick = {
                    value = value.start..(value.endInclusive + 1)
                    onTalentLevelChange(
                        talentId,
                        value.start.toInt(),
                        value.endInclusive.toInt()
                    )
                },
                modifier = Modifier.size(24.dp),
                enabled = value.endInclusive < maxTalentLevel
            ) {
                Icon(Icons.Filled.Add, null)
            }
        }
    }
}
