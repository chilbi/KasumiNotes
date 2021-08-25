package com.kasuminotes.ui.app.home

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.kasuminotes.R

@Composable
fun IsLastDbDialog(
    onConfirm: () -> Unit,
    onReDownload: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onConfirm,
        title = { Text(stringResource(R.string.check_complete)) },
        text = { Text(stringResource(R.string.is_last_db)) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(stringResource(R.string.confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onReDownload) {
                Text(stringResource(R.string.re_download))
            }
        }
    )
}
