package com.kasuminotes.state

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
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

    lateinit var navController: NavHostController

    fun popBackStack() {
        navController.popBackStackSafe()
    }

    fun navigateTo(selectedIndex: Int) {
        when (selectedIndex) {
            0 -> {
                if (navController.currentDestination?.route != AppNavData.Home.route) {
                    navController.popBackStackSafe()
                }
            }
            1 -> {
                if (!dbState.questInitializing) {
                    questState.initQuest(dbState.userState.maxUserData!!.maxArea)
                    navController.navigateSafe(AppNavData.Quest.route) {
                        popUpTo(AppNavData.Home.route)
                    }
                }
            }
            2 -> {
                clanBattleState.initPeriodList()
                navController.navigateSafe(AppNavData.ClanBattle.route) {
                    popUpTo(AppNavData.Home.route)
                }
            }
        }
    }

    fun navigateToAbout() {
        navController.navigateSafe(AppNavData.About.route)
    }

    fun navigateToChara(userProfile: UserProfile) {
        charaState.initUserProfile(
            userProfile,
            dbState.userState.charaListState.profiles,
            dbState.userState.maxUserData!!.maxCharaLevel
        )
        navController.navigateSafe(AppNavData.Chara.route)
    }

    fun navigateToEquipById(equipId: Int) {
        equipState.destroy()
        equipState.initEquip(dbState.userState.maxUserData!!.maxArea, equipId)
        navController.navigateSafe(AppNavData.Equip.route)
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
        navController.navigateSafe(AppNavData.Equip.route)
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
        navController.navigateSafe(AppNavData.Equip.route)
    }

    fun navigateToExEquip(exEquipSlot: ExEquipSlot) {
        exEquipState.destroy()
        val slotNum = exEquipSlot.category / 100
        val originSubPercentList = charaState.userData!!.subPercentMap
            .getOrElse(slotNum) { null } ?: emptyList()
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
                        .getOrElse(index + 1) { null } ?: emptyList()
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
        navController.navigateSafe(AppNavData.ExEquip.route)
    }

    fun navigateToSummons(summons: List<Int>, skillLevel: Int) {
        summonsState.initSummons(summons, skillLevel, charaState.userData!!)
        navController.navigateSafe(AppNavData.Summons.route)
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
        navController.navigateSafe(AppNavData.Summons.route)
    }

    fun navigateToExtraEffect(extraEffectData: ExtraEffectData, epTableName: String) {
        summonsState.initMinionDataList(extraEffectData.enemyIdList, epTableName, extraEffectData)
        navController.navigateSafe(AppNavData.Summons.route)
    }

    fun navigateToMapList(label: String, period: ClanBattlePeriod) {
        clanBattleState.initPeriod(label, period)
        navController.navigateSafe(AppNavData.ClanBattleMapList.route)
    }

    fun navigateToClanBattleEnemy(enemyData: EnemyData, talentWeaknessList: List<Int>) {
        enemyState.initEnemy(enemyData, talentWeaknessList, "enemy_parameter", null)
        navController.navigateSafe(AppNavData.Enemy.route)
    }

    fun navigateToDungeonEnemy(enemyId: Int, talentWeaknessList: List<Int>, waveGroupId: Int?) {
        val isSucceed = enemyState.initEnemy(enemyId, talentWeaknessList, "enemy_parameter", waveGroupId)
        if (isSucceed) {
            navController.navigateSafe(AppNavData.Enemy.route)
        }
    }

    fun navigateToTalentQuestEnemy(enemyId: Int, waveGroupId: Int?) {
        val isSucceed = enemyState.initEnemy(enemyId, emptyList(), "talent_quest_enemy_parameter", waveGroupId)
        if (isSucceed) {
            navController.navigateSafe(AppNavData.Enemy.route)
        }
    }

    fun navigateToAbyssQuestEnemy(enemyId: Int, waveGroupId: Int?) {
        val isSucceed = enemyState.initEnemy(enemyId, emptyList(), "abyss_enemy_parameter", waveGroupId)
        if (isSucceed) {
            navController.navigateSafe(AppNavData.Enemy.route)
        }
    }

    fun navigateToMirageQuestEnemy(enemyId: Int, waveGroupId: Int?) {
        val isSucceed = enemyState.initEnemy(enemyId, emptyList(), "mirage_enemy_parameter", waveGroupId)
        if (isSucceed) {
            navController.navigateSafe(AppNavData.Enemy.route)
        }
    }

    fun navigateToDungeon() {
        dungeonState.initAreaDataList()
        navController.navigateSafe(AppNavData.Dungeon.route)
    }

    fun navigateToTalentQuest() {
        talentQuestState.initTalentQuestDataList()
        navController.navigateSafe(AppNavData.TalentQuest.route)
    }

    fun navigateToAbyssQuest() {
        abyssQuestState.initAbyssScheduleList()
        navController.navigateSafe(AppNavData.AbyssQuest.route)
    }

    fun navigateToMirageQuest() {
        mirageQuestState.initMirageQuest()
        navController.navigateSafe(AppNavData.MirageQuest.route)
    }

    fun navigateToImagesForChangeUserImage() {
        viewModelScope.launch {
            dbState.userState.charaListState.initImages(
                dbState.userState.getAllProfiles(dbState.userState.charaListState.profiles)
            )
            navController.navigateSafe(AppNavData.Images.route)
        }
    }

    fun navigateToImagesForSignInUserImage() {
        dbState.userState.charaListState.initImages(
            dbState.userState.allProfiles!!
        )
        navController.navigateSafe(AppNavData.Images.route)

    }

    fun navigateToEditorForChangeUserProfiles() {
        viewModelScope.launch {
            val unlockedProfiles = dbState.userState.charaListState.profiles
            dbState.userState.charaListState.initEditor(
                dbState.userState.getAllProfiles(unlockedProfiles),
                unlockedProfiles
            )
            navController.navigateSafe(AppNavData.Editor.route)
        }

    }

    fun navigateToEditorForSignInUserProfiles() {
        val unlockedProfiles = dbState.userState.newProfiles ?: emptyList()
        dbState.userState.charaListState.initEditor(
            dbState.userState.allProfiles!!,
            unlockedProfiles
        )
        navController.navigateSafe(AppNavData.Editor.route)
    }

    fun userEditorBack() {
        navController.popBackStackSafe()
        dbState.userState.charaListState.destroy()
    }

    fun confirmNewImage(userId: Int, userName: String) {
        if (dbState.userState.allProfiles == null) {
            dbState.userState.changeImage(userId, userName)
            navController.popBackStackSafe()
        } else {
            dbState.userState.changeNewUserIdName(userId, userName)
            navController.popBackStackSafe()
            dbState.userState.charaListState.destroy()
        }
    }

    fun confirmNewUserProfiles() {
        if (dbState.userState.allProfiles == null) {
            dbState.userState.confirmEditedProfiles()
            navController.popBackStackSafe()
        } else {
            dbState.userState.changeNewProfiles()
            navController.popBackStackSafe()
            dbState.userState.charaListState.destroy()
        }
    }

    fun linkTo(uriString: String) {
        val intent = Intent(Intent.ACTION_VIEW, uriString.toUri())
        MainActivity.instance.startActivity(intent)
    }
}

private object NavigationManager {
    private var lastNavigationTime = 0L
    private const val NAVIGATION_THROTTLE_TIME = 500L

    fun canNavigate(): Boolean {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastNavigationTime > NAVIGATION_THROTTLE_TIME) {
            lastNavigationTime = currentTime
            return true
        }
        return false
    }
}

private fun NavHostController.navigateSafe(route: String) {
    if (NavigationManager.canNavigate()) {
        this.navigate(route)
    }
}

private fun NavHostController.navigateSafe(route: String, builder: NavOptionsBuilder.() -> Unit) {
    if (NavigationManager.canNavigate()) {
        this.navigate(route, builder)
    }
}

private fun NavHostController.popBackStackSafe() {
    if (NavigationManager.canNavigate()) {
        this.popBackStack()
    }
}
