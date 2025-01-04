package com.kasuminotes

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        context = this
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
