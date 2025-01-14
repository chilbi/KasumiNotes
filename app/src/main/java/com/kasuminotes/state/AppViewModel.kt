package com.kasuminotes.state

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.DialogNavigator
import com.kasuminotes.MainActivity
import com.kasuminotes.data.ClanBattlePeriod
import com.kasuminotes.data.EnemyData
import com.kasuminotes.data.EquipData
import com.kasuminotes.data.ExEquipSlot
import com.kasuminotes.data.ExtraEffectData
import com.kasuminotes.data.UniqueData
import com.kasuminotes.data.UserData
import com.kasuminotes.data.UserProfile
import com.kasuminotes.ui.app.AppNavData
import com.kasuminotes.ui.app.AppRepository
import com.kasuminotes.utils.Helper
import kotlinx.coroutines.launch
import androidx.core.net.toUri
import com.kasuminotes.data.Property

class AppViewModel(appRepository: AppRepository = AppRepository()) : ViewModel() {
    val uiState = UiState(appRepository)
    val dbState = DbState(appRepository, viewModelScope)
    val charaState = CharaState(appRepository, viewModelScope, dbState.userState::changeMaxUserData)
    val equipState = EquipState(appRepository, viewModelScope)
    val questState = QuestState(appRepository, viewModelScope)
    val exEquipState = ExEquipState(appRepository, viewModelScope)
    val summonsState = SummonsState(appRepository, viewModelScope)
    val enemyState = EnemyState(appRepository, viewModelScope)
    val clanBattleState = ClanBattleState(appRepository, viewModelScope)
    val dungeonState = DungeonState(appRepository, viewModelScope)
    val talentQuestState = TalentQuestState(appRepository, viewModelScope)
    val abyssQuestState = AbyssQuestState(appRepository, viewModelScope)
    val mirageQuestState = MirageQuestState(appRepository, viewModelScope)

    val navController = NavHostController(appRepository.applicationContext).apply {
        navigatorProvider.addNavigator(ComposeNavigator())
        navigatorProvider.addNavigator(DialogNavigator())

        addOnDestinationChangedListener { _, destination, _ ->
            when (destination.route) {
                AppNavData.Home.route -> {
                    charaState.destroy()
                    dbState.userState.charaListState.destroy()
                }
            }
        }
    }

    fun popBackStack() {
        navController.popBackStack()
    }

    fun navigateTo(selectedIndex: Int) {
        when (selectedIndex) {
            0 -> {
                if (navController.currentDestination?.route != AppNavData.Home.route) {
                    navController.popBackStack()
                }
            }
            1 -> {
                if (!dbState.questInitializing) {
                    questState.initQuest(dbState.userState.maxUserData!!.maxArea)
                    navController.navigate(AppNavData.Quest.route) {
                        popUpTo(AppNavData.Home.route)
                    }
                }
            }
            2 -> {
                clanBattleState.initPeriodList()
                navController.navigate(AppNavData.ClanBattle.route) {
                    popUpTo(AppNavData.Home.route)
                }
            }
        }
    }

    fun navigateToAbout() {
        navController.navigate(AppNavData.About.route)
    }

    fun navigateToChara(userProfile: UserProfile) {
        charaState.initUserProfile(
            userProfile,
            dbState.userState.charaListState.profiles,
            dbState.userState.maxUserData!!.maxCharaLevel
        )
        navController.navigate(AppNavData.Chara.route)
    }

    fun navigateToEquipById(equipId: Int) {
        equipState.destroy()
        equipState.initEquip(dbState.userState.maxUserData!!.maxArea, equipId)
        navController.navigate(AppNavData.Equip.route)
    }

    fun navigateToEquip(equipData: EquipData, slot: Int?) {
        equipState.destroy()
        if (slot == null) {
            equipState.initEquipData(
                equipData,
                dbState.userState.maxUserData!!.maxArea
            )
        } else {
            equipState.initEquipData(
                equipData,
                dbState.userState.maxUserData!!.maxArea,
                charaState.userData!!.getEquipLevel(slot),
                onLevelChange = { value, _ ->
                    charaState.changeEquipLevel(value, slot)
                }
            )
        }
        navController.navigate(AppNavData.Equip.route)
    }

