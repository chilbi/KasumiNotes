package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.Property
import com.kasuminotes.data.SkillAction
import kotlin.math.roundToInt

fun SkillAction.getStatus(skillLevel: Int, actions: List<SkillAction>, property: Property?): D {
    // 不受技能影响
    var isConst = false
    var detail1 = actionDetail1
    if (detail1 > 1000) {
        detail1 -= 1000
        isConst = true
    }

    // 提升或下降
    var isUp = detail1 % 10 == 0
    // 是否受身
    var isReceived = false
    // 是否百分比
    var isPercent = actionValue1 == 2.0
    if (detail1 >= 140) {
        isUp = !isUp
        isReceived = true
        isPercent = true
    }

    if (detail1 in 110..129) {
        isPercent = true
    }

    val formula = if (isPercent) {
        val percent = if (actionValue3 == 0.0) {
            var str = "${actionValue2.roundToInt()}%"
            // 计算EX装备被动技能加的百分比数值
            val index = getStatusIndex(detail1 / 10)
            if (property != null && index != null) {
                str += if (isUp) "(+" else "(-"
                str += "${(property[index] * actionValue2 / 100).roundToInt()})"
            }
            D.Text(str)
        } else {
            getBaseLvFormula(actionValue2, actionValue3, skillLevel).append(D.Text("%"))
        }
        // 10-40为物魔攻防，估计只有这4项值加的百分比值是以初始值为基础的
        if (detail1 < 50) D.Format(R.string.content_initial_value1, arrayOf(percent))
        else percent
    } else {
        getBaseLvFormula(actionValue2, actionValue3, skillLevel)
    }

    val content = getStatusContent(detail1 / 10)

    val resId = if (isUp) {
        if (isReceived) {
            R.string.action_up_target1_received_content2_formula3_time4
        } else {
            R.string.action_up_target1_content2_formula3_time4
        }
    } else {
        if (isReceived) {
            R.string.action_down_target1_received_content2_formula3_time4
        } else {
            R.string.action_down_target1_content2_formula3_time4
        }
    }

    val time = if (actionDetail2 == 2) {
        val action = actions.find { action -> action.actionType == 17 && action.actionDetail1 == 9 }
        action?.actionValue3 ?: 0.0
    } else {
        actionValue4
    }

    val result = D.Format(
        resId,
        arrayOf(
            getTarget(depend),
            content,
            formula,
            D.Text(time.toNumStr())
        )
    )

    return if (isConst) {
        result.append(D.Format(
            if (isUp) R.string.action_const_up_content1 else R.string.action_const_down_content1,
            arrayOf(content)
        ))
    } else {
        result
    }
}
