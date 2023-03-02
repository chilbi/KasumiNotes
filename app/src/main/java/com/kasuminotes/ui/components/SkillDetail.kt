package com.kasuminotes.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kasuminotes.BuildConfig
import com.kasuminotes.R
import com.kasuminotes.action.ActionBuilder
import com.kasuminotes.action.D
import com.kasuminotes.action.getUnknown
import com.kasuminotes.action.stringDescription
import com.kasuminotes.data.Property
import com.kasuminotes.data.SkillAction
import com.kasuminotes.ui.theme.LightInfo
import com.kasuminotes.ui.theme.LightWarning

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

    BgBorderColumn(Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SkillLabel(label)
            if (isRfSkill) {
                SkillLabel(
                    text = "RF",
                    color = MaterialTheme.colors.onSecondary,
                    bgColor = MaterialTheme.colors.secondary
                )
            }
            Spacer(Modifier.weight(1f))
            if (BuildConfig.DEBUG && actions != null) {
                IconButton(onClick = { visible.value = !visible.value }) {
                    Icon(
                        imageVector = Icons.Filled.Code,
                        contentDescription = null,
                        tint = if (visible.value) MaterialTheme.colors.secondary
                        else LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
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
                color = LightInfo
            } else {
                labelId = R.string.search_area_width
                value = searchAreaWidth
                color = LightWarning
            }
            Infobar(
                label = stringResource(labelId),
                value = value.toString(),
                modifier = Modifier.width(100.dp),
                width = 57.dp,
                color = color,
                contentColor = Color.Black
            )
        }

        ImageCard(
            imageUrl = iconUrl,
            primaryText = if (name != "") name
            else stringResource(R.string.cool_time_s, coolTime),
            secondaryText = stringResource(R.string.cast_time_s, castTime)
        )

        if (description != "") {
            Text(
                text = description,
                modifier = Modifier.padding(4.dp),
                fontSize = 14.sp,
                lineHeight = 28.sp
            )
        }

        val descriptionList: List<D>? by remember(skillLevel, property, rawDepends, actions) {
            derivedStateOf {
                if (property != null && rawDepends != null && actions != null) {
                    ActionBuilder(rawDepends, actions, false).buildDescriptionList(
                        skillLevel,
                        property
                    )
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
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
                UnderlineLabel(
                    label = stringResource(R.string.skill_effect),
                    color = MaterialTheme.colors.primary
                )
            }
        }

        if (!visible.value && descriptionList != null) {
            descriptionList!!.forEachIndexed { index, d ->
                Row(Modifier.padding(4.dp)) {
                    ActionLabel(index + 1)

                    Text(
                        text = stringDescription(d),
                        modifier = Modifier.padding(start = 4.dp),
                        fontSize = 14.sp,
                        lineHeight = 28.sp
                    )
                }
            }

            if (summons.isNotEmpty()) {
                Button(
                    onClick = { onSummonsClick?.invoke(summons, skillLevel) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colors.secondary)
                ) {
                    Text(stringResource(R.string.summons_info))
                }
            }
        }

        if (visible.value && BuildConfig.DEBUG && actions != null) {
            actions.forEachIndexed { index, action ->
                Row(Modifier.padding(4.dp)) {
                    ActionLabel(index + 1)

                    Text(
                        text = stringDescription(action.getUnknown()),
                        modifier = Modifier.padding(start = 4.dp),
                        fontSize = 14.sp,
                        lineHeight = 28.sp
                    )
                }
            }
        }
    }
}
