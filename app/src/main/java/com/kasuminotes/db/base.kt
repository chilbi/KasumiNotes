package com.kasuminotes.db

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import com.kasuminotes.data.MaxUserData
import com.kasuminotes.data.SummonData
import com.kasuminotes.data.User
import com.kasuminotes.data.UserData
import com.kasuminotes.ui.app.DefaultUserId

fun AppDatabase.putUserData(userData: UserData) {
    useDatabase {
        execSQL("REPLACE INTO `user_data` VALUES (${userData.stringValues})")
    }
}

fun AppDatabase.putUserDataList(userDataList: List<UserData>) {
    if (userDataList.isEmpty()) return

    var sql = "REPLACE INTO `user_data`\nSELECT ${userDataList[0].stringValues}"
    val len = userDataList.size
    var i = 1

    while (i < len) {
        sql += "\nUNION SELECT ${userDataList[i].stringValues}"
        i++
    }

    useDatabase {
        execSQL(sql)
    }
}

fun AppDatabase.deleteUser(userId: Int) {
    useDatabase {
        execSQL("DELETE FROM user_data WHERE user_id=$userId")
    }
}

fun AppDatabase.deleteUserData(userId: Int, deleteChara: List<Int>) {
    if (userId == DefaultUserId || deleteChara.isEmpty()) return

    val sql = """DELETE FROM user_data
WHERE user_id=$userId AND unit_id IN (${deleteChara.joinToString(",")})"""

    useDatabase {
        execSQL(sql)
    }
}

fun AppDatabase.getMaxUserData(userId: Int): MaxUserData {
    val sql = """SELECT max_chara_level,max_promotion_level,max_unique_level,
max_area,max_chara,max_unique,max_rarity_6,user_chara,user_unique,user_rarity_6
FROM max_data
LEFT JOIN (SELECT COUNT(user_id) AS user_chara FROM user_data WHERE user_id=$userId)
LEFT JOIN (SELECT COUNT(user_id) AS user_unique FROM user_data WHERE user_id=$userId AND unique1_level>0)
LEFT JOIN (SELECT COUNT(user_id) AS user_rarity_6 FROM user_data WHERE user_id=$userId AND rarity=6)"""

    return useDatabase {
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

fun AppDatabase.getUserName(userId: Int): String {
    val unitId = userId / 100 * 100 + 1
    val sql = "SELECT actual_name FROM chara_data WHERE unit_id=$unitId"

    return useDatabase {
        rawQuery(sql, null).use {
            if (it.moveToFirst()) {
                it.getString(0)
            } else {
                "NULL"
            }
        }
    }
}

fun AppDatabase.getAllUser(): List<Int> {
    val sql = "SELECT user_id FROM user_data GROUP BY user_id"

    return useDatabase {
        rawQuery(sql, null).use {
            val list = mutableListOf<Int>()

            while (it.moveToNext()) {
                list.add(it.getInt(0))
            }

            list
        }
    }
}

fun AppDatabase.getUserList(): List<User> {
    val sql = """SELECT user_id,actual_name,user_chara
FROM (SELECT user_id,COUNT(user_id) AS user_chara FROM user_data GROUP BY user_id) AS ud
LEFT JOIN chara_data AS cd ON SUBSTR(ud.user_id,1,4)=SUBSTR(cd.unit_id,1,4)"""

    return useDatabase {
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

fun AppDatabase.getSummonData(unitId: Int): SummonData {
    val sql = "SELECT ${SummonData.getFields()} FROM unit_data WHERE unit_id=$unitId"

    return useDatabase {
        rawQuery(sql, null).use {
            it.moveToFirst()
            SummonData(
                unitId,
                it.getString(0),
                it.getInt(1),
                it.getInt(2),
                it.getFloat(3)
            )
        }
    }
}

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

fun AppDatabase.existsTable(tableName: String): Boolean {
    val sql = "SELECT count(*) FROM sqlite_master WHERE type=\"table\" AND name=\"$tableName\""
    return useDatabase {
        rawQuery(sql, null).use {
            it.moveToFirst()
            it.getInt(0) > 0
        }
    }
}

fun AppDatabase.existsTables(tableNames: List<String>): Boolean {
    return useDatabase {
        tableNames.all { tableName ->
            val sql = "SELECT count(*) FROM sqlite_master WHERE type=\"table\" AND name=\"$tableName\""
            rawQuery(sql, null).use {
                it.moveToFirst()
                it.getInt(0) > 0
            }
        }
    }
}

fun AppDatabase.existsColumn(tableName: String, columnName: String): Boolean {
    val sql = "SELECT * FROM sqlite_master WHERE name=\"$tableName\" AND sql LIKE \"%$columnName%\""
    return useDatabase {
        rawQuery(sql, null).use {
            it.moveToFirst()
        }
    }
}

fun SQLiteDatabase.renameColumn(tableName: String, oldName: String, newName: String) {

    val escapedTable = tableName.escapeSqlIdentifier()
    val escapedOld = oldName.escapeSqlIdentifier()
    val escapedNew = newName.escapeSqlIdentifier()

    try {
        execSQL("ALTER TABLE $escapedTable RENAME COLUMN $escapedOld TO $escapedNew")
        return
    } catch (e: SQLiteException) {
        if (!e.message.orEmpty().contains("syntax error", ignoreCase = true)) {
            throw e
        }
    }

    try {
        beginTransaction()

        val cursor = rawQuery("PRAGMA table_info($tableName)", null)
        val columns = mutableListOf<String>()
        val columnNames = mutableListOf<String>()
        while (cursor.moveToNext()) {
            val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
            val type = cursor.getString(cursor.getColumnIndexOrThrow("type"))
            val notNull = cursor.getInt(cursor.getColumnIndexOrThrow("notnull")) == 1
            val defaultValue = cursor.getString(cursor.getColumnIndexOrThrow("dflt_value"))
            val pk = cursor.getInt(cursor.getColumnIndexOrThrow("pk")) == 1

            val columnDef = buildString {
                append(if (name == escapedOld.removeSurrounding("`")) escapedNew else name.escapeSqlIdentifier())
                append(" $type")
                if (notNull) append(" NOT NULL")
                if (defaultValue != null) append(" DEFAULT $defaultValue")
                if (pk) append(" PRIMARY KEY")
            }
            columns.add(columnDef)
            columnNames.add(if (name == escapedOld.removeSurrounding("`")) escapedNew else name.escapeSqlIdentifier())
        }
        cursor.close()

        val tempTable = "${tableName.removeSurrounding("`")}_temp".escapeSqlIdentifier()
        execSQL("CREATE TABLE $tempTable (${columns.joinToString(", ")})")

        val selectColumns = columnNames.joinToString(", ") { name ->
            if (name == escapedNew) escapedOld else name
        }
        val insertColumns = columnNames.joinToString(", ")
        execSQL("INSERT INTO $tempTable ($insertColumns) SELECT $selectColumns FROM $tableName")

        execSQL("DROP TABLE $tableName")

        execSQL("ALTER TABLE $tempTable RENAME TO $tableName")

        setTransactionSuccessful()
    } finally {
        endTransaction()
    }
}

private fun String.escapeSqlIdentifier(): String {
    if (startsWith("`") && endsWith("`")) return this
    return "`$this`"
}
