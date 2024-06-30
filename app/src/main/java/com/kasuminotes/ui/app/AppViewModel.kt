package com.kasuminotes.ui.app

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.DialogNavigator
import com.kasuminotes.MainActivity
import com.kasuminotes.data.ClanBattlePeriod
import com.kasuminotes.data.DungeonAreaData
import com.kasuminotes.data.EnemyData
import com.kasuminotes.data.EquipData
import com.kasuminotes.data.ExEquipSlot
import com.kasuminotes.data.UniqueData
import com.kasuminotes.data.UserProfile
import com.kasuminotes.ui.app.state.CharaState
import com.kasuminotes.ui.app.state.ClanBattleState
import com.kasuminotes.ui.app.state.DbState
import com.kasuminotes.ui.app.state.DungeonState
import com.kasuminotes.ui.app.state.EquipState
import com.kasuminotes.ui.app.state.ExEquipState
import com.kasuminotes.ui.app.state.QuestState
import com.kasuminotes.ui.app.state.SummonsState
import com.kasuminotes.ui.app.state.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AppViewModel(appRepository: AppRepository = AppRepository()) : ViewModel() {
    val uiState = UiState(appRepository)
    val dbState = DbState(appRepository, viewModelScope)
    val charaState = CharaState(appRepository, viewModelScope, dbState.userState::changeMaxUserData)
    val equipState = EquipState(appRepository, viewModelScope)
    val questState = QuestState(appRepository, viewModelScope)
    val exEquipState = ExEquipState(appRepository, viewModelScope)
    val summonsState = SummonsState(appRepository, viewModelScope)
    val clanBattleState = ClanBattleState(appRepository, viewModelScope)
    val dungeonState = DungeonState(appRepository, viewModelScope)

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
        exEquipState.initExEquipSlot(
            exEquipSlot,
            charaState.baseProperty,
            when (exEquipSlot.category / 100) {
                1 -> charaState.userData!!.exEquip1Level
                2 -> charaState.userData!!.exEquip2Level
                else -> charaState.userData!!.exEquip3Level
            },
            charaState::changeExEquip,
            charaState::changeExEquipLevel
        )
        navController.navigate(AppNavData.ExEquip.route)
    }

    fun navigateToSummons(summons: List<Int>, skillLevel: Int) {
        summonsState.initSummons(summons, skillLevel, charaState.userData!!)
        navController.navigate(AppNavData.Summons.route)
    }

    fun navigateToMinions(minions: List<Int>, skillLevel: Int) {
        skillLevel + 0
        summonsState.initMinionDataList(minions)
        navController.navigate(AppNavData.Summons.route)
    }

    fun navigateToMapList(label: String, period: ClanBattlePeriod) {
        clanBattleState.initPeriod(label, period)
        navController.navigate(AppNavData.ClanBattleMapList.route)
    }

    fun navigateToEnemy(enemyData: EnemyData, talentWeaknessList: List<Int>) {
        clanBattleState.initEnemy(enemyData, talentWeaknessList)
        navController.navigate(AppNavData.ClanBattleEnemy.route)
    }

    fun navigateToEnemyById(enemyId: Int, talentWeaknessList: List<Int>) {
        val isSucceed = clanBattleState.initEnemy(enemyId, talentWeaknessList)
        if (isSucceed) {
            navController.navigate(AppNavData.ClanBattleEnemy.route)
        }
    }

    fun navigateToDungeon() {
        dungeonState.initAreaDataList()
        navController.navigate(AppNavData.Dungeon.route)
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
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uriString))
        MainActivity.instance.startActivity(intent)
    }
}
