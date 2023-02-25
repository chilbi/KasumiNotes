package com.kasuminotes.utils

import com.kasuminotes.common.DbServer

object UrlUtil {
    // API URL
    private const val API_URL = "https://redive.estertion.win"
    private const val ICON_EX_EQUIPMENT_DIR = "https://wthee.xyz/redive/jp/resource/icon/ex_equipment"

    //  CN database
    private const val DB_FILE_NAME_CN = "redive_cn.db"
    private const val DB_FILE_URL_CN = "$API_URL/db/$DB_FILE_NAME_CN.br"
    private const val LAST_VERSION_URL_CN = "$API_URL/last_version_cn.json"

    //  JP database
    private const val DB_FILE_NAME_JP = "redive_jp.db"
    private const val DB_FILE_URL_JP = "$API_URL/db/$DB_FILE_NAME_JP.br"
    private const val LAST_VERSION_URL_JP = "$API_URL/last_version_jp.json"

    //  Resource URL
    private const val STILL_UNIT_URL = "$API_URL/card/full/%d.webp"
    private const val UNIT_PLATE_URL = "$API_URL/icon/plate/%d.webp"
    private const val ICON_UNIT_URL = "$API_URL/icon/unit/%d.webp"
    private const val ICON_SKILL_URL = "$API_URL/icon/skill/%d.webp"
    private const val ICON_EQUIPMENT_URL = "$API_URL/icon/equipment/%d.webp"
    private const val ICON_ITEM_URL = "$API_URL/icon/item/%d.webp"
    private const val ICON_EX_EQUIPMENT_URL = "$ICON_EX_EQUIPMENT_DIR/%d.webp"
    private const val ICON_EX_EQUIPMENT_CATEGORY_URL = "$ICON_EX_EQUIPMENT_DIR/category/%d.webp"
    private const val ICON_EX_EQUIPMENT_MAP_URL = "$ICON_EX_EQUIPMENT_DIR/map/%d.webp"

    // App Release URL
    const val APP_RELEASE_URL = "https://api.github.com/repos/chilbi/KasumiNotes/releases/latest"

    val dbFileNameMap = mapOf(DbServer.CN to DB_FILE_NAME_CN, DbServer.JP to DB_FILE_NAME_JP)
    val dbFileUrlMap = mapOf(DbServer.CN to DB_FILE_URL_CN, DbServer.JP to DB_FILE_URL_JP)
    val lastVersionUrl =
        mapOf(DbServer.CN to LAST_VERSION_URL_CN, DbServer.JP to LAST_VERSION_URL_JP)

    const val summonIconUrl = "$API_URL/icon/unit/000001.webp"

    private fun getImageId(unitId: Int, rarity: Int) = (if (rarity > 5) 6 else if (rarity > 2) 3 else 1) * 10 + unitId

    fun getUnitStillUrl(unitId: Int, rarity: Int) =
        String.format(STILL_UNIT_URL, (if (rarity > 5) 6 else 3) * 10 + unitId)

    fun getUnitPlateUrl(unitId: Int, rarity: Int) =
        String.format(UNIT_PLATE_URL, getImageId(unitId, rarity))

    fun getUnitIconUrl(unitId: Int, rarity: Int) =
        String.format(ICON_UNIT_URL, getImageId(unitId, rarity))

    fun getBossUnitIconUrl(unitId: Int) =
        String.format(ICON_UNIT_URL, if (unitId == 301305) 301300 else unitId)

    fun getUserIconUrl(userId: Int) = String.format(ICON_UNIT_URL, userId)

    fun getUserStillUrl(userId: Int) =
        getUnitStillUrl(userId / 100 * 100 + 1, (userId % 100 - 1) / 10)

    fun getEquipIconUrl(equipId: Int) = String.format(ICON_EQUIPMENT_URL, equipId)

    fun getItemIconUrl(itemId: Int) = String.format(ICON_ITEM_URL, itemId)

    fun getSkillIconUrl(iconType: Int) = String.format(ICON_SKILL_URL, iconType)

    fun getAtkIconUrl(atkType: Int) = getEquipIconUrl(if (atkType == 1) 101011 else 101251)

    fun getKanNaPlateUrl(unitId: Int, rarity: Int) =
        "https://wthee.xyz/redive/cn/resource/icon/plate/${getImageId(unitId, rarity)}.webp"

    fun getExEquipUrl(exEquipId: Int) = String.format(ICON_EX_EQUIPMENT_URL, exEquipId)

    fun getExEquipCategoryUrl(categoryId: Int) = String.format(ICON_EX_EQUIPMENT_CATEGORY_URL, categoryId)

    fun getExEquipMapUrl(mapId: Int) = String.format(ICON_EX_EQUIPMENT_MAP_URL, mapId)
}
