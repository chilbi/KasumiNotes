package com.kasuminotes.db

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import com.kasuminotes.common.DbServer
import com.kasuminotes.ui.app.AppRepository

object DatabaseTableCopier {
    // 从EN数据库复制表到目标数据库
    fun copyTablesFromEN(server: DbServer, appRepository: AppRepository) {
        if (server == DbServer.EN) {
            return
        }
        val enDbFile = appRepository.getDbFile(DbServer.EN)
        if (!enDbFile.exists()) {
            return
        }
        val targetDbFile = appRepository.getDbFile(server)
        if (!targetDbFile.exists()) {
            return
        }
        val sourceDbPath = enDbFile.path
        val targetDbPath = targetDbFile.path
        val commonTables = listOf(
            //额外效果
            "extra_effect_data",
            //EX装备副属性
            "ex_equipment_sub_status",
            "ex_equipment_sub_status_group",
            //地下城
            "dungeon_area",
            "dungeon_quest_data",
            "dungeon_special_battle",
            "dungeon_pattern_battle",
            "dungeon_area_data",
            //深域
            "talent_quest_area_data",
            "talent_quest_data",
            "talent_quest_wave_group_data",
            "talent_quest_enemy_parameter",
            //深渊讨伐战
            "abyss_schedule",
            "abyss_quest_data",
            "abyss_boss_data",
            "abyss_wave_group_data",
            "abyss_enemy_parameter",
            //追忆的战域
            "mirage_floor_quest_display",
            "mirage_nemesis_quest_display",
            "mirage_floor_quest",
            "mirage_nemesis_quest",
            "mirage_wave_group_data",
            "mirage_enemy_parameter",
            //属性等级
            "experience_talent_level",
            //属性技能
            "talent_skill_node",
            "talent_skill_enhance_level",
            "talent_skill_enhance_data",
            //大师技能
            "team_skill_node",
            "team_skill_enhance_level",
            "team_skill_enhance_data",
            //职能精通
            "unit_role_mastery_id",
            "unit_role_mastery_slot_data",
            "unit_role_mastery_item_data",
            "unit_role_mastery_level",
            "unit_role_mastery_enhance_data"
        )
        val connectRankTables = listOf(
            //连结RANK
            "connect_rank_chart",
            "connect_rank_bonus",
            "connect_rank_status",
            //专1SP
            "ex_unique_equipment_1"
        )
        if (server == DbServer.JP) {
            val allTables = commonTables.plus(connectRankTables)
            copyTables(sourceDbPath, targetDbPath, allTables)
        } else if (server == DbServer.CN) {
            copyTables(sourceDbPath, targetDbPath, commonTables)
            //ネフィ＝ネラ（鬼面仏心）id:138201卡池实装连结RANK
            if (existsChara(138201, targetDbPath)) {
                copyTables(sourceDbPath, targetDbPath, connectRankTables)
            }
        }
    }

    private fun copyTables(
        sourceDbPath: String,
        targetDbPath: String,
        tablesToCopy: List<String>
    ): Boolean {
        var db: SQLiteDatabase? = null
        try {
            db = SQLiteDatabase.openDatabase(
                targetDbPath,
                null,
                SQLiteDatabase.OPEN_READWRITE
            )
            db.beginTransaction()
            db.execSQL("ATTACH DATABASE ? AS source", arrayOf(sourceDbPath))

            for (tableName in tablesToCopy) {
                if (!existsTable(db, "source", tableName)) {
                    continue
                }
                if (existsTable(db, "main", tableName)) {
                    continue
                }
                val createTableSql = getCreateTableStatement(db, tableName)
                if (createTableSql != null) {
                    db.execSQL(createTableSql)
                    copyTableData(db, tableName)
                    copyIndexes(db, tableName)
                } else {
                    throw SQLiteException("Cannot find create statement for table: $tableName")
                }
            }

            db.setTransactionSuccessful()
            return true
        } catch (_: SQLiteException) {
            return false
        } finally {
            db?.let {
                it.endTransaction()
                it.execSQL("DETACH DATABASE source")
                it.close()
            }
        }
    }

    private fun copyTableData(db: SQLiteDatabase, tableName: String) {
        val copySql = "INSERT INTO $tableName SELECT * FROM source.$tableName"
        db.execSQL(copySql)
    }

    private fun copyIndexes(db: SQLiteDatabase, tableName: String) {
        val query = "SELECT sql FROM source.sqlite_master WHERE type='index' AND tbl_name=? AND sql IS NOT NULL"
        db.rawQuery(query, arrayOf(tableName)).use { cursor ->
            while (cursor.moveToNext()) {
                val createIndexSql = cursor.getString(0)
                db.execSQL(createIndexSql)
            }
        }
    }

    private fun existsTable(db: SQLiteDatabase, database: String, tableName: String): Boolean {
        val query = "SELECT name FROM ${database}.sqlite_master WHERE type='table' AND name=?"
        db.rawQuery(query, arrayOf(tableName)).use { cursor ->
            return cursor.count > 0
        }
    }

    private fun getCreateTableStatement(db: SQLiteDatabase, tableName: String): String? {
        val query = "SELECT sql FROM source.sqlite_master WHERE type='table' AND name=?"
        db.rawQuery(query, arrayOf(tableName)).use { cursor ->
            return if (cursor.moveToFirst()) {
                cursor.getString(0)
            } else {
                null
            }
        }
    }

    private fun existsChara(unitId: Int, dbPath: String): Boolean {
        var db: SQLiteDatabase? = null
        try {
            db = SQLiteDatabase.openDatabase(
                dbPath,
                null,
                SQLiteDatabase.OPEN_READONLY
            )
            val sql = "SELECT unit_id FROM unit_data WHERE unit_id=$unitId"
            db.rawQuery(sql, null).use { cursor ->
                return cursor.count > 0
            }
        } catch (_: SQLiteException) {
            return false
        } finally {
            db?.close()
        }
    }
}
