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
        val willRemoveIndexList = mutableListOf<Int>()

        actions.forEachIndexed { index, action ->
            val dependId = rawDepends[index]
            if (dependId != 0) {
                action.depend = actions.find { it.actionId == dependId }
            }
            originList.add(action.getDescription(skillLevel, property))
        }

        val branchModify = ModifyDescription(rawDepends, actions)
        val ignoreProvocationModify = ModifyDescription(rawDepends, actions)
        val totalCriticalModifyList = mutableListOf<Pair<Int, Int>>()

        actions.forEachIndexed { index, action ->
            if (action.depend != null && action.depend!!.depend == action) {
                willRemoveIndexList.add(index)
            }
            /** [getTargetFocus], 94 [getUnknown] */
            if (action.actionType in arrayOf(7, 94)) {
                willRemoveIndexList.add(index)
            }
            /** [getSustainDamage] */
            if (action.actionType == 18 && action.actionValue1 == 0.0) {
                willRemoveIndexList.add(rawDepends.indexOfFirst { it == action.actionId })
            }
            /** [getAbnormalField] */
            if (action.actionType == 39) {
                val modifyIndex = actions.indexOfFirst { it.actionId == action.actionDetail1 }
                willRemoveIndexList.add(modifyIndex)
            }
            /** [getGiveValue] */
            if (action.actionType in arrayOf(26, 27, 74)) {
                willRemoveIndexList.add(index)
                val modifyIndex = actions.indexOfFirst { it.actionId == action.actionDetail1 }
                originList[modifyIndex] = originList[modifyIndex].append(originList[index])
            }
            /** [getDamageBaseAtk] */
            if (action.actionType == 103) {
                willRemoveIndexList.add(index)
                val modifyIndex = actions.indexOfFirst { it.actionId == action.actionDetail2 }
                originList[modifyIndex] = originList[modifyIndex].append(originList[index])
            }
            /** [getTotalCritical] */
            if (action.actionType == 107) {
                willRemoveIndexList.add(index)
                val modifyIndex = actions.indexOfFirst { it.actionId == action.actionDetail1 }
                totalCriticalModifyList.add(modifyIndex to index)
            }
            /** [getHitCount] */
            if (action.actionType == 75) {
                willRemoveIndexList.add(index)
                val modifyIndex = actions.indexOfFirst { it.actionId == action.actionDetail2 }
                originList[modifyIndex] = originList[modifyIndex].insert(originList[index])
            }
            /** [getInjuredEnergy] */
            if (action.actionType == 92) {
                willRemoveIndexList.add(index)
                val injuredEnergyContent = action.getInjuredEnergy()
                val modifyTypes = arrayOf(1, 9, 36, 46, 79)
                val willRemoveTypes = arrayOf(7, 93)
                rawDepends.forEachIndexed { dependIndex, dependActionId ->
                    if (dependActionId == action.actionId) {
                        val dependAction = actions[dependIndex]
                        if (dependAction.actionType in modifyTypes) {
                            originList[dependIndex] = originList[dependIndex].append(injuredEnergyContent)
                        } else if (dependAction.actionType in willRemoveTypes) {
                            val willRemoveModify = ModifyDescription(rawDepends, actions)
                            val targetIndex = when (dependAction.actionType) {
                                7 -> dependIndex
                                93 -> actions.indexOfFirst { it.actionId == dependAction.actionDetail1 }
                                else -> 0
                            }
                            willRemoveModify.collectModify(targetIndex, injuredEnergyContent)
                            willRemoveModify.collectDepend()
                            willRemoveModify.forEachModify { modifyIndex, modifyContent ->
                                if (actions[modifyIndex].actionType in modifyTypes) {
                                    originList[modifyIndex] = originList[modifyIndex].append(modifyContent)
                                }
                            }
                        }
                    }
                }
            }
            /** [getExEquipPassive] */
            if (action.actionType in arrayOf(901, 902)) {
                willRemoveIndexList.add(index)
                var modifyIndex = index + 1
                if (actions[modifyIndex].actionType in arrayOf(26, 27, 74)) {
                    modifyIndex = actions.indexOfFirst { it.actionId == actions[modifyIndex].actionDetail1 }
                }
                originList[modifyIndex] = originList[modifyIndex].insert(originList[index])
            }
            /** [getBranch] */
            if (action.actionType in arrayOf(23, 28, 42, 53)) {
                val branch = action.getBranch()
                if (branch.isEmpty()) {
                    if (action.actionDetail2 == 0 && action.actionDetail3 == 0) {
                        willRemoveIndexList.add(index)
                    } else {
                        originList[index] = action.getUnknown()
                    }
                } else {
                    willRemoveIndexList.add(index)
                    branch.forEach { item ->
                        branchModify.collectBranch(item.first, item.second, null)
                    }
                }
            }
            /** [getIgnoreProvocation] */
            if (action.actionType == 93) {
                willRemoveIndexList.add(index)
                val ignoreProvocation = action.getIgnoreProvocation()
                val modifyIndex = actions.indexOfFirst { it.actionId == action.actionDetail1 }
                ignoreProvocationModify.collectModify(modifyIndex, ignoreProvocation)
                ignoreProvocationModify.collectModify(index, ignoreProvocation)
            }
        }

        if (totalCriticalModifyList.isNotEmpty()) {
            totalCriticalModifyList.forEach { item ->
                originList[item.first] = originList[item.first].append(originList[item.second])
            }
        }

        branchModify.collectDepend()
        branchModify.forEachModify { modifyIndex, modifyContent ->
            originList[modifyIndex] = originList[modifyIndex].insert(modifyContent)
        }

        ignoreProvocationModify.collectDepend()
        ignoreProvocationModify.forEachModify { modifyIndex, modifyContent ->
            originList[modifyIndex] = originList[modifyIndex].insert(modifyContent)
        }

        return if (willRemoveIndexList.isEmpty()) originList
        else originList.filterIndexed { index, _ -> !willRemoveIndexList.contains(index) }
    }

    private fun SkillAction.getDescription(skillLevel: Int, property: Property): D {
        return when (actionType) {
            901, 902 -> getExEquipPassive()
            90 -> getExSkillPassive(skillLevel)
            1 -> getDamage(skillLevel, property)
            2 -> getMove()
            3 -> getKnock()
            4 -> getHeal(skillLevel, property)
            6 -> getBarrier(skillLevel)
            7 -> D.Unknown/** [getTargetFocus] */
            8 -> getAbnormal(skillLevel)
            9 -> getAbnormalDamage(skillLevel)
            10 -> getStatus(skillLevel, actions, if (isExEquipPassive) property else null)
            11 -> getCharm()
            12 -> getDarkness()
            13 -> getSilence()
            14 -> getChangeMode()
            15 -> getSummon()
            16 -> getChangeEnergy(skillLevel)
            17 -> getTrigger()
            18 -> getSustainDamage()
            20 -> getProvoke(skillLevel)
            21 -> getNoDamage(skillLevel)
            22 -> getChangePattern()
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
            60, 77, 101 -> getMark()
            61 -> getFear()
            71 -> getKnightGuard(skillLevel, property)
            72 -> getDamageCut()
            73 -> getDamageAttenuation()
            75 -> getHitCount()
            79 -> getFixedDamage()
            83 -> getSpeedOverlay()
            92 -> D.Unknown/** [getInjuredEnergy] */
            93 -> D.Unknown/** [getIgnoreProvocation] */
            95 -> getHiding()
            96 -> getEnergyField(skillLevel)
            97 -> getInjuredEnergyMark()
            98 -> getEnergyCut()
            99 -> getSpeedField()
            100 -> getAkinesiaInvalid()
            103 -> getDamageBaseAtk()
            105 -> getEnvironment()
            106 -> getFallenAngelGuard()
            107 -> getTotalCritical()
            else -> getUnknown()
        }
    }
}

