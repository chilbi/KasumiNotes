package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getSustain(skillLevel: Int): D {
    val arr = getStatusArray(skillLevel, emptyList(), null)
    val contentDesc = arr[0]!!
    val timeDesc = arr[1]!!
    val constDesc = arr[2]

    val result = D.Format(
        R.string.action_sustain_time1_content2_count3,
        arrayOf(
            timeDesc,
            contentDesc,
            D.Text(actionDetail3.toString())
        )
    )

    return if (constDesc == null) result else result.append(constDesc)
}
