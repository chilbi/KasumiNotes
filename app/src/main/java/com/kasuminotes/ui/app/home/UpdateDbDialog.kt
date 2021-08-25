package com.kasuminotes.ui.app.home

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.kasuminotes.R

@Composable
fun UpdateDbDialog(
    newDbVersion: String,
    onUpdate: (newDbVersion: String) -> Unit,
    onCancel: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCancel,
        title = {
            Text(stringResource(R.string.update_db))
        },
        text = {
            Text(stringResource(R.string.update_db_message, newDbVersion))
        },
        confirmButton = {
            TextButton(onClick = { onUpdate(newDbVersion) }) {
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
