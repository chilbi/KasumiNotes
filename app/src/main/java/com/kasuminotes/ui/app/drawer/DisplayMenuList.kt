package com.kasuminotes.ui.app.drawer

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brightness4
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.state.ToggleableState
import com.kasuminotes.R
import com.kasuminotes.common.Language

@Composable
fun DisplayMenuList(
    language: Language,
    darkTheme: ToggleableState,
    onLanguageChange: (Language) -> Unit,
    onDarkThemeToggle: () -> Unit
) {
    MenuCaption(stringResource(R.string.display))

    ListItemWithDropdownMenu(
        iconVector = Icons.Filled.Language,
        text = stringResource(language.resId)
    ) { onCollapse ->
        listOf(
            language,
            if (language == Language.CN) Language.JP else Language.CN
        ).forEach { lang ->
            DropdownMenuItem(
                text = { MenuItemText(stringResource(lang.resId)) },
                onClick = {
                    onLanguageChange(lang)
                    onCollapse()
                },
                trailingIcon = if (lang == language) { { CheckIcon() } } else null
            )
        }
    }

    ListItem(
        headlineContent = {
            Text(
                stringResource(
                    when (darkTheme) {
                        ToggleableState.Indeterminate -> R.string.follow_system
                        ToggleableState.On -> R.string.night
                        ToggleableState.Off -> R.string.day
                    }
                )
            )
        },
        modifier = Modifier.clickable(onClick = onDarkThemeToggle),
        leadingContent = { Icon(Icons.Filled.Brightness4, null) },
        trailingContent = { TriStateCheckbox(state = darkTheme, onClick = null) }
    )
}
