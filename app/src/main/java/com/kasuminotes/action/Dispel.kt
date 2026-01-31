package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getDispel(): D {
    val content = when (actionDetail1) {
        1, 3 -> D.Format(R.string.content_status_up_effect)
        2 -> D.Format(R.string.content_status_down_effect)
        10 -> D.Format(R.string.content_barrier)
        20 -> {
            val effectValues = listOf(actionValue4, actionValue5, actionValue6, actionValue7).filter { it != 0.0 }
            if (effectValues.isNotEmpty()) {
                val effects: List<D> = effectValues.map {
                    val detail1 = it.toInt()
                    D.Join(arrayOf(
                        getStatusContent(detail1 / 10),
                        D.Format(if (detail1 % 10 == 0) R.string.content_up else R.string.content_down)
                    ))
                }
                val list = mutableListOf<D>()
                effects.forEachIndexed { index, effect ->
                    if (index != 0) {
                        list.add(D.Format(R.string.comma))
                    }
                    list.add(effect)
                }
                D.Format(R.string.content_effect1, arrayOf(D.Join(list.toTypedArray())))
            } else {
                D.Unknown
            }
        }
        else -> D.Unknown
    }

    val result = D.Format(
        R.string.action_dispel_target1_content2,
        arrayOf(
            getTarget(depend),
            content.style(underline = true)
        )
    )

    return if (actionValue1 < 100.0) {
        result.insert(
            D.Format(
                R.string.action_odds1,
                arrayOf(D.Text("${actionValue1.toNumStr()}%").style(primary = true, bold = true))
            )
        )
    } else {
        result
    }
}
