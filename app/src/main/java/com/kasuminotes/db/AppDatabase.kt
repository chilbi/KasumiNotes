package com.kasuminotes.db

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext
import java.io.Closeable
import java.lang.Exception
import java.util.concurrent.atomic.AtomicInteger

class AppDatabase(
    context: Context,
    name: String,
    private val ioDispatcher: CoroutineDispatcher
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

    fun <T> use(block: SQLiteDatabase.() -> T): T {
        try {
            return openDatabase().block()
        } finally {
            closeDatabase()
        }
    }

    suspend fun <T> withIOContext(block: suspend CoroutineScope.() -> T) = withContext(ioDispatcher, block)

    suspend fun <T> safelyUse(block: SQLiteDatabase.() -> T): T {
        return withIOContext {
            use(block)
        }
    }

    companion object {
        private var currentName = ""
        private var instance: AppDatabase? = null

        const val NullId = 999999

        @Synchronized
        fun getInstance(context: Context, name: String, ioDispatcher: CoroutineDispatcher): AppDatabase {
            return if (name == currentName) {
                instance ?: AppDatabase(context, name, ioDispatcher).also { instance = it }
            } else {
                instance?.close()
                currentName = name
                AppDatabase(context, name, ioDispatcher).also { instance = it }
            }
        }

        @Synchronized
        fun getInstance(context: Context, ioDispatcher: CoroutineDispatcher): AppDatabase {
            if (currentName == "") throw Exception("The database is not uninitialized")
            return getInstance(context, currentName, ioDispatcher)
        }
    }
}
