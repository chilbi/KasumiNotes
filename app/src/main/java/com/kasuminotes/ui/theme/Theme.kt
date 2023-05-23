package com.kasuminotes.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.state.ToggleableState

@Composable
fun KasumiNotesTheme(
    themeIndex: Int,
    darkThemeState: ToggleableState,
    content: @Composable () -> Unit
) {
    val theme = Themes[themeIndex].value
    val darkTheme = when (darkThemeState) {
        ToggleableState.Indeterminate -> isSystemInDarkTheme()
        ToggleableState.On -> true
        ToggleableState.Off -> false
    }

    MaterialTheme(
        colorScheme = if (darkTheme) theme.DarkColors else theme.LightColors,
        shapes = Shapes,
        typography = Typography,
        content = content
    )
}
