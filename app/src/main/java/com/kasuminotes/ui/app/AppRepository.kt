package com.kasuminotes.ui.app

import android.content.Context
import androidx.compose.ui.state.ToggleableState
import com.kasuminotes.MainActivity
import com.kasuminotes.MainApplication
import com.kasuminotes.common.DbServer
import com.kasuminotes.common.ImageVariant
import com.kasuminotes.common.Language
import com.kasuminotes.common.QuestMode
import com.kasuminotes.common.QuestType
import com.kasuminotes.db.AppDatabase
import com.kasuminotes.utils.HttpUtil
//import com.kasuminotes.utils.LocaleUtil
import com.kasuminotes.utils.UrlUtil
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.io.File
import java.util.Locale

class AppRepository(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val context: Context = MainApplication.context
) {
    val applicationContext: Context get() = context.applicationContext

    fun getUserId() = context.userIdSP

    fun setUserId(value: Int) {
        context.userIdSP = value
    }

    fun getDbAutoUpdate() = context.dbAutoUpdateSP

    fun setDbAutoUpdate(dbAutoUpdate: Boolean) {
        context.dbAutoUpdateSP = dbAutoUpdate
    }

    fun getDbServer() = DbServer.valueOf(context.dbServerSP)

    fun setDbServer(server: DbServer) {
        context.dbServerSP = server.name
    }

    fun getDbVersion(server: DbServer) =
        when (server) {
            DbServer.CN -> context.dbVersionCNSP
            DbServer.JP -> context.dbVersionJPSP
        }

    fun setDbVersion(server: DbServer, version: String) {
        when (server) {
            DbServer.CN -> context.dbVersionCNSP = version
            DbServer.JP -> context.dbVersionJPSP = version
        }
    }

    fun getImageVariant() = ImageVariant.valueOf(context.imageVariantSP)

    fun setImageVariant(variant: ImageVariant) {
        context.imageVariantSP = variant.name
    }

    fun getLanguage() = when (context.languageSP) {
        Locale.JAPANESE.language -> Language.JP
        else -> Language.CN
    }

    fun setLanguage(value: Language) {
        val language = when (value) {
            Language.CN -> Locale.CHINESE.language
            Language.JP -> Locale.JAPANESE.language
        }
        context.languageSP = language
//        LocaleUtil.setLocale(context, language)
        MainActivity.instance.reload()
    }

    fun getDarkTheme() = ToggleableState.valueOf(context.darkThemeSP)

    fun setDarkTheme(state: ToggleableState) {
        context.darkThemeSP = state.name
    }

    fun getQuestMode() = QuestMode.valueOf(context.questModeSP)

    fun setQuestMode(mode: QuestMode) {
        context.questModeSP = mode.name
    }

    fun getQuestMapType() = QuestType.valueOf(context.questMapTypeSP)

    fun setQuestMapType(type: QuestType) {
        context.questMapTypeSP = type.name
    }

    fun getQuestMapArea() = context.questMapAreaSP

    fun setQuestMapArea(value: Int) {
        context.questMapAreaSP = value
    }

    fun getQuestSearchId() = context.questSearchIdSP

    fun setQuestSearchId(value: Int) {
        context.questSearchIdSP = value
    }

    fun getQuestSearchSet(): List<Pair<Int, Array<Int>>> {
        return context.questSearchSetSP.split(";").map { item ->
           val numbs = item.split(",").map { n -> n.toInt() }
            val id = numbs[0]
            val searches = arrayOf(numbs[1], numbs[2], numbs[3])
            id to searches
        }
    }

    fun setQuestSearchSet(set: List<Pair<Int, Array<Int>>>) {
        context.questSearchSetSP = set.joinToString(";") { item ->
            val id = item.first
            val searches = item.second
            "$id," + searches.joinToString(",")
        }
    }

    fun getDbFile(server: DbServer): File = context.getDatabasePath(UrlUtil.dbFileNameMap[server]!!)

    fun getDatabase(name: String) =
        AppDatabase.getInstance(context.applicationContext, name, ioDispatcher)

    fun getDatabase() = AppDatabase.getInstance(context.applicationContext, ioDispatcher)

    suspend fun fetchLastDbVersion(server: DbServer): String = withContext(ioDispatcher) {
        HttpUtil.fetchLastDbVersion(UrlUtil.lastVersionUrl[server]!!)
    }

    fun downloadTempDbFile(server: DbServer) = HttpUtil.downloadDbFile(
        UrlUtil.dbFileUrlMap[server]!!,
        context.getDatabasePath("temp_${UrlUtil.dbFileNameMap[server]!!}.br")
    ).flowOn(ioDispatcher)
}
