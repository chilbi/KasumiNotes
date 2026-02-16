package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.Property
import com.kasuminotes.data.SkillAction

fun SkillAction.getShieldCounter(skillLevel: Int, property: Property, actions: List<SkillAction>): D {
    val target = getTarget(depend)
    val content = getMarkContent(if (actionId / 1000 == 104401) 119 else actionDetail2)// 104401 イリヤ
    val type = if (actionDetail1 == 5) D.Text("") else getDamageType(actionDetail1)
    val desc = if (actionValue3 > 1.0) {
        val add = D.Text(actionValue3.toNumStr()).style(primary = true, bold = true)
        D.Format(
            R.string.action_shield_counter_target1_content2_type3_add4,
            arrayOf(target, content, type, add)
        )
    } else {
        D.Format(
            R.string.action_shield_counter_target1_content2_type3,
            arrayOf(target, content, type)
        )
    }
    val damage = if (actionDetail3 != 0) {
        val damageAction = actions.find { action -> action.actionId == actionDetail3 }
        if (damageAction != null) {
            D.Format(R.string.action_shield_counter_damage1, arrayOf(
                damageAction.getDamage(skillLevel, property, D.Format(R.string.target_attacking_enemy))
            ))
        } else {
            D.Format(R.string.full_stop)
        }
    } else {
        if (actionValue1 != 0.0 || actionValue2 != 0.0) {
            D.Format(
                R.string.action_shield_counter_formula1,
                arrayOf(getBaseLvFormula(actionValue1, actionValue2, skillLevel))
            )
        } else {
            D.Format(R.string.full_stop)
        }
    }
    return D.Join(arrayOf(desc, damage))
}