    fun navigateToUnique(uniqueData: UniqueData, slot: Int) {
        equipState.destroy()
        if (slot == 1) {
            equipState.initUnique1Data(
                uniqueData,
                charaState.userData!!.unique1Level,
                dbState.userState.maxUserData!!.maxUniqueLevel,
                charaState::changeUniqueLevel
            )
        } else {
            equipState.initUnique2Data(
                uniqueData,
                charaState.userData!!.unique2Level,
                charaState::changeUniqueLevel
            )
        }
        navController.navigate(AppNavData.Equip.route)
    }

    fun navigateToExEquip(exEquipSlot: ExEquipSlot) {
        exEquipState.destroy()
        val slotNum = exEquipSlot.category / 100
        val originSubPercentList = charaState.userData!!.subPercentMap
            .getOrDefault(slotNum, null) ?: emptyList()
        val originLevel = when (slotNum) {
            1 -> charaState.userData!!.exEquip1Level
            2 -> charaState.userData!!.exEquip2Level
            else -> charaState.userData!!.exEquip3Level
        }
        val base = charaState.baseProperty
        val exSkill = charaState.exSkillProperty
        val slots = charaState.userProfile!!.exEquipSlots
        val userData = charaState.userData!!
        val otherExEquipProperty = if (slots.isEmpty()) {
            Property.zero
        } else {
            val propertyList = slots.mapIndexed { index, slot ->
                if (slot.exEquipData == null || slotNum == index + 1) {
                    Property.zero
                } else {
                    val percent = slot.exEquipData.getPercentProperty(
                        when (index) {
                            0 -> userData.exEquip1Level
                            1 -> userData.exEquip2Level
                            else -> userData.exEquip3Level
                        }
                    )
                    val subPercentList = userData.subPercentMap
                        .getOrDefault(index + 1, null) ?: emptyList()
                    slot.exEquipData.getExEquipProperty(subPercentList, percent, base)
                }
            }
            Property { i -> propertyList.sumOf { it[i] } }
        }
        val withoutSelfBattleProperty = Property { i ->
            base[i] + exSkill[i] + otherExEquipProperty[i]
        }
        exEquipState.initExEquipSlot(
            slotNum,
            exEquipSlot,
            charaState.baseProperty,
            withoutSelfBattleProperty,
            originSubPercentList,
            originLevel,
            charaState::changeExEquip,
            charaState::changeExEquipLevel,
            charaState::changeSubPercentList
        )
        navController.navigate(AppNavData.ExEquip.route)
    }

    fun navigateToSummons(summons: List<Int>, skillLevel: Int) {
        summonsState.initSummons(summons, skillLevel, charaState.userData!!)
        navController.navigate(AppNavData.Summons.route)
    }

    fun navigateToMinions(minions: List<Int>, skillLevel: Int, enemyData: EnemyData, epTableName: String) {
        if (Helper.isShadowChara(enemyData.unitId)) {
            val shadowUserData = UserData(
                0,
                enemyData.unitId,
                enemyData.rarity,
                enemyData.level,
                0,
                0,
                0,
                enemyData.promotionLevel,
                enemyData.unionBurstLevel,
                enemyData.mainSkillLvList[0],
                enemyData.mainSkillLvList[1],
                enemyData.exSkillLvList[0],
                5,
                5,
                5,
                5,
                5,
                5
            )
            summonsState.initSummons(minions, skillLevel, shadowUserData)
        } else {
            summonsState.initMinionDataList(minions, epTableName, null)
        }
        navController.navigate(AppNavData.Summons.route)
    }

    fun navigateToExtraEffect(extraEffectData: ExtraEffectData, epTableName: String) {
        summonsState.initMinionDataList(extraEffectData.enemyIdList, epTableName, extraEffectData)
        navController.navigate(AppNavData.Summons.route)
    }

    fun navigateToMapList(label: String, period: ClanBattlePeriod) {
        clanBattleState.initPeriod(label, period)
        navController.navigate(AppNavData.ClanBattleMapList.route)
    }

    fun navigateToClanBattleEnemy(enemyData: EnemyData, talentWeaknessList: List<Int>) {
        enemyState.initEnemy(enemyData, talentWeaknessList, "enemy_parameter", null)
        navController.navigate(AppNavData.Enemy.route)
    }

