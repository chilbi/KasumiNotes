package com.kasuminotes.db

import com.kasuminotes.data.ConnectRankBonus
import com.kasuminotes.data.ConnectRankData
import com.kasuminotes.data.ConnectRankStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

//fun AppDatabase.createConnectRankBonus() {
//    if (!existsTable("connect_rank_bonus")) {
//        useDatabase {
//            execSQL(
//"""CREATE TABLE `connect_rank_bonus`(
//'bonus_level' INTEGER NOT NULL,
//'bonus_number' INTEGER NOT NULL,
//'bonus_type' INTEGER NOT NULL,
//'description_type' INTEGER NOT NULL,
//'value_1' INTEGER NOT NULL,
//'value_2' INTEGER NOT NULL,
//'description' TEXT NOT NULL,
//PRIMARY KEY ('bonus_number', 'bonus_level')
//)"""
//            )
//        }
//    }
//}

fun AppDatabase.getMaxConnectRank(): Int {
    return if (existsTable("connect_rank_chart")) {
        useDatabase {
            try {
                rawQuery("SELECT MAX(rank) FROM connect_rank_chart", null).use {
                    if (it.moveToFirst()) {
                        it.getInt(0)
                    } else {
                        0
                    }
                }
            } catch (_: Throwable){
                0
            }
        }
    } else {
        0
    }
}

fun AppDatabase.getSumConnectRankBonusCharaLevel(): Int {
    return if (existsTable("connect_rank_bonus")) {
        val sql = "SELECT SUM(value_1) FROM connect_rank_bonus WHERE bonus_type=3"
        useDatabase {
            try {
                rawQuery(sql, null).use {
                    if (it.moveToFirst()) {
                        it.getInt(0)
                    } else {
                        0
                    }
                }
            } catch (_: Throwable) {
                0
            }
        }
    } else {
        0
    }
}

suspend fun AppDatabase.getConnectRankData(): ConnectRankData? {
    try {
        if (!existsTables(listOf("connect_rank_chart", "connect_rank_bonus", "connect_rank_status"))) return null
        return withContext(Dispatchers.IO) {
            val list = awaitAll(
                async {
                    useDatabase {
                        rawQuery("SELECT rank,bonus_level FROM connect_rank_chart ORDER BY rank ASC", null).use {
                            val map = mutableMapOf<Int, Int>()
                            while (it.moveToNext()) {
                                map[it.getInt(0)] = it.getInt(1)
                            }
                            map
                        }
                    }
                },
                async {
                    useDatabase {
                        rawQuery("SELECT ${ConnectRankBonus.getFields()} FROM connect_rank_bonus ORDER BY bonus_level,bonus_number ASC", null).use {
                            val list = mutableListOf<ConnectRankBonus>()
                            while (it.moveToNext()) {
                                var i = 0
                                list.add(
                                    ConnectRankBonus(
                                        it.getInt(i++),
                                        it.getInt(i++),
                                        it.getInt(i++),
                                        it.getInt(i++),
                                        it.getInt(i++),
                                        it.getInt(i++),
                                        it.getString(i)
                                    )
                                )
                            }
                            list
                        }
                    }
                },
                async {
                    useDatabase {
                        rawQuery("SELECT ${ConnectRankStatus.getFields()} FROM connect_rank_status ORDER BY id ASC", null).use {
                            val list = mutableListOf<ConnectRankStatus>()
                            while (it.moveToNext()) {
                                var i = 0
                                val valueList = mutableListOf<Int>()
                                while (i < 5) {
                                    valueList.add(it.getInt(i++))
                                }
                                val paramTypeList = mutableListOf<Int>()
                                while (i < 10) {
                                    paramTypeList.add(it.getInt(i++))
                                }
                                list.add(
                                    ConnectRankStatus(
                                        it.getInt(i++),
                                        it.getInt(i++),
                                        it.getInt(i++),
                                        it.getInt(i),
                                        valueList,
                                        paramTypeList
                                    )
                                )
                            }
                            list
                        }
                    }
                }
            )
            @Suppress("UNCHECKED_CAST")
            ConnectRankData(
                list[0] as Map<Int, Int>,
                list[1] as List<ConnectRankBonus>,
                list[2] as List<ConnectRankStatus>
            )
        }
    } catch (_: Throwable) {
        return null
    }
}
