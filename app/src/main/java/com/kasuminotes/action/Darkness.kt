package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getDarkness(): D {
    return D.Join(
        arrayOf(
            D.Format(
                R.string.action_abnormal_target1_content2_time3,
                arrayOf(
                    getTarget(depend),
                    getDarknessContent(actionDetail2).style(underline = true),
                    D.Text(actionValue1.toNumStr()).style(primary = true, bold = true)
                )
            ),
            D.Format(
                if (actionDetail2 == 0) R.string.action_physical_darkness_formula1
                else R.string.action_magic_darkness_formula1,
                arrayOf(D.Text("${actionDetail1}%").style(primary = true, bold = true))
            )
        )
    )
}

fun getDarknessContent(detail: Int): D {
    val darknessType = D.Format(if (detail == 0) R.string.physical else R.string.magic)
    return darknessType.append(D.Format(R.string.darkness))
}
