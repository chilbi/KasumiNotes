package com.kasuminotes.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.state.ToggleableState

//@Composable
//fun KasumiNotesTheme(
//    darkThemeState: ToggleableState,
//    content: @Composable () -> Unit
//) {
//    val darkTheme = when (darkThemeState) {
//        ToggleableState.Indeterminate -> isSystemInDarkTheme()
//        ToggleableState.On -> true
//        ToggleableState.Off -> false
//    }
//
//    MaterialTheme(
//        colors = if (darkTheme) DarkColorPalette else LightColorPalette,
//        typography = Typography,
//        shapes = Shapes
//    ) {
//        Surface(
//            modifier = Modifier.fillMaxSize(),
//            color = MaterialTheme.colors.background
//        ) {
//            content()
//        }
//    }
//}

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

    androidx.compose.material3.MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        shapes = Shapes,
        typography = Typography,
        content = content
    )
}