    fun navigateToDungeonEnemy(enemyId: Int, talentWeaknessList: List<Int>, waveGroupId: Int?) {
        val isSucceed = enemyState.initEnemy(enemyId, talentWeaknessList, "enemy_parameter", waveGroupId)
        if (isSucceed) {
            navController.navigate(AppNavData.Enemy.route)
        }
    }

    fun navigateToTalentQuestEnemy(enemyId: Int, waveGroupId: Int?) {
        val isSucceed = enemyState.initEnemy(enemyId, emptyList(), "talent_quest_enemy_parameter", waveGroupId)
        if (isSucceed) {
            navController.navigate(AppNavData.Enemy.route)
        }
    }

    fun navigateToAbyssQuestEnemy(enemyId: Int, waveGroupId: Int?) {
        val isSucceed = enemyState.initEnemy(enemyId, emptyList(), "abyss_enemy_parameter", waveGroupId)
        if (isSucceed) {
            navController.navigate(AppNavData.Enemy.route)
        }
    }

    fun navigateToMirageQuestEnemy(enemyId: Int, waveGroupId: Int?) {
        val isSucceed = enemyState.initEnemy(enemyId, emptyList(), "mirage_enemy_parameter", waveGroupId)
        if (isSucceed) {
            navController.navigate(AppNavData.Enemy.route)
        }
    }

    fun navigateToDungeon() {
        dungeonState.initAreaDataList()
        navController.navigate(AppNavData.Dungeon.route)
    }

    fun navigateToTalentQuest() {
        talentQuestState.initTalentQuestDataList()
        navController.navigate(AppNavData.TalentQuest.route)
    }

    fun navigateToAbyssQuest() {
        abyssQuestState.initAbyssScheduleList()
        navController.navigate(AppNavData.AbyssQuest.route)
    }

    fun navigateToMirageQuest() {
        mirageQuestState.initMirageQuest()
        navController.navigate(AppNavData.MirageQuest.route)
    }

    fun navigateToImagesForChangeUserImage() {
        viewModelScope.launch {
            dbState.userState.charaListState.initImages(
                dbState.userState.getAllProfiles(dbState.userState.charaListState.profiles)
            )
            navController.navigate(AppNavData.Images.route)
        }
    }

    fun navigateToImagesForSignInUserImage() {
        dbState.userState.charaListState.initImages(
            dbState.userState.allProfiles!!
        )
        navController.navigate(AppNavData.Images.route)

    }

    fun navigateToEditorForChangeUserProfiles() {
        viewModelScope.launch {
            val unlockedProfiles = dbState.userState.charaListState.profiles
            dbState.userState.charaListState.initEditor(
                dbState.userState.getAllProfiles(unlockedProfiles),
                unlockedProfiles
            )
            navController.navigate(AppNavData.Editor.route)
        }

    }

    fun navigateToEditorForSignInUserProfiles() {
        val unlockedProfiles = dbState.userState.newProfiles ?: emptyList()
        dbState.userState.charaListState.initEditor(
            dbState.userState.allProfiles!!,
            unlockedProfiles
        )
        navController.navigate(AppNavData.Editor.route)
    }

    fun userEditorBack() {
        navController.popBackStack()
        dbState.userState.charaListState.destroy()
    }

    fun confirmNewImage(userId: Int, userName: String) {
        if (dbState.userState.allProfiles == null) {
            dbState.userState.changeImage(userId, userName)
            navController.popBackStack()
        } else {
            dbState.userState.changeNewUserIdName(userId, userName)
            navController.popBackStack()
            dbState.userState.charaListState.destroy()
        }
    }

    fun confirmNewUserProfiles() {
        if (dbState.userState.allProfiles == null) {
            dbState.userState.confirmEditedProfiles()
            navController.popBackStack()
        } else {
            dbState.userState.changeNewProfiles()
            navController.popBackStack()
            dbState.userState.charaListState.destroy()
        }
    }

    fun linkTo(uriString: String) {
        val intent = Intent(Intent.ACTION_VIEW, uriString.toUri())
        MainActivity.instance.startActivity(intent)
    }
}
