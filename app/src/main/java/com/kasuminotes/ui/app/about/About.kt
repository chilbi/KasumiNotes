package com.kasuminotes.ui.app.about

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kasuminotes.BuildConfig
import com.kasuminotes.R
import com.kasuminotes.ui.components.BackButton
import com.kasuminotes.ui.components.ImmersiveTopAppBar

@Composable
fun About(
    onBack: () -> Unit,
    onLinkTo: (uriString: String) -> Unit
) {
    Scaffold(
        topBar = {
            ImmersiveTopAppBar(
                title = { Text(stringResource(R.string.about_app)) },
                navigationIcon = { BackButton(onBack) }
            )
        },
        bottomBar = {
            Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
        },
        content = { contentPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(painterResource(R.mipmap.ic_launcher_foreground), null)
                Text(
                    text = "v${BuildConfig.VERSION_NAME}",
                    fontSize = 22.sp
                )

                Spacer(Modifier.height(14.dp))

                Text(
                    text = "Github Repository Page",
                    fontSize = 18.sp
                )
                TextLink(
                    text = "https://github.com/chilbi/KasumiNotes",
                    onClick = { onLinkTo("https://github.com/chilbi/KasumiNotes")}
                )

                Spacer(Modifier.height(14.dp))

                Text(
                    text = "App Releases Page",
                    fontSize = 18.sp
                )
                TextLink(
                    text = "https://github.com/chilbi/KasumiNotes/releases",
                    onClick = { onLinkTo("https://github.com/chilbi/KasumiNotes/releases") }
                )

                Spacer(Modifier.height(24.dp))

                Text(
                    text = "API References",
                    fontSize = 18.sp
                )
                TextLink(
                    text = "https://redive.estertion.win",
                    onClick = { onLinkTo("https://redive.estertion.win") }
                )
                Spacer(Modifier.height(8.dp))
                TextLink(
                    text = "https://wthee.xyz/redive/",
                    onClick = { onLinkTo("https://wthee.xyz/redive/") }
                )

                Spacer(Modifier.weight(1f))

                Text(
                    text = "LICENSE",
                    fontSize = 18.sp
                )
                TextLink(
                    text = "Apache License Version 2.0",
                    onClick = { onLinkTo("http://www.apache.org/licenses/LICENSE-2.0.txt") }
                )

                Spacer(Modifier.height(14.dp))
            }
        }
    )
}

@Composable
private fun TextLink(
    text: String,
    color: Color = MaterialTheme.colors.primary,
    fontSize: TextUnit = 14.sp,
    onClick: () -> Unit
) {
    Text(
        text = text,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(2.dp),
        color = color,
        fontSize = fontSize,
        textDecoration = TextDecoration.Underline
    )
}
