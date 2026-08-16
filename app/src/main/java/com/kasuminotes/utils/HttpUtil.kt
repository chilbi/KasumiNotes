package com.kasuminotes.utils

import com.kasuminotes.BuildConfig
import com.kasuminotes.common.DbServer
import com.kasuminotes.common.DownloadState
import com.kasuminotes.data.AppReleaseInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.apache.commons.compress.compressors.brotli.BrotliCompressorInputStream
import org.json.JSONObject
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
    fun downloadDbFile(url: String, file: File, isBrFile: Boolean): Flow<DownloadState> = flow {
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
            fos = FileOutputStream(file)
            val buf = ByteArray(8192)
            var len: Int
            while (fis.read(buf).also { len = it } != -1) {
                fos.write(buf, 0, len)
                bytesRead += len
                emit(DownloadState.Progress(bytesRead, contentLength))
            }
            fos.flush()
            val dbFile = if (isBrFile) decompress(file) else file
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
    fun fetchRoboninonLastDbVersion(url: String): String {
        var call: Call? = null
        var response: Response? = null
        try {
            val client = OkHttpClient.Builder().build()
            val request = Request.Builder()
                .url(url)
                .build()
            call = client.newCall(request)
            response = call.execute()
            val responseJson = JSONObject(response.body.string())
            return responseJson.getString("TruthVersion")
        } catch (e: Throwable) {
            call?.cancel()
            throw e
        } finally {
            response?.close()
        }
    }

    @Throws(Throwable::class)
    fun fetchWtheeLastDbVersion(url: String, server: DbServer): String {
        var call: Call? = null
        var response: Response? = null
        try {
            val obj = JSONObject()
            obj.put("regionCode", if (server == DbServer.CN) "cn" else "jp")
            val requestBody = obj.toString().toRequestBody("application/json".toMediaType())
            val client = OkHttpClient.Builder().build()
            val request = Request.Builder()
                .url(url)
                .header("app-version", "9.9.9")
                .post(requestBody)
                .build()
            call = client.newCall(request)
            response = call.execute()
            val responseJson = JSONObject(response.body.string())
            if (responseJson.getInt("status") != 0) {
                throw Exception("fetch lastDbVersion error!\n${responseJson.toString(2)}")
            }
            return responseJson.getJSONObject("data").getString("truthVersion")
        } catch (e: Throwable) {
            call?.cancel()
            throw e
        } finally {
            response?.close()
        }
    }

    @Throws(Throwable::class)
    fun fetchCialloworldLastDbVersion(url: String, server: DbServer): String {
        val region = if (server == DbServer.CN) "cn" else "jp"
        var call: Call? = null
        var response: Response? = null
        try {
            val client = OkHttpClient.Builder().build()
            val request = Request.Builder()
                .url("$url?region=$region")
                .build()
            call = client.newCall(request)
            response = call.execute()
            val responseJson = JSONObject(response.body.string())
            return responseJson.getJSONObject("latest")
                .getJSONObject(region)
                .getString("version")
        } catch (e: Throwable) {
            call?.cancel()
            throw e
        } finally {
            response?.close()
        }
    }

    @Throws(Throwable::class)
    fun fetchEstertionLastDbVersion(url: String, server: DbServer): String {
        var call: Call? = null
        var response: Response? = null
        try {
            val client = OkHttpClient.Builder().build()
            val request = Request.Builder()
                .url("$url/last_version_${if (server == DbServer.CN) "cn" else "jp"}.json")
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

    fun fetchStringsVersion(): String? {
        var call: Call? = null
        var response: Response? = null
        try {
            val client = OkHttpClient.Builder().build()
            val request = Request.Builder()
                .url("https://gitee.com/chilbi/strings/raw/main/version.txt")
                .build()
            call = client.newCall(request)
            response = call.execute()
            return response.body.string()
        } catch (e: Throwable) {
            call?.cancel()
            return null
        } finally {
            response?.close()
        }
    }

    fun fetchStrings(): String? {
        var call: Call? = null
        var response: Response? = null
        try {
            val client = OkHttpClient.Builder().build()
            val request = Request.Builder()
                .url("https://gitee.com/chilbi/strings/raw/main/strings.json")
                .build()
            call = client.newCall(request)
            response = call.execute()
//            val body = response.body.string()
//            val content = JSONObject(body).getString("content")
//            return String(Base64.decode(content, Base64.DEFAULT))
            return response.body.string()
        } catch (e: Throwable) {
            call?.cancel()
            return null
        } finally {
            response?.close()
        }
    }
}
