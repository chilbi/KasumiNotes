package com.kasuminotes.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFontLoader
import androidx.compose.ui.state.ToggleableState

@Composable
fun KasumiNotesTheme(
    darkThemeState: ToggleableState,
    content: @Composable () -> Unit
) {
    val darkTheme = when (darkThemeState) {
        ToggleableState.Indeterminate -> isSystemInDarkTheme()
        ToggleableState.On -> true
        ToggleableState.Off -> false
    }

    MaterialTheme(
        colors = if (darkTheme) DarkColorPalette else LightColorPalette,
        typography = Typography,
        shapes = Shapes
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            content()
        }
    }
}
