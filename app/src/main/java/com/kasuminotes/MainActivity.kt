package com.kasuminotes

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.kasuminotes.ui.app.App
import com.kasuminotes.utils.LocaleUtil

class MainActivity : ComponentActivity() {
    private var broadcastReceiver: BroadcastReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        instance = this

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            App()
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

    fun listenerDownload(id: Long) {
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver)
        }
        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                if (downloadId == id) {
                    val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                    val downloadUri = downloadManager.getUriForDownloadedFile(downloadId)
                    val install = Intent(Intent.ACTION_VIEW).apply {
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        setDataAndType(downloadUri, "application/vnd.android.package-archive")
                    }
                    unregisterReceiver(broadcastReceiver)
                    context.startActivity(install)
                }
            }
        }
        registerReceiver(broadcastReceiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var instance: MainActivity
            private set
    }
}
