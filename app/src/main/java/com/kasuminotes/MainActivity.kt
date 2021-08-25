package com.kasuminotes

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.core.view.WindowCompat
import coil.annotation.ExperimentalCoilApi
import com.google.accompanist.insets.ProvideWindowInsets
import com.kasuminotes.ui.app.App
import com.kasuminotes.utils.LocaleUtil
import com.kasuminotes.utils.ProvideImageLoader

class MainActivity : ComponentActivity() {

    @ExperimentalCoilApi
    @ExperimentalAnimationApi
    @ExperimentalMaterialApi
    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        instance = this

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            ProvideWindowInsets {
                ProvideImageLoader {
                    App()
                }
            }
        }

    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleUtil.attachBaseContext(base))
    }

    fun reload() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var instance: MainActivity
            private set
    }
}
