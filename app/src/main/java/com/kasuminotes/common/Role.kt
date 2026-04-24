package com.kasuminotes.common

import com.kasuminotes.R

enum class Role(val strId: Int, val imgId: Int, val halfImgId: Int) {
    All(R.string.all_role, 0, 0),
    Attacker(R.string.attacker, R.drawable.icon_unit_role_001, R.drawable.icon_unit_role_half_001),
    Breaker(R.string.breaker, R.drawable.icon_unit_role_002, R.drawable.icon_unit_role_half_002),
    Buffer(R.string.buffer, R.drawable.icon_unit_role_003, R.drawable.icon_unit_role_half_003),
    Debuffer(R.string.debuffer, R.drawable.icon_unit_role_004, R.drawable.icon_unit_role_half_004),
    Booster(R.string.booster, R.drawable.icon_unit_role_005, R.drawable.icon_unit_role_half_005),
    Healer(R.string.healer, R.drawable.icon_unit_role_006, R.drawable.icon_unit_role_half_006),
    Tank(R.string.tank, R.drawable.icon_unit_role_007, R.drawable.icon_unit_role_half_007),
    Jammer(R.string.jammer, R.drawable.icon_unit_role_008, R.drawable.icon_unit_role_half_008);

    companion object {
        val roleIdList = (1..8).toList()

        fun fromId(roleId: Int): Role = when (roleId) {
            1 -> Attacker
            2 -> Breaker
            3 -> Buffer
            4 -> Debuffer
            5 -> Booster
            6 -> Healer
            7 -> Tank
            else -> Jammer
        }
    }
}
