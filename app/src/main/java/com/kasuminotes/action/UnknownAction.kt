package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getUnknown(): D {
    val str = StringBuilder()
    str.append("action_type=$actionType")
    str.append(", action_id=$actionId")
    if (depend != null) {
        str.append(", depend_id=${depend!!.actionId}")
    }
    str.append("; ")
    str.append("details($actionDetail1, $actionDetail2, $actionDetail3); ")
    str.append(
        arrayOf(actionValue1, actionValue2, actionValue3, actionValue4, actionValue5, actionValue6, actionValue7)
            .joinToString(", ", "values(", "); ") {
                it.toNumStr()
            }
    )
    str.append("target(type=$targetType")
    str.append(", assignment=$targetAssignment")
    str.append(", area=$targetArea")
    str.append(", count=$targetCount")
    str.append(", range=$targetRange")
    str.append(", number=$targetNumber")
    str.append(")")
    if (description.isNotEmpty()) {
        str.append("; description=$description")
    }

    return D.Format(
        R.string.action_unknown_s1,
        arrayOf(D.Text(str.toString()))
    )
}
