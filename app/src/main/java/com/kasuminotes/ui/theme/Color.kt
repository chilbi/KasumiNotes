package com.kasuminotes.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix

val ImmersiveSysUi = Color(0x33000000)
val ShadowColor = Color(0x775E6FA2)
val UniqueColor = Color(0xFFD34BEF)
val LightError = Color(0xFFFFCDD2)
val DarkError = Color(0xFFE53935)
val LightWarning = Color(0xFFFFE082)
val DarkWarning = Color(0xFFFFA000)
val LightInfo = Color(0xFFB3E5FC)
val DarkInfo = Color(0xFF039BE5)
val LightSuccess = Color(0xFFA5D6A7)
val DarkSuccess = Color(0xFF388E3C)

val GrayFilter by lazy {
    ColorFilter.colorMatrix(
        ColorMatrix().apply {
            setToSaturation(0f)
        }
    )
}

val md_theme_light_primary = Color(0xFFA03F28)
val md_theme_light_onPrimary = Color(0xFFFFFFFF)
val md_theme_light_primaryContainer = Color(0xFFFFDAD2)
val md_theme_light_onPrimaryContainer = Color(0xFF3D0700)
val md_theme_light_secondary = Color(0xFF77574F)
val md_theme_light_onSecondary = Color(0xFFFFFFFF)
val md_theme_light_secondaryContainer = Color(0xFFFFDAD2)
val md_theme_light_onSecondaryContainer = Color(0xFF2C1510)
val md_theme_light_tertiary = Color(0xFF6D5D2E)
val md_theme_light_onTertiary = Color(0xFFFFFFFF)
val md_theme_light_tertiaryContainer = Color(0xFFF8E1A6)
val md_theme_light_onTertiaryContainer = Color(0xFF241A00)
val md_theme_light_error = Color(0xFFBA1A1A)
val md_theme_light_errorContainer = Color(0xFFFFDAD6)
val md_theme_light_onError = Color(0xFFFFFFFF)
val md_theme_light_onErrorContainer = Color(0xFF410002)
val md_theme_light_background = Color(0xFFFFFBFF)
val md_theme_light_onBackground = Color(0xFF201A19)
val md_theme_light_surface = Color(0xFFFFFBFF)
val md_theme_light_onSurface = Color(0xFF201A19)
val md_theme_light_surfaceVariant = Color(0xFFF5DDD8)
val md_theme_light_onSurfaceVariant = Color(0xFF534340)
val md_theme_light_outline = Color(0xFF85736F)
val md_theme_light_inverseOnSurface = Color(0xFFFBEEEB)
val md_theme_light_inverseSurface = Color(0xFF362F2D)
val md_theme_light_inversePrimary = Color(0xFFFFB4A3)
//val md_theme_light_shadow = Color(0xFF000000)
val md_theme_light_surfaceTint = Color(0xFFA03F28)
val md_theme_light_outlineVariant = Color(0xFFD8C2BD)
val md_theme_light_scrim = Color(0xFF000000)

val md_theme_dark_primary = Color(0xFFFFB4A3)
val md_theme_dark_onPrimary = Color(0xFF611201)
val md_theme_dark_primaryContainer = Color(0xFF812813)
val md_theme_dark_onPrimaryContainer = Color(0xFFFFDAD2)
val md_theme_dark_secondary = Color(0xFFE7BDB3)
val md_theme_dark_onSecondary = Color(0xFF442A23)
val md_theme_dark_secondaryContainer = Color(0xFF5D3F38)
val md_theme_dark_onSecondaryContainer = Color(0xFFFFDAD2)
val md_theme_dark_tertiary = Color(0xFFDAC58C)
val md_theme_dark_onTertiary = Color(0xFF3C2F04)
val md_theme_dark_tertiaryContainer = Color(0xFF544519)
val md_theme_dark_onTertiaryContainer = Color(0xFFF8E1A6)
val md_theme_dark_error = Color(0xFFFFB4AB)
val md_theme_dark_errorContainer = Color(0xFF93000A)
val md_theme_dark_onError = Color(0xFF690005)
val md_theme_dark_onErrorContainer = Color(0xFFFFDAD6)
val md_theme_dark_background = Color(0xFF201A19)
val md_theme_dark_onBackground = Color(0xFFEDE0DD)
val md_theme_dark_surface = Color(0xFF201A19)
val md_theme_dark_onSurface = Color(0xFFEDE0DD)
val md_theme_dark_surfaceVariant = Color(0xFF534340)
val md_theme_dark_onSurfaceVariant = Color(0xFFD8C2BD)
val md_theme_dark_outline = Color(0xFFA08C88)
val md_theme_dark_inverseOnSurface = Color(0xFF201A19)
val md_theme_dark_inverseSurface = Color(0xFFEDE0DD)
val md_theme_dark_inversePrimary = Color(0xFFA03F28)
//val md_theme_dark_shadow = Color(0xFF000000)
val md_theme_dark_surfaceTint = Color(0xFFFFB4A3)
val md_theme_dark_outlineVariant = Color(0xFF534340)
val md_theme_dark_scrim = Color(0xFF000000)

