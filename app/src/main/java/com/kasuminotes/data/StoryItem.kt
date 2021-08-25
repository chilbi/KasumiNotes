package com.kasuminotes.data

import com.kasuminotes.utils.UrlUtil

data class StoryItem(
    private val unitId: Int,
    private val rarity: Int,
    private val unitName: String,
    val status: List<Property>?,
    val unlockCount: Int
) {
    val iconUrl: String by lazy {
        UrlUtil.getUnitIconUrl(unitId, rarity)
    }

    val label: String by lazy {
        Regex("（([\\s\\S]+)）").find(unitName)?.let { matchResult ->
            matchResult.groupValues[1]
        } ?: unitName
    }
}
