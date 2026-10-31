package com.kasuminotes

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.kasuminotes.ui.app.errorScreen.SQLiteErrorHandler
import java.io.File

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        context = this
        SQLiteErrorHandler.init()
        cleanAllApks()
    }

    private fun cleanAllApks() {
        val root = getExternalFilesDir(null) ?: return
        root.walkTopDown()
            .filter { it.isFile && it.extension.equals("apk", ignoreCase = true) }
            .forEach { runCatching { it.delete() } }
        root.walkBottomUp()
            .filter { it != root && it.isDirectory && it.listFiles()?.isEmpty() == true }
            .forEach { runCatching { it.delete() } }
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
            private set

        var strings:
                Map<String/*CN|JP*/,
                        Map<String/*mark|abnormal|summon*/,
                                Map<String/*id*/,
                                        String>>>? = null
    }
}
