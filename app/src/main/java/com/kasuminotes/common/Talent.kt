package com.kasuminotes.common

import androidx.compose.ui.graphics.Color
import com.kasuminotes.R

// itemId = 25010 + talentId
enum class Talent(val strId: Int, val imgId: Int, val halfImgId: Int, val color: Color) {
    All(R.string.all_talent, 0, 0, Color(0xFFF67BB1)),
    Fire(R.string.fire, R.drawable.icon_talent_001, R.drawable.icon_talent_half_001, Color(0xFFE65D49)),
    Water(R.string.water, R.drawable.icon_talent_002, R.drawable.icon_talent_half_002, Color(0xFF1A78A7)),
    Wind(R.string.wind, R.drawable.icon_talent_003, R.drawable.icon_talent_half_003, Color(0xFF628D2E)),
    Light(R.string.light, R.drawable.icon_talent_004, R.drawable.icon_talent_half_004, Color(0xFFC7A004)),
    Dark(R.string.dark, R.drawable.icon_talent_005, R.drawable.icon_talent_half_005, Color(0xFFB86ADA));

    companion object {
        val talentIdList = (1..5).toList()

        fun fromId(talentId: Int): Talent = when (talentId) {
            1 -> Fire
            2 -> Water
            3 -> Wind
            4 -> Light
            else -> Dark
        }
    }
}
