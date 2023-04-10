package com.kasuminotes.ui.app.drawer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brightness4
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kasuminotes.R
import com.kasuminotes.common.Language
import com.kasuminotes.ui.theme.PrimaryPalettes

@Composable
fun DisplayMenuList(
    language: Language,
    themeIndex: Int,
    darkTheme: ToggleableState,
    onLanguageChange: (Language) -> Unit,
    onThemeIndexChange: (Int) -> Unit,
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

    val shape = MaterialTheme.shapes.medium
    val style = MaterialTheme.typography.labelMedium
    val themeColorStr = stringResource(R.string.theme_color)
    ListItemWithDropdownMenu(
        iconVector = Icons.Filled.Palette,
        text = themeColorStr
    ) { onCollapse ->
        PrimaryPalettes.forEachIndexed { index, color ->
            DropdownMenuItem(
                text = {
                    Text(
                        text = themeColorStr + (index + 1),
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color, shape)
                            .padding(vertical = 4.dp),
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        style = style
                    )
                },
                onClick = {
                    onThemeIndexChange(index)
                    onCollapse()
                },
                trailingIcon = {
                    if (index == themeIndex) CheckIcon() else Spacer(Modifier.size(24.dp))
                }
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
