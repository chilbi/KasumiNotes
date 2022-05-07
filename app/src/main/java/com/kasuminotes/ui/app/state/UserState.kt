package com.kasuminotes.ui.app.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.kasuminotes.data.MaxUserData
import com.kasuminotes.data.User
import com.kasuminotes.data.UserData
import com.kasuminotes.data.UserProfile
import com.kasuminotes.db.AppDatabase
import com.kasuminotes.db.deleteUser
import com.kasuminotes.db.deleteUserData
import com.kasuminotes.db.getAllUser
import com.kasuminotes.db.getMaxUserData
import com.kasuminotes.db.getUserList
import com.kasuminotes.db.getUserName
import com.kasuminotes.db.getUserProfileList
import com.kasuminotes.db.putUserDataList
import com.kasuminotes.ui.app.AppRepository
import com.kasuminotes.ui.app.DefaultUserId
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class UserState(
    private val appRepository: AppRepository,
    private val scope: CoroutineScope
) {
    var allProfiles by mutableStateOf<List<UserProfile>?>(null)
        private set
    var userList by mutableStateOf<List<User>?>(null)
        private set
    var allUser by mutableStateOf<List<Int>>(emptyList())
        private set
    var userId by mutableStateOf(appRepository.getUserId())
        private set
    var userName by mutableStateOf("")
        private set
    var maxUserData by mutableStateOf<MaxUserData?>(null)
        private set
    var newUserId by mutableStateOf<Int?>(null)
        private set
    var newUserName by mutableStateOf<String?>(null)
        private set
    var newProfiles by mutableStateOf<List<UserProfile>?>(null)
        private set

    val charaListState = CharaListState()

    fun logOut() {
        if (allUser.size == 1) {
            openSignIn()
        } else {
            openLogIn()
        }
    }

    fun openSignIn() {
        scope.launch {
            allProfiles = getAllProfiles(charaListState.profiles)
        }
    }

    fun closeSignIn() {
        allProfiles = null
        newUserId = null
        newUserName = null
        newProfiles = null
    }

    private fun openLogIn() {
        scope.launch {
            val db = appRepository.getDatabase()
            userList = db.getUserList()
        }
    }

    fun closeLogIn() {
        userList = null
    }

    fun signIn() {
        val user = newUserId!!
        val name = newUserName!!
        val profilesData = getProfilesData(newProfiles!!, user)

        allUser = allUser.plus(user)
        userId = user
        userName = name

        maxUserData = maxUserData!!.copy(
            userChara = profilesData.userProfiles.size,
            userUnique = profilesData.userUnique,
            userRarity6 = profilesData.userRarity6
        )

        charaListState.changeProfiles(profilesData.userProfiles)

        allProfiles = null
        newUserId = null
        newUserName = null
        newProfiles = null

        appRepository.setUserId(user)

        scope.launch {
            val db = appRepository.getDatabase()
            db.putUserDataList(profilesData.userDataList)
        }
    }

    fun logIn(user: User) {
        if (user.userId != userId) {
            scope.launch {
                val db = appRepository.getDatabase()
                val list = listOf(
                    async { db.getMaxUserData(user.userId) },
                    async { db.getUserProfileList(user.userId) }
                ).awaitAll()

                userId = user.userId
                userName = user.userName
                maxUserData = list[0] as MaxUserData
                @Suppress("UNCHECKED_CAST")
                charaListState.changeProfiles(list[1] as List<UserProfile>)

                userList = null

                appRepository.setUserId(user.userId)
            }
        }
    }

    fun deleteUser(user: User) {
        userList = userList!!.filter { it.userId != user.userId }
        allUser = allUser.filter { it != user.userId }
        if (user.userId == userId) {
            logIn(userList!!.find { it.userId == DefaultUserId }!!)
        }
        scope.launch {
            val db = appRepository.getDatabase()
            db.deleteUser(user.userId)
        }
    }

    fun clearAllUser() {
        allUser = emptyList()
    }

    fun changeNewUserIdName(id: Int, name: String) {
        newUserId = id
        newUserName = name
    }

    fun changeNewProfiles() {
        val lockedChara = charaListState.lockedChara
        newProfiles = charaListState.profiles.filter { userProfile ->
            !lockedChara.contains(userProfile.unitData.unitId)
        }
    }

    fun confirmEditedProfiles() {
        val user = userId
        val lockedChara = charaListState.lockedChara

        val hasLockedChara = lockedChara.isNotEmpty()

        val profiles = if (hasLockedChara) {
            charaListState.profiles.filter { userProfile ->
                !lockedChara.contains(userProfile.unitData.unitId)
            }
        } else {
            charaListState.profiles
        }

        val profilesData = getProfilesData(profiles, user)
        maxUserData = maxUserData!!.copy(
            userChara = profilesData.userProfiles.size,
            userUnique = profilesData.userUnique,
            userRarity6 = profilesData.userRarity6
        )

        val deleteChara = if (hasLockedChara) {
            val list = mutableListOf<Int>()
            charaListState.backupProfiles!!.forEach { userProfile ->
                val unitId = userProfile.unitData.unitId
                if (lockedChara.contains(unitId)) {
                    list.add(unitId)
                }
            }
            list
        } else {
            emptyList()
        }

        charaListState.destroy(profilesData.userProfiles)

        scope.launch {
            val db = appRepository.getDatabase()
            if (hasLockedChara) {
                db.deleteUserData(user, deleteChara)
            }
            db.putUserDataList(profilesData.userDataList)
        }
    }

    fun changeImage(id: Int, name: String) {
        val originUser = userId
        userId = id
        userName = name
        val newAllUser = allUser.filter { it != originUser }
        allUser = newAllUser.plus(id)

        val profiles = charaListState.backupProfiles!!

        val userDataList = mutableListOf<UserData>()

        profiles.forEach { userProfile ->
            val userData = userProfile.userData.copy(userId = id)
            userProfile.userData = userData
            userDataList.add(userData)
        }

        charaListState.destroy(profiles)

        appRepository.setUserId(id)

        scope.launch {
            val db = appRepository.getDatabase()
            db.putUserDataList(userDataList)
            db.deleteUser(originUser)
        }
    }

    fun changeMaxUserData(userUniqueDiff: Int, userRarity6Diff: Int) {
        maxUserData = maxUserData!!.copy(
            userUnique = maxUserData!!.userUnique + userUniqueDiff,
            userRarity6 = maxUserData!!.userRarity6 + userRarity6Diff
        )
    }

    fun updateStateFromDb(db: AppDatabase) {
        scope.launch {
            val users = allUser.ifEmpty {
                allUser = db.getAllUser()
                allUser
            }
            var user = userId
            if (!users.contains(user)) {
                user = DefaultUserId
                userId = DefaultUserId
                appRepository.setUserId(DefaultUserId)
            }

            val list = listOf(
                async { db.getUserName(user) },
                async { db.getMaxUserData(user) },
                async { db.getUserProfileList(user) }
            ).awaitAll()
            userName = list[0] as String
            maxUserData = list[1] as MaxUserData
            @Suppress("UNCHECKED_CAST")
            charaListState.changeProfiles(list[2] as List<UserProfile>)
        }
    }

    suspend fun getAllProfiles(unlockedProfiles: List<UserProfile>): List<UserProfile> {
        return if (unlockedProfiles.size == maxUserData!!.maxChara) {
            unlockedProfiles.map { userProfile ->
                UserProfile(userProfile.userData.copy(userId = 0), userProfile.unitData)
            }
        } else {
            val db = appRepository.getDatabase()
            db.getUserProfileList(DefaultUserId)
        }
    }

    private fun getProfilesData(profiles: List<UserProfile>, user: Int): ProfilesData {
        var userUnique = 0
        var userRarity6 = 0
        val userDataList = mutableListOf<UserData>()

        profiles.forEach { userProfile ->
            val userData = userProfile.userData.copy(userId = user)
            userProfile.userData = userData
            userDataList.add(userData)

            if (userProfile.userData.uniqueLevel > 0) {
                userUnique++
            }
            if (userProfile.userData.rarity > 5) {
                userRarity6++
            }
        }

        return ProfilesData(userUnique, userRarity6, userDataList, profiles)
    }

    private class ProfilesData(
        val userUnique: Int,
        val userRarity6: Int,
        val userDataList: List<UserData>,
        val userProfiles: List<UserProfile>
    )
}
