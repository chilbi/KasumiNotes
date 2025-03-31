package com.kasuminotes.ui.app.drawer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalAbsoluteTonalElevation
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.dp
import com.kasuminotes.common.DbServer
import com.kasuminotes.common.Language
import com.kasuminotes.data.MaxUserData

@Composable
fun DrawerContent(
    userId: Int,
    userName: String,
    maxUserData: MaxUserData?,
    dbServer: DbServer,
    dbVersion: String,
    appAutoUpdate: Boolean,
    dbAutoUpdate: Boolean,
    lastVersionFetching: Boolean,
    latestAppReleaseInfoFetching: Boolean,
    language: Language,
    themeIndex: Int,
    darkTheme: ToggleableState,
    onImageClick: () -> Unit,
    onLogOut: () -> Unit,
    onDbServerChange: (DbServer) -> Unit,
    onLastDbVersionFetch: () -> Unit,
    onDbAutoUpdateToggle: () -> Unit,
    onLanguageChange: (Language) -> Unit,
    onThemeIndexChange: (Int) -> Unit,
    onDarkThemeToggle: () -> Unit,
    onLatestAppReleaseInfoFetch: () -> Unit,
    onAppAutoUpdateToggle: () -> Unit,
    onAboutClick: () -> Unit
) {
    val color = MaterialTheme.colorScheme.surface
    val contentColor = contentColorFor(color)
    val absoluteElevation = LocalAbsoluteTonalElevation.current + DrawerDefaults.ModalDrawerElevation
    val backgroundColor = MaterialTheme.colorScheme.surfaceColorAtElevation(absoluteElevation)

    CompositionLocalProvider(
        LocalContentColor provides contentColor,
        LocalAbsoluteTonalElevation provides absoluteElevation
    ) {
        Column(
            Modifier
                .sizeIn(
                    minWidth = 240.dp,// MinimumDrawerWidth,
                    maxWidth = 320.dp,// MaximumDrawerWidth
                )
                .fillMaxHeight()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .background(backgroundColor)
        ) {

            UserImage(userId, backgroundColor)

            UserHeader(
                userId,
                userName,
                backgroundColor,
                onImageClick,
                onLogOut
            )

            UserInfo(maxUserData)

            HorizontalDivider(Modifier.padding(16.dp))

            DatabaseMenuList(
                dbServer,
                dbVersion,
                dbAutoUpdate,
                lastVersionFetching,
                onDbServerChange,
                onLastDbVersionFetch,
                onDbAutoUpdateToggle
            )

            HorizontalDivider(Modifier.padding(16.dp))

            DisplayMenuList(
                language,
                themeIndex,
                darkTheme,
                onLanguageChange,
                onThemeIndexChange,
                onDarkThemeToggle
            )

            HorizontalDivider(Modifier.padding(16.dp))

            AppMenuList(
                appAutoUpdate,
                latestAppReleaseInfoFetching,
                onLatestAppReleaseInfoFetch,
                onAppAutoUpdateToggle,
                onAboutClick
            )
        }
    }
}
