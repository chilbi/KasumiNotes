package com.kasuminotes.db

import android.database.sqlite.SQLiteDatabase
import com.kasuminotes.data.UserData
import com.kasuminotes.data.UserKnightEnhance

/**
 * talent_levels: "750,750,750,750,750"
 * talent_nodes: "531:5,532:5,533:5"//nodeId:level
 * team_node: 95//nodeId
 * roles: "1:1:4:2,1:2:4:2,..."//roleId:slotId:slotLevel:level
 */
fun SQLiteDatabase.createUserKnightEnhance() {
    try {
        execSQL(
            """CREATE TABLE IF NOT EXISTS `user_knight_enhance`(
'user_id' INTEGER NOT NULL,
'talent_levels' TEXT NOT NULL,
'talent_nodes' TEXT NOT NULL,
'team_node' INTEGER NOT NULL,
'roles' TEXT NOT NULL,
PRIMARY KEY('user_id')
)"""
        )
    } catch (_: Throwable) {}
}

fun AppDatabase.getUserKnightEnhance(userId: Int): UserKnightEnhance? {
    if (!existsTable("user_knight_enhance")) {
        useDatabase { createUserKnightEnhance() }
        return null
    }
    val sql = """SELECT talent_levels,talent_nodes,team_node,roles
FROM user_knight_enhance WHERE user_id=$userId"""
    return useDatabase {
        rawQuery(sql, null).use {
            if (it.moveToFirst()) {
                val rawTalentLevels = it.getString(0)
                val rawTalentNodes = it.getString(1)
                val teamNode = it.getInt(2)
                val rawRoles = it.getString(3)
                val talentLevels = if (rawTalentLevels == "") {
                    listOf(1, 1, 1, 1, 1)
                } else{
                    rawTalentLevels.split(",").map { lv -> lv.toInt() }
                }
                val talentNodes = if (rawTalentNodes == "") {
                    emptyList()
                } else {
                    rawTalentNodes.split(",")
                        .map { segment ->
                            val list = segment.split(":")
                            list[0].toInt() to list[1].toInt()
                        }
                }
                val roles = rawRoles.split(",")
                    .map { segment -> segment.split(":").map(String::toInt) }
                    .groupBy({ list -> list[0] }, { list -> list[1] to Pair(list[2], list[3]) })
                    .mapValues { (_, values) -> values.toMap() }
                UserKnightEnhance(
                    userId,
                    talentLevels,
                    talentNodes,
                    teamNode,
                    roles
                )
            } else {
                null
            }
        }
    }
}

fun AppDatabase.getBackupUserKnightEnhanceList(): List<UserKnightEnhance> {
    if (!existsTable("user_knight_enhance")) {
        useDatabase { createUserKnightEnhance() }
        return emptyList()
    }
    val sql = """SELECT user_id,talent_levels,talent_nodes,team_node,roles
FROM user_knight_enhance"""
    return useDatabase {
        rawQuery(sql, null).use {
            val list = mutableListOf<UserKnightEnhance>()
            while (it.moveToNext()) {
                val userId = it.getInt(0)
                val rawTalentLevels = it.getString(1)
                val rawTalentNodes = it.getString(2)
                val teamNode = it.getInt(3)
                val rawRoles = it.getString(4)
                val talentLevels = if (rawTalentLevels == "") {
                    listOf(1, 1, 1, 1, 1)
                } else{
                    rawTalentLevels.split(",").map { lv -> lv.toInt() }
                }
                val talentNodes = if (rawTalentNodes == "") {
                    emptyList()
                } else {
                    rawTalentNodes.split(",")
                        .map { segment ->
                            val list = segment.split(":")
                            list[0].toInt() to list[1].toInt()
                        }
                }
                val roles = rawRoles.split(",")
                    .map { segment -> segment.split(":").map(String::toInt) }
                    .groupBy({ list -> list[0] }, { list -> list[1] to Pair(list[2], list[3]) })
                    .mapValues { (_, values) -> values.toMap() }
                list.add(UserKnightEnhance(
                    userId,
                    talentLevels,
                    talentNodes,
                    teamNode,
                    roles
                ))
            }
            list
        }
    }
}

fun AppDatabase.putUserKnightEnhance(userKnightEnhance: UserKnightEnhance) {
    useDatabase {
        execSQL("""REPLACE INTO `user_knight_enhance` (user_id,talent_levels,talent_nodes,team_node,roles)
VALUES (${userKnightEnhance.stringValues})""")
    }
}

fun AppDatabase.putUserKnightEnhanceList(userKnightEnhanceList: List<UserKnightEnhance>) {
    if (userKnightEnhanceList.isEmpty()) return
    var sql = "REPLACE INTO `user_knight_enhance` (user_id,talent_levels,talent_nodes,team_node,roles)\nSELECT ${userKnightEnhanceList[0].stringValues}"
    val len = userKnightEnhanceList.size
    var i = 1
    while (i < len) {
        sql += "\nUNION SELECT ${userKnightEnhanceList[i].stringValues}"
        i++
    }
    useDatabase {
        execSQL(sql)
    }
}

fun AppDatabase.deleteUserKnightEnhance(userId: Int) {
    useDatabase {
        execSQL("DELETE FROM user_knight_enhance WHERE user_id=$userId")
    }
}
