package com.kasuminotes.db

import com.kasuminotes.data.CharaStoryStatus
import com.kasuminotes.data.ExSkillData
import com.kasuminotes.data.PromotionBonus
import com.kasuminotes.data.Property
import com.kasuminotes.data.SkillAction
import com.kasuminotes.data.UniqueData
import com.kasuminotes.data.UnitPromotion
import com.kasuminotes.data.UnitPromotionStatus
import com.kasuminotes.data.UnitRarity
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll

suspend fun AppDatabase.getUnitRarity(unitId: Int, rarity: Int): UnitRarity {
    val sql = """SELECT ${UnitRarity.getFields()}
FROM unit_rarity WHERE unit_id=$unitId AND rarity=$rarity"""

    return safelyUse {
        rawQuery(sql, null).use {
            it.moveToFirst()
            var i = 0

            val baseProperty = Property { _ ->
                it.getDouble(i++)
            }

            val growthProperty = Property { _ ->
                it.getDouble(i++)
            }

            UnitRarity(baseProperty, growthProperty)
        }
    }
}

suspend fun AppDatabase.getUnitPromotionStatus(unitId: Int, promotionLevel: Int): UnitPromotionStatus {
    if (promotionLevel < 2) {
        return UnitPromotionStatus(Property())
    }

    val sql = """SELECT ${UnitPromotionStatus.getFields()}
FROM unit_promotion_status WHERE unit_id=$unitId AND promotion_level=$promotionLevel"""

    return safelyUse {
        rawQuery(sql, null).use {
            it.moveToFirst()
            var i = 0

            val baseProperty = Property { _ ->
                it.getDouble(i++)
            }

            UnitPromotionStatus(baseProperty)
        }
    }
}

suspend fun AppDatabase.getUnitPromotion(unitId: Int, promotionLevel: Int): UnitPromotion {
    val sql = """SELECT ${UnitPromotion.getFields()}
FROM unit_promotion WHERE unit_id=$unitId AND promotion_level=$promotionLevel"""

    return withIOContext {
        val slots = use {
            rawQuery(sql, null).use {
                val list = mutableListOf<Int>()

                it.moveToFirst()
                var i = 0

                while (i < 6) {
                    list.add(it.getInt(i++))
                }

                list
            }
        }

        val equips = slots.map { slotId ->
            async {
                if (slotId != AppDatabase.NullId) getEquipData(slotId) else null
            }
        }.awaitAll()

        UnitPromotion(
            equips[0],
            equips[1],
            equips[2],
            equips[3],
            equips[4],
            equips[5]
        )
    }
}

suspend fun AppDatabase.getUniqueData(equipId: Int): UniqueData? {
    if (equipId == 0) return null

    val sql = """SELECT ${UniqueData.getFields("ued", "ueer")}
FROM unique_equipment_data AS ued
JOIN unique_equipment_enhance_rate AS ueer ON ued.equipment_id=ueer.equipment_id
WHERE ued.equipment_id=$equipId"""

    return safelyUse {
        rawQuery(sql, null).use {
            it.moveToFirst()
            var i = 0

            val baseProperty = Property { _ ->
                it.getDouble(i++)
            }

            val growthProperty = Property { _ ->
                it.getDouble(i++)
            }

            UniqueData(
                it.getInt(i++),
                it.getString(i++),
                it.getString(i),
                baseProperty,
                growthProperty
            )
        }
    }
}

suspend fun AppDatabase.getCharaStoryStatus(charaId: Int): CharaStoryStatus {
    // charaId > 10000 即是 unitId，转为 charaId
    val selfId = if (charaId > 10000) charaId / 100 else charaId

    val sql = """SELECT ${CharaStoryStatus.getFields()}
FROM chara_story_status WHERE chara_id_1=$selfId"""

    return safelyUse {
        rawQuery(sql, null).use {
            val status = mutableListOf<Property>()
            val sharedChara = mutableListOf<Int>()
            var flg = false

            while (it.moveToNext()) {
                var i = 0

                val pairs = mutableListOf<Pair<Int, Double>>()
                while (i < 10) {
                    val statusType = it.getInt(i++)
                    if (statusType == 0) break
                    val statusRate = it.getInt(i++)
                    pairs.add(statusType to statusRate.toDouble())
                }

                status.add(Property(pairs))

                if (flg) continue
                flg = true

                i = 10

                while (i < 19) {
                    val sharedId = it.getInt(i++)
                    if (sharedId == 0) break
                    // charaId -> unitId
                    sharedChara.add(sharedId * 100 + 1)
                }
            }

            CharaStoryStatus(status, sharedChara)
        }
    }
}

suspend fun AppDatabase.getExSkillData(unitId: Int): ExSkillData {
    val fields = (1..7).joinToString(",") { i -> "action_$i" }

    return withIOContext {
        val exSkillActions: List<List<SkillAction>> = listOf("ex_skill_1", "ex_skill_evolution_1").map { field ->
            val sql = """SELECT $fields
FROM skill_data JOIN unit_skill_data ON skill_id=$field
WHERE unit_id=$unitId"""

            async {
                val actions = use {
                    rawQuery(sql, null).use {
                        it.moveToFirst()
                        val actionList = mutableListOf<Int>()
                        var i = 0

                        while (i < 7) {
                            val actionId = it.getInt(i++)
                            if (actionId == 0) break

                            actionList.add(actionId)
                        }

                        actionList
                    }
                }

                actions.map { actionId ->
                    async { getSkillAction(actionId) }
                }.awaitAll()
            }
        }.awaitAll()

        ExSkillData(
            exActions = exSkillActions[0],
            exEvolutionActions = exSkillActions[1]
        )
    }
}

suspend fun AppDatabase.getPromotionBonusList(unitId: Int): List<PromotionBonus> {

    return withIOContext {
        val existsTable = use {
            rawQuery("SELECT count(*) FROM sqlite_master WHERE type=\"table\" AND name = \"promotion_bonus\"", null).use {
                it.moveToFirst()
                it.getInt(0) > 0
            }
        }

        if (existsTable) {
            val sql = "SELECT ${PromotionBonus.getFields()} FROM promotion_bonus WHERE unit_id=$unitId"
            use {
                rawQuery(sql, null). use {
                    val list = mutableListOf<PromotionBonus>()

                    while (it.moveToNext()) {
                        var i = 0

                        val baseProperty = Property { _ ->
                            it.getDouble(i++)
                        }

                        list.add(
                            PromotionBonus(
                                it.getInt(i),
                                baseProperty
                            )
                        )
                    }

                    list
                }
            }
        } else {
            emptyList()
        }
    }
}
