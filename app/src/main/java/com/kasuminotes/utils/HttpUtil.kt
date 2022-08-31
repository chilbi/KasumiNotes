package com.kasuminotes.utils

import com.kasuminotes.BuildConfig
import com.kasuminotes.common.DownloadState
import com.kasuminotes.data.AppReleaseInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.apache.commons.compress.compressors.brotli.BrotliCompressorInputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream

object HttpUtil {
    private val userAgent = "kasumiNotes/${BuildConfig.VERSION_NAME} ${System.getProperty("http.agent")}"

    private fun decompress(brFile: File): File {
        var fis: FileInputStream? = null
        var fos: FileOutputStream? = null
        var bis: BrotliCompressorInputStream? = null
        try {
            val filePath = brFile.absolutePath.replace(".br", "")
            fis = FileInputStream(brFile)
            fos = FileOutputStream(filePath)
            bis = BrotliCompressorInputStream(fis)
            val buf = ByteArray(8192)
            var len: Int
            while (bis.read(buf).also { len = it } != -1) {
                fos.write(buf, 0, len)
            }
            fos.flush()
            brFile.delete()
            return File(filePath)
        } catch (e: Throwable) {
            throw e
        } finally {
            bis?.close()
            fis?.close()
            fos?.close()
        }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    fun downloadDbFile(url: String, brFile: File): Flow<DownloadState> = flow {
        var call: Call? = null
        var response: Response? = null
        var fis: InputStream? = null
        var fos: FileOutputStream? = null
        try {
            val client = OkHttpClient.Builder().build()
            val request = Request.Builder()
                .url(url)
                .header("User-Agent", userAgent)
                .build()
            call = client.newCall(request)
            response = call.execute()
            val body = response.body
            var bytesRead: Long = 0
            val contentLength = body.contentLength()
            emit(DownloadState.Progress(bytesRead, contentLength))
            fis = body.byteStream()
            fos = FileOutputStream(brFile)
            val buf = ByteArray(8192)
            var len: Int
            while (fis.read(buf).also { len = it } != -1) {
                fos.write(buf, 0, len)
                bytesRead += len
                emit(DownloadState.Progress(bytesRead, contentLength))
            }
            fos.flush()
            val dbFile = decompress(brFile)
            emit(DownloadState.Success(dbFile))
        } catch (e: Throwable) {
            call?.cancel()
            emit(DownloadState.Error(e))
        } finally {
            response?.close()
            fis?.close()
            fos?.close()
        }
    }

    @Throws(Throwable::class)
    fun fetchLastDbVersion(url: String): String {
        var call: Call? = null
        var response: Response? = null
        try {
            val client = OkHttpClient.Builder().build()
            val request = Request.Builder()
                .url(url)
                .header("User-Agent", userAgent)
                .build()
            call = client.newCall(request)
            response = call.execute()
            val body = response.body
            val pattern = "\"TruthVersion\"\\s*:\\s*\"(\\d+)\""
            val matchResult = Regex(pattern).find(body.string()) ?: throw Exception("regex match error")
            return matchResult.groupValues[1]
        } catch (e: Throwable) {
            call?.cancel()
            throw e
        } finally {
            response?.close()
        }
    }

    @Throws(Throwable::class)
    fun fetchLatestAppReleaseInfo(url: String): AppReleaseInfo? {
        var call: Call? = null
        var response: Response? = null
        try {
            val client = OkHttpClient.Builder().build()
            val request = Request.Builder()
                .url(url)
                .header("User-Agent", userAgent)
                .build()
            call = client.newCall(request)
            response = call.execute()
            val body = response.body
            val bodyString = body.string()
            val versionNamePattern = "\"tag_name\"\\s*:\\s*\"v([^\"]+)\""
            val versionNameMatchResult = Regex(versionNamePattern).find(bodyString) ?: throw Exception("regex match error")
            val versionName = versionNameMatchResult.groupValues[1]
            return if (versionName != BuildConfig.VERSION_NAME) {
                val downloadURLPattern = "\"browser_download_url\"\\s*:\\s*\"([^\"]+)\""
                val downloadURLMatchResult = Regex(downloadURLPattern).find(bodyString) ?: throw Exception("regex match error")
                val downloadURL = downloadURLMatchResult.groupValues[1]
                val descriptionPattern = "\"body\"\\s*:\\s*\"([^\"]+)\""
                val descriptionMatchResult = Regex(descriptionPattern).find(bodyString) ?: throw Exception("regex match error")
                val description = descriptionMatchResult.groupValues[1]
                AppReleaseInfo(versionName, downloadURL, description)
            } else {
                null
            }
        } catch (e: Throwable) {
            call?.cancel()
            throw e
        } finally {
            response?.close()
        }
    }
}

//    private class DownloadResponseBody(
//        private val responseBody: ResponseBody,
//        private val offer: (DownloadState) -> Unit
//    ) : ResponseBody() {
//
//        private val bufferedSource: BufferedSource by lazy {
//            object : ForwardingSource(responseBody.source()) {
//                private var bytesRead = 0L
//
//                override fun read(sink: Buffer, byteCount: Long): Long {
//                    val read = super.read(sink, byteCount)
//                    if (read != -1L) {
//                        bytesRead += read
//                        offer(DownloadState.Progress(bytesRead, responseBody.contentLength()))
//                    }
//                    return read
//                }
//            }.buffer()
//        }
//
//        override fun contentLength(): Long = responseBody.contentLength()
//
//        override fun contentType(): MediaType? = responseBody.contentType()
//
//        override fun source(): BufferedSource = bufferedSource
//    }
//
//    private class DownloadInterceptor(
//        private val offer: (DownloadState) -> Unit
//    ) : Interceptor {
//
//        override fun intercept(chain: Interceptor.Chain): Response {
//            val response = chain.proceed(chain.request())
//            val body = response.body ?: return response
//            return response.newBuilder()
//                .body(DownloadResponseBody(body, offer))
//                .build()
//        }
//    }

//    @Suppress("BlockingMethodInNonBlockingContext")
//    @Throws(Throwable::class)
//    fun downloadDbFile(url: String, brFile: File): Flow<DownloadState> = callbackFlow {
//        var call: Call? = null
//        var response: Response? = null
//        var fis: InputStream? = null
//        var fos: FileOutputStream? = null
//        try {
//            val client = OkHttpClient.Builder()
//                .addNetworkInterceptor(DownloadInterceptor(::offer))
//                .build()
//            val request = Request.Builder()
//                .url(url)
//                .header("User-Agent", userAgent)
//                .build()
//            call = client.newCall(request)
//            response = call.execute()
//            val body = response.body ?: throw Exception("body is null")
//            fis = body.byteStream()
//            fos = FileOutputStream(brFile)
//            val buf = ByteArray(8192)
//            var len: Int
//            while (fis.read(buf).also { len = it } != -1) {
//                fos.write(buf, 0, len)
//            }
//            fos.flush()
//            val dbFile = decompress(brFile)
//            offer(DownloadState.Success(dbFile))
//        } catch (e: Throwable) {
//            call?.cancel()
//            offer(DownloadState.Error(e))
//        } finally {
//            response?.close()
//            fis?.close()
//            fos?.close()
//            close()
//        }
//    }
