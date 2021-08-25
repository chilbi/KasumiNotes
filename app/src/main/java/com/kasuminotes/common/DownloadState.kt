package com.kasuminotes.common

import java.io.File

sealed class DownloadState {
    object Loading : DownloadState()
    data class Progress(val bytesRead: Long, val contentLength: Long) : DownloadState()
    data class Success(val dbFile: File) : DownloadState()
    data class Error(val e: Throwable) : DownloadState()
}