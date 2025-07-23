package com.kasuminotes.ui.app.enemy

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kasuminotes.R
import com.kasuminotes.action.stringDescription
import com.kasuminotes.data.ExtraEffectData
import com.kasuminotes.data.SkillEffect
import com.kasuminotes.ui.components.Container
import com.kasuminotes.ui.components.Infobar
import com.kasuminotes.ui.components.UnderlineLabel
import com.kasuminotes.ui.components.VerticalGrid
import com.kasuminotes.ui.components.VerticalGridCells

@Composable
fun ExtraEffect(
    extraEffectData: ExtraEffectData,
    epTableName: String,
    onExtraEffectClick: (extraEffectData: ExtraEffectData, epTableName: String) -> Unit
) {
    Container {
        Button(
            onClick = { onExtraEffectClick(extraEffectData, epTableName) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp)
        ) {
            Text(stringResource(R.string.extra_effect) + "<type${extraEffectData.contentType}>")
        }

//        Row {
//            Infobar(
//                label = "content_type",
//                value = extraEffectData.contentType.toString(),
//                modifier = Modifier.weight(1f),
//                width = 95.dp,
//                color = MaterialTheme.colorScheme.secondary
//            )
//            Infobar(
//                label = "exec_timing",
//                value = extraEffectData.execTimingList.filter { it > 0 }.joinToString(","),
//                modifier = Modifier.weight(1f),
//                width =  88.dp,
//                color = MaterialTheme.colorScheme.secondary
//            )
//        }

        SkillEffectList(extraEffectData.skillEffectList)
    }
}

@Composable
private fun SkillEffectList(list: List<SkillEffect>) {
    val skillEffectList = list.map { item ->
        item.targetText = stringDescription(item.target)
        item.labelText = stringDescription(item.label)
        item.valueText = stringDescription(item.value)
        val labelLength = item.labelText.length
        if (labelLength > 7 || labelLength + item.valueText.length > 11) {
            item.weight = 1f
        }
        item
    }
    val targetGrouped = skillEffectList.groupBy { it.targetText }

    targetGrouped.forEach { targetGroup ->
        val fullWidthList = mutableListOf<SkillEffect>()
        val halfWidthList = mutableListOf<SkillEffect>()
        targetGroup.value.forEach { skillEffect ->
            if (skillEffect.weight == 1f) {
                fullWidthList.add(skillEffect)
            } else {
                halfWidthList.add(skillEffect)
            }
        }

        Spacer(Modifier.height(8.dp))
        UnderlineLabel(label = targetGroup.key, color = MaterialTheme.colorScheme.primary)

        fullWidthList.forEach { skillEffect ->
            Infobar(
                label = skillEffect.labelText,
                value = skillEffect.valueText,
                width = (2 + skillEffect.labelText.length * 14).dp
            )
        }

        VerticalGrid(
            size = halfWidthList.size,
            cells = VerticalGridCells.Fixed(2),
        ) { index ->
            val skillEffect = halfWidthList[index]
            Infobar(
                label = skillEffect.labelText,
                value = skillEffect.valueText,
                width = (2 + skillEffect.labelText.length * 14).dp
            )
        }
    }
}
