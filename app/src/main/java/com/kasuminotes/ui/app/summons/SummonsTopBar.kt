package com.kasuminotes.ui.app.summons

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.kasuminotes.R
import com.kasuminotes.ui.components.BackButton
import com.kasuminotes.ui.components.ImmersiveTopAppBar

@Composable
fun SummonsTopBar(onBack: () -> Unit) {
    ImmersiveTopAppBar(
        title = { Text(stringResource(R.string.summons_info)) },
        navigationIcon = { BackButton(onBack) }
    )
}
