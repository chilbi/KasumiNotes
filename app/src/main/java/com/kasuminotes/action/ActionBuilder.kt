package com.kasuminotes.action

import com.kasuminotes.data.Property
import com.kasuminotes.data.SkillAction

class ActionBuilder(
    private val rawDepends: List<Int>,
    private val actions: List<SkillAction>,
    private val isExEquipPassive: Boolean
) {
    fun buildDescriptionList(skillLevel: Int, property: Property): List<D> {
        val origin = mutableListOf<D>()
        val remove = mutableListOf<Int>()
        val processedBranch = mutableListOf<Int>()

        actions.forEachIndexed { index, action ->
            val dependId = rawDepends[index]
            if (dependId != 0) {
                action.depend = actions.find { it.actionId == dependId }
            }
            origin.add(action.getDescription(skillLevel, property))
        }

        actions.forEachIndexed { index, action ->
            // ExEquipPassive
            if (arrayOf(901, 902).contains(action.actionType)) {
                remove.add(index)
                var modifyIndex = index + 1
                if (arrayOf(26, 27, 74).contains(actions[modifyIndex].actionType)) {
                    modifyIndex = actions.indexOfFirst { it.actionId == actions[modifyIndex].actionDetail1 }
                }
                origin[modifyIndex] = origin[modifyIndex].insert(origin[index])
            }
            // Focus, 94Unknown
            if (action.actionType == 7 || action.actionType == 94) {
                remove.add(index)
            }
            // AbnormalField
            if (action.actionType == 39) {
                val modifyIndex = actions.indexOfFirst { it.actionId == action.actionDetail1 }
                remove.add(modifyIndex)
            }
            // Modify
            if (arrayOf(26, 27, 74).contains(action.actionType)) {
                remove.add(index)
                val modifyIndex = actions.indexOfFirst { it.actionId == action.actionDetail1 }
                origin[modifyIndex] = origin[modifyIndex].append(origin[index])
            }
            // HitCount
            if (action.actionType == 75) {
                remove.add(index)
                val modifyIndex = actions.indexOfFirst { it.actionId == action.actionDetail2 }
                origin[modifyIndex] = origin[modifyIndex].insert(origin[index])
            }
            // Branch
            if (arrayOf(23, 28, 42, 53).contains(action.actionType)) {
                val branch = action.getBranch()
                if (branch.isEmpty()) {
                    if (action.actionDetail2 == 0 && action.actionDetail3 ==0) {
                        remove.add(index)
                    } else {
                        origin[index] = action.getUnknown()
                    }
                } else {
                    remove.add(index)
                    branch.forEach { pair ->
                        val modifyActionId = pair.first
                        val modifyContent = pair.second
                        val modifyIndex = actions.indexOfFirst { it.actionId == modifyActionId }
                        if (!processedBranch.contains(modifyActionId)) {
                            origin[modifyIndex] = origin[modifyIndex].insert(modifyContent)
                            processedBranch.add(modifyActionId)
                        }
                        rawDepends.forEachIndexed { dependIndex, dependActionId ->
                            if (dependActionId == modifyActionId) {
                                val modifyDependActionId = actions[dependIndex].actionId
                                if (!processedBranch.contains(modifyDependActionId)) {
                                    origin[dependIndex] = origin[dependIndex].insert(modifyContent)
                                    processedBranch.add(modifyDependActionId)
                                }
                            }
                        }
                    }
                }
            }
            //InjuredEnergy
            if (action.actionType == 92) {
                remove.add(index)
                rawDepends.forEachIndexed { dependIndex, dependActionId ->
                    if (dependActionId == action.actionId) {
                        origin[dependIndex] = origin[dependIndex].append(action.getInjuredEnergy())
                    }
                }
            }
        }

        return if (remove.isEmpty()) origin
        else origin.filterIndexed { index, _ -> !remove.contains(index) }
    }

    private fun SkillAction.getDescription(skillLevel: Int, property: Property): D {
        return when (actionType) {
            901, 902 -> getExEquipPassive()
            90 -> getPassive(skillLevel)
            1 -> getDamage(skillLevel, property)
            2 -> getMove()
            3 -> getKnock()
            4 -> getHeal(skillLevel, property)
            6 -> getBarrier(skillLevel)
            7 -> D.Unknown//Focus
            8 -> getAbnormal()
            9 -> getAbnormalDamage(skillLevel)
            10 -> getStatus(skillLevel, if (isExEquipPassive) property else null)
            11 -> getCharm()
            12 -> getDarkness()
            13 -> getUncontrol()
            14 -> getMode()
            15 -> getSummon()
            16 -> getEnergy(skillLevel)
            17 -> getTrigger()
            18 -> getPosture()
            20 -> getProvoke(skillLevel)
            21 -> getNoDamage(skillLevel)
            22 -> getPattern()
            23, 28, 42, 53 -> D.Unknown//Branch
            26, 27, 74 -> getModify(skillLevel, actions)
            30 -> getDestroy()
            32 -> getLifeSteal(skillLevel)
            33 -> getShieldCounter(skillLevel)
            34 -> getAccumulativeDamage(skillLevel)
            35 -> getChangeMark()
            36 -> getDamageField(skillLevel, property)
            37 -> getHealField(skillLevel, property)
            38 -> getStatusField(skillLevel)
            39 -> getAbnormalField(skillLevel, actions)
            44 -> getWaveStartIdle()
            45 -> getSkillCounter()
            46 -> getRatioDamage()
            48 -> getRegeneration(skillLevel, property)
            49 -> getDispel()
            50 -> getSustain(skillLevel)
            56 -> getBlind()
            57 -> getCountDown()
            58 -> getRelieveField()
            59 -> getHpRecoveryDown()
            60, 77 -> getMark()
            72 -> getCutDamage()
            61 -> getFear()
            71 -> getKnightGuard(skillLevel, property)
            75 -> getHitCount()
            79 -> D.Unknown//PoisonDamageByBehaviour
            83 -> getGuildOfStrength()
            92 -> D.Unknown//InjuredEnergy
            95 -> getHiding()
            96 -> getEnergyField(skillLevel)
            97 -> getInjuredEnergyMark()
            else -> getUnknown()
        }
    }
}

