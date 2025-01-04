package com.kasuminotes.utils

import android.content.Context

object FileUtil {
    fun readStrings(context: Context): String? {
        try {
            val inputStream = context.openFileInput("strings.json")
            return inputStream.bufferedReader().use { reader ->
                reader.readText()
            }
        } catch (_: Throwable) {
            return null
        }
    }

    fun writeStrings(context: Context, text: String): Boolean {
        try {
            val outputStream = context.openFileOutput("strings.json", Context.MODE_PRIVATE)
            outputStream.bufferedWriter().use { writer ->
                writer.write(text)
            }
            return true
        } catch (_: Throwable) {
            return false
        }
    }
}
