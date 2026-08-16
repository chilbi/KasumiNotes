package com.kasuminotes.utils

import com.kasuminotes.common.AtkType
import com.kasuminotes.common.DbServer
import java.util.Locale

object UrlUtil {
    const val useWtheeRes = true
    // API URL
    private const val ESTERTION_API_URL = "https://redive.estertion.win"
    private const val WTHEE_API_URL = "https://wthee.xyz"
    private const val ROBONINON_API_URL = "https://roboninon.win"
    private const val CIALLOWORLD_API_URL = "https://pcr.cialloworld.com"
    // /api/databases
    // ?region=cn (tw|jp，返回的JSON结构:latest.cn.version)
    // ?region=jp&download=1 (下载的并不是.br压缩文件，是.db文件)
    private const val WTHEE_API_RESOURCE_URL = "$WTHEE_API_URL/redive/jp/resource"
    private val RES_URL = if (useWtheeRes) WTHEE_API_RESOURCE_URL else ESTERTION_API_URL

    // CN database
    private const val DB_FILE_NAME_CN = "redive_cn.db"
    val ESTERTION_DB_FILE_URL_CN = "$ESTERTION_API_URL/db/$DB_FILE_NAME_CN.br"
    val WTHEE_DB_FILE_URL_CN = "$WTHEE_API_URL/db/$DB_FILE_NAME_CN.br"
    val CIALLOWORLD_DB_FILE_URL_CN = "$CIALLOWORLD_API_URL/api/databases?region=cn&compression=br&download=1"

    // JP database
    private const val DB_FILE_NAME_JP = "redive_jp.db"
    val ESTERTION_DB_FILE_URL_JP = "$ESTERTION_API_URL/db/$DB_FILE_NAME_JP.br"
    val WThEE_DB_FILE_URL_JP = "$WTHEE_API_URL/db/$DB_FILE_NAME_JP.br"
    val CIALLOWORLD_DB_FILE_URL_JP = "$CIALLOWORLD_API_URL/api/databases?region=jp&compression=br&download=1"

    // EN database
    private const val DB_FILE_NAME_EN = "redive_en.db"
    val ROBONINON_DB_FILE_URL_EN = "$ROBONINON_API_URL/db/download?compressed=true"

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

    // App Release URL
    const val APP_RELEASE_URL = "https://api.github.com/repos/chilbi/KasumiNotes/releases/latest"

    // DB Last Version
    const val estertionLastVersionApiUrl = ESTERTION_API_URL// /last_version_{cn|jp}.json
    const val wtheeLastVersionApiUrl = "$WTHEE_API_URL/pcr/api/v1/db/info/v2"
    const val cialloworldLastVersionApiUrl = "$CIALLOWORLD_API_URL/api/databases"// ?region={cn|jp}
    const val roboninonLastVersionApiUrl = "$ROBONINON_API_URL/db/version"

    val dbFileNameMap = mapOf(DbServer.CN to DB_FILE_NAME_CN, DbServer.JP to DB_FILE_NAME_JP, DbServer.EN to DB_FILE_NAME_EN)
//    val dbFileUrlMap = mapOf(DbServer.CN to DB_FILE_URL_CN, DbServer.JP to DB_FILE_URL_JP, DbServer.EN to DB_FILE_URL_EN)

//    const val summonIconUrl = "$API_URL/icon/unit/000001.webp"

    private fun getImageId(unitId: Int, rarity: Int) = (if (rarity > 5) 6 else if (rarity > 2) 3 else 1) * 10 + unitId

    fun getUnitStillUrl(unitId: Int, rarity: Int) =
        String.format(Locale.US, STILL_UNIT_URL, (if (rarity > 5) 6 else 3) * 10 + unitId)

    fun getUnitPlateUrl(unitId: Int, rarity: Int) =
        String.format(Locale.US, UNIT_PLATE_URL, getImageId(unitId, rarity))

    fun getUnitIconUrl(unitId: Int, rarity: Int) =
        String.format(Locale.US, ICON_UNIT_URL, getImageId(unitId, rarity))

    fun getEnemyUnitIconUrl(unitId: Int): String {
        val id = if (unitId == 301305) 301300 else unitId
        return if (Helper.isShadowChara(unitId)) {
            String.format(Locale.US, ICON_UNIT_SHADOW_URL, "1${unitId.toString().substring(1, 4)}31".toInt())
        } else {
            String.format(Locale.US, ICON_UNIT_URL, id)
        }
    }

    fun getUserIconUrl(userId: Int) = String.format(Locale.US, ICON_UNIT_URL, userId)

    fun getUserStillUrl(userId: Int) =
        getUnitStillUrl(userId / 100 * 100 + 1, (userId % 100 - 1) / 10)

    fun getEquipIconUrl(equipId: Int) = String.format(Locale.US, ICON_EQUIPMENT_URL, equipId)

    fun getItemIconUrl(itemId: Int) = String.format(Locale.US, ICON_ITEM_URL, itemId)

    fun getSkillIconUrl(iconType: Int) = String.format(Locale.US, ICON_SKILL_URL, iconType)

    fun getAtkIconUrl(atkType: Int) = getEquipIconUrl(if (AtkType.isPhysical(atkType)) 101011 else 101251)

    fun getKanNaPlateUrl(unitId: Int, rarity: Int) =
        "$WTHEE_API_RESOURCE_URL/icon/plate/${getImageId(unitId, rarity)}.webp"

    fun getExEquipUrl(exEquipId: Int) = String.format(Locale.US, ICON_EX_EQUIPMENT_URL, exEquipId)

    fun getExEquipCategoryUrl(categoryId: Int) = String.format(Locale.US, ICON_EX_EQUIPMENT_CATEGORY_URL, categoryId)

//    fun getExEquipMapUrl(mapId: Int) = String.format(ICON_EX_EQUIPMENT_MAP_URL, mapId)
}
