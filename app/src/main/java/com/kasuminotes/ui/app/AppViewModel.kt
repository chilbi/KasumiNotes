package com.kasuminotes.ui.app

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.DialogNavigator
import com.kasuminotes.MainActivity
import com.kasuminotes.data.EquipData
import com.kasuminotes.data.ExEquipSlot
import com.kasuminotes.data.UniqueData
import com.kasuminotes.data.UserProfile
import com.kasuminotes.ui.app.state.CharaState
import com.kasuminotes.ui.app.state.DbState
import com.kasuminotes.ui.app.state.EquipState
import com.kasuminotes.ui.app.state.ExEquipState
import com.kasuminotes.ui.app.state.QuestState
import com.kasuminotes.ui.app.state.UiState
import kotlinx.coroutines.launch

class AppViewModel(appRepository: AppRepository = AppRepository()) : ViewModel() {
    val uiState = UiState(appRepository)
    val dbState = DbState(appRepository, viewModelScope)
    val charaState = CharaState(appRepository, viewModelScope, dbState.userState::changeMaxUserData)
    val equipState = EquipState(appRepository, viewModelScope)
    val questState = QuestState(appRepository, viewModelScope)
    val exEquipState = ExEquipState(appRepository, viewModelScope)

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
                }
                "quest" -> {
                    equipState.destroy()
                }
            }
        }
    }

    fun popBackStack() {
        navController.popBackStack()
    }

    fun navigateTo(selectedIndex: Int) {
        if (selectedIndex == 1 && !dbState.questInitializing) {
            questState.initState(dbState.userState.maxUserData!!.maxArea)
            navController.navigate("quest")
        } else if (selectedIndex == 0) {
            navController.popBackStack()
        }
    }

    fun navigateToAbout() {
        navController.navigate("about")
    }

    fun navigateToChara(userProfile: UserProfile) {
        charaState.selectUserProfile(
            userProfile,
            dbState.userState.charaListState.profiles,
            dbState.userState.maxUserData!!.maxCharaLevel
        )
        navController.navigate("chara")
    }

    fun navigateToEquipById(equipId: Int) {
        viewModelScope.launch {
            equipState.selectEquip(dbState.userState.maxUserData!!.maxArea, equipId)
            navController.navigate("equip")
        }
    }

    fun navigateToEquip(equipData: EquipData, slot: Int?) {
        if (slot == null) {
            equipState.selectEquipData(
                dbState.userState.maxUserData!!.maxArea,
                equipData
            )
        } else {
            equipState.selectEquipData(
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
        equipState.selectUniqueData(
            uniqueData,
            charaState.userData!!.uniqueLevel,
            dbState.userState.maxUserData!!.maxUniqueLevel,
            charaState::changeUniqueLevel
        )
        navController.navigate("equip")
    }

    fun navigateToExEquip(exEquipSlot: ExEquipSlot) {
        viewModelScope.launch {
            exEquipState.selectExEquipSlot(exEquipSlot, charaState.baseProperty)
            navController.navigate("exEquip")
        }
    }

    fun navigateToImages(allUserProfile: List<UserProfile>?, unlockedProfiles: List<UserProfile>) {
        navController.navigate("images")
        if (allUserProfile == null) {
            viewModelScope.launch {
                dbState.userState.charaListState.changeToImages(
                    dbState.userState.getAllProfiles(unlockedProfiles)
                )
            }
        } else {
            dbState.userState.charaListState.changeToImages(allUserProfile)
        }
    }

    fun navigateToEditor(allUserProfile: List<UserProfile>?, unlockedProfiles: List<UserProfile>) {
        navController.navigate("editor")
        if (allUserProfile == null) {
            viewModelScope.launch {
                dbState.userState.charaListState.changeToEditor(
                    dbState.userState.getAllProfiles(unlockedProfiles),
                    unlockedProfiles
                )
            }
        } else {
            dbState.userState.charaListState.changeToEditor(allUserProfile, unlockedProfiles)
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
