package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getRelieveField(): D {
    return D.Format(
        R.string.action_relieve_field_content1,
        arrayOf(getSkillLabel(actionDetail1))
    )
}
