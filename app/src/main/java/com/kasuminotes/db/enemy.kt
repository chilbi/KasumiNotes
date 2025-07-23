package com.kasuminotes.db

import android.database.sqlite.SQLiteException
import com.kasuminotes.data.EnemyData
import com.kasuminotes.data.ExtraEffectData
import com.kasuminotes.data.Property

fun AppDatabase.getEnemyData(enemyId: Int, epTableName: String): EnemyData? {
    val sql = """SELECT ${EnemyData.getFields()}
FROM $epTableName AS ep
LEFT JOIN enemy_m_parts AS emp ON emp.enemy_id=ep.enemy_id
LEFT JOIN unit_enemy_data AS ued ON ued.unit_id=ep.unit_id
WHERE ep.enemy_id=$enemyId"""

    return useDatabase {
        rawQuery(sql, null).use {
            if (it.moveToFirst()) {
                var i = 0

                val mainSkillLvList = mutableListOf<Int>()
                while (i < 10) {
                    mainSkillLvList.add(it.getInt(i++))
                }

                val exSkillLvList = mutableListOf<Int>()
                while (i < 15) {
                    exSkillLvList.add(it.getInt(i++))
                }

                val multiParts = mutableListOf<Int>()
                while (i < 20) {
                    val id = it.getInt(i++)
                    if (id != 0) {
                        multiParts.add(id)
                    }
                }

                val property = Property { _ ->
                    it.getDouble(i++)
                }

                EnemyData(
                    it.getInt(i++),
                    it.getInt(i++),
                    it.getString(i++),
                    it.getInt(i++),
                    it.getInt(i++),
                    it.getFloat(i++),
                    it.getString(i++),
                    it.getInt(i++),
                    it.getInt(i++),
                    it.getInt(i++),
                    it.getInt(i++),
                    it.getInt(i),
                    mainSkillLvList,
                    exSkillLvList,
                    multiParts,
                    property
                )
            } else {
                null
            }
        }
    }
}

fun AppDatabase.getMultiEnemyParts(multiParts: List<Int>, epTableName: String): List<EnemyData> {
    val sql = """SELECT ${EnemyData.getFields()}
FROM $epTableName AS ep
LEFT JOIN enemy_m_parts AS emp ON emp.enemy_id=ep.enemy_id
LEFT JOIN unit_enemy_data AS ued ON ued.unit_id=ep.unit_id
WHERE ep.enemy_id IN (${multiParts.joinToString(",")})"""

    return useDatabase {
        rawQuery(sql, null).use {
            val list = mutableListOf<EnemyData>()
            while (it.moveToNext()) {
                var i = 0

                val mainSkillLvList = mutableListOf<Int>()
                while (i < 10) {
                    mainSkillLvList.add(it.getInt(i++))
                }

                val exSkillLvList = mutableListOf<Int>()
                while (i < 15) {
                    exSkillLvList.add(it.getInt(i++))
                }

                val parts = emptyList<Int>()
                i = 20
//                while (i < 20) {
//                    val id = it.getInt(i++)
//                    if (id != 0) {
//                        parts.add(id)
//                    }
//                }

                val property = Property { _ ->
                    it.getDouble(i++)
                }

                list.add(
                    EnemyData(
                        it.getInt(i++),
                        it.getInt(i++),
                        it.getString(i++),
                        it.getInt(i++),
                        it.getInt(i++),
                        it.getFloat(i++),
                        it.getString(i++),
                        it.getInt(i++),
                        it.getInt(i++),
                        it.getInt(i++),
                        it.getInt(i++),
                        it.getInt(i),
                        mainSkillLvList,
                        exSkillLvList,
                        parts,
                        property
                    )
                )
            }
            list
        }
    }
}

