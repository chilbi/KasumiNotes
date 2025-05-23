package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction
import com.kasuminotes.data.SkillEffect

fun SkillAction.getUnknown(): D {
    val str = StringBuilder()
    str.append("\naction(type=$actionType")
    str.append(", id=$actionId")
    if (depend != null) {
        str.append(", depend=${depend!!.actionId}")
    }
    str.append(");\n")
    str.append("details($actionDetail1, $actionDetail2, $actionDetail3);\n")
    str.append(
        arrayOf(actionValue1, actionValue2, actionValue3, actionValue4, actionValue5, actionValue6, actionValue7)
            .joinToString(", ", "values(", ");\n") {
                it.toNumStr()
            }
    )
    str.append("target(type=$targetType")
    str.append(", area=$targetArea")
    str.append(", count=$targetCount")
    str.append(", number=$targetNumber")
    str.append(", range=$targetRange")
    str.append(", assignment=$targetAssignment")
    str.append(")")
//    str.append("target=\"${getStringDescription(getTarget(depend))}\"")
    if (description.isNotEmpty()) {
        str.append(";\ndescription=\"$description\"")
    }

    return D.Format(
        R.string.action_unknown_s1,
        arrayOf(D.Text(str.toString()))
    )
}

fun SkillAction.getUnknownEffect(): SkillEffect {
    return SkillEffect(
        getTarget(null),
        D.Format(R.string.effect_unknown),
        D.Text(actionType.toString()),
        0.0,
        0.5f,
        SkillEffect.unknownType
    )
}
