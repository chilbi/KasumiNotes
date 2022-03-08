package com.kasuminotes.utils

import com.kasuminotes.common.QuestRange
import com.kasuminotes.common.QuestType
import kotlin.math.max
import kotlin.math.min

object Helper {
    fun getStoryUnlockCount(statusSize: Int, loveLevel: Int, maxRarity: Int): Int {
        return if (maxRarity > 5 && statusSize < 9) {
            if (loveLevel < 9) loveLevel / 2 - 1 else loveLevel - 5
        } else {
            (if (statusSize < 4) loveLevel / 2 else loveLevel) - 1
        }
    }

    fun getEquipRarityString(equipId: Int): String {
        val idStr = equipId.toString()
        if (idStr.length < 6) return "-1"
        return idStr.substring(2, 3) + idStr.substring(5, 6)
    }

    fun getEquipRarity(equipId: Int): Int {
        return getEquipRarityString(equipId).toInt()
    }

    fun getEquipType(equipId: Int): Int {
        return equipId.toString().substring(3, 5).toInt()
    }

    fun getQuestType(questId: Int): QuestType {
        return when {
            questId < 12000000 -> QuestType.N
            questId < 13000000 -> QuestType.H
            questId < 18000000 -> QuestType.VH
            else -> QuestType.S
        }
    }

    fun mergeQuestRanges(ranges: MutableList<QuestRange>): MutableList<QuestRange> {
        return mergeQuestRanges(ranges, 0, 1, ranges.size)
    }

    private fun mergeQuestRanges(
        ranges: MutableList<QuestRange>,
        i: Int,
        j: Int,
        size: Int
    ): MutableList<QuestRange> {
        if (i + 1 >= size) return ranges

        val iMin = ranges[i].min
        val iMax = ranges[i].max
        val jMin = ranges[j].min
        val jMax = ranges[j].max

        return if (iMax < jMin || jMax < iMin) {
            if (j + 1 == size) {
                mergeQuestRanges(ranges, i + 1, i + 2, size)
            } else {
                mergeQuestRanges(ranges, i, j + 1, size)
            }
        } else {
            ranges[j] = QuestRange(min(iMin, jMin), max(iMax, jMax))
            ranges.removeAt(i)
            mergeQuestRanges(ranges, 0, 1, size - 1)
        }
    }
}
