package com.kasuminotes.ui.app.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.kasuminotes.R
import com.kasuminotes.data.User
import com.kasuminotes.ui.app.DefaultUserId
import com.kasuminotes.ui.components.Infobar
import com.kasuminotes.ui.components.PlaceImage
import com.kasuminotes.utils.UrlUtil

@Composable
fun LogInDialog(
    userList: List<User>,
    maxChara: Int,
    onClose: () -> Unit,
    onSignOpen: () -> Unit,
    onLogIn: (User) -> Unit,
    onDelete: (User) -> Unit
) {
    Dialog(onClose) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = AlertDialogDefaults.TonalElevation
        ) {
            Column(Modifier.fillMaxWidth().padding(16.dp)) {
                LogInTitle()

                Column(
                    Modifier
                        .heightIn(max = 420.dp)
                        .verticalScroll(rememberScrollState())
                        .padding(8.dp)
                ) {
                    userList.forEach { user ->
                        UserItem(user, maxChara, onLogIn, onDelete)
                    }
                }

                Row(Modifier.padding(8.dp)) {
                    Spacer(Modifier.weight(1f))

                    TextButton(onClose) {
                        Text(stringResource(R.string.cancel))
                    }

                    Spacer(Modifier.width(8.dp))

                    Button(onClick = {
                        onClose()
                        onSignOpen()
                    }) {
                        Text(stringResource(R.string.create_user))
                    }
                }
            }
        }
    }
}

@Composable
private fun LogInTitle() {
    Text(
        text = stringResource(R.string.switch_user),
        modifier = Modifier.padding(8.dp),
        style = MaterialTheme.typography.headlineSmall
    )
}

@Composable
private fun UserItem(
    user: User,
    maxChara: Int,
    onLogIn: (User) -> Unit,
    onDelete: (User) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onLogIn(user) }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(Modifier.size(48.dp)) {
                PlaceImage(
                    url = UrlUtil.getUserIconUrl(user.userId),
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
                    text = user.userName,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "uid:${user.userId}",
                    color = LocalContentColor.current.copy(0.5f),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Row(
            modifier = Modifier.padding(top = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { onDelete(user) },
                enabled = user.userId != DefaultUserId
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = null,
                    tint = if (user.userId != DefaultUserId) MaterialTheme.colorScheme.error
                    else LocalContentColor.current
                )
            }

            Infobar(
                label = stringResource(R.string.chara_count),
                value = "${user.userChara}/$maxChara",
                modifier = Modifier
                    .padding(start = 4.dp)
                    .width(160.dp),
                width = 80.dp
            )
        }
    }

    Divider(Modifier.padding(horizontal = 16.dp))
}
