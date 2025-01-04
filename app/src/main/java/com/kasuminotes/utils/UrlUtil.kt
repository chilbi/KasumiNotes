package com.kasuminotes.utils

import com.kasuminotes.common.DbServer

object UrlUtil {
    const val useWtheeDb = true
    const val useWtheeRes = true
    // API URL
    private const val ESTERTION_API_URL = "https://redive.estertion.win"
    private const val WTHEE_API_URL = "https://wthee.xyz"
    private const val WTHEE_API_RESOURCE_URL = "$WTHEE_API_URL/redive/jp/resource"
    private val RES_URL = if (useWtheeRes) WTHEE_API_RESOURCE_URL else ESTERTION_API_URL

    //  CN database
    private const val DB_FILE_NAME_CN = "redive_cn.db"
    private val DB_FILE_URL_CN = "${if (useWtheeDb) WTHEE_API_URL else ESTERTION_API_URL}/db/$DB_FILE_NAME_CN.br"
    private const val LAST_VERSION_URL_CN = "$ESTERTION_API_URL/last_version_cn.json"

    //  JP database
    private const val DB_FILE_NAME_JP = "redive_jp.db"
    private val DB_FILE_URL_JP = "${if (useWtheeDb) WTHEE_API_URL else ESTERTION_API_URL}/db/$DB_FILE_NAME_JP.br"
    private const val LAST_VERSION_URL_JP = "$ESTERTION_API_URL/last_version_jp.json"

    //  Resource URL
    private val STILL_UNIT_URL = "$RES_URL/card/full/%d.webp"
    private val UNIT_PLATE_URL = "$RES_URL/icon/plate/%d.webp"
    private val ICON_UNIT_URL = "$RES_URL/icon/unit/%d.webp"
    private val ICON_SKILL_URL = "$RES_URL/icon/skill/%d.webp"
    private val ICON_EQUIPMENT_URL = "$RES_URL/icon/equipment/%d.webp"
    private val ICON_ITEM_URL = "$RES_URL/icon/item/%d.webp"
    //wthee没有unit_shadow的图片
    private val ICON_UNIT_SHADOW_URL = if (useWtheeRes) ICON_UNIT_URL else "$ESTERTION_API_URL/icon/unit_shadow/%d.webp"
    //estertion没有ex_equipment的图片
    private val ICON_EX_EQUIPMENT_URL = "$WTHEE_API_RESOURCE_URL/icon/ex_equipment/%d.webp"
    private val ICON_EX_EQUIPMENT_CATEGORY_URL = "$WTHEE_API_RESOURCE_URL/icon/ex_equipment/category/%d.webp"

//    const val StringsJsonUrl = "https://api.github.com/repos/chilbi/KasumiNotes/contents/app/src/main/res/json/strings.json"
    const val StringsJsonUrl = "https://gitee.com/chilbi/strings/raw/main/strings.json"
    // App Release URL
    const val APP_RELEASE_URL = "https://api.github.com/repos/chilbi/KasumiNotes/releases/latest"
    // hashed TableNames ColumnNames
    const val RainbowJsonUrl = "https://api.github.com/repos/MalitsPlus/ShizuruNotes/contents/app/src/main/res/raw/rainbow.json"

    const val lastVersionApiUrl = "$WTHEE_API_URL/pcr/api/v1/db/info/v2"

    val dbFileNameMap = mapOf(DbServer.CN to DB_FILE_NAME_CN, DbServer.JP to DB_FILE_NAME_JP)
    val dbFileUrlMap = mapOf(DbServer.CN to DB_FILE_URL_CN, DbServer.JP to DB_FILE_URL_JP)
    val lastVersionUrl = mapOf(DbServer.CN to LAST_VERSION_URL_CN, DbServer.JP to LAST_VERSION_URL_JP)

//    const val summonIconUrl = "$API_URL/icon/unit/000001.webp"

    private fun getImageId(unitId: Int, rarity: Int) = (if (rarity > 5) 6 else if (rarity > 2) 3 else 1) * 10 + unitId

    fun getUnitStillUrl(unitId: Int, rarity: Int) =
        String.format(STILL_UNIT_URL, (if (rarity > 5) 6 else 3) * 10 + unitId)

    fun getUnitPlateUrl(unitId: Int, rarity: Int) =
        String.format(UNIT_PLATE_URL, getImageId(unitId, rarity))

    fun getUnitIconUrl(unitId: Int, rarity: Int) =
        String.format(ICON_UNIT_URL, getImageId(unitId, rarity))

    fun getEnemyUnitIconUrl(unitId: Int): String {
        val id = if (unitId == 301305) 301300 else unitId
        return if (Helper.isShadowChara(unitId)) {
            String.format(ICON_UNIT_SHADOW_URL, "1${unitId.toString().substring(1, 4)}31".toInt())
        } else {
            String.format(ICON_UNIT_URL, id)
        }
    }

    fun getUserIconUrl(userId: Int) = String.format(ICON_UNIT_URL, userId)

    fun getUserStillUrl(userId: Int) =
        getUnitStillUrl(userId / 100 * 100 + 1, (userId % 100 - 1) / 10)

    fun getEquipIconUrl(equipId: Int) = String.format(ICON_EQUIPMENT_URL, equipId)

    fun getItemIconUrl(itemId: Int) = String.format(ICON_ITEM_URL, itemId)

    fun getSkillIconUrl(iconType: Int) = String.format(ICON_SKILL_URL, iconType)

    fun getAtkIconUrl(atkType: Int) = getEquipIconUrl(if (atkType == 1) 101011 else 101251)

    fun getKanNaPlateUrl(unitId: Int, rarity: Int) =
        "$WTHEE_API_RESOURCE_URL/icon/plate/${getImageId(unitId, rarity)}.webp"

    fun getExEquipUrl(exEquipId: Int) = String.format(ICON_EX_EQUIPMENT_URL, exEquipId)

    fun getExEquipCategoryUrl(categoryId: Int) = String.format(ICON_EX_EQUIPMENT_CATEGORY_URL, categoryId)

//    fun getExEquipMapUrl(mapId: Int) = String.format(ICON_EX_EQUIPMENT_MAP_URL, mapId)
}
