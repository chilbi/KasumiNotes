package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getSustainStatus(skillLevel: Int): D {
    val arr = getStatusArray(skillLevel, emptyList(), null)
    val contentDesc = arr[0]!!
    val timeDesc = arr[1]!!
    val constDesc = arr[2]

    val sustainTime = D.Format(R.string.action_sustain_time1, arrayOf(timeDesc))
    val result = sustainTime.append(
        D.Format(
            R.string.action_sustain_status_content1_count2,
            arrayOf(contentDesc, D.Text(actionDetail3.toString()).style(primary = true, bold = true))
        )
    )

    return if (constDesc == null) result else result.append(constDesc)
}
