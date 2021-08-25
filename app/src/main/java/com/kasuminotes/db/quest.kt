package com.kasuminotes.db

import com.kasuminotes.common.QuestRange
import com.kasuminotes.data.EnemyDropData
import com.kasuminotes.data.EnemyRewardData
import com.kasuminotes.data.QuestData
import com.kasuminotes.data.RewardData
import com.kasuminotes.data.WaveGroupData
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll

suspend fun AppDatabase.getEnemyRewardData(dropRewardId: Int): EnemyRewardData {
    val sql = "SELECT ${EnemyRewardData.getFields()} FROM enemy_reward_data WHERE drop_reward_id=$dropRewardId"

    return safelyUse {
        rawQuery(sql, null).use {
            it.moveToFirst()
            val rewardDataList = mutableListOf<RewardData>()
            var i = 0

            while (i < 15) {
                val rewardId = it.getInt(i++)
                if (rewardId == 0) break

                val rewardType = it.getInt(i++)
                val odds = it.getInt(i++)

                if (rewardType == 4 || (rewardId in 30001..39999) || rewardId == 25001) {
                    rewardDataList.add(
                        RewardData(
                            rewardId,
                            rewardType,
                            odds
                        )
                    )
                }
            }

            EnemyRewardData(
                dropRewardId,
                rewardDataList
            )
        }
    }
}

suspend fun AppDatabase.getWaveGroupDataList(waveGroupIdList: List<Int>): List<WaveGroupData> {
    val condition = waveGroupIdList.joinToString(" OR ") { waveGroupId ->
        "wave_group_id=$waveGroupId"
    }
    val sql = "SELECT ${WaveGroupData.getFields()} FROM wave_group_data WHERE $condition"

    return withIOContext {
        val rawWaveGroupDataList: List<RawWaveGroupData> = use {
            rawQuery(sql, null).use {
                val list = mutableListOf<RawWaveGroupData>()

                while (it.moveToNext()) {
                    val enemyDropDataList = mutableListOf<RawEnemyDropData>()
                    var i = 0

                    while (i < 15) {
                        val enemyId = it.getInt(i++)
                        if (enemyId == 0) break

                        enemyDropDataList.add(
                            RawEnemyDropData(
                                enemyId,
                                it.getInt(i++),
                                it.getInt(i++)
                            )
                        )
                    }

                    list.add(
                        RawWaveGroupData(
                            it.getInt(15),
                            enemyDropDataList
                        )
                    )
                }

                list
            }
        }

        rawWaveGroupDataList.map { item ->
            async {
                val enemyDropDataList = item.enemyDropDataList.map { rawEnemyDropData ->
                    async {
                        val enemyRewardData = if (rawEnemyDropData.dropRewardId != 0) {
                            getEnemyRewardData(rawEnemyDropData.dropRewardId)
                        } else {
                            null
                        }

                        EnemyDropData(
                            rawEnemyDropData.enemyId,
                            rawEnemyDropData.dropGold,
                            enemyRewardData
                        )
                    }
                }.awaitAll()

                WaveGroupData(
                    item.waveGroupId,
                    enemyDropDataList
                )
            }
        }.awaitAll()
    }
}

suspend fun AppDatabase.getQuestDataList(range: QuestRange): List<QuestData> {
    val sql = """SELECT ${QuestData.getFields()} FROM quest_data
WHERE quest_id>=${range.min} AND quest_id<=${range.max}"""

    return withIOContext {
        val rawQuestList: List<RawQuestData> = use {
            rawQuery(sql, null).use {
                val list = mutableListOf<RawQuestData>()

                while (it.moveToNext()) {
                    val waveGroupIdList = mutableListOf<Int>()
                    val rewardImageList = mutableListOf<Int>()

                    var i = 0
                    while (i < 3) {
                        val waveGroupId = it.getInt(i++)
                        if (waveGroupId == 0) break

                        waveGroupIdList.add(waveGroupId)
                    }

                    i = 3
                    while (i < 8) {
                        val rewardImage = it.getInt(i++)
                        if (rewardImage == 0) break

                        rewardImageList.add(rewardImage)
                    }

                    i = 8

                    if (rewardImageList.size > 0 && waveGroupIdList.size > 0) {
                        list.add(
                            RawQuestData(
                                it.getInt(i++),
                                it.getInt(i++),
                                it.getString(i),
                                waveGroupIdList,
                                rewardImageList
                            )
                        )
                    }
                }

                list
            }
        }

        rawQuestList.map { item ->
            async {
                val waveGroupDataList = getWaveGroupDataList(item.waveGroupIdList)
                QuestData(
                    item.questId,
                    item.areaId,
                    item.questName,
                    waveGroupDataList,
                    item.rewardImageList
                )
            }
        }.awaitAll()
    }
}

suspend fun AppDatabase.getDropRangeMap(): Map<Int, QuestRange> = safelyUse {
    val sql = "SELECT range_id,range_min,range_max FROM drop_range"

    rawQuery(sql, null).use {
        val map = mutableMapOf<Int, QuestRange>()

        while (it.moveToNext()) {
            val rangeId = it.getInt(0)
            val rangeMin = it.getInt(1)
            val rangeMax = it.getInt(2)

            map[rangeId] = QuestRange(rangeMin, rangeMax)
        }

        map
    }
}

suspend fun AppDatabase.getMemoryPieces(): Array<List<Int>> = safelyUse {
    val sql = "SELECT id FROM memory_piece ORDER BY id DESC"

    rawQuery(sql, null).use {
        val normalPieceList = mutableListOf<Int>()
        val purePieceList = mutableListOf<Int>()

        while (it.moveToNext()) {
            val id = it.getInt(0)
            if (id < 32000) {
                normalPieceList.add(id)
            } else {
                purePieceList.add(id)
            }
        }

        arrayOf(normalPieceList, purePieceList)
    }
}

private class RawEnemyDropData(
    val enemyId: Int,
    val dropGold: Int,
    val dropRewardId: Int
)

private class RawWaveGroupData(
    val waveGroupId: Int,
    val enemyDropDataList: List<RawEnemyDropData>//1-5
)

private class RawQuestData(
    val questId: Int,
    val areaId: Int,
    val questName: String,
    val waveGroupIdList: List<Int>,//1-3
    val rewardImageList: List<Int>//1-5
)
