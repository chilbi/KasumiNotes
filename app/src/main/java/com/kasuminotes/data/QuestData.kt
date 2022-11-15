package com.kasuminotes.data

import java.lang.Exception

data class QuestData(
    val questId: Int,
    val areaId: Int,
    val questName: String,
    val waveGroupDataList: List<WaveGroupData>,//1-3 wave_group_id
    val rewardImageList: List<Int>//1-5
) {
    private var dropGold: Int? = null

    private var dropList: List<RewardData>? = null

    fun getDropGold(): Int {
        return dropGold ?: throw Exception("First call getDropList() or contains()")
    }

    fun getDropList(): List<RewardData> {
        if (dropList != null) return dropList!!

        val list = mutableListOf<RewardData>()
        val sortedList = mutableListOf<RewardData>()
        var totalDropGold = 0

        waveGroupDataList.forEach { waveGroupData ->
            waveGroupData.enemyDropDataList.forEach { enemyDropData ->
                totalDropGold += enemyDropData.dropGold
                if (enemyDropData.enemyRewardData != null) {
                    list.addAll(enemyDropData.enemyRewardData.rewardDataList)
                }
            }
        }

        rewardImageList.forEach { rewardImage ->
            val rewardImageItem = list.find { rewardData -> rewardData.rewardId == rewardImage }
            if (rewardImageItem != null) {
                sortedList.add(rewardImageItem)
                list.remove(rewardImageItem)
            }
        }
        sortedList.addAll(list)

        dropGold = totalDropGold
        dropList = sortedList
        return sortedList
    }

    fun contains(rewardId: Int): Boolean {
        return getDropList().any { item -> item.rewardId == rewardId }
    }

    companion object {
        private var fields: String? = null

        fun getFields(): String {
            if (fields == null) {
                val waveGroupFields = (1..3).joinToString(",") { i ->
                    "wave_group_id_$i"
                }
                val rewardImageFields = (1..5).joinToString(",") { i ->
                    "reward_image_$i"
                }
                fields = "$waveGroupFields,$rewardImageFields,quest_id,area_id,quest_name"
            }
            return fields!!
        }
    }
}
