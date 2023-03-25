package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.Property
import com.kasuminotes.data.SkillAction
import kotlin.math.roundToInt

fun SkillAction.getStatus(skillLevel: Int, actions: List<SkillAction>, property: Property?): D {
    val arr = getStatusArray(skillLevel, actions, property)
    val contentDesc = arr[0]!!
    val timeDesc = D.Format(R.string.action_time1, arrayOf(arr[1]!!))
    val constDesc = arr[2]
    return if (constDesc == null) {
        D.Join(arrayOf(contentDesc, timeDesc))
    } else {
        D.Join(arrayOf(contentDesc, timeDesc, constDesc))
    }
}

fun SkillAction.isStatusPercent(): Boolean {
    var detail1 = actionDetail1
    if (detail1 > 1000) {
        detail1 -= 1000
    }
    var isPercent = actionValue1 == 2.0
    if (detail1 >= 140) {
        isPercent = true
    }
    if (detail1 in 110..129) {
        isPercent = true
    }
    return isPercent
}

/** arrayOf(contentDesc, timeDesc, constDesc) */
fun SkillAction.getStatusArray(skillLevel: Int, actions: List<SkillAction>, property: Property?): Array<D?> {
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
            var str = "${actionValue2.roundToInt()}%"// TODO 不确定的取整方式
            // 计算EX装备被动技能加的百分比数值
            val index = getStatusIndex(detail1 / 10)
            if (property != null && index != null) {
                str += if (isUp) "(+" else "(-"
                str += "${(property[index] * actionValue2 / 100).roundToInt()})"// TODO 不确定的取整方式
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
            R.string.action_up_target1_received_content2_formula3
        } else {
            R.string.action_up_target1_content2_formula3
        }
    } else {
        if (isReceived) {
            R.string.action_down_target1_received_content2_formula3
        } else {
            R.string.action_down_target1_content2_formula3
        }
    }

    val time = if (actionDetail2 == 2) {
        val action = actions.find { action -> action.actionType == 17 && action.actionDetail1 == 9 }
        action?.actionValue3 ?: actionValue4
    } else {
        actionValue4
    }

    val contentDesc = D.Format(resId, arrayOf(getTarget(depend), content, formula))

    val timeDesc = D.Text(time.toNumStr())

    val constDesc = if (isConst) {
        D.Format(
            if (isUp) R.string.action_const_up_content1 else R.string.action_const_down_content1,
            arrayOf(content)
        )
    } else {
        null
    }

    return arrayOf(contentDesc, timeDesc, constDesc)
}