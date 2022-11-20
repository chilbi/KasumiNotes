package com.kasuminotes.db

import com.kasuminotes.data.MaxUserData
import com.kasuminotes.data.UnitConversionData
import com.kasuminotes.data.UnitData
import com.kasuminotes.data.User
import com.kasuminotes.data.UserData
import com.kasuminotes.data.UserProfile
import com.kasuminotes.ui.app.DefaultUserId
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll

suspend fun AppDatabase.getBackupUserDataList(defaultUserId: Int): List<UserData> {
    val sql = """SELECT ${UserData.getFields(pk = true, fk = true)} 
FROM user_data WHERE user_id!=$defaultUserId"""

    return safelyUse {
        val list = mutableListOf<UserData>()
        rawQuery(sql, null).use {

            while (it.moveToNext()) {
                var i = 0

                val userProfile = UserData(
                    it.getInt(i++),
                    it.getInt(i++),
                    it.getInt(i++),
                    it.getInt(i++),
                    it.getInt(i++),
                    it.getInt(i++),
                    it.getInt(i++),
                    it.getInt(i++),
                    it.getInt(i++),
                    it.getInt(i++),
                    it.getInt(i++),
                    it.getInt(i++),
                    it.getInt(i++),
                    it.getInt(i++),
                    it.getInt(i++),
                    it.getInt(i++),
                    it.getInt(i++),
                    it.getInt(i++),
                    it.getInt(i++),
                    it.getInt(i++),
                    it.getInt(i++),
                    it.getInt(i++),
                    it.getInt(i)
                )

                list.add(userProfile)
            }
            list
        }
    }
}

suspend fun AppDatabase.putUserData(userData: UserData) {
    safelyUse {
        execSQL("REPLACE INTO `user_data` VALUES (${userData.stringValues})")
    }
}

suspend fun AppDatabase.putUserDataList(userDataList: List<UserData>) {
    if (userDataList.isEmpty()) return

    var sql = "REPLACE INTO `user_data`\nSELECT ${userDataList[0].stringValues}"
    val len = userDataList.size
    var i = 1

    while (i < len) {
        sql += "\nUNION SELECT ${userDataList[i].stringValues}"
        i++
    }

    safelyUse {
        execSQL(sql)
    }
}

suspend fun AppDatabase.deleteUser(userId: Int) {
    safelyUse {
        execSQL("DELETE FROM user_data WHERE user_id=$userId")
    }
}

suspend fun AppDatabase.deleteUserData(userId: Int, deleteChara: List<Int>) {
    if (userId == DefaultUserId || deleteChara.isEmpty()) return

    val sql = """DELETE FROM user_data
WHERE user_id=$userId AND unit_id IN (${deleteChara.joinToString(",")})"""

    safelyUse {
        execSQL(sql)
    }
}

suspend fun AppDatabase.getUserProfileList(userId: Int): List<UserProfile> {
    val sql = """SELECT ud.unit_id,
${UserData.getFields(pk = false, fk = false)},
${UnitData.getFields(pk = false)}
FROM user_data AS ud
LEFT JOIN chara_data AS cd ON ud.unit_id=cd.unit_id
WHERE user_id=$userId"""

    return withIOContext {
        val originalProfiles = use {
            rawQuery(sql, null).use {
                val list = mutableListOf<UserProfile>()

                while (it.moveToNext()) {
                    var i = 0

                    val unitId = it.getInt(i++)
                    val userData = UserData(
                        userId,//同参数userId
                        unitId,//i=0
                        it.getInt(i++),
                        it.getInt(i++),
                        it.getInt(i++),
                        it.getInt(i++),
                        it.getInt(i++),
                        it.getInt(i++),
                        it.getInt(i++),
                        it.getInt(i++),
                        it.getInt(i++),
                        it.getInt(i++),
                        it.getInt(i++),
                        it.getInt(i++),
                        it.getInt(i++),
                        it.getInt(i++),
                        it.getInt(i++),
                        it.getInt(i++),
                        it.getInt(i++),
                        it.getInt(i++),
                        it.getInt(i++),
                        it.getInt(i++),
                        it.getInt(i++)
                    )

                    val unitData = UnitData(
                        unitId,//同
                        it.getString(i++),
                        it.getString(i++),
                        it.getString(i++),
                        it.getInt(i++),
                        it.getInt(i++),
                        it.getInt(i++),
                        it.getInt(i++),
                        it.getFloat(i++),
                        it.getString(i++),
                        it.getString(i++),
                        it.getString(i++),
                        it.getString(i++),
                        it.getString(i++),
                        it.getString(i++),
                        it.getString(i++),
                        it.getString(i++),
                        it.getString(i++),
                        it.getString(i++),
                        it.getString(i++),
                        it.getString(i++),
                        it.getString(i++),
                        it.getString(i)
                    )

                    list.add(UserProfile(userData, unitData))
                }

                list
            }
        }

        originalProfiles.map { userProfile ->
            async {
                userProfile.unitConversionData = getUnitConversionData(userProfile.unitData)
                userProfile
            }
        }.awaitAll()
    }
}

