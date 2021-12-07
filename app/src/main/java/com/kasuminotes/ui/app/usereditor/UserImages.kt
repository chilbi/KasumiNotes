package com.kasuminotes.ui.app.usereditor

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.kasuminotes.data.UserProfile
import com.kasuminotes.ui.app.state.UserState
import com.kasuminotes.ui.components.PlaceImage
import com.kasuminotes.ui.components.SizedBox
import com.kasuminotes.ui.components.selectedBg
import com.kasuminotes.ui.theme.selected
import com.kasuminotes.utils.UrlUtil

@Composable
fun UserImages(
    userState: UserState,
    onBack: () -> Unit,
    onConfirm: (userId: Int, userName: String) -> Unit
) {
    val listState = userState.charaListState
    var selectedItem by remember {
        mutableStateOf<UserProfile?>(null)
    }

    UserEditorScaffold(
        title = stringResource(R.string.select_user_image),
        searchText = listState.searchText,
        atkType = listState.atkType,
        position = listState.position,
        orderBy = listState.orderBy,
        sortDesc = listState.sortDesc,
        onSearchTextChange = listState::changeSearchText,
        onAtkTypeChange = listState::changeAtkType,
        onPositionChange = listState::changePosition,
        onOrderByChange = listState::changeOrderBy,
        onBack = onBack,
        content = { contentPadding ->
            Box(Modifier.padding(contentPadding)) {
                LazyVerticalGrid(
                    cells = GridCells.Fixed(4),
                    contentPadding = PaddingValues(4.dp)
                ) {
                    items(listState.derivedProfiles) { userProfile ->
                        SizedBox(1f, true) {
                            PlaceImage(
                                url = UrlUtil.getUnitIconUrl(userProfile.unitData.unitId, 3),
                                shape = CircleShape,
                                modifier = Modifier.clickable {
                                    selectedItem = userProfile
                                }
                            )
                        }
                    }
                }
            }
        }
    )

    if (selectedItem != null) {
        ImagesDialog(
            unitId = selectedItem!!.unitData.unitId,
            actualName = selectedItem!!.unitData.actualName,
            maxRarity = selectedItem!!.unitData.maxRarity,
            allUser = userState.allUser,
            onConfirm = { userId, userName ->
                selectedItem = null
                onConfirm(userId, userName)
            },
            onClose = { selectedItem = null }
        )
    }
}

@Composable
private fun ImagesDialog(
    unitId: Int,
    actualName: String,
    maxRarity: Int,
    allUser: List<Int>,
    onConfirm: (userId: Int, userName: String) -> Unit,
    onClose: () -> Unit
) {
    var userId by remember { mutableStateOf(0) }

    val users: List<Int> by remember(unitId, maxRarity) {
        derivedStateOf {
            val list = mutableListOf<Int>()
            list.add(unitId + 10)
            list.add(unitId + 30)
            if (maxRarity > 5) {
                list.add(unitId + 60)
            }
            list
        }
    }

    Dialog(onClose) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            elevation = 16.dp
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(
                    text = actualName,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = MaterialTheme.typography.subtitle1
                )

                Row(Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    users.forEach { user ->
                        Column(
                            modifier = Modifier.padding(end = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            val registered = allUser.contains(user)

                            Box(
                                Modifier
                                    .clip(CircleShape)
                                    .selectedBg(
                                        user == userId,
                                        MaterialTheme.colors.selected,
                                        CircleShape
                                    )
                                    .clickable(
                                        enabled = !registered,
                                        onClick = { userId = user }
                                    )
                                    .padding(4.dp)
                                    .size(48.dp)
                            ) {
                                PlaceImage(
                                    url = UrlUtil.getUserIconUrl(user),
                                    shape = CircleShape
                                )
                            }

                            Text(
                                text = if (registered) stringResource(R.string.registered)
                                else user.toString(),
                                color = LocalContentColor.current.copy(0.5f),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                Row(Modifier.padding(top = 8.dp)) {
                    Spacer(Modifier.weight(1f))

                    TextButton(onClose) {
                        Text(stringResource(R.string.cancel))
                    }

                    Button(
                        onClick = { onConfirm(userId, actualName) },
                        enabled = userId != 0
                    ) {
                        Text(stringResource(R.string.confirm))
                    }
                }
            }
        }
    }
}
