package com.kasuminotes.ui.app.drawer

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.kasuminotes.BuildConfig
import com.kasuminotes.R
import com.kasuminotes.ui.components.SyncIcon

@Composable
fun AppMenuList(
    appAutoUpdate: Boolean,
    latestAppReleaseInfoFetching: Boolean,
    onLatestAppReleaseInfoFetch: () -> Unit,
    onAppAutoUpdateToggle: () -> Unit,
    onAboutClick: () -> Unit
) {
    MenuCaption(stringResource(R.string.app))

    ListItem(
        headlineContent = { Text("v${BuildConfig.VERSION_NAME}") },
        modifier = Modifier.clickable(onClick = onLatestAppReleaseInfoFetch),
        leadingContent = { Icon(Icons.Filled.Android, null) },
        trailingContent = { SyncIcon(latestAppReleaseInfoFetching) }
    )

    ListItem(
        headlineContent = { Text(stringResource(R.string.auto_update)) },
        modifier = Modifier.clickable(onClick = onAppAutoUpdateToggle),
        leadingContent = { Icon(Icons.Filled.Update, null) },
        trailingContent = {
            Switch(
                checked = appAutoUpdate,
                onCheckedChange = null,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    uncheckedThumbColor = Color.White,
                    uncheckedBorderColor = Color.Transparent
                )
            )
        }
    )

    ListItem(
        headlineContent = { Text(stringResource(R.string.about)) },
        modifier = Modifier.clickable(onClick = onAboutClick),
        leadingContent = { Icon(Icons.Filled.Info, null) },
        trailingContent = { Icon(Icons.AutoMirrored.Filled.ArrowForward, null) }
    )
}
