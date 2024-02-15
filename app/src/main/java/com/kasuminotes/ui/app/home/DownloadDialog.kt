package com.kasuminotes.ui.app.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.DialogProperties
import com.kasuminotes.R
import com.kasuminotes.common.DownloadState

@Composable
fun DownloadDialog(
    state: DownloadState,
    onRetry: () -> Unit,
    onCancel: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {},
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
        title = {
            Text(stringResource(R.string.download_database))
        },
        text = {
            DownloadDialogContent(state)
        },
        confirmButton = {
            TextButton(
                onClick = onRetry,
                enabled = state is DownloadState.Error
            ) {
                Text(stringResource(R.string.retry))
            }
        },
        dismissButton = {
            TextButton(
                onClick = onCancel,
                enabled = state is DownloadState.Error
            ) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}

@Composable
private fun DownloadDialogContent(state: DownloadState) {
    val progress: Float?
    val text: String

    when (state) {
        is DownloadState.Progress -> {
            val value = state.bytesRead.toFloat()
            val total = state.contentLength.toFloat()
            progress = value / total
            text = stringResource(R.string.downloading, value / 1024f, total / 1024f)
        }
        is DownloadState.Loading -> {
            progress = null
            text = stringResource(R.string.download_loading)
        }
        is DownloadState.Success -> {
            progress = null
            text = stringResource(R.string.download_success)
        }
        is DownloadState.Error -> {
            progress = 0f
            text = stringResource(R.string.download_failed, state.e.message.toString())
        }
    }

    Column {
        if (progress != null) {
            LinearProgressIndicator(progress = { progress }, modifier = Modifier.fillMaxWidth())
        } else {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }
        Text(text, style = MaterialTheme.typography.bodySmall)
    }
}
