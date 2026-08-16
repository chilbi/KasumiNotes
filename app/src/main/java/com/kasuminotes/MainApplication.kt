package com.kasuminotes

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.kasuminotes.ui.app.errorScreen.SQLiteErrorHandler

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        context = this
        SQLiteErrorHandler.init()
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
