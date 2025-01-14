package com.kasuminotes.ui.app

import android.app.DownloadManager
import android.content.Context
import androidx.compose.ui.state.ToggleableState
import com.kasuminotes.MainActivity
import com.kasuminotes.MainApplication
import com.kasuminotes.common.DbServer
import com.kasuminotes.common.ImageVariant
import com.kasuminotes.common.Language
import com.kasuminotes.common.QuestMode
import com.kasuminotes.common.QuestType
import com.kasuminotes.data.AppReleaseInfo
import com.kasuminotes.db.AppDatabase
import com.kasuminotes.utils.FileUtil
import com.kasuminotes.utils.HttpUtil
import com.kasuminotes.utils.UrlUtil
import org.json.JSONObject
import java.io.File
import java.util.Locale
import androidx.core.net.toUri

class AppRepository(
    private val context: Context = MainApplication.context,
) {
    val applicationContext: Context get() = context.applicationContext

    fun getUserId() = context.userIdSP

    fun setUserId(value: Int) {
        context.userIdSP = value
    }

    fun getAppAutoUpdate() = context.appAutoUpdateSP

    fun setAppAutoUpdate(appAutoUpdate: Boolean) {
        context.appAutoUpdateSP = appAutoUpdate
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

    fun getThemeIndex() = context.themeIndexSP

    fun setThemeIndex(value: Int) {
        context.themeIndexSP = value
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

    fun getBackupDbFile(server: DbServer): File =
        context.getDatabasePath("backup_" + UrlUtil.dbFileNameMap[server]!!)

    fun getDatabase(name: String) = AppDatabase.getInstance(context.applicationContext, name)

    fun getDatabase() = AppDatabase.getInstance(context.applicationContext)

    fun fetchLastDbVersion(server: DbServer): String = if (UrlUtil.useWtheeDb) {
        HttpUtil.fetchWtheeLastDbVersion(UrlUtil.lastVersionApiUrl, server)
    } else {
        HttpUtil.fetchLastDbVersion(UrlUtil.lastVersionUrl[server]!!)
    }

    fun fetchRainbowJson(): JSONObject = HttpUtil.fetchLastRainbowJson(UrlUtil.RainbowJsonUrl)

    fun fetchLatestAppReleaseInfo(): AppReleaseInfo? =
        HttpUtil.fetchLatestAppReleaseInfo(UrlUtil.APP_RELEASE_URL)

    fun downloadTempDbFile(server: DbServer) = HttpUtil.downloadDbFile(
        UrlUtil.dbFileUrlMap[server]!!,
        context.getDatabasePath("temp_${UrlUtil.dbFileNameMap[server]!!}.br")
    )

    fun downloadApp(info: AppReleaseInfo) {
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val request = DownloadManager.Request(info.downloadURL.toUri()).apply {
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            setMimeType("application/vnd.android.package-archive")
            setDestinationInExternalFilesDir(
                context,
                context.filesDir.absolutePath,
                "kasuminotes-release-${info.versionName}.apk"
            )
        }
        val file =
            File("${context.filesDir.absolutePath}/kasuminotes-release-${info.versionName}.apk")
        if (file.exists()) file.delete()
        val downloadId = downloadManager.enqueue(request)
        MainActivity.instance.listenerDownload(downloadId)
    }

    fun syncStrings() {
        val localStrings = FileUtil.readStrings(context)
        if (localStrings == null) {
            fetchAndSetLatestStrings()
        } else {
            val localJson = JSONObject(localStrings)
            val localVersion = localJson.getString("version")
            val newVersion = HttpUtil.fetchStringsVersion()
            if (newVersion != null && newVersion != localVersion) {
                fetchAndSetLatestStrings()
            } else {
                setStrings(localJson)
            }
        }
    }

    private fun fetchAndSetLatestStrings() {
        val newStrings = HttpUtil.fetchStrings()
        if (newStrings != null) {
            setStrings(JSONObject(newStrings))
            FileUtil.writeStrings(context, newStrings)
        }
    }

    private fun setStrings(jsonObject: JSONObject) {
        val map = mutableMapOf<String, Map<String, Map<String, String>>>()
        val langKeys = jsonObject.keys()
        while (langKeys.hasNext()) {
            val langMap = mutableMapOf<String, Map<String, String>>()
            val langKey = langKeys.next()
            if (langKey == "version") continue
            val langValueJson = jsonObject.getJSONObject(langKey)
            val typeKeys = langValueJson.keys()
            while (typeKeys.hasNext()) {
                val idMap = mutableMapOf<String, String>()
                val typeKey = typeKeys.next()
                val typeValueJson = langValueJson.getJSONObject(typeKey)
                val idKeys = typeValueJson.keys()
                while (idKeys.hasNext()) {
                    val idKey = idKeys.next()
                    val idValue = typeValueJson.getString(idKey)
                    idMap[idKey] = idValue
                }
                langMap[typeKey] = idMap
            }
            map[langKey] = langMap
        }
        MainApplication.strings = map
    }
}
