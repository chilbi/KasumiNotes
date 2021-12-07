package com.kasuminotes.ui.app.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.kasuminotes.R
import com.kasuminotes.ui.components.ImageIcon
import com.kasuminotes.ui.components.Infobar
import com.kasuminotes.ui.theme.place
import com.kasuminotes.utils.UrlUtil

@Composable
fun SignInDialog(
    newUserId: Int?,
    newUserName: String?,
    charaCount: Int,
    maxChara: Int,
    onImageSelect: () -> Unit,
    onCharaEdit: () -> Unit,
    onClose: () -> Unit,
    onSignIn: () -> Unit
) {
    Dialog(onClose) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            elevation = 20.dp
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                SignInTitle()

                Column(Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    SignInImage(
                        newUserId,
                        newUserName,
                        onImageSelect
                    )

                    Spacer(Modifier.height(8.dp))

                    SignInCharaCount(
                        charaCount,
                        maxChara,
                        onCharaEdit
                    )
                }

                Row(Modifier.padding(top = 8.dp)) {
                    Spacer(Modifier.weight(1f))

                    TextButton(onClick = onClose) {
                        Text(stringResource(R.string.cancel))
                    }

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
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        style = MaterialTheme.typography.subtitle1
    )
}

@Composable
private fun SignInImage(
    newUserId: Int?,
    newUserName: String?,
    onImageSelect: () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        if (newUserId == null && newUserName == null) {
            Spacer(
                Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colors.place)
                    .clickable(onClick = onImageSelect)
            )
            Text(
                text = stringResource(R.string.select_user_image),
                modifier = Modifier.padding(start = 8.dp)
            )
        } else {
            ImageIcon(
                url = UrlUtil.getUserIconUrl(newUserId!!),
                onClick = onImageSelect,
                shape = CircleShape
            )
            Column(
                modifier = Modifier
                    .height(48.dp)
                    .padding(start = 8.dp),
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Text(
                    text = newUserName!!,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                Text(
                    text = "uid:$newUserId",
                    color = LocalContentColor.current.copy(0.6f),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun SignInCharaCount(
    charaCount: Int,
    maxChara: Int,
    onCharaEdit: () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Infobar(
            label = stringResource(R.string.chara_count),
            value = "$charaCount/$maxChara",
            modifier = Modifier.width(128.dp),
            width = 64.dp
        )

        IconButton(onCharaEdit) {
            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = null,
                tint = MaterialTheme.colors.primary
            )
        }
    }
}
