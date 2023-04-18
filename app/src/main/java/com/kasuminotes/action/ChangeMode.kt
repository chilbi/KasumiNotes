package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getChangeMode(): D {
    val hasMode = actionValue4 == 1.0
    val patternNum = D.Text((actionDetail2 % 10).toString())
    return when (actionDetail1) {
        1, 2 -> {
            val pattern = D.Format(
                if (actionDetail1 == 1) R.string.action_mode_pattern1_time2
                else R.string.action_mode_pattern1_energy2,
                arrayOf(patternNum, D.Text(actionValue1.toNumStr()))
            )
            if (hasMode) {
                val mode = when (actionValue3) {
                    0.0 -> {
                        if (actionValue5 == 1.0 && actionValue6 == 1.0) {
                            D.Format(R.string.mode_fly)
                        } else {
                            D.Text("")
                        }
                    }
                    1158100.0 -> D.Format(R.string.mode_1158100)
                    else -> D.Format(R.string.mode_id1, arrayOf(D.Text(actionValue3.toNumStr())))
                }
                mode.append(pattern)
            } else {
                pattern
            }
        }
        3 -> {
            if (hasMode && actionValue3 != 0.0) {
                D.Format(
                    R.string.action_mode_return_mode1_pattern2,
                    arrayOf(
                        D.Format(R.string.mode_return),
                        patternNum
                    )
                )
            } else {
                D.Format(
                    R.string.action_mode_return_pattern1,
                    arrayOf(patternNum)
                )
            }
        }
        else -> getUnknown()
    }
}
