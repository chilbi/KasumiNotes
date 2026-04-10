package com.kasuminotes.common

import com.kasuminotes.R

enum class AtkType(val strId: Int, val imgId: Int) {
    All(R.string.all_type, 0),
    Physical(R.string.physical, R.drawable.icon_unit_sort_physics),
    Magic(R.string.magic, R.drawable.icon_unit_sort_magic);

    companion object {
        fun isPhysical(atkType: Int): Boolean = atkType == 1

        fun fromId(atkType: Int): AtkType = if (isPhysical(atkType)) Physical else Magic
    }
}