suspend fun AppDatabase.getMaxUserData(userId: Int): MaxUserData {
    val sql = """SELECT max_chara_level,max_promotion_level,max_unique_level,
max_area,max_chara,max_unique,max_rarity_6,user_chara,user_unique,user_rarity_6
FROM max_data
LEFT JOIN (SELECT COUNT(user_id) AS user_chara FROM user_data WHERE user_id=$userId)
LEFT JOIN (SELECT COUNT(user_id) AS user_unique FROM user_data WHERE user_id=$userId AND unique_level>0)
LEFT JOIN (SELECT COUNT(user_id) AS user_rarity_6 FROM user_data WHERE user_id=$userId AND rarity=6)"""

    return safelyUse {
        rawQuery(sql, null).use {
            it.moveToFirst()
            var i = 0

            MaxUserData(
                it.getInt(i++),
                it.getInt(i++),
                it.getInt(i++),
                it.getInt(i++),
                it.getInt(i++),
                it.getInt(i++),
                it.getInt(i++),
                it.getInt(i++),
                it.getInt(i++),
                it.getInt(i),
            )
        }
    }
}

suspend fun AppDatabase.getUserName(userId: Int): String {
    val unitId = userId / 100 * 100 + 1
    val sql = "SELECT actual_name FROM chara_data WHERE unit_id=$unitId"

    return safelyUse {
        rawQuery(sql, null).use {
            it.moveToFirst()
            it.getString(0)
        }
    }
}

suspend fun AppDatabase.getAllUser(): List<Int> {
    val sql = "SELECT user_id FROM user_data GROUP BY user_id"

    return safelyUse {
        rawQuery(sql, null).use {
            val list = mutableListOf<Int>()

            while (it.moveToNext()) {
                list.add(it.getInt(0))
            }

            list
        }
    }
}

suspend fun AppDatabase.getUserList(): List<User> {
    val sql = """SELECT user_id,actual_name,user_chara
FROM (SELECT user_id,COUNT(user_id) AS user_chara FROM user_data GROUP BY user_id) AS ud
LEFT JOIN chara_data AS cd ON SUBSTR(ud.user_id,1,4)=SUBSTR(cd.unit_id,1,4)"""

    return safelyUse {
        rawQuery(sql, null).use {
            val list = mutableListOf<User>()

            while (it.moveToNext()) {
                list.add(
                    User(
                        it.getInt(0),
                        it.getString(1),
                        it.getInt(2)
                    )
                )
            }

            list
        }
    }
}

fun AppDatabase.existsTable(tableName: String): Boolean {
    val sql = "SELECT count(*) FROM sqlite_master WHERE type=\"table\" AND name=\"$tableName\""
    return use {
        rawQuery(sql, null).use {
            it.moveToFirst()
            it.getInt(0) > 0
        }
    }
}

private suspend fun AppDatabase.getUnitConversionData(originalUnitData: UnitData): UnitConversionData? {
    return if (originalUnitData.maxRarity > 5) {
        if (existsTable("unit_conversion")) {
            withIOContext {
                val convertedUnitId = use {
                    val sql = "SELECT unit_id FROM unit_conversion WHERE original_unit_id=${originalUnitData.unitId}"
                    rawQuery(sql, null).use {
                        if (it.moveToFirst()) {
                            it.getInt(0)
                        } else {
                            null
                        }
                    }
                }
                if (convertedUnitId == null) {
                    null
                } else {
                    use {
                        val sql =
                            """SELECT unit_name,kana,search_area_width,atk_type,normal_atk_cast_time,comment,start_time
FROM unit_data WHERE unit_id=${convertedUnitId}"""
                        rawQuery(sql, null).use {
                            it.moveToFirst()
                            UnitConversionData(
                                convertedUnitId,
                                originalUnitData.copy(
                                    unitName = it.getString(0),
                                    kana = it.getString(1),
                                    searchAreaWidth = it.getInt(2),
                                    atkType = it.getInt(3),
                                    normalAtkCastTime = it.getFloat(4),
                                    comment = it.getString(5),
                                    startTime = it.getString(6)
                                )
                            )
                        }
                    }
                }
            }
        } else {
            null
        }
    } else {
        null
    }
}
