package com.kasuminotes.ui.app

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.ui.state.ToggleableState
import com.kasuminotes.common.DbServer
import com.kasuminotes.common.ImageVariant
import com.kasuminotes.common.QuestMode
import com.kasuminotes.common.QuestType
import kotlin.reflect.KProperty

val Context.AppSP: SharedPreferences
    get() = getSharedPreferences("APP_SP", Context.MODE_PRIVATE)

private class SP<T>(val key: String, val defValue: T) {
    @Suppress("UNCHECKED_CAST")
    operator fun getValue(thisRef: Context, property: KProperty<*>): T =
        when (defValue) {
            is String -> thisRef.AppSP.getString(key, defValue)
            is Int -> thisRef.AppSP.getInt(key, defValue)
            is Boolean -> thisRef.AppSP.getBoolean(key, defValue)
            else -> throw TypeCastException("${defValue!!::class.simpleName} is not implemented")
        } as T

    operator fun setValue(thisRef: Context, property: KProperty<*>, value: T) {
        with(thisRef.AppSP.edit()) {
            when (value) {
                is String -> putString(key, value)
                is Int -> putInt(key, value)
                is Boolean -> putBoolean(key, value)
                else -> throw TypeCastException("${value!!::class.simpleName} is not implemented")
            }
            apply()
        }
    }
}

const val DefaultUserId = 101431

var Context.userIdSP by SP("USER_ID", DefaultUserId)
var Context.dbAutoUpdateSP by SP("DB_AUTO_UPDATE", false)
var Context.dbServerSP by SP("DB_SERVER", DbServer.CN.name)
var Context.dbVersionCNSP by SP("DB_VERSION_CN", "0")
var Context.dbVersionJPSP by SP("DB_VERSION_JP", "0")
var Context.imageVariantSP by SP("IMAGE_VARIANT", ImageVariant.Icon.name)
var Context.darkThemeSP by SP("DARK_THEME", ToggleableState.Indeterminate.name)
var Context.languageSP by SP("LANGUAGE", "")
var Context.questModeSP by SP("QUEST_MODE", QuestMode.Equip.name)
var Context.questMapTypeSP by SP("QUEST_MAP_TYPE", QuestType.N.name)
var Context.questMapAreaSP by SP("QUEST_MAP_AREA", 0)
var Context.questSearchIdSP by SP("QUEST_SEARCH_ID", 1)
var Context.questSearchSetSP by SP("QUEST_SEARCH_SET", "1,0,0,0")