//val seed = Color(0xFFA03F28)

val LightColors = lightColorScheme(
    primary = md_theme_light_primary,
    onPrimary = md_theme_light_onPrimary,
    primaryContainer = md_theme_light_primaryContainer,
    onPrimaryContainer = md_theme_light_onPrimaryContainer,
    secondary = md_theme_light_secondary,
    onSecondary = md_theme_light_onSecondary,
    secondaryContainer = md_theme_light_secondaryContainer,
    onSecondaryContainer = md_theme_light_onSecondaryContainer,
    tertiary = md_theme_light_tertiary,
    onTertiary = md_theme_light_onTertiary,
    tertiaryContainer = md_theme_light_tertiaryContainer,
    onTertiaryContainer = md_theme_light_onTertiaryContainer,
    error = md_theme_light_error,
    errorContainer = md_theme_light_errorContainer,
    onError = md_theme_light_onError,
    onErrorContainer = md_theme_light_onErrorContainer,
    background = md_theme_light_background,
    onBackground = md_theme_light_onBackground,
    surface = md_theme_light_surface,
    onSurface = md_theme_light_onSurface,
    surfaceVariant = md_theme_light_surfaceVariant,
    onSurfaceVariant = md_theme_light_onSurfaceVariant,
    outline = md_theme_light_outline,
    inverseOnSurface = md_theme_light_inverseOnSurface,
    inverseSurface = md_theme_light_inverseSurface,
    inversePrimary = md_theme_light_inversePrimary,
    surfaceTint = md_theme_light_surfaceTint,
    outlineVariant = md_theme_light_outlineVariant,
    scrim = md_theme_light_scrim,
)


val DarkColors = darkColorScheme(
    primary = md_theme_dark_primary,
    onPrimary = md_theme_dark_onPrimary,
    primaryContainer = md_theme_dark_primaryContainer,
    onPrimaryContainer = md_theme_dark_onPrimaryContainer,
    secondary = md_theme_dark_secondary,
    onSecondary = md_theme_dark_onSecondary,
    secondaryContainer = md_theme_dark_secondaryContainer,
    onSecondaryContainer = md_theme_dark_onSecondaryContainer,
    tertiary = md_theme_dark_tertiary,
    onTertiary = md_theme_dark_onTertiary,
    tertiaryContainer = md_theme_dark_tertiaryContainer,
    onTertiaryContainer = md_theme_dark_onTertiaryContainer,
    error = md_theme_dark_error,
    errorContainer = md_theme_dark_errorContainer,
    onError = md_theme_dark_onError,
    onErrorContainer = md_theme_dark_onErrorContainer,
    background = md_theme_dark_background,
    onBackground = md_theme_dark_onBackground,
    surface = md_theme_dark_surface,
    onSurface = md_theme_dark_onSurface,
    surfaceVariant = md_theme_dark_surfaceVariant,
    onSurfaceVariant = md_theme_dark_onSurfaceVariant,
    outline = md_theme_dark_outline,
    inverseOnSurface = md_theme_dark_inverseOnSurface,
    inverseSurface = md_theme_dark_inverseSurface,
    inversePrimary = md_theme_dark_inversePrimary,
    surfaceTint = md_theme_dark_surfaceTint,
    outlineVariant = md_theme_dark_outlineVariant,
    scrim = md_theme_dark_scrim,
)

