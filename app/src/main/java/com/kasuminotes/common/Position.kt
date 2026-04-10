package com.kasuminotes.common

import com.kasuminotes.R

enum class Position(val strId: Int, val imgId: Int) {
    All(R.string.all_position, 0),
    Front(R.string.front, R.drawable.position_1),
    Middle(R.string.middle, R.drawable.position_2),
    Back(R.string.back, R.drawable.position_3);

    companion object {
        fun getPositionId(searchAreaWidth: Int): Int = when {
            searchAreaWidth < 300 -> 1
            searchAreaWidth < 600 -> 2
            else -> 3
        }

        fun fromId(positionId: Int): Position = when (positionId) {
            1 -> Front
            2 -> Middle
            else -> Back
        }
    }
}
