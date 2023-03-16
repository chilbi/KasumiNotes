package com.kasuminotes.ui.app.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.kasuminotes.R
import com.kasuminotes.ui.components.Infobar
import com.kasuminotes.ui.components.PlaceImage
import com.kasuminotes.utils.UrlUtil

@Composable
fun SignInDialog(
    newUserId: Int?,
    newUserName: String?,
    charaCount: Int,
    maxChara: Int,
    onImageClick: () -> Unit,
    onEditClick: () -> Unit,
    onClose: () -> Unit,
    onSignIn: () -> Unit
) {
    Dialog(onClose) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = AlertDialogDefaults.TonalElevation
        ) {
            Column(Modifier.fillMaxWidth().padding(16.dp)) {
                SignInTitle()

                Column(Modifier.padding(8.dp)) {
                    SignInImage(
                        newUserId,
                        newUserName,
                        onImageClick
                    )

                    Divider(Modifier.padding(vertical = 16.dp))

                    SignInCharaCount(
                        charaCount,
                        maxChara,
                        onEditClick
                    )
                }

                Row(Modifier.padding(8.dp)) {
                    Spacer(Modifier.weight(1f))

                    TextButton(onClick = onClose) {
                        Text(stringResource(R.string.cancel))
                    }

                    Spacer(Modifier.width(8.dp))

                    Button(
                        onClick = onSignIn,
                        enabled = newUserId != null && newUserName != null && charaCount > 0
                    ) {
                        Text(stringResource(R.string.confirm))
                    }
                }
            }
        }
    }
}

@Composable
private fun SignInTitle() {
    Text(
        text = stringResource(R.string.create_user),
        modifier = Modifier.padding(8.dp),
        style = MaterialTheme.typography.headlineSmall
    )
}

@Composable
private fun SignInImage(
    newUserId: Int?,
    newUserName: String?,
    onImageClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable(onClick = onImageClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (newUserId == null && newUserName == null) {
            Spacer(
                Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )
            Text(
                text = stringResource(R.string.select_user_image),
                modifier = Modifier.padding(start = 8.dp),
                color = MaterialTheme.colorScheme.primary
            )
        } else {
            Box(Modifier.size(48.dp)) {
                PlaceImage(
                    url = UrlUtil.getUserIconUrl(newUserId!!),
                    shape = CircleShape
                )
            }
            Column(
                modifier = Modifier
                    .height(48.dp)
                    .padding(start = 8.dp),
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Text(
                    text = newUserName!!,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "uid:$newUserId",
                    color = LocalContentColor.current.copy(0.5f),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
private fun SignInCharaCount(
    charaCount: Int,
    maxChara: Int,
    onEditClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable(onClick = onEditClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Infobar(
            label = stringResource(R.string.chara_count),
            value = "$charaCount/$maxChara",
            modifier = Modifier.width(160.dp),
            width = 80.dp
        )

        Icon(
            imageVector = Icons.Filled.Edit,
            contentDescription = null,
            modifier = Modifier.padding(8.dp),
            tint = MaterialTheme.colorScheme.primary
        )
    }
}
