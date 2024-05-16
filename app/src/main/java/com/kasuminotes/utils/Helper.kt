package com.kasuminotes.utils

import com.kasuminotes.common.QuestType

object Helper {
    const val NullId = 999999

    fun getStoryDiffCount(statusSize: Int, maxRarity: Int): Int {
        val maxSize = if (maxRarity < 6) {
            if (statusSize < 5)  4 else 8
        } else {
            if (statusSize < 9) 8 else 12
        }
        return maxSize - statusSize
    }

    fun getStoryUnlockCount(statusSize: Int, loveLevel: Int, maxRarity: Int, diffCount: Int): Int {
        return if (maxRarity > 5 && statusSize < 9) {
            if (loveLevel < 9) loveLevel / 2 - diffCount else loveLevel - 4 - diffCount
        } else {
            (if (statusSize < 5) loveLevel / 2 else loveLevel) - diffCount
        }
    }

    private fun getEquipRarityString(equipId: Int): String {
        val idStr = equipId.toString()
        if (idStr.length < 6) return "-1"
        return if (idStr.length > 6) {
            val rank = if (idStr.length > 7) idStr.substring(6, 8).toInt() else 31
            "1" + idStr.substring(2, 3) + (rank - 31)
        } else {
            idStr.substring(2, 3) + idStr.substring(5, 6)
        }
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
    }/*

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
    }*/
}
