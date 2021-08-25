package com.kasuminotes.ui.app.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.kasuminotes.common.DbServer
import com.kasuminotes.common.DownloadState
import com.kasuminotes.data.MaxUserData
import com.kasuminotes.data.UserProfile
import com.kasuminotes.db.AppDatabase
import com.kasuminotes.db.getAllUser
import com.kasuminotes.db.getBackupUserDataList
import com.kasuminotes.db.getMaxUserData
import com.kasuminotes.db.getUserName
import com.kasuminotes.db.getUserProfileList
import com.kasuminotes.db.initDatabase
import com.kasuminotes.db.initQuestDropData
import com.kasuminotes.db.putUserDataList
import com.kasuminotes.ui.app.AppRepository
import com.kasuminotes.ui.app.DefaultUserId
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.File

class DbState(
    private val appRepository: AppRepository,
    private val scope: CoroutineScope
) {
    private var downloadingDbServer: DbServer? = null
    private var downloadingDbVersion: String? = null

    var dbAutoUpdate by mutableStateOf(appRepository.getDbAutoUpdate())
        private set
    var dbServer by mutableStateOf(appRepository.getDbServer())
        private set
    var dbVersion by mutableStateOf(appRepository.getDbVersion(dbServer))
        private set
    var newDbVersion by mutableStateOf<String?>(null)
        private set
    var lastVersionFetching by mutableStateOf(false)
        private set
    var isAlreadyLatest by mutableStateOf(false)
        private set
    var downloadState by mutableStateOf<DownloadState?>(null)
        private set
    var questInitializing by mutableStateOf(false)
        private set

    val userState = UserState(appRepository, scope)

    init {
        updateDbState(dbServer, dbVersion)
        if (dbAutoUpdate) {
            scope.launch {
                autoFetchLastDbVersion(false)
            }
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

    fun cancelUpdate() {
        newDbVersion = null
    }

    fun fetchLastDbVersion() = autoFetchLastDbVersion(true)

    fun confirmIsAlreadyLatest() {
        isAlreadyLatest = false
    }

    fun reDownload() {
        isAlreadyLatest = false
        downloadDbFile(dbServer, "0")
    }

    fun toggleDbAutoUpdate() {
        dbAutoUpdate = !dbAutoUpdate
        appRepository.setDbAutoUpdate(dbAutoUpdate)
    }

    private fun autoFetchLastDbVersion(mutableIsAlreadyLatest: Boolean) {
        if (!lastVersionFetching) {
            scope.launch {
                try {
                    lastVersionFetching = true
                    val lastDbVersion = appRepository.fetchLastDbVersion(dbServer)
                    lastVersionFetching = false
                    if (lastDbVersion != dbVersion) {
                        newDbVersion = lastDbVersion
                    } else if (mutableIsAlreadyLatest) {
                        isAlreadyLatest = true
                    }
                } catch (e: Throwable) {
                    lastVersionFetching = false
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
        scope.launch {
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
        try {
            val db: AppDatabase
            val lastDbVersion: String
            val dbFile = appRepository.getDbFile(server)
            if (version == "0") {
                lastDbVersion = appRepository.fetchLastDbVersion(server)
                tempDbFile.renameTo(dbFile)
                db = appRepository.getDatabase(dbFile.name)
                db.initDatabase(DefaultUserId, server)
            } else {
                lastDbVersion = version
                db = appRepository.getDatabase(dbFile.name)
                val backupUserDataList = db.getBackupUserDataList(DefaultUserId)
                tempDbFile.renameTo(dbFile)
                db.initDatabase(DefaultUserId, server)
                db.putUserDataList(backupUserDataList)
            }
            userState.updateStateFromDb(db)
            downloadingDbServer = null
            downloadingDbVersion = null
            syncDbServerVersion(server, lastDbVersion)
            downloadState = null

            questInitializing = true
            db.initQuestDropData()
            questInitializing = false
        } catch (e: Throwable) {
            downloadState = DownloadState.Error(e)
        }
    }

    private fun syncDbServerVersion(server: DbServer, version: String) {
        dbServer = server
        dbVersion = version
        appRepository.setDbServer(server)
        appRepository.setDbVersion(server, version)
    }
}
