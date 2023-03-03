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
import com.kasuminotes.data.EnemyData
import com.kasuminotes.data.EquipData
import com.kasuminotes.data.ExEquipSlot
import com.kasuminotes.data.UniqueData
import com.kasuminotes.data.UserProfile
import com.kasuminotes.ui.app.state.CharaState
import com.kasuminotes.ui.app.state.ClanBattleState
import com.kasuminotes.ui.app.state.DbState
import com.kasuminotes.ui.app.state.EquipState
import com.kasuminotes.ui.app.state.ExEquipState
import com.kasuminotes.ui.app.state.QuestState
import com.kasuminotes.ui.app.state.SummonsState
import com.kasuminotes.ui.app.state.UiState
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

    val navController = NavHostController(appRepository.applicationContext).apply {
        navigatorProvider.addNavigator(ComposeNavigator())
        navigatorProvider.addNavigator(DialogNavigator())

        addOnDestinationChangedListener { _, destination, _ ->
            when (destination.route) {
                "home" -> {
                    charaState.destroy()
                    questState.destroy()
                    dbState.userState.charaListState.destroy()
                }
                "chara" -> {
                    equipState.destroy()
                    exEquipState.destroy()
                    summonsState.destroy()
                }
                "quest" -> {
                    equipState.destroy()
                    exEquipState.destroy()
                }
                "clanBattleMapList" -> {
                    clanBattleState.destroy()
                }
                "clanBattleEnemy" -> {
                    summonsState.destroy()
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
                navController.popBackStack()
            }
            1 -> {
                if (!dbState.questInitializing) {
                    questState.initQuest(dbState.userState.maxUserData!!.maxArea)
                    navController.navigate("quest") {
                        popUpTo("home")
                    }
                }
            }
            2 -> {
                clanBattleState.initPeriodList()
                navController.navigate("clanBattle") {
                    popUpTo("home")
                }
            }
        }
    }

    fun navigateToAbout() {
        navController.navigate("about")
    }

    fun navigateToChara(userProfile: UserProfile) {
        charaState.initUserProfile(
            userProfile,
            dbState.userState.charaListState.profiles,
            dbState.userState.maxUserData!!.maxCharaLevel
        )
        navController.navigate("chara")
    }

    fun navigateToEquipById(equipId: Int) {
        equipState.initEquip(dbState.userState.maxUserData!!.maxArea, equipId)
        navController.navigate("equip")
    }

    fun navigateToEquip(equipData: EquipData, slot: Int?) {
        if (slot == null) {
            equipState.initEquipData(
                dbState.userState.maxUserData!!.maxArea,
                equipData
            )
        } else {
            equipState.initEquipData(
                dbState.userState.maxUserData!!.maxArea,
                equipData,
                charaState.userData!!.getEquipLevel(slot),
                onLevelChange = { value ->
                    charaState.changeEquipLevel(slot, value)
                }
            )
        }
        navController.navigate("equip")
    }

    fun navigateToUnique(uniqueData: UniqueData) {
        equipState.initUniqueData(
            uniqueData,
            charaState.userData!!.uniqueLevel,
            dbState.userState.maxUserData!!.maxUniqueLevel,
            charaState::changeUniqueLevel
        )
        navController.navigate("equip")
    }

    fun navigateToExEquip(exEquipSlot: ExEquipSlot) {
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
        navController.navigate("exEquip")
    }

    fun navigateToSummons(summons: List<Int>, skillLevel: Int) {
        summonsState.initSummons(summons, skillLevel, charaState.userData!!)
        navController.navigate("summons")
    }

    fun navigateToMinions(minions: List<Int>, skillLevel: Int) {
        skillLevel + 0
        summonsState.initMinionDataList(minions)
        navController.navigate("summons")
    }

    fun navigateToMapList(label: String, period: ClanBattlePeriod) {
        clanBattleState.initPeriod(label, period)
        navController.navigate("clanBattleMapList")
    }

    fun navigateToEnemy(enemyData: EnemyData) {
        clanBattleState.initEnemy(enemyData)
        navController.navigate("clanBattleEnemy")
    }

    fun navigateToImages(allUserProfile: List<UserProfile>?, unlockedProfiles: List<UserProfile>) {
        navController.navigate("images")
        if (allUserProfile == null) {
            viewModelScope.launch {
                dbState.userState.charaListState.initImages(
                    dbState.userState.getAllProfiles(unlockedProfiles)
                )
            }
        } else {
            dbState.userState.charaListState.initImages(allUserProfile)
        }
    }

    fun navigateToEditor(allUserProfile: List<UserProfile>?, unlockedProfiles: List<UserProfile>) {
        navController.navigate("editor")
        if (allUserProfile == null) {
            viewModelScope.launch {
                dbState.userState.charaListState.initEditor(
                    dbState.userState.getAllProfiles(unlockedProfiles),
                    unlockedProfiles
                )
            }
        } else {
            dbState.userState.charaListState.initEditor(allUserProfile, unlockedProfiles)
        }
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
