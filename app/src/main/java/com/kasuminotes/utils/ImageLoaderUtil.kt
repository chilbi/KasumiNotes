package com.kasuminotes.utils

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.StatFs
import okhttp3.Cache
import java.io.File

object ImageLoaderUtil {
    private const val CACHE_DIRECTORY_NAME = "image_cache"

    private const val MIN_DISK_CACHE_SIZE_BYTES = 10L * 1024 * 1024 // 10MB
    private const val MAX_DISK_CACHE_SIZE_BYTES = 750L * 1024 * 1024 // 750MB

    private const val DISK_CACHE_PERCENTAGE = 0.02

    private fun getCacheDirectory(context: Context): File {
        return File(context.externalCacheDir, CACHE_DIRECTORY_NAME).apply { mkdirs() }
    }

    @Suppress("DEPRECATION")
    private inline val StatFs.blockCountCompat: Long
        @SuppressLint("ObsoleteSdkInt")
        get() = if (Build.VERSION.SDK_INT >= 18) blockCountLong else blockCount.toLong()

    @Suppress("DEPRECATION")
    private inline val StatFs.blockSizeCompat: Long
        @SuppressLint("ObsoleteSdkInt")
        get() = if (Build.VERSION.SDK_INT >= 18) blockSizeLong else blockSize.toLong()

    /** Modified from Picasso. */
    private fun calculateDiskCacheSize(cacheDirectory: File): Long {
        return try {
            val cacheDir = StatFs(cacheDirectory.absolutePath)
            val size = DISK_CACHE_PERCENTAGE * cacheDir.blockCountCompat * cacheDir.blockSizeCompat
            return size.toLong().coerceIn(MIN_DISK_CACHE_SIZE_BYTES, MAX_DISK_CACHE_SIZE_BYTES)
        } catch (_: Exception) {
            MIN_DISK_CACHE_SIZE_BYTES
        }
    }

    /** Create an OkHttp disk cache with a reasonable default size and location. */
    fun createCache(context: Context): Cache {
        val cacheDirectory = getCacheDirectory(context)
        val cacheSize = calculateDiskCacheSize(cacheDirectory)
        return Cache(cacheDirectory, cacheSize)
    }
}
