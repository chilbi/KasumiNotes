package com.kasuminotes.common

import okhttp3.Call
import java.io.File

sealed class DownloadState {
    data class Loading(val call: Call?) : DownloadState()
    data class Progress(val bytesRead: Long, val contentLength: Long) : DownloadState()
    data class Success(val dbFile: File) : DownloadState()
    data class Error(val e: Throwable) : DownloadState()
    data object Cancelled : DownloadState()
}