package com.kasuminotes.ui.app.home

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.kasuminotes.R
import com.kasuminotes.data.AppReleaseInfo

@Composable
fun UpdateAppDialog(
    info: AppReleaseInfo,
    onUpdate: (AppReleaseInfo) -> Unit,
    onCancel: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCancel,
        title = {
            Text(stringResource(R.string.update_app, info.versionName))
        },
        text = {
            var description = info.description.replace("\\r\\n- ", "\n")
            description = description.replace("\\n- ", "\n")
            if (description.startsWith("- ")) {
                description = description.substring(2)
            }
            Text(description)
        },
        confirmButton = {
            TextButton(onClick = { onUpdate(info) }) {
                Text(stringResource(R.string.update))
            }
        },
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text(stringResource(R.string.later))
            }
        }
    )
}
