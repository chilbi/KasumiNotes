package com.kasuminotes.utils

//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.CompositionLocalProvider
//import androidx.compose.runtime.remember
//import androidx.compose.ui.platform.LocalContext
//import coil.ImageLoader
//import coil.compose.LocalImageLoader
//import okhttp3.OkHttpClient
//
//@Composable
//fun ProvideImageLoader(content: @Composable () -> Unit) {
//    val context = LocalContext.current
//    val loader = remember(context) {
//        ImageLoader.Builder(context)
//            .callFactory {
//                OkHttpClient.Builder()
//                    .cache(ImageLoaderUtil.createCache(context))
//                    .build()
//            }
//            .build()
//    }
//    CompositionLocalProvider(LocalImageLoader provides loader, content = content)
//}
