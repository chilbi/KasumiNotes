package com.kasuminotes.ui.app.home

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Divider
import androidx.compose.material.DrawerDefaults
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ListItem
import androidx.compose.material.LocalAbsoluteElevation
import androidx.compose.material.LocalElevationOverlay
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.TriStateCheckbox
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Brightness4
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.DonutSmall
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Update
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.kasuminotes.BuildConfig
import com.kasuminotes.R
import com.kasuminotes.common.DbServer
import com.kasuminotes.common.Language
import com.kasuminotes.data.MaxUserData
import com.kasuminotes.ui.app.DefaultUserId
import com.kasuminotes.ui.components.PlaceImage
import com.kasuminotes.ui.components.StillSizeModify
import com.kasuminotes.ui.components.VerticalGrid
import com.kasuminotes.ui.components.VerticalGridCells
import com.kasuminotes.utils.UrlUtil

@Composable
fun HomeDrawer(
    userId: Int,
    userName: String,
    maxUserData: MaxUserData?,
    dbServer: DbServer,
    dbVersion: String,
    appAutoUpdate: Boolean,
    dbAutoUpdate: Boolean,
    lastVersionFetching: Boolean,
    latestAppReleaseInfoFetching: Boolean,
    language: Language,
    darkTheme: ToggleableState,
    onImagesClick: () -> Unit,
    onLogOut: () -> Unit,
    onDbServerChange: (DbServer) -> Unit,
    onLastDbVersionFetch: () -> Unit,
    onDbAutoUpdateToggle: () -> Unit,
    onLanguageChange: (Language) -> Unit,
    onDarkThemeToggle: () -> Unit,
    onLatestAppReleaseInfoFetch: () -> Unit,
    onAppAutoUpdateToggle: () -> Unit,
    onAboutClick: () -> Unit
) {
    val color = MaterialTheme.colors.surface
    val elevationOverlay = LocalElevationOverlay.current
    val absoluteElevation = LocalAbsoluteElevation.current + DrawerDefaults.Elevation
    val backgroundColor = elevationOverlay?.apply(color, absoluteElevation) ?: color

    Column(
        Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
    ) {

        UserImage(userId, backgroundColor)

        UserHeader(
            userId,
            userName,
            backgroundColor,
            onImagesClick,
            onLogOut
        )

        UserInfo(maxUserData)

        Divider(Modifier.padding(16.dp))

        DatabaseMenuList(
            dbServer,
            dbVersion,
            dbAutoUpdate,
            lastVersionFetching,
            onDbServerChange,
            onLastDbVersionFetch,
            onDbAutoUpdateToggle
        )

        Divider(Modifier.padding(16.dp))

        DisplayMenuList(
            language,
            darkTheme,
            onLanguageChange,
            onDarkThemeToggle
        )

        Divider(Modifier.padding(16.dp))

        AppMenuList(
            appAutoUpdate,
            latestAppReleaseInfoFetching,
            onLatestAppReleaseInfoFetch,
            onAppAutoUpdateToggle,
            onAboutClick
        )
    }
}

@Composable
private fun UserImage(
    userId: Int,
    backgroundColor: Color
) {
    Box(StillSizeModify) {
        PlaceImage(
            url = UrlUtil.getUserStillUrl(userId),
            shape = RectangleShape
        )
        Box(
            Modifier
                .matchParentSize()
                .background(
                    brush = Brush.verticalGradient(
                        0.5f to Color.Transparent,
                        1.0f to backgroundColor
                    )
                )
        )
    }
}

