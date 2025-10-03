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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.kasuminotes.BuildConfig
import com.kasuminotes.R
import com.kasuminotes.ui.components.BackButton
import com.kasuminotes.ui.components.TopBar

@Composable
fun About(
    onBack: () -> Unit,
    onLinkTo: (uriString: String) -> Unit
) {
    Scaffold(
        topBar = {
            TopBar(
                title = { Text(stringResource(R.string.about_app)) },
                navigationIcon = { BackButton(onBack) }
            )
        },
        bottomBar = {
            Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
        },
        containerColor = MaterialTheme.colorScheme.surface,
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
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(Modifier.height(14.dp))

                Text(
                    text = "Github Repository Page",
                    style = MaterialTheme.typography.titleMedium
                )
                TextLink(
                    text = "https://github.com/chilbi/KasumiNotes",
                    onClick = { onLinkTo("https://github.com/chilbi/KasumiNotes")}
                )

                Spacer(Modifier.height(14.dp))

                Text(
                    text = "App Releases Page",
                    style = MaterialTheme.typography.titleMedium
                )
                TextLink(
                    text = "https://github.com/chilbi/KasumiNotes/releases",
                    onClick = { onLinkTo("https://github.com/chilbi/KasumiNotes/releases") }
                )

                Spacer(Modifier.height(24.dp))

                Text(
                    text = "API References",
                    style = MaterialTheme.typography.titleMedium
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
                Spacer(Modifier.height(8.dp))
                TextLink(
                    text = "https://roboninon.win",
                    onClick = { onLinkTo("https://roboninon.win/db/version") }
                )

                Spacer(Modifier.height(24.dp))
                Spacer(Modifier.weight(1f))

                Text(
                    text = "LICENSE",
                    style = MaterialTheme.typography.titleMedium
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
    color: Color = MaterialTheme.colorScheme.primary,
    onClick: () -> Unit
) {
    Text(
        text = text,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(2.dp),
        color = color,
        textDecoration = TextDecoration.Underline,
        style = MaterialTheme.typography.bodyMedium
    )
}
