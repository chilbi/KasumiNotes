package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.Property
import com.kasuminotes.data.SkillAction

class ActionBuilder(
    private val rawDepends: List<Int>,
    private val actions: List<SkillAction>,
    private val isExEquipPassive: Boolean
) {
    fun buildDescriptionList(skillLevel: Int, property: Property): List<D> {
        val originList = mutableListOf<D>()
        val processedBranchModifyIndexList = mutableListOf<Int>()
        val willRemoveIndexList = mutableListOf<Int>()
        val willModifyBranchList = mutableListOf<Pair<Int, D>>()
        val willModifyIgnoreProvocationIndexList = mutableListOf<Int>()
        var ignoreProvocation: D? = null

        actions.forEachIndexed { index, action ->
            val dependId = rawDepends[index]
            if (dependId != 0) {
                action.depend = actions.find { it.actionId == dependId }
            }
            originList.add(action.getDescription(skillLevel, property))
        }

        actions.forEachIndexed { index, action ->
            // Focus, 94Unknown
            if (
                action.actionType in arrayOf(7, 94) ||
                (action.actionType == 21 && action.actionValue1 == 0.0 && action.actionValue2 == 0.0)
            ) {
                willRemoveIndexList.add(index)
            }
            // AbnormalField
            if (action.actionType == 39) {
                val modifyIndex = actions.indexOfFirst { it.actionId == action.actionDetail1 }
                willRemoveIndexList.add(modifyIndex)
            }
            // GiveValue
            if (action.actionType in arrayOf(26, 27, 74)) {
                willRemoveIndexList.add(index)
                val modifyIndex = actions.indexOfFirst { it.actionId == action.actionDetail1 }
                originList[modifyIndex] = originList[modifyIndex].append(originList[index])
            }
            // HitCount
            if (action.actionType == 75) {
                willRemoveIndexList.add(index)
                val modifyIndex = actions.indexOfFirst { it.actionId == action.actionDetail2 }
                originList[modifyIndex] = originList[modifyIndex].insert(originList[index])
            }
            //InjuredEnergy
            if (action.actionType == 92) {
                willRemoveIndexList.add(index)
                rawDepends.forEachIndexed { dependIndex, dependActionId ->
                    if (
                        dependActionId == action.actionId &&
                        actions[dependIndex].actionType in arrayOf(1, 9, 36, 46, 79)
                    ) {
                        originList[dependIndex] = originList[dependIndex].append(action.getInjuredEnergy())
                    }
                }
            }
            //IgnoreProvocation
            if (action.actionType == 93) {
                willRemoveIndexList.add(index)
                val modifyIndex = actions.indexOfFirst { it.actionId == action.actionDetail1 }
                if (!willModifyIgnoreProvocationIndexList.contains(modifyIndex)) {
                    willModifyIgnoreProvocationIndexList.add(modifyIndex)
                }
                val dependIndexList = collectDependIndex(modifyIndex) { dependIndex ->
                    !willModifyIgnoreProvocationIndexList.contains(dependIndex)
                }
                willModifyIgnoreProvocationIndexList.addAll(dependIndexList)
                ignoreProvocation = action.getIgnoreProvocation()
            }
            // ExEquipPassive
            if (action.actionType in arrayOf(901, 902)) {
                willRemoveIndexList.add(index)
                var modifyIndex = index + 1
                if (actions[modifyIndex].actionType in arrayOf(26, 27, 74)) {
                    modifyIndex = actions.indexOfFirst { it.actionId == actions[modifyIndex].actionDetail1 }
                }
                originList[modifyIndex] = originList[modifyIndex].insert(originList[index])
            }
            // Branch
            if (action.actionType in arrayOf(23, 28, 42, 53)) {
                val branch = action.getBranch()
                if (branch.isEmpty()) {
                    if (action.actionDetail2 == 0 && action.actionDetail3 ==0) {
                        willRemoveIndexList.add(index)
                    } else {
                        originList[index] = action.getUnknown()
                    }
                } else {
                    willRemoveIndexList.add(index)
                    fun collectBranch(modifyActionId: Int, modifyContent: D, preContent: D?) {
                        val content = if (preContent == null) {
                            modifyContent
                        } else {
                            D.Join(arrayOf(
                                preContent,
                                D.Format(R.string.action_branch_and),
                                modifyContent
                            ))
                        }
                        val modifyIndex = actions.indexOfFirst { it.actionId == modifyActionId }
                        if (!processedBranchModifyIndexList.contains(modifyIndex)) {
                            val modifyAction = actions[modifyIndex]
                            if (modifyAction.actionType == 28) {
                                val andBranch = modifyAction.getBranch()
                                andBranch.forEach { andPair ->
                                    collectBranch(andPair.first, andPair.second, content)
                                }
                            } else {
                                willModifyBranchList.add(modifyIndex to content)
                            }
                            processedBranchModifyIndexList.add(modifyIndex)
                        }
                    }
                    branch.forEach { pair ->
                        val modifyActionId = pair.first
                        val modifyContent = pair.second
                        collectBranch(modifyActionId, modifyContent, null)
                        willModifyBranchList.toTypedArray().forEach { collectedPair ->
                            val mIndex = collectedPair.first
                            val mContent = collectedPair.second
                            val dependIndexList = collectDependIndex(mIndex) { dependIndex ->
                                val noContains = !processedBranchModifyIndexList.contains(dependIndex)
                                if (noContains) {
                                    processedBranchModifyIndexList.add(dependIndex)
                                }
                                noContains
                            }
                            dependIndexList.forEach { dependIndex ->
                                willModifyBranchList.add(dependIndex to mContent)
                            }
                        }
                    }
                }
            }
        }

        if (willModifyBranchList.isNotEmpty()) {
            willModifyBranchList.forEach { collectedPair ->
                val mIndex = collectedPair.first
                val mContent = collectedPair.second
                originList[mIndex] = originList[mIndex].insert(mContent)
            }
        }

        if (ignoreProvocation != null && willModifyIgnoreProvocationIndexList.isNotEmpty()) {
            willModifyIgnoreProvocationIndexList.forEach { willModifyIndex ->
                originList[willModifyIndex] = originList[willModifyIndex].insert(ignoreProvocation!!)
            }
        }

        return if (willRemoveIndexList.isEmpty()) originList
        else originList.filterIndexed { index, _ -> !willRemoveIndexList.contains(index) }
    }

    private fun collectDependIndex(index: Int, predicate: (dependIndex: Int) -> Boolean): List<Int> {
        val list = mutableListOf<Int>()
        rawDepends.forEachIndexed { dependIndex, dependActionId ->
            if (dependActionId == actions[index].actionId && predicate(dependIndex)) {
                list.add(dependIndex)
                list.addAll(collectDependIndex(dependIndex, predicate))
            }
        }
        return list
    }

    private fun SkillAction.getDescription(skillLevel: Int, property: Property): D {
        return when (actionType) {
            901, 902 -> getExEquipPassive()
            90 -> getExPassive(skillLevel)
            1 -> getDamage(skillLevel, property)
            2 -> getMove()
            3 -> getKnock()
            4 -> getHeal(skillLevel, property)
            6 -> getBarrier(skillLevel)
            7 -> D.Unknown/** [getFocus] */
            8 -> getAbnormal(skillLevel)
            9 -> getAbnormalDamage(skillLevel)
            10 -> getStatus(skillLevel, actions, if (isExEquipPassive) property else null)
            11 -> getCharm()
            12 -> getDarkness()
            13 -> getSilence()
            14 -> getMode()
            15 -> getSummon()
            16 -> getEnergy(skillLevel)
            17 -> getTrigger()
            18 -> getSustainDamage()
            20 -> getProvoke(skillLevel)
            21 -> getNoDamage(skillLevel)
            22 -> getPattern()
            23, 28, 42, 53 -> D.Unknown/** [getBranch] */
            26, 27, 74 -> getGiveValue(skillLevel, actions)
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
            46 -> getRatioDamage(skillLevel)
            48 -> getRegeneration(skillLevel, property)
            49 -> getDispel()
            50 -> getSustainStatus(skillLevel)
            54 -> getStealth()
            55 -> getMoveParts()
            56 -> getBlind()
            57 -> getCountDown()
            58 -> getRelieveField()
            59 -> getHpRecoveryDown()
            60, 77 -> getMark()
            72 -> getDamageCut()
            73 -> getDamageAttenuation()
            61 -> getFear()
            71 -> getKnightGuard(skillLevel, property)
            75 -> getHitCount()
            79 -> D.Unknown/** [getAbnormalField] */
            83 -> getSpeedOverlay()
            92 -> D.Unknown/** [getInjuredEnergy] */
            93 -> D.Unknown/** [getIgnoreProvocation] */
            95 -> getHiding()
            96 -> getEnergyField(skillLevel)
            97 -> getInjuredEnergyMark()
            98 -> getEnergyCut()
            99 -> getSpeedField()
            100 -> getAkinesiaInvalid()
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