//val UnknownType = arrayOf<Any>(
//
//    15, "シノブ、ネネカ、チカ、チカ（クリスマス）、スズメ（サマー）",
//
//    23, "ぺコリーヌ（プリンセス）、レイ（ハロウィン）、クリスティーナ（クリスマス）、ミサト（サマー）、マコト（サマー）",
//    23, "イオ、タマキ、アン、レム、ルナ、アオイ、アオイ（編入生）、カヤ（タイムトラベル）、ミツキ（オーエド）、ホマレ、ミサキ",
//    23, "キャル（オーバーロード）、ミミ（サマー）",
//
//    28, "クロエ（聖学祭）、チエル（聖学祭）、リノ（ワンダー）、スズナ（サマー）、マコト（サマー）、ルカ（サマー）、アンナ（サマー）",
//    28, "レイ、カヤ、スズメ、エリコ、ニノン、ミフユ、アリサ、ルナ、シノブ（ハロウィン）、クリスティーナ（クリスマス）",
//    28, "ぺコリーヌ（ニューイヤー）、ホマレ、ルカ（ニューイヤー）、ぺコリーヌ（オーバーロード）、ランファ、キャル（サマー）",
//    28, "イノリ（怪盗）、カヤ（タイムトラベル）、ミミ（サマー）、シノブ",
//
//    35, "アン、レイ、シェフィ、チエル、ルナ、マツリ（ハロウィン）、クリスティーナ（クリスマス）、カリン、ホマレ",
//    35, "ラビリスタ（オーバーロード）、ユニ（聖学祭）",
//
//    45, "カヤ、アリサ、シェフィ、クロエ（聖学祭）、チカ（サマー）、スズナ（サマー）、ルカ（サマー）",
//
//    60, "クロエ、チェル、ルナ、チエル（聖学祭）",
//
//    75, "ルカ（サマー）、クロエ（聖学祭）",
//
//    77, "アキノ（クリスマス）",
//)
