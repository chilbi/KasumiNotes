package com.kasuminotes.utils

import android.content.Context
import androidx.collection.LruCache
import java.util.Locale

class IconCache(context: Context) {
    private val cache = LruCache<String, Int>(100)
    private val ctx = context

    fun getNodeIconResId(iconId: Int): Int? {
        val key = String.format(Locale.US, "%06d", iconId)
        return cache[key] ?: run {
            val resId = ctx.resources.getIdentifier("node_icon_$key", "drawable",ctx.packageName)
            if (resId == 0) {
                null
            } else {
                cache.put(key, resId)
                resId
            }
        }
    }

    fun getTeamNodeIconResId(iconId: Int): Int? {
        val key = String.format(Locale.US, "%06d", iconId)
        return cache[key] ?: run {
            val resId = ctx.resources.getIdentifier("team_node_icon_$key", "drawable",ctx.packageName)
            if (resId == 0) {
                null
            } else {
                cache.put(key, resId)
                resId
            }
        }
    }
}
