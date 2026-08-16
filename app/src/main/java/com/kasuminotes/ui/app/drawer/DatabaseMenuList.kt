package com.kasuminotes.ui.app.drawer

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.DonutSmall
import androidx.compose.material.icons.filled.Source
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.kasuminotes.R
import com.kasuminotes.common.DbServer
import com.kasuminotes.ui.components.SyncIcon
import kotlin.enums.enumEntries

@Composable
fun DatabaseMenuList(
    dbServer: DbServer,
    dbVersion: String,
    dbSource: Int,
    dbAutoUpdate: Boolean,
    lastVersionFetching: Boolean,
    onDbServerChange: (DbServer) -> Unit,
    onDbSourceChange: (Int) -> Unit,
    onLastDbVersionFetch: () -> Unit,
    onDbAutoUpdateToggle: () -> Unit
) {
    MenuCaption(stringResource(R.string.db_server))

    ListItemWithDropdownMenu(
        iconVector = Icons.Filled.Cloud,
        text = stringResource(dbServer.strId)
    ) { onCollapse ->
        val allServer = enumEntries<DbServer>().toMutableList()
        allServer.remove(dbServer)
        allServer.add(0, dbServer)
        allServer.forEach { server ->
            DropdownMenuItem(
                text = { MenuItemText(stringResource(server.strId)) },
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

    ListItemWithDropdownMenu(
        iconVector = Icons.Filled.Source,
        text = getDbSourceText(dbSource)
    ) { onCollapse ->
        val allSource = mutableListOf(0, 1)
        allSource.remove(dbSource)
        allSource.add(0, dbSource)
        allSource.forEach { source ->
            DropdownMenuItem(
                text = { MenuItemText(getDbSourceText(source)) },
                onClick = {
                    onDbSourceChange(source)
                    onCollapse()
                },
                trailingIcon = if (source == dbSource) { { CheckIcon()} } else null
            )
        }
    }

    ListItem(
        headlineContent = { Text(stringResource(R.string.auto_update)) },
        modifier = Modifier.clickable(onClick = onDbAutoUpdateToggle),
        leadingContent = { Icon(Icons.Filled.Update, null) },
        trailingContent = {
            Switch(
                checked = dbAutoUpdate,
                onCheckedChange = null
            )
        }
    )
}

private fun getDbSourceText(dbSource: Int) = when (dbSource) {
    1 -> "pcr.cialloworld.com"
    else -> "wthee.xyz"
}