@Composable
private fun UserHeader(
    userId: Int,
    userName: String,
    backgroundColor: Color,
    onImageClick: () -> Unit,
    onLogOut: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 16.dp, end = 16.dp)
            .height(56.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(UrlUtil.getUserIconUrl(userId)),
            contentDescription = null,
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .clickable(
                    enabled = userId != DefaultUserId,
                    onClick = onImageClick
                )
                .border(
                    border = BorderStroke(
                        width = 2.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(
                                MaterialTheme.colors.primary,
                                MaterialTheme.colors.secondary
                            )
                        )
                    ),
                    shape = CircleShape
                )
                .border(
                    border = BorderStroke(
                        width = 4.dp,
                        brush = SolidColor(backgroundColor)
                    ),
                    shape = CircleShape
                )
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .height(56.dp)
                .padding(start = 16.dp),
            verticalArrangement = Arrangement.SpaceAround,
        ) {
            Text(
                text = userName,
                color = MaterialTheme.colors.primaryVariant,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.15.sp
                )
            )
            Text(
                text = "uid:$userId",
                color = MaterialTheme.colors.onSurface.copy(ContentAlpha.medium),
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    letterSpacing = 0.25.sp
                )
            )
        }

        IconButton(
            onClick = onLogOut,
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            Icon(imageVector = Icons.Filled.Logout, contentDescription = null)
        }
    }
}

@Composable
private fun UserInfo(maxUserData: MaxUserData?) {
    if (maxUserData == null) return

    val pairs: List<Pair<String, String>> = listOf(
        stringResource(R.string.level) to maxUserData.maxCharaLevel.toString(),
        stringResource(R.string.rank) to maxUserData.maxPromotionLevel.toString(),
        stringResource(R.string.map) to maxUserData.maxArea.toString(),
        stringResource(R.string.chara) to "${maxUserData.userChara}/${maxUserData.maxChara}",
        stringResource(R.string.unique) to "${maxUserData.userUnique}/${maxUserData.maxUnique}",
        stringResource(R.string.rarity_6) to "${maxUserData.userRarity6}/${maxUserData.maxRarity6}"
    )

    VerticalGrid(
        size = pairs.size,
        cells = VerticalGridCells.Fixed(3)
    ) { index ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val pair = pairs[index]

            Text(
                text = pair.first,
                color = MaterialTheme.colors.onSurface.copy(ContentAlpha.medium),
                style = MaterialTheme.typography.body2
            )

            Text(
                text = pair.second,
                style = MaterialTheme.typography.body1
            )
        }
    }
}

@Composable
private fun MenuCaption(text: String) {
    Text(
        text = text,
        modifier = Modifier.padding(start = 16.dp),
        color = MaterialTheme.colors.onSurface.copy(ContentAlpha.medium),
        style = MaterialTheme.typography.caption
    )
}

@Composable
private fun DatabaseMenuList(
    dbServer: DbServer,
    dbVersion: String,
    dbAutoUpdate: Boolean,
    lastVersionFetching: Boolean,
    onDbServerChange: (DbServer) -> Unit,
    onLastDbVersionFetch: () -> Unit,
    onDbAutoUpdateToggle: () -> Unit
) {
    MenuCaption(stringResource(R.string.db_server))

    ListItemWithDropdownMenu(
        iconVector = Icons.Filled.Cloud,
        text = stringResource(dbServer.resId)
    ) { onCollapse ->
        listOf(dbServer, if (dbServer == DbServer.CN) DbServer.JP else DbServer.CN).forEach { server ->
            DropdownMenuItem(
                onClick = {
                    onDbServerChange(server)
                    onCollapse()
                }
            ) {
                Text(
                    text = stringResource(server.resId),
                    modifier = Modifier.weight(1f)
                )
                if (server == dbServer) {
                    Icon(Icons.Filled.Check, null)
                }
            }
        }
    }

    ListItem(
        modifier = Modifier.clickable(onClick = onLastDbVersionFetch),
        icon = { Icon(Icons.Filled.DonutSmall, null) },
        trailing = { SyncIcon(lastVersionFetching) }
    ) {
        Text("v$dbVersion")
    }

    ListItem(
        modifier = Modifier.clickable(onClick = onDbAutoUpdateToggle),
        icon = { Icon(Icons.Filled.Update, null) },
        trailing = { Switch(checked = dbAutoUpdate, onCheckedChange = null) }
    ) {
        Text(stringResource(R.string.auto_update))
    }
}

