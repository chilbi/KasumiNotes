package com.kasuminotes.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.lang.Exception
import java.util.concurrent.atomic.AtomicInteger

class AppDatabase(
    context: Context,
    name: String
) : SQLiteOpenHelper(context, name, null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

    private val counter = AtomicInteger()
    private var db: SQLiteDatabase? = null

    @Synchronized
    private fun openDatabase(): SQLiteDatabase {
        if (counter.incrementAndGet() == 1) {
            db = writableDatabase
        }
        return db!!
    }

    @Synchronized
    private fun closeDatabase() {
        if (counter.decrementAndGet() == 0) {
            db?.close()
        }
    }

    fun <T> useDatabase(block: SQLiteDatabase.() -> T): T {
        try {
            return openDatabase().block()
        } finally {
            closeDatabase()
        }
    }

    companion object {
        private var currentName = ""
        private var instance: AppDatabase? = null

        @Synchronized
        fun getInstance(context: Context, name: String): AppDatabase {
            return if (name == currentName) {
                instance ?: AppDatabase(context, name).also { instance = it }
            } else {
                instance?.close()
                currentName = name
                AppDatabase(context, name).also { instance = it }
            }
        }

        @Synchronized
        fun getInstance(context: Context): AppDatabase {
            if (currentName == "") throw Exception("The database is not uninitialized")
            return getInstance(context, currentName)
        }
    }
}