private class ModifyDescription(
    private val rawDepends: List<Int>,
    private val actions: List<SkillAction>,
) {
    private val processedModifyIndexList = mutableListOf<Int>()
    private val willModifyList = mutableListOf<Pair<Int, D>>()

    private fun collectDependIndex(index: Int): List<Int> {
        val list = mutableListOf<Int>()
        rawDepends.forEachIndexed { dependIndex, dependActionId ->
            if (dependActionId == actions[index].actionId && !processedModifyIndexList.contains(dependIndex)) {
                processedModifyIndexList.add(dependIndex)
                list.add(dependIndex)
                list.addAll(collectDependIndex(dependIndex))
            }
        }
        return list
    }

    fun forEachModify(action: (modifyIndex: Int, modifyContent: D) -> Unit) {
        if (willModifyList.isNotEmpty()) {
            willModifyList.forEach { item ->
                action(item.first, item.second)
            }
        }
    }

    fun collectDepend() {
        willModifyList.toTypedArray().forEach { item ->
            val modifyIndex = item.first
            val modifyContent = item.second
            val dependIndexList = collectDependIndex(modifyIndex)
            dependIndexList.forEach { dependIndex ->
                willModifyList.add(dependIndex to modifyContent)
            }
        }
    }

    fun collectModify(modifyIndex: Int, modifyContent: D) {
        if (!processedModifyIndexList.contains(modifyIndex)) {
            processedModifyIndexList.add(modifyIndex)
            willModifyList.add(modifyIndex to modifyContent)
        }
    }

    fun collectBranch(modifyActionId: Int, modifyContent: D, preContent: D?) {
        val content = if (preContent == null) {
            modifyContent
        } else {
            D.Join(arrayOf(preContent, D.Format(R.string.action_branch_and), modifyContent))
        }
        val modifyIndex = actions.indexOfFirst { it.actionId == modifyActionId }
        if (modifyIndex > -1 && !processedModifyIndexList.contains(modifyIndex)) {
            val modifyAction = actions[modifyIndex]
            if (modifyAction.isBranch()) {
                val andBranch = modifyAction.getBranch()
                andBranch.forEach { andPair ->
                    collectBranch(andPair.first, andPair.second, content)
                }
            } else {
                willModifyList.add(modifyIndex to content)
            }
            processedModifyIndexList.add(modifyIndex)
        }
    }
}
