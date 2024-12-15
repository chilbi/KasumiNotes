package com.kasuminotes.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.kasuminotes.common.DbServer
import com.kasuminotes.common.DownloadState
import com.kasuminotes.data.AppReleaseInfo
import com.kasuminotes.db.AppDatabase
import com.kasuminotes.db.getBackupUserDataList
import com.kasuminotes.db.initDatabase
import com.kasuminotes.db.initQuestDropData
import com.kasuminotes.db.putUserDataList
import com.kasuminotes.db.unHashDb
import com.kasuminotes.ui.app.AppRepository
import com.kasuminotes.ui.app.DefaultUserId
import com.kasuminotes.utils.UrlUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class DbState(
    private val appRepository: AppRepository,
    private val scope: CoroutineScope
) {
    private var downloadingDbServer: DbServer? = null
    private var downloadingDbVersion: String? = null

    var appAutoUpdate by mutableStateOf(appRepository.getAppAutoUpdate())
        private set
    var dbAutoUpdate by mutableStateOf(appRepository.getDbAutoUpdate())
        private set
    var dbServer by mutableStateOf(appRepository.getDbServer())
        private set
    var dbVersion by mutableStateOf(appRepository.getDbVersion(dbServer))
        private set
    var newDbVersion by mutableStateOf<String?>(null)
        private set
    var newAppReleaseInfo by mutableStateOf<AppReleaseInfo?>(null)
        private set
    var lastVersionFetching by mutableStateOf(false)
        private set
    var latestAppReleaseInfoFetching by mutableStateOf(false)
        private set
    var isLastDb by mutableStateOf(false)
        private set
    var isLatestApp by mutableStateOf(false)
        private set
    var downloadState by mutableStateOf<DownloadState?>(null)
        private set
    var questInitializing by mutableStateOf(false)
        private set

    val userState = UserState(appRepository, scope)

    fun init() {
        updateDbState(dbServer, dbVersion)
        if (dbAutoUpdate) {
            autoFetchLastDbVersion(false)
        }
        if (appAutoUpdate) {
            autoFetchLatestAppReleaseInfo(false)
        }
    }

    fun changeDbServer(otherServer: DbServer) {
        if (otherServer != dbServer) {
            userState.clearAllUser()
            updateDbState(otherServer, appRepository.getDbVersion(otherServer))
        }
    }

    fun retryDownload() {
        downloadDbFile(downloadingDbServer!!, downloadingDbVersion!!)
    }

    fun cancelDownload() {
        downloadingDbServer = null
        downloadingDbVersion = null
        downloadState = null
    }

    fun updateDb(lastVersion: String) {
        downloadDbFile(dbServer, lastVersion)
        newDbVersion = null
    }

    fun updateApp(info: AppReleaseInfo) {
        scope.launch(Dispatchers.IO) {
            appRepository.downloadApp(info)
            newAppReleaseInfo = null
        }
    }

    fun cancelUpdateDb() {
        newDbVersion = null
    }

    fun cancelUpdateApp() {
        newAppReleaseInfo = null
    }

    fun fetchLastDbVersion() = autoFetchLastDbVersion(true)

    fun fetchLatestAppReleaseInfo() = autoFetchLatestAppReleaseInfo(true)

    fun confirmIsLastDb() {
        isLastDb = false
    }

    fun confirmIsLatestApp() {
        isLatestApp = false
    }

    fun reDownload() {
        isLastDb = false
        downloadDbFile(dbServer, dbVersion)
    }

    fun toggleAppAutoUpdate() {
        appAutoUpdate = !appAutoUpdate
        appRepository.setAppAutoUpdate(appAutoUpdate)
    }

    fun toggleDbAutoUpdate() {
        dbAutoUpdate = !dbAutoUpdate
        appRepository.setDbAutoUpdate(dbAutoUpdate)
    }

    private fun autoFetchLastDbVersion(mutableIsLastDb: Boolean) {
        if (!lastVersionFetching) {
            scope.launch(Dispatchers.IO) {
                try {
                    lastVersionFetching = true
                    val lastDbVersion = appRepository.fetchLastDbVersion(dbServer)
                    lastVersionFetching = false
                    if (lastDbVersion != dbVersion) {
                        newDbVersion = lastDbVersion
                    } else if (mutableIsLastDb) {
                        isLastDb = true
                    }
                } catch (e: Throwable) {
                    downloadState = DownloadState.Error(e)
                    lastVersionFetching = false
                }
            }
        }
    }

    private fun autoFetchLatestAppReleaseInfo(mutableIsLatestApp: Boolean) {
        if (!latestAppReleaseInfoFetching) {
            scope.launch(Dispatchers.IO) {
                try {
                    latestAppReleaseInfoFetching = true
                    val info = appRepository.fetchLatestAppReleaseInfo()
                    latestAppReleaseInfoFetching = false
                    if (info != null) {
                        newAppReleaseInfo = info
                    } else if (mutableIsLatestApp) {
                        isLatestApp = true
                    }
                } catch (e: Throwable) {
                    latestAppReleaseInfoFetching = false
                }
            }
        }
    }

    private fun updateDbState(server: DbServer, version: String) {
        val dbFile = appRepository.getDbFile(server)
        if (dbFile.exists()) {
            userState.updateStateFromDb(appRepository.getDatabase(dbFile.name))
            if (server != dbServer) {
                syncDbServerVersion(server, version)
            }
        } else {
            // TODO 可以设计成弹出警告框让用户选择是否下载
            downloadDbFile(server, "0")
        }
    }

    private fun downloadDbFile(server: DbServer, version: String) {
        scope.launch(Dispatchers.IO) {
            downloadState = DownloadState.Loading
            downloadingDbServer = server
            downloadingDbVersion = version
            appRepository.downloadTempDbFile(server).collect { state ->
                downloadState = state
                if (state is DownloadState.Success) {
                    initDb(server, version, state.dbFile)
                }
            }
        }
    }

    private suspend fun initDb(server: DbServer, version: String, tempDbFile: File) {
        val db: AppDatabase
        val lastDbVersion: String
        val dbFile = appRepository.getDbFile(server)
        val backupDbFile = appRepository.getBackupDbFile(server)
        if (version == "0") {
            try {
                lastDbVersion = appRepository.fetchLastDbVersion(server)
                tempDbFile.renameTo(dbFile)
                db = appRepository.getDatabase(dbFile.name)
                if (!UrlUtil.useWthee && server == DbServer.JP) {
                    val rainbowJson = appRepository.fetchRainbowJson()
                    db.unHashDb(rainbowJson)
                }
                db.initDatabase(DefaultUserId)
            } catch (e: Throwable) {
                downloadState = DownloadState.Error(e)
                if (dbFile.exists()) {
                    dbFile.delete()
                }
                changeDbServer(if (server == DbServer.JP) DbServer.CN else DbServer.JP)
                return
            }
        } else {
            try {
                dbFile.copyTo(backupDbFile, true)
                lastDbVersion = version
                db = appRepository.getDatabase(dbFile.name)
                val backupUserDataList = db.getBackupUserDataList(DefaultUserId)
                tempDbFile.renameTo(dbFile)
                if (!UrlUtil.useWthee && server == DbServer.JP) {
                    val rainbowJson = appRepository.fetchRainbowJson()
                    db.unHashDb(rainbowJson)
                }
                db.initDatabase(DefaultUserId)
                db.putUserDataList(backupUserDataList)
            } catch (e: Throwable) {
                downloadState = DownloadState.Error(e)
                if (backupDbFile.exists()) {
                    backupDbFile.copyTo(dbFile, true)
                    return
                } else {
                    if (dbFile.exists()) {
                        dbFile.delete()
                    }
                    changeDbServer(if (server == DbServer.JP) DbServer.CN else DbServer.JP)
                    return
                }
            }
        }
        userState.updateStateFromDb(db)
        downloadingDbServer = null
        downloadingDbVersion = null
        syncDbServerVersion(server, lastDbVersion)
        downloadState = null
        questInitializing = true
        db.initQuestDropData()
        questInitializing = false
    }

    private fun syncDbServerVersion(server: DbServer, version: String) {
        dbServer = server
        dbVersion = version
        appRepository.setDbServer(server)
        appRepository.setDbVersion(server, version)
    }
}