fun AppDatabase.getExtraEffectData(targetId: Int, epTableName: String): ExtraEffectData? {
    if (!existsTable("extra_effect_data")) return null

    try {
        val sql = """SELECT ${ExtraEffectData.getFields()}
FROM extra_effect_data AS eed
JOIN $epTableName AS ep ON ep.enemy_id in (eed.enemy_id_1,eed.enemy_id_2,eed.enemy_id_3,eed.enemy_id_4,eed.enemy_id_5)
WHERE $targetId IN (eed.target_value_1,eed.target_value_2)"""

        return useDatabase {
            rawQuery(sql, null).use {
                if (it.moveToFirst()) {
                    var i = 0
                    val targetValueList = mutableListOf<Int>()
                    while (i < 2) {
                        targetValueList.add(it.getInt(i++))
                    }
                    val enemyIdList = mutableListOf<Int>()
                    while (i < 7) {
                        enemyIdList.add(it.getInt(i++))
                    }
                    val execTimingList = mutableListOf<Int>()
                    while (i < 12) {
                        execTimingList.add(it.getInt(i++))
                    }
                    ExtraEffectData(
                        it.getInt(i++),
                        it.getInt(i++),
                        it.getInt(i),
                        targetValueList,
                        enemyIdList,
                        execTimingList
                    )
                } else {
                    null
                }
            }
        }
    } catch (_: SQLiteException) {
        if (fix) {
            return null
        } else {
            fix = true
            if (fixExtraEffectData()) {
                return getExtraEffectData(targetId, epTableName)
            }
            return null
        }
    }
}

var fix = false
// 硬编码修复extra_effect_data表未解密
private fun AppDatabase.fixExtraEffectData(): Boolean {
    //extra_effect_id=430001386 exec_timing_5==4 enemy_id_3!=0 enemy_id_5!=0
    //extra_effect_id=441004012 exec_timing_5==0 enemy_id_3!=0 enemy_id_5==0
    return useDatabase {
        val columns = mutableListOf<String>()
        try {
            rawQuery("PRAGMA table_info(extra_effect_data)", null).use {
                val nameIndex = it.getColumnIndex("name")
                if (nameIndex > -1) {
                    while (it.moveToNext()) {
                        columns.add(it.getString(nameIndex))
                    }
                }
            }
            if (columns.isNotEmpty()) {
                val fields = ExtraEffectData.getFields().split(",")
                val hashColumns = columns.filter { col -> !fields.contains(col) }
                if (hashColumns.isNotEmpty()) {
                    val renames = mutableListOf<Pair<String, String>>()
                    if (!columns.contains("exec_timing_5")) {
                        rawQuery("SELECT ${hashColumns.joinToString(",") { "\"$it\"" }} FROM extra_effect_data WHERE extra_effect_id=430001386", null).use {
                            if (it.moveToFirst()) {
                                for (hashCol in hashColumns) {
                                    val index = it.getColumnIndex(hashCol)
                                    if (index > -1) {
                                        val value = it.getInt(index)
                                        if (value < 10 && value > 0) {//==4
                                            renames.add(hashCol to "exec_timing_5")
                                            break
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (renames.isNotEmpty() && !columns.contains("enemy_id_3") && !columns.contains("enemy_id_5")) {
                        val execTiming5HashName = renames[0].first
                        val enemyIdHashNames = hashColumns.filter { col -> col != execTiming5HashName }
                        rawQuery("SELECT ${enemyIdHashNames.joinToString(",") { "\"$it\"" }} FROM extra_effect_data WHERE extra_effect_id=441004012", null).use {
                            if (it.moveToFirst()) {
                                for (hashCol in enemyIdHashNames) {
                                    val index = it.getColumnIndex(hashCol)
                                    if (index > - 1) {
                                        val value = it.getInt(index)
                                        if (value > 10) {
                                            renames.add(hashCol to "enemy_id_3")
                                        } else {
                                            renames.add(hashCol to "enemy_id_5")
                                        }
                                    }
                                }
                            }
                        }
                    }
                    renames.forEach { rename ->
                        execSQL("ALTER TABLE extra_effect_data RENAME COLUMN \"${rename.first}\" TO ${rename.second}")
                    }
                }
                true
            } else {
                false
            }
        } catch (_: SQLiteException) {
            false
        }
    }
}