@Composable
private fun DisplayMenuList(
    language: Language,
    darkTheme: ToggleableState,
    onLanguageChange: (Language) -> Unit,
    onDarkThemeToggle: () -> Unit
) {
    MenuCaption(stringResource(R.string.display))

    ListItemWithDropdownMenu(
        iconVector = Icons.Filled.Language,
        text = stringResource(language.resId)
    ) { onCollapse ->
        listOf(language, if (language == Language.CN) Language.JP else Language.CN).forEach { lang ->
            DropdownMenuItem(
                onClick = {
                    onLanguageChange(lang)
                    onCollapse()
                }
            ) {
                Text(
                    text = stringResource(lang.resId),
                    modifier = Modifier.weight(1f)
                )
                if (lang == language) {
                    Icon(Icons.Filled.Check, null)
                }
            }
        }
    }

    ListItem(
        modifier = Modifier.clickable(onClick = onDarkThemeToggle),
        icon = { Icon(Icons.Filled.Brightness4, null) },
        trailing = { TriStateCheckbox(state = darkTheme, onClick = null) }
    ) {
        Text(
            text = stringResource(
                when (darkTheme) {
                    ToggleableState.Indeterminate -> R.string.follow_system
                    ToggleableState.On -> R.string.night
                    ToggleableState.Off -> R.string.day
                }
            )
        )
    }
}

@Composable
private fun AppMenuList(
    appAutoUpdate: Boolean,
    latestAppReleaseInfoFetching: Boolean,
    onLatestAppReleaseInfoFetch: () -> Unit,
    onAppAutoUpdateToggle: () -> Unit,
    onAboutClick: () -> Unit
) {
    MenuCaption(stringResource(R.string.app))

    ListItem(
        modifier = Modifier.clickable(onClick = onLatestAppReleaseInfoFetch),
        icon = { Icon(Icons.Filled.Android, null) },
        trailing = { SyncIcon(latestAppReleaseInfoFetching) }
    ) {
        Text("v${BuildConfig.VERSION_NAME}")
    }

    ListItem(
        modifier = Modifier.clickable(onClick = onAppAutoUpdateToggle),
        icon = { Icon(Icons.Filled.Update, null) },
        trailing = { Switch(checked = appAutoUpdate, onCheckedChange = null) }
    ) {
        Text(stringResource(R.string.auto_update))
    }

    ListItem(
        modifier = Modifier.clickable(onClick = onAboutClick),
        icon = { Icon(Icons.Filled.Info, null) },
        trailing = { Icon(Icons.Filled.ArrowForward, null) }
    ) {
        Text(stringResource(R.string.about))
    }
}

@Composable
private fun ListItemWithDropdownMenu(
    iconVector: ImageVector,
    text: String,
    content: @Composable ColumnScope.(onCollapse: () -> Unit) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ListItem(
        modifier = Modifier.clickable { expanded = true },
        icon = { Icon(iconVector, null) },
        trailing = {
            Icon(
                imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                contentDescription = null
            )
        }
    ) {
        Text(text)

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(160.dp),
            offset = DpOffset((-16).dp, (-43).dp)
        ) {
            content {
                expanded = false
            }
        }
    }
}

@Composable
private fun SyncIcon(enable: Boolean) {
    if (enable) {
        val infiniteTransition = rememberInfiniteTransition()
        val degrees by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = -360f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 1000,
                    easing = LinearEasing
                )
            )
        )
        Icon(
            imageVector = Icons.Filled.Sync,
            contentDescription = null,
            modifier = Modifier.rotate(degrees),
            tint = MaterialTheme.colors.secondary
        )
    } else {
        Icon(Icons.Filled.Sync, null)
    }
}
