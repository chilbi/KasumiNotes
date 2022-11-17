package com.kasuminotes.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kasuminotes.data.SkillAction
import com.kasuminotes.ui.theme.DarkError
import com.kasuminotes.ui.theme.DarkInfo
import com.kasuminotes.ui.theme.DarkSuccess
import com.kasuminotes.ui.theme.DarkWarning

@Composable
fun SkillDataParamTable(
    rawDepends: List<Int>,
    actions: List<SkillAction>
) {
    actions.forEachIndexed { index, action ->
        val dependId = rawDepends[index]

        Text(
            text = "action_id:${action.actionId}",
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .background(DarkWarning)
                .padding(vertical = 2.dp),
            color = Color.White,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )

        Row {
            KeyValue(
                label = "action_type",
                value = action.actionType.toString(),
                modifier = Modifier.weight(1f),
                color = DarkSuccess
            )
            KeyValue(
                label = "depend_id",
                value = dependId.toString(),
                modifier = Modifier.weight(1f),
                color = DarkSuccess
            )
            KeyValue(
                label = "class_id",
                value = action.classId.toString(),
                modifier = Modifier.weight(1f),
                color = DarkSuccess
            )
        }

        Row {
            KeyValue(
                label = "detail_1",
                value = action.actionDetail1.toString(),
                modifier = Modifier.weight(1f),
                color = DarkError
            )
            KeyValue(
                label = "detail_2",
                value = action.actionDetail2.toString(),
                modifier = Modifier.weight(1f),
                color = DarkError
            )
            KeyValue(
                label = "detail_3",
                value = action.actionDetail3.toString(),
                modifier = Modifier.weight(1f),
                color = DarkError
            )
        }

        Row {
            KeyValue(
                label = "value_1",
                value = action.actionValue1.toString(),
                modifier = Modifier.weight(1f)
            )
            KeyValue(
                label = "value_2",
                value = action.actionValue2.toString(),
                modifier = Modifier.weight(1f)
            )
            KeyValue(
                label = "value_3",
                value = action.actionValue3.toString(),
                modifier = Modifier.weight(1f)
            )
            KeyValue(
                label = "value_4",
                value = action.actionValue4.toString(),
                modifier = Modifier.weight(1f)
            )
        }

        Row {
            KeyValue(
                label = "value_5",
                value = action.actionValue5.toString(),
                modifier = Modifier.weight(1f)
            )
            KeyValue(
                label = "value_6",
                value = action.actionValue6.toString(),
                modifier = Modifier.weight(1f)
            )
            KeyValue(
                label = "value_7",
                value = action.actionValue7.toString(),
                modifier = Modifier.weight(1f)
            )
        }

        Row {
            KeyValue(
                label = "target_type",
                value = action.targetType.toString(),
                modifier = Modifier.weight(1f),
                color = DarkSuccess
            )
            KeyValue(
                label = "assignment",
                value = action.targetAssignment.toString(),
                modifier = Modifier.weight(1f),
                color = DarkSuccess
            )
            KeyValue(
                label = "area",
                value = action.targetArea.toString(),
                modifier = Modifier.weight(1f),
                color = DarkSuccess
            )
        }

        Row {
            KeyValue(
                label = "count",
                value = action.targetCount.toString(),
                modifier = Modifier.weight(1f),
                color = DarkSuccess
            )
            KeyValue(
                label = "range",
                value = action.targetRange.toString(),
                modifier = Modifier.weight(1f),
                color = DarkSuccess
            )
            KeyValue(
                label = "number",
                value = action.targetNumber.toString(),
                modifier = Modifier.weight(1f),
                color = DarkSuccess
            )
        }

        KeyValue(
            label = "description",
            value = action.description.ifEmpty { "NULL" }
        )

        KeyValue(
            label = "level_up_disp",
            value = action.levelUpDisp.ifEmpty { "NULL" }
        )
    }
}

@Composable
private fun KeyValue(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    color: Color = DarkInfo
) {
    Column(
        modifier
            .padding(4.dp)
            .border(1.dp, color)
    ) {
        Text(
            text = label,
            modifier = Modifier
                .fillMaxWidth()
                .background(color)
                .padding(vertical = 2.dp),
            color = Color.White,
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )
        Text(
            text = value,
            modifier = Modifier.fillMaxWidth(),
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )
    }
}