val phaseColors = listOf(
    Color(0xFF77D39E),
    Color(0xFFFFBB99),
    Color(0xFFFDCA64),
    Color(0xFFFFA819),
    Color(0xFFFD8013)
)

val Int.rankRarity: Int
    get() = when {
        this > 27 -> 9
        this > 23 -> 8
        this > 20 -> 7
        this > 17 -> 6
        this > 10 -> 5
        this > 6 -> 4
        this > 3 -> 3
        this > 1 -> 2
        else -> 1
    }

 data class RarityColors(
    val highLight: Color,//CenterTop
    val light: Color,//Top
    val middle: Color,//Bottom
    val dark: Color,//CenterBottom
    val deepDark: Color//Border
)

object RaritiesColors {
    private val r9: RarityColors by lazy {
        RarityColors(
            highLight = Color(0xFFDBFFFF),
            light = Color(0xFF5ACAFD),
            middle = Color(0xFF4DC2FF),
            dark = Color(0xFF0092FF),
            deepDark = Color(0xFF346690)
        )
    }
    private val r8: RarityColors by lazy {
        RarityColors(
            highLight = Color(0xFFFFEEAA),
            light = Color(0xFFFF6339),
            middle = Color(0xFFFFA819),
            dark = Color(0xFFFF6611),
            deepDark = Color(0xFFB03010)
        )
    }

    private val r7: RarityColors by lazy {
        RarityColors(
            highLight = Color(0xFF67DE9A),
            light = Color(0xFF21D673),
            middle = Color(0xFF00c660),
            dark = Color(0xFF106241),
            deepDark = Color(0xFF224444)
        )
    }

    private val r6: RarityColors by lazy {
        RarityColors(
            highLight = Color(0xFFFCA7A7),
            light = Color(0xFFEF6778),
            middle = Color(0xFFEB5252),
            dark = Color(0xFFE12648),
            deepDark = Color(0xFF614048)
        )
    }

    private val r5: RarityColors by lazy {
        RarityColors(
            highLight = Color(0xFFF4B0FF),
            light = Color(0xFFCC77FF),
            middle = Color(0xFFD086F1),
            dark = Color(0xFF9F4AF4),
            deepDark = Color(0xFF474769)
        )
    }

    private val r4: RarityColors by lazy {
        RarityColors(
            highLight = Color(0xFFFFEE99),
            light = Color(0xFFFAE276),
            middle = Color(0xFFFDCA64),
            dark = Color(0xFFEFAB34),
            deepDark = Color(0xFF665544)
        )
    }

    private val r3: RarityColors by lazy {
        RarityColors(
            highLight = Color(0xFFFFFFFF),
            light = Color(0xFFD6E7F7),
            middle = Color(0xFFC1C1D2),
            dark = Color(0xFFABABCD),
            deepDark = Color(0xFF555577)
        )
    }

    private val r2: RarityColors by lazy {
        RarityColors(
            highLight = Color(0xFFFFD5B3),
            light = Color(0xFFFFBB99),
            middle = Color(0xFFFA9461),
            dark = Color(0xFFCD7845),
            deepDark = Color(0xFF774433)
        )
    }

    private val r1: RarityColors by lazy {
        RarityColors(
            highLight = Color(0xFF96D3FF),
            light = Color(0xFFA8D1FF),
            middle = Color(0xFF94B9F7),
            dark = Color(0xFF93CEFF),
            deepDark = Color(0xFF2C4D86)
        )
    }

    fun getRarityColors(rankRarity: Int): RarityColors = when (rankRarity) {
        9 -> r9
        8 -> r8
        7 -> r7
        6 -> r6
        5 -> r5
        4 -> r4
        3 -> r3
        2 -> r2
        else -> r1
    }
}
