package com.kasuminotes.ui.app.drawer

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.DonutSmall
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.kasuminotes.R
import com.kasuminotes.common.DbServer

@Composable
fun DatabaseMenuList(
    dbServer: DbServer,
    dbVersion: String,
    dbAutoUpdate: Boolean,
    lastVersionFetching: Boolean,
    onDbServerChange: (DbServer) -> Unit,
    onLastDbVersionFetch: () -> Unit,
    onDbAutoUpdateToggle: () -> Unit
) {
    MenuCaption(stringResource(R.string.db_server))

    ListItemWithDropdownMenu(
        iconVector = Icons.Filled.Cloud,
        text = stringResource(dbServer.resId)
    ) { onCollapse ->
        listOf(
            dbServer,
            if (dbServer == DbServer.CN) DbServer.JP else DbServer.CN
        ).forEach { server ->
            DropdownMenuItem(
                text = { MenuItemText(stringResource(server.resId)) },
                onClick = {
                    onDbServerChange(server)
                    onCollapse()
                },
                trailingIcon = if (server == dbServer) { { CheckIcon()} } else null
            )
        }
    }

    ListItem(
        headlineContent = { Text("v$dbVersion") },
        modifier = Modifier.clickable(onClick = onLastDbVersionFetch),
        leadingContent = { Icon(Icons.Filled.DonutSmall, null) },
        trailingContent = { SyncIcon(lastVersionFetching) }
    )

    ListItem(
        headlineContent = { Text(stringResource(R.string.auto_update)) },
        modifier = Modifier.clickable(onClick = onDbAutoUpdateToggle),
        leadingContent = { Icon(Icons.Filled.Update, null) },
        trailingContent = {
            Switch(
                checked = dbAutoUpdate,
                onCheckedChange = null,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    uncheckedThumbColor = Color.White,
                    uncheckedBorderColor = Color.Transparent
                )
            )
        }
    )
}
