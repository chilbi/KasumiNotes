package com.kasuminotes.ui.app.userEditor

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kasuminotes.R
import com.kasuminotes.data.UserProfile
import com.kasuminotes.ui.app.state.CharaImageState
import com.kasuminotes.ui.app.state.UserState
import com.kasuminotes.ui.components.PlaceImage
import com.kasuminotes.ui.components.SizedBox
import com.kasuminotes.utils.UrlUtil

@Composable
fun UserImages(
    userState: UserState,
    onBack: () -> Unit,
    onConfirm: (userId: Int, userName: String) -> Unit
) {
    var selectedItem by remember {
        mutableStateOf<UserProfile?>(null)
    }

    UserEditorScaffold(
        userState.charaListState,
        title = stringResource(R.string.select_user_image),
        onBack = onBack,
        content = { contentPadding ->
            Box(Modifier.padding(contentPadding)) {
                LazyVerticalGrid(
                    columns = CharaImageState.iconGridCells,
                    contentPadding = PaddingValues(4.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    items( userState.charaListState.derivedProfiles) { userProfile ->
                        SizedBox(1f) {
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
        CharaImagesDialog(
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
