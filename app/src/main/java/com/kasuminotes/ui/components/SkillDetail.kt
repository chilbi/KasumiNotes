package com.kasuminotes.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kasuminotes.BuildConfig
import com.kasuminotes.R
import com.kasuminotes.action.ActionBuilder
import com.kasuminotes.action.D
import com.kasuminotes.action.getUnknown
import com.kasuminotes.action.stringDescription
import com.kasuminotes.data.Property
import com.kasuminotes.data.SkillAction

@Composable
fun SkillDetail(
    label: String,
    isRfSkill: Boolean,
    iconUrl: String,
    name: String,
    coolTime: Float,
    castTime: Float,
    description: String,
    searchAreaWidth: Int = 0,
    skillLevel: Int = 0,
    property: Property? = null,
    rawDepends: List<Int>? = null,
    actions: List<SkillAction>? = null,
    onSummonsClick: ((summons: List<Int>, skillLevel: Int) -> Unit)? = null
) {
    val visible = remember { mutableStateOf(false) }

    Container {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AdaptiveWidthLabel(label)
            if (isRfSkill) {
                AdaptiveWidthLabel(
                    text = "RF",
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            Spacer(Modifier.weight(1f))
            if (BuildConfig.DEBUG && actions != null) {
                IconButton(
                    modifier = Modifier.height(28.dp),
                    onClick = { visible.value = !visible.value }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Code,
                        contentDescription = null,
                        tint = if (visible.value) MaterialTheme.colorScheme.secondary
                        else LocalContentColor.current.copy(0.75f)
                    )
                }
            }
            @StringRes
            val labelId: Int
            val value: Int
            val color: Color
            if (searchAreaWidth == 0) {
                labelId = R.string.skill_level
                value = skillLevel
                color = MaterialTheme.colorScheme.primaryContainer
            } else {
                labelId = R.string.search_area_width
                value = searchAreaWidth
                color = MaterialTheme.colorScheme.secondary
            }
            Infobar(
                label = stringResource(labelId),
                value = value.toString(),
                modifier = Modifier.width(110.dp),
                width = 64.dp,
                color = color,
                textAlign = TextAlign.Center
            )
        }

        val primaryText = name.ifBlank { label }
        var secondaryText = stringResource(R.string.cast_time_s, castTime)
        if (coolTime != 0f) {
            secondaryText += "; " + stringResource(R.string.cool_time_s, coolTime)
        }
        ImageCard(iconUrl, primaryText, secondaryText)

        if (description != "") {
            Text(
                text = description,
                modifier = Modifier.padding(4.dp),
                style = MaterialTheme.typography.bodyMedium
            )
        }

        val descriptionList: List<D>? by remember(skillLevel, property, rawDepends, actions) {
            derivedStateOf {
                if (property != null && rawDepends != null && actions != null) {
                    ActionBuilder(rawDepends, actions, false)
                        .buildDescriptionList(skillLevel, property)
                } else {
                    null
                }
            }
        }

        val summons: List<Int> by remember(actions) {
            derivedStateOf {
                val list = mutableListOf<Int>()
                actions?.forEach { action ->
                    if (action.actionType == 15) {
                        list.add(action.actionDetail2)
                    }
                }
                list.distinct()
            }
        }

        if (actions != null) {
            Box(Modifier.fillMaxWidth().padding(4.dp)) {
                UnderlineLabel(
                    label = stringResource(R.string.skill_effect),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        if (descriptionList != null && !visible.value) {
            descriptionList!!.forEachIndexed { index, d ->
                Row(Modifier.padding(4.dp)) {
                    ActionLabel(index + 1)
                    Text(
                        text = stringDescription(d),
                        modifier = Modifier.padding(start = 4.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            if (summons.isNotEmpty()) {
                Button(
                    onClick = { onSummonsClick?.invoke(summons, skillLevel) },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp)
                ) {
                    Text(stringResource(R.string.summons_info))
                }
            }
        }

        if (BuildConfig.DEBUG && actions != null && visible.value) {
            actions.forEachIndexed { index, action ->
                Row(Modifier.padding(4.dp)) {
                    ActionLabel(index + 1)
                    Text(
                        text = stringDescription(action.getUnknown()),
                        modifier = Modifier.padding(start = 4.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}
