package com.kasuminotes.common

import com.kasuminotes.data.QuestData
import com.kasuminotes.utils.Helper

class QuestRange(
    val min: Int,
    val max: Int
) {
    operator fun plus(other: QuestRange): QuestRange {
        return QuestRange(
            minOf(min, other.min),
            maxOf(max, other.max)
        )
    }

    companion object {
        val N get() = QuestRange(11001001, 12000000)
        val H get() = QuestRange(12001001, 13000000)
        val VH get() = QuestRange(13001001, 14000000)
        val S get() = QuestRange(18001001, 20000000)

        fun getFilteredQuestDataList(
            questDataList: List<QuestData>,
            questTypes: Array<QuestType>,
            min37: Boolean
        ): List<QuestData> {
            var list = questDataList
            if (!questTypes.contains(QuestType.N)) {
                list = list.filter { item -> item.questId > N.max }
            }
            if (!questTypes.contains(QuestType.H)) {
                list = list.filter { item -> item.questId < H.min || item.questId > H.max }
            }
            if (!questTypes.contains(QuestType.VH)) {
                list = list.filter { item -> item.questId < VH.min || item.questId > VH.max }
            }
            if (!questTypes.contains(QuestType.S)) {
                list = list.filter { item -> item.questId < S.min }
            }
            if (min37) {
                list = list.filter { item -> item.questId > 11037000 }
            }
            return list
        }

        fun getQuestRangeList(
            searchedList: List<Int>,
            questTypes: Array<QuestType>,
            dropMap: Map<Int, QuestRange>,
            min37: Boolean
        ): List<QuestRange> {
            val list = mutableListOf<QuestRange>()
            val hasH = questTypes.contains(QuestType.H)
            val hasVH = questTypes.contains(QuestType.VH)

            questTypes.forEach { type ->
                if (type == QuestType.S) {
                    list.add(S)
                } else {
                    searchedList.forEach { search ->
                        if (search > 33000) {
                            val rangeId = type.value + Helper.getEquipRarity(search)
                            val questRange = dropMap[rangeId]
                            if (questRange != null) {
                                list.add(questRange)
                            }
                        } else if (search > 32000 && hasVH) {
                            list.add(VH)
                        } else if (search > 31000 && hasH) {
                            list.add(H)
                        }
                    }
                }
            }

            if (list.isEmpty()) return list

            val mergedRanges = Helper.mergeQuestRanges(list)

            return if (min37 && questTypes.contains(QuestType.N)) {
                val minId = 11037000
                val result = mutableListOf<QuestRange>()
                mergedRanges.forEach { item ->
                    if (item.min > minId) {
                        result.add(item)
                    } else if (item.max > minId) {
                        result.add(QuestRange(minId, item.max))
                    }
                }
                result
            } else {
                mergedRanges
            }
        }
    }
}