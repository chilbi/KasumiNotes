package com.kasuminotes.db

import android.util.Log
import com.kasuminotes.common.QuestRange
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import org.json.JSONObject

fun AppDatabase.getCreateTable(tableName: String): String? {
    return try {
        readableDatabase.rawQuery(
            "SELECT sql FROM sqlite_master WHERE type='table' AND name='$tableName'",
            null
        ).use {
            it.moveToNext()
            it.getString(0)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun AppDatabase.execTransaction(sqls: List<String>): Boolean {
    writableDatabase.beginTransaction()
    return try {
        sqls.forEach {
            writableDatabase.execSQL(it)
        }
        writableDatabase.setTransactionSuccessful()
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    } finally {
        writableDatabase.endTransaction()
    }
}
suspend fun AppDatabase.unHashDb(rainbowJson: JSONObject) = safelyUse {
    val keysIterator = rainbowJson.keys()
    while (keysIterator.hasNext()) {
        val hashedTableName = keysIterator.next()
        val colsObject = JSONObject(rainbowJson.get(hashedTableName).toString())
        val colsIterator = colsObject.keys()

        val intactTableName = colsObject.getString("--table_name")
        var createTableStatement = getCreateTable(hashedTableName)
        if (createTableStatement == null) {
            Log.w("unHashDb", "CreateTableStatement for '$intactTableName' not found.")
            continue
        }
        val hashedCols = mutableListOf<String>()
        val intactCols = mutableListOf<String>()
        while (colsIterator.hasNext()) {
            val hashedColName = colsIterator.next()
            val intactColName = colsObject.getString(hashedColName)
            if (hashedColName != "--table_name") {
                hashedCols.add(hashedColName)
                intactCols.add(intactColName)
            }
            createTableStatement = createTableStatement?.replace(
                if (hashedColName == "--table_name") hashedTableName else hashedColName,
                if (hashedColName == "--table_name") intactTableName else intactColName
            )
        }
        val insertStatement = "INSERT INTO $intactTableName(${intactCols.joinToString("`,`", "`", "`")}) SELECT ${hashedCols.joinToString("`,`", "`", "`")} FROM $hashedTableName"
        val dropTableStatement = "DROP TABLE $hashedTableName"

        val transactionCmd = listOf(createTableStatement!!, insertStatement, dropTableStatement)

        if (execTransaction(transactionCmd)) {
            Log.e("unHashDb", "Failed when executing a transaction for '$intactTableName' ($hashedTableName). Transaction: $transactionCmd")
            continue
        }
    }
}

suspend fun AppDatabase.initDatabase(defaultUserId: Int) = safelyUse {
    // TODO 国服实装水怜专武后就删除该代码片段
    // 修改unit_unique_equipment表为unit_unique_equip
    if (existsTable("unit_unique_equipment")) {
        try {
            execSQL("DROP TABLE unit_unique_equip")
            execSQL("ALTER TABLE unit_unique_equipment RENAME TO unit_unique_equip")
        } catch (_: Throwable) {}
    }
    // TODO 国服实装270专武上限后就删除该代码片段
    // 修改unique_equip_enhance_rate表为unique_equipment_enhance_rate
    if (existsTable("unique_equip_enhance_rate")) {
        try {
            execSQL("DROP TABLE unique_equipment_enhance_rate")
            execSQL("ALTER TABLE unique_equip_enhance_rate RENAME TO unique_equipment_enhance_rate")
        } catch (_: Throwable) {}
    }
    if (!existsColumn("unique_equipment_enhance_rate", "min_lv")) {
        try {
            // 添加列 min_lv
            execSQL("ALTER TABLE unique_equipment_enhance_rate ADD COLUMN min_lv INTEGER NOT NULL DEFAULT 2")
        } catch (_: Throwable) {}
    }
    if (!existsColumn("unique_equipment_enhance_rate", "max_lv")) {
        try {
            // 添加列 max_lv
            execSQL("ALTER TABLE unique_equipment_enhance_rate ADD COLUMN max_lv INTEGER NOT NULL DEFAULT -1")
        } catch (_: Throwable) {}
    }
    if (!existsColumn("skill_data", "boss_ub_cool_time")) {
        try {
            // 添加列 boss_ub_cool_time
            execSQL("ALTER TABLE skill_data ADD COLUMN boss_ub_cool_time REAL NOT NULL DEFAULT 0.0")
        } catch (_: Throwable) {}
    }
    listOf(
        "action_8",
        "action_9",
        "action_10",
        "depend_action_8",
        "depend_action_9",
        "depend_action_10"
    ).forEach { columnName ->
        if (!existsColumn("skill_data", columnName)) {
            try {
                // 添加列 action_8-10,depend_action_8-10
                execSQL("ALTER TABLE skill_data ADD COLUMN $columnName INTEGER NOT NULL DEFAULT 0")
            } catch (_: Throwable) {}
        }
    }
    // TODO 国服实装限制tp上升最高上限后就删除该代码片段
    listOf(
        "sp_union_burst",
        "sp_skill_evolution_1",
        "sp_skill_evolution_2"
    ).forEach { columnName ->
        if (!existsColumn("unit_skill_data", columnName)) {
            try {
                // 添加列 sp...
                execSQL("ALTER TABLE unit_skill_data ADD COLUMN $columnName INTEGER NOT NULL DEFAULT 0")
            } catch (_: Throwable) {}
        }
    }
    // TODO 雪菲专武实装的话后就删除该代码片段
    val hasUnique1 = rawQuery(
        "SELECT equip_id FROM unit_unique_equip WHERE unit_id=106401", null
    ).use { it.moveToFirst() }
    if (!hasUnique1) {
        try {
            // 删除错误的雪菲专武
            execSQL("UPDATE unit_skill_data SET main_skill_evolution_1=0 WHERE unit_id=106401")
        } catch (_: Throwable) {}
    }

    execSQL(
"""CREATE TABLE `chara_data`(
'unit_id' INTEGER NOT NULL,
'unit_name' TEXT NOT NULL,
'kana' TEXT NOT NULL,
'actual_name' TEXT NOT NULL,
'max_rarity' INTEGER NOT NULL,
'equip1_id' INTEGER NOT NULL,
'equip2_id' INTEGER NOT NULL,
'search_area_width' INTEGER NOT NULL,
'atk_type' INTEGER NOT NULL,
'normal_atk_cast_time' REAL NOT NULL,
'comment' TEXT NOT NULL,
'start_time' TEXT NOT NULL,
'age' TEXT NOT NULL,
'guild' TEXT NOT NULL,
'race' TEXT NOT NULL,
'height' TEXT NOT NULL,
'weight' TEXT NOT NULL,
'birth_month' TEXT NOT NULL,
'birth_day' TEXT NOT NULL,
'blood_type' TEXT NOT NULL,
'favorite' TEXT NOT NULL,
'voice' TEXT NOT NULL,
'catch_copy' TEXT NOT NULL,
'self_text' TEXT NOT NULL,
PRIMARY KEY('unit_id')
)"""
    )

    execSQL(
"""INSERT INTO `chara_data`
SELECT ud.unit_id,ud.unit_name,kana,IFNULL(aub.unit_name,ud.kana) AS actual_name,
max_rarity,IFNULL(uue1.equip_id, 0) AS equip1_id,IFNULL(uue2.equip_id, 0) AS equip2_id,
search_area_width,atk_type,normal_atk_cast_time,comment,start_time,
age,guild,race,height,weight,birth_month,birth_day,blood_type,favorite,voice,catch_copy,self_text
FROM unit_data AS ud JOIN unit_profile AS up ON ud.unit_id=up.unit_id
LEFT JOIN actual_unit_background AS aub ON SUBSTR(ud.unit_id,1,4)=SUBSTR(aub.unit_id,1,4)
LEFT JOIN (SELECT unit_id,COUNT(unit_id) AS max_rarity FROM unit_rarity GROUP BY unit_id) AS ur ON ud.unit_id=ur.unit_id
LEFT JOIN unit_unique_equip AS uue1 ON ud.unit_id=uue1.unit_id AND uue1.equip_slot=1
LEFT JOIN unit_unique_equip AS uue2 ON ud.unit_id=uue2.unit_id AND uue2.equip_slot=2
WHERE comment!='' AND ud.unit_id<400000"""
    )

    execSQL(
"""CREATE TABLE `max_data`(
'id' INTEGER NOT NULL,
'max_chara_level' INTEGER NOT NULL,
'max_promotion_level' INTEGER NOT NULL,
'max_unique_level' INTEGER NOT NULL,
'max_area' INTEGER NOT NULL,
'max_chara' INTEGER NOT NULL,
'max_unique' INTEGER NOT NULL,
'max_rarity_6' INTEGER NOT NULL,
PRIMARY KEY('id')
)"""
    )

    execSQL(
"""INSERT INTO `max_data`
SELECT id,max_chara_level,max_promotion_level,max_unique_level,max_area,max_chara,max_unique,max_rarity_6
FROM (SELECT 0 AS id,MAX(team_level)-1 AS max_chara_level FROM experience_team)
LEFT JOIN (SELECT MAX(promotion_level) AS max_promotion_level FROM unit_promotion)
LEFT JOIN (SELECT MAX(enhance_level) AS max_unique_level FROM unique_equipment_enhance_data)
LEFT JOIN (SELECT MAX(area_id)-11000 AS max_area FROM quest_data WHERE area_id<12000 AND reward_image_1!=0)
LEFT JOIN (SELECT COUNT(*) AS max_chara FROM chara_data)
LEFT JOIN (SELECT COUNT(*) AS max_unique FROM chara_data WHERE equip1_id!=0)
LEFT JOIN (SELECT COUNT(*) AS max_rarity_6 FROM chara_data WHERE max_rarity=6)"""
    )

    execSQL(
"""CREATE TABLE `user_data`(
'user_id' INTEGER NOT NULL,
'unit_id' INTEGER NOT NULL,
'rarity' INTEGER NOT NULL,
'chara_level' INTEGER NOT NULL,
'love_level' INTEGER NOT NULL,
'unique1_level' INTEGER NOT NULL,
'unique2_level' INTEGER NOT NULL,
'promotion_level' INTEGER NOT NULL,
'ub_level' INTEGER NOT NULL,
'skill1_level' INTEGER NOT NULL,
'skill2_level' INTEGER NOT NULL,
'ex_level' INTEGER NOT NULL,
'equip1_level' INTEGER NOT NULL,
'equip2_level' INTEGER NOT NULL,
'equip3_level' INTEGER NOT NULL,
'equip4_level' INTEGER NOT NULL,
'equip5_level' INTEGER NOT NULL,
'equip6_level' INTEGER NOT NULL,
'ex_equip1' INTEGER NOT NULL,
'ex_equip2' INTEGER NOT NULL,
'ex_equip3' INTEGER NOT NULL,
'ex_equip1_level' INTEGER NOT NULL,
'ex_equip2_level' INTEGER NOT NULL,
'ex_equip3_level' INTEGER NOT NULL,
PRIMARY KEY('user_id','unit_id')
)"""
    )

    execSQL(
"""INSERT INTO `user_data`
SELECT $defaultUserId AS user_id,unit_id,max_rarity AS rarity,max_chara_level AS chara_level,
CASE(max_rarity) WHEN 6 THEN 12 ELSE 8 END AS love_level,
CASE(equip1_id) WHEN 0 THEN 0 ELSE max_unique_level END AS unique1_level,
CASE(equip2_id) WHEN 0 THEN -1 ELSE 5 END AS unique2_level,
max_promotion_level AS promotion_level,
max_chara_level AS ub_level,max_chara_level AS skill1_level,max_chara_level AS skill2_level,max_chara_level AS ex_level,
5 AS equip1_level,5 AS equip2_level,5 AS equip3_level,5 AS equip4_level,5 AS equip5_level,5 AS equip6_level,
0 AS ex_equip1,0 AS ex_equip2,0 AS ex_equip3,-1 AS ex_equip1_level,-1 AS ex_equip2_level,-1 AS ex_equip3_level
FROM chara_data LEFT JOIN max_data"""
    )
}

suspend fun AppDatabase.initQuestDropData() {
    withIOContext {
        val list = listOf(
//            async { getQuestDataList(QuestRange.N) },
            async { getQuestDataList(QuestRange.H) },
            async { getQuestDataList(QuestRange.VH) }
//            async {
//                use {
//                    rawQuery(
//                        "SELECT equipment_id FROM equipment_data WHERE equipment_id<110000",
//                        null
//                    ).use {
//                        val set = mutableSetOf<Int>()
//
//                        while (it.moveToNext()) {
//                            val equipId = it.getInt(0)
//                            set.add(Helper.getEquipRarity(equipId))
//                        }
//
//                        set.sortedBy { o -> o }
//                    }
//                }
//            }
        ).awaitAll()
//        @Suppress("UNCHECKED_CAST")
//        val nQuestList = list[0] as List<QuestData>
//        @Suppress("UNCHECKED_CAST")
        val hQuestList = list[0]// as List<QuestData>
//        @Suppress("UNCHECKED_CAST")
        val vhQuestList = list[1]// as List<QuestData>
//        @Suppress("UNCHECKED_CAST")
//        val equipRarityList = list[3] as List<Int>

        val pieceSet = mutableSetOf<Int>()
        hQuestList.forEach { item ->
            pieceSet.add(item.rewardImageList[0])
        }

        var memoryPieceSql = "REPLACE INTO `memory_piece`\nSELECT ${pieceSet.elementAt(0)}"
        val pieceSize = pieceSet.size
        var i = 1

        while (i < pieceSize) {
            memoryPieceSql += "\nUNION SELECT ${pieceSet.elementAt(i++)}"
        }

        vhQuestList.forEach { item ->
            val rewardImage = item.rewardImageList[0]
            if (rewardImage > 0) {
                memoryPieceSql += "\nUNION SELECT $rewardImage"
            }
        }
/*
        val dropRangeList = mutableListOf<Pair<Int, QuestRange>>()
        val pairs = arrayOf(
            QuestType.N.value to nQuestList,
            QuestType.H.value to hQuestList,
            QuestType.VH.value to vhQuestList
        )

        pairs.forEach { pair ->
            val type = pair.first
            val questList = pair.second
            equipRarityList.forEach { rarity ->
                val rarityStr = rarity.toString()
                var rangeMin = 0
                var rangeMax = 0
                questList.forEach { item ->
                    if (item.getDropList().any { rewardData ->
                        Helper.getEquipRarityString(rewardData.rewardId) == rarityStr
                    }) {
                        val questId = item.questId
                        if (rangeMin == 0) {
                            rangeMin = questId
                            rangeMax = questId
                        }
                        rangeMin = min(rangeMin, questId)
                        rangeMax = max(rangeMax, questId)
                    }
                }
                if (rangeMin != 0) {
                    dropRangeList.add((type + rarity) to QuestRange(rangeMin, rangeMax))
                }
            }
        }

        val toValues: (Pair<Int, QuestRange>) -> String = { pair ->
            "${pair.first},${pair.second.min},${pair.second.max}"
        }

        var dropRangeSql = "REPLACE INTO `drop_range`\nSELECT ${toValues(dropRangeList[0])}"
        val dropRangeSize = dropRangeList.size
        i = 1

        while (i < dropRangeSize) {
            dropRangeSql += "\nUNION SELECT ${toValues(dropRangeList[i++])}"
        }
*/
        use {
            execSQL("CREATE TABLE `memory_piece`('id' INTEGER NOT NULL,PRIMARY KEY('id'))")
            execSQL(memoryPieceSql)

//            execSQL(
//                """CREATE TABLE `drop_range`(
//'range_id' INTEGER NOT NULL,
//'range_min' INTEGER NOT NULL,
//'range_max' INTEGER NOT NULL,
//PRIMARY KEY('range_id')
//)"""
//            )
//            execSQL(dropRangeSql)
        }
    }
}
