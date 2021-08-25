package com.kasuminotes.utils

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import androidx.annotation.RequiresApi
import com.kasuminotes.ui.app.languageSP
import java.util.Locale

object LocaleUtil {
    fun attachBaseContext(context: Context): Context {
        return updateResources(context, context.languageSP)
//        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { } else { context }
    }

//    fun setLocale(context: Context, language: String) {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
//            updateResources(context, language)
//        }
//    }

    private fun updateResources(context: Context, language: String): Context {
        val res = context.resources
        val config = res.configuration
        config.fontScale = 1f
        val locale = if (language.isEmpty()) {
            getSystemLocale(config)
        } else {
            Locale(language)
        }
        Locale.setDefault(locale)
        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> {
                setLocaleForApi24(config, locale)
                context.createConfigurationContext(config)
            }
            else -> {// SDK_INT is always >= 21
                config.setLocale(locale)
                context.createConfigurationContext(config)
            }
//            Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 -> {
//                config.setLocale(locale)
//                context.createConfigurationContext(config)
//            }
//            else -> {
//                config.locale = locale
//                res.updateConfiguration(config, res.displayMetrics)
//                context
//            }
        }
    }

    private fun getSystemLocale(config: Configuration): Locale {
        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> {
                config.locales[0]
            }
            else -> {// SDK_INT is always >= 21
                Locale.getDefault()
            }
//            Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 -> {
//                Locale.getDefault()
//            }
//            else -> {
//                config.locale
//            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun setLocaleForApi24(config: Configuration, locale: Locale) {
        val localeSet = linkedSetOf(locale)
        val localeList: LocaleList = LocaleList.getDefault()
        val size = localeList.size()
        var i = 0
        while (i < size) {
            localeSet.add(localeList.get(i))
            i++
        }
        val locales = localeSet.toTypedArray()
        config.setLocales(LocaleList(*locales))
    }
}
