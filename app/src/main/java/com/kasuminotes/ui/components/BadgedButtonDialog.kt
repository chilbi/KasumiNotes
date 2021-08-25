package com.kasuminotes.ui.components

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Dialog

@ExperimentalMaterialApi
@Composable
fun BadgedButtonDialog(
    value: Int,
    originValue: Int,
    label: @Composable () -> Unit,
    content: @Composable (onClose: () -> Unit) -> Unit
) {
    var visible by rememberSaveable { mutableStateOf(false) }
    val onOpen = { visible = true }
    val onClose = { visible = false }

    BadgedButton(value, originValue, onOpen, label)

    if (visible) {
        Dialog(onClose) {
            content(onClose)
        }
    }
}
