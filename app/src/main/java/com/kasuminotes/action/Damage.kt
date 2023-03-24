package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.Property
import com.kasuminotes.data.SkillAction

fun SkillAction.getDamage(skillLevel: Int, property: Property): D {
    val mustHit = actionDetail1 == 3
    var mustCritical = actionValue5 == 1.0
    val hasCriticalDamageRate = actionValue6 > 1.0
    val damageType = getDamageType(actionDetail1)

    val content = if (actionDetail2 == 1) {
        D.Format(R.string.content_lower_defense).append(damageType)
    } else if (actionValue5 == -1.0) {
        mustCritical = true
        D.Format(R.string.content_disregard_def1_value_2,
            arrayOf(
                getDefType(actionDetail1),
                D.Text(actionValue7.toNumStr())
            )
        ).append(damageType)
    } else {
        damageType
    }

    var damage: D = D.Format(
        R.string.action_damage_target1_formula2_content3,
        arrayOf(
            getTarget(depend),
            getBaseLvAtkFormula(actionDetail1, actionValue1, actionValue2, actionValue3, actionValue4, skillLevel, property),
            content
        )
    )

    if (mustHit && mustCritical) {
        damage = damage.append(D.Format(R.string.action_damage_must_hit_and_critical))
    } else if (mustHit) {
        damage = damage.append(D.Format(R.string.action_damage_must_hit))
    } else if (mustCritical) {
        damage = damage.append(D.Format(R.string.action_damage_must_critical))
    }

    if (hasCriticalDamageRate) {
        damage = damage.append(
            D.Format(
                R.string.action_damage_critical_rate1,
                arrayOf(D.Text((actionValue6 * 2).toNumStr()))
            )
        )
    }

    return damage
}
