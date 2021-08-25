package com.kasuminotes.ui.app.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.state.ToggleableState
import com.kasuminotes.common.Language
import com.kasuminotes.ui.app.AppRepository

class UiState(
    private val appRepository: AppRepository
) {
    var language by mutableStateOf(appRepository.getLanguage())
        private set
    var darkTheme by mutableStateOf(appRepository.getDarkTheme())
        private set
    var charaImageState by mutableStateOf(CharaImageState(appRepository.getImageVariant()))
        private set

    fun changeLanguage(value: Language) {
        if (value != language) {
            language = value
            appRepository.setLanguage(value)
        }
    }

    fun toggleDarkTheme() {
        val value = when (darkTheme) {
            ToggleableState.Indeterminate -> ToggleableState.On
            ToggleableState.On -> ToggleableState.Off
            ToggleableState.Off -> ToggleableState.Indeterminate
        }
        darkTheme = value
        appRepository.setDarkTheme(value)
    }

    fun toggleImageVariant() {
        val nextVariant = charaImageState.nextVariant
        charaImageState = CharaImageState(nextVariant)
        appRepository.setImageVariant(nextVariant)
    }
}
