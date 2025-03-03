package com.kasuminotes.db

import com.kasuminotes.data.UnitConversionData
import com.kasuminotes.data.UnitData
import com.kasuminotes.data.UserData
import com.kasuminotes.data.UserProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

suspend fun AppDatabase.getUserProfileList(userId: Int): List<UserProfile> {
    val list = mutableListOf<UserProfile>()
    useDatabase {
        if (!existsColumn("user_data", "sub_percent_json")) {
            execSQL("ALTER TABLE user_data ADD COLUMN sub_percent_json TEXT NOT NULL DEFAULT ''")
        }

        if (!existsTable("unit_talent")) {
            execSQL(
                """CREATE TABLE `unit_talent`(
'setting_id' INTEGER NOT NULL,
'unit_id' INTEGER NOT NULL,
'talent_id' INTEGER NOT NULL,
PRIMARY KEY ('setting_id')
)"""
            )
        }

        if (!existsTable("unit_role_data")) {
            execSQL(
                """CREATE TABLE `unit_role_data`(
'unit_id' INTEGER NOT NULL,
'unit_role_id' INTEGER NOT NULL,
PRIMARY KEY ('unit_id')
)"""
            )
        }

        val sql = """SELECT ud.unit_id,
${UserData.getFields(pk = false, fk = false)},
${UnitData.getFields(pk = false)},
IFNULL(ut.talent_id, 0) AS talent_id,
IFNULL(urd.unit_role_id, 0) AS unit_role_id
FROM user_data AS ud
LEFT JOIN chara_data AS cd ON ud.unit_id=cd.unit_id
LEFT JOIN unit_talent AS ut ON ud.unit_id=ut.unit_id
LEFT JOIN unit_role_data AS urd ON ud.unit_id=urd.unit_id
WHERE user_id=$userId"""
        rawQuery(sql, null).use {
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
                    it.getInt(i++),
                    it.getInt(i++),
                    it.getString(i++)
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
                    it.getString(i++),
                    it.getInt(i++),
                    it.getInt(i)
                )

                list.add(UserProfile(userData, unitData))
            }
        }
    }
    return if (existsTable("unit_conversion")) {
        withContext(Dispatchers.IO) {
            list.map { userProfile ->
                async {
                    userProfile.unitConversionData = getUnitConversionData(userProfile.unitData)
                    userProfile
                }
            }.awaitAll()
        }
    } else {
        list
    }
}

fun AppDatabase.getBackupUserDataList(defaultUserId: Int): List<UserData> {
    val sql = """SELECT ${UserData.getFields(pk = true, fk = true)} 
FROM user_data WHERE user_id!=$defaultUserId"""

    return useDatabase {
        if (!existsColumn("user_data", "sub_percent_json")) {
            execSQL("ALTER TABLE user_data ADD COLUMN sub_percent_json TEXT NOT NULL DEFAULT ''")
        }

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
                    it.getInt(i++),
                    it.getInt(i++),
                    it.getString(i)
                )

                list.add(userProfile)
            }
            list
        }
    }
}

private fun AppDatabase.getUnitConversionData(originalUnitData: UnitData): UnitConversionData? {
    if (originalUnitData.maxRarity < 6) return null
    return useDatabase {
        val convertedUnitId = rawQuery(
            "SELECT unit_id FROM unit_conversion WHERE original_unit_id=${originalUnitData.unitId}",
            null
        ).use {
            if (it.moveToFirst()) {
                it.getInt(0)
            } else {
                null
            }
        }
        if (convertedUnitId == null) {
            null
        } else {
            rawQuery(
                """SELECT unit_name,kana,search_area_width,atk_type,normal_atk_cast_time,comment,start_time
FROM unit_data WHERE unit_id=${convertedUnitId}""",
                null
            ).use {
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
