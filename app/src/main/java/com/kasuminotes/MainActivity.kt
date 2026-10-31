package com.kasuminotes

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.content.FileProvider
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import com.kasuminotes.ui.app.App
import com.kasuminotes.utils.LocaleUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

class MainActivity : ComponentActivity() {
    private var broadcastReceiver: BroadcastReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        instance = this
        val splashScreen = installSplashScreen()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            splashScreen.setOnExitAnimationListener { splashScreenView -> splashScreenView.remove() }
        }
        super.onCreate(savedInstanceState)
        setContent {
            App()
        }
        var keepSplash = true
        splashScreen.setKeepOnScreenCondition { keepSplash }
        lifecycleScope.launch(Dispatchers.IO) {
            delay(250)
            keepSplash = false
        }
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleUtil.attachBaseContext(base))
    }

    override fun onDestroy() {
        super.onDestroy()
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver)
        }
    }

    fun reload() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun listenerDownload(id: Long, apkFile: File) {
        broadcastReceiver?.let {
            runCatching { unregisterReceiver(it) }
        }
        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1L)

                if (downloadId != id) return
                runCatching { unregisterReceiver(this) }
                broadcastReceiver = null

                val downloadManager = context.getSystemService(DOWNLOAD_SERVICE) as DownloadManager
                downloadManager.query(DownloadManager.Query().setFilterById(downloadId))?.use { cursor ->
                    if (!cursor.moveToFirst()) return
                    val statusIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                    val status = if (statusIndex > -1) cursor.getInt(statusIndex) else -1

                    if (status != DownloadManager.STATUS_SUCCESSFUL) {
                        runCatching { apkFile.delete() }
                        Toast.makeText(context, "DOWNLOAD_FAILED", Toast.LENGTH_SHORT).show()
                        return
                    }
                }

                if (!apkFile.exists() || apkFile.length() == 0L) {
                    Toast.makeText(context, "DOWNLOAD_FAILED", Toast.LENGTH_SHORT).show()
                    return
                }

                try {
                    val downloadUri = FileProvider.getUriForFile(
                        context,
                        "${context.packageName}.fileprovider",
                        apkFile
                    )
                    val install = Intent(Intent.ACTION_VIEW).apply {
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        setDataAndType(downloadUri, "application/vnd.android.package-archive")
                    }
                    context.startActivity(install)
                } catch (e: Throwable) {
                    Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                }
            }
        }
        val intentFilter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        if (Build.VERSION.SDK_INT >= 33) {
            registerReceiver(broadcastReceiver, intentFilter, Context.RECEIVER_EXPORTED)
        } else {
            registerReceiver(broadcastReceiver, intentFilter)
        }
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var instance: MainActivity
            private set
    }
}
