package com.kasuminotes.common

import com.kasuminotes.R

enum class Talent(val strId: Int, val imgId: Int, val halfImgId: Int) {
    All(R.string.all_talent, 0, 0),
    Fire(R.string.fire, R.drawable.icon_talent_001, R.drawable.icon_talent_half_001),
    Water(R.string.water, R.drawable.icon_talent_002, R.drawable.icon_talent_half_002),
    Wind(R.string.wind, R.drawable.icon_talent_003, R.drawable.icon_talent_half_003),
    Light(R.string.light, R.drawable.icon_talent_004, R.drawable.icon_talent_half_004),
    Dark(R.string.dark, R.drawable.icon_talent_005, R.drawable.icon_talent_half_005);

    companion object {
        fun fromId(talentId: Int): Talent = when (talentId) {
            1 -> Fire
            2 -> Water
            3 -> Wind
            4 -> Light
            else -> Dark
        }
    }
}
