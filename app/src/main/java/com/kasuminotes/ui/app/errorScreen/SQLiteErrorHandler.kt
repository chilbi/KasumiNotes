package com.kasuminotes.ui.app.errorScreen

import android.database.sqlite.SQLiteException
import android.os.Handler
import android.os.Looper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object SQLiteErrorHandler {
    private val _errorState = MutableStateFlow<Throwable?>(null)
    val errorState: StateFlow<Throwable?> = _errorState

    private var isHandling = false

    fun init() {
        val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()

        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            if (throwable is SQLiteException || throwable.cause is SQLiteException) {
                if (isHandling) {
                    defaultHandler?.uncaughtException(thread, throwable)
                    return@setDefaultUncaughtExceptionHandler
                }

                isHandling = true

                Handler(Looper.getMainLooper()).post {
                    _errorState.value = throwable
                    isHandling = false
                }
            } else {
                defaultHandler?.uncaughtException(thread, throwable)
            }
        }
    }

    fun clearError() {
        _errorState.value = null
    }
}
