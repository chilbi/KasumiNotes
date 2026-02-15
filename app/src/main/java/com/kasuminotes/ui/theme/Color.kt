package com.kasuminotes.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix

val Translucent = Color(0x33000000)
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

@Stable
class Palettes(
    val lightColors: ColorScheme,
    val darkColors: ColorScheme
)

val PrimaryPalettes: Array<Color> = arrayOf(
    Color(0xFF215FA6),
    Color(0xFFAE2660),
    Color(0xFF436915),
    Color(0xFF616200),
    Color(0xFFA03F28)
)

val Themes: Array<Lazy<Palettes>> = arrayOf(
    lazy {
        Palettes(
            lightColors = lightColorScheme(
                primary = Color(0xFF215FA6),
                onPrimary = Color(0xFFFFFFFF),
                primaryContainer = Color(0xFFD5E3FF),
                onPrimaryContainer = Color(0xFF001C3B),
                secondary = Color(0xFF555F71),
                onSecondary = Color(0xFFFFFFFF),
                secondaryContainer = Color(0xFFD9E3F8),
                onSecondaryContainer = Color(0xFF121C2B),
                tertiary = Color(0xFF6E5676),
                onTertiary = Color(0xFFFFFFFF),
                tertiaryContainer = Color(0xFFF8D8FF),
                onTertiaryContainer = Color(0xFF27132F),
                error = Color(0xFFBA1A1A),
                errorContainer = Color(0xFFFFDAD6),
                onError = Color(0xFFFFFFFF),
                onErrorContainer = Color(0xFF410002),
                background = Color(0xFFFDFBFF),
                onBackground = Color(0xFF1A1C1E),
                surface = Color(0xFFFDFBFF),
                onSurface = Color(0xFF1A1C1E),
                surfaceVariant = Color(0xFFE0E2EC),
                onSurfaceVariant = Color(0xFF43474E),
                outline = Color(0xFF74777F),
                inverseOnSurface = Color(0xFFF1F0F4),
                inverseSurface = Color(0xFF2F3033),
                inversePrimary = Color(0xFFA6C8FF),
                surfaceTint = Color(0xFF215FA6),
                outlineVariant = Color(0xFFC4C6CF),
                scrim = Color(0xFF000000)
            ),
            darkColors = darkColorScheme(
                primary = Color(0xFFA6C8FF),
                onPrimary = Color(0xFF003060),
                primaryContainer = Color(0xFF004787),
                onPrimaryContainer = Color(0xFFD5E3FF),
                secondary = Color(0xFFBDC7DC),
                onSecondary = Color(0xFF273141),
                secondaryContainer = Color(0xFF3D4758),
                onSecondaryContainer = Color(0xFFD9E3F8),
                tertiary = Color(0xFFDBBDE2),
                onTertiary = Color(0xFF3E2846),
                tertiaryContainer = Color(0xFF553F5D),
                onTertiaryContainer = Color(0xFFF8D8FF),
                error = Color(0xFFFFB4AB),
                errorContainer = Color(0xFF93000A),
                onError = Color(0xFF690005),
                onErrorContainer = Color(0xFFFFDAD6),
                background = Color(0xFF1A1C1E),
                onBackground = Color(0xFFE3E2E6),
                surface = Color(0xFF1A1C1E),
                onSurface = Color(0xFFE3E2E6),
                surfaceVariant = Color(0xFF43474E),
                onSurfaceVariant = Color(0xFFC4C6CF),
                outline = Color(0xFF8D9199),
                inverseOnSurface = Color(0xFF1A1C1E),
                inverseSurface = Color(0xFFE3E2E6),
                inversePrimary = Color(0xFF215FA6),
                surfaceTint = Color(0xFFA6C8FF),
                outlineVariant = Color(0xFF43474E),
                scrim = Color(0xFF000000)
            )
        )
    },
    lazy {
        Palettes(
            lightColors = lightColorScheme(
                primary = Color(0xFFAE2660),
                onPrimary = Color(0xFFFFFFFF),
                primaryContainer = Color(0xFFFFD9E2),
                onPrimaryContainer = Color(0xFF3F001C),
                secondary = Color(0xFF74565E),
                onSecondary = Color(0xFFFFFFFF),
                secondaryContainer = Color(0xFFFFD9E2),
                onSecondaryContainer = Color(0xFF2B151C),
                tertiary = Color(0xFF7C5634),
                onTertiary = Color(0xFFFFFFFF),
                tertiaryContainer = Color(0xFFFFDCC0),
                onTertiaryContainer = Color(0xFF2E1600),
                error = Color(0xFFBA1A1A),
                errorContainer = Color(0xFFFFDAD6),
                onError = Color(0xFFFFFFFF),
                onErrorContainer = Color(0xFF410002),
                background = Color(0xFFFFFBFF),
                onBackground = Color(0xFF201A1B),
                surface = Color(0xFFFFFBFF),
                onSurface = Color(0xFF201A1B),
                surfaceVariant = Color(0xFFF2DDE1),
                onSurfaceVariant = Color(0xFF514346),
                outline = Color(0xFF837376),
                inverseOnSurface = Color(0xFFFAEEEF),
                inverseSurface = Color(0xFF352F30),
                inversePrimary = Color(0xFFFFB1C7),
                surfaceTint = Color(0xFFAE2660),
                outlineVariant = Color(0xFFD6C2C5),
                scrim = Color(0xFF000000)
            ),
            darkColors = darkColorScheme(
                primary = Color(0xFFFFB1C7),
                onPrimary = Color(0xFF650031),
                primaryContainer = Color(0xFF8E0348),
                onPrimaryContainer = Color(0xFFFFD9E2),
                secondary = Color(0xFFE3BDC6),
                onSecondary = Color(0xFF422930),
                secondaryContainer = Color(0xFF5B3F47),
                onSecondaryContainer = Color(0xFFFFD9E2),
                tertiary = Color(0xFFEEBD93),
                onTertiary = Color(0xFF47290B),
                tertiaryContainer = Color(0xFF613F1F),
                onTertiaryContainer = Color(0xFFFFDCC0),
                error = Color(0xFFFFB4AB),
                errorContainer = Color(0xFF93000A),
                onError = Color(0xFF690005),
                onErrorContainer = Color(0xFFFFDAD6),
                background = Color(0xFF201A1B),
                onBackground = Color(0xFFECE0E1),
                surface = Color(0xFF201A1B),
                onSurface = Color(0xFFECE0E1),
                surfaceVariant = Color(0xFF514346),
                onSurfaceVariant = Color(0xFFD6C2C5),
                outline = Color(0xFF9E8C90),
                inverseOnSurface = Color(0xFF201A1B),
                inverseSurface = Color(0xFFECE0E1),
                inversePrimary = Color(0xFFAE2660),
                surfaceTint = Color(0xFFFFB1C7),
                outlineVariant = Color(0xFF514346),
                scrim = Color(0xFF000000)
            )
        )
    },
    lazy {
        Palettes(
            lightColors = lightColorScheme(
                primary = Color(0xFF436915),
                onPrimary = Color(0xFFFFFFFF),
                primaryContainer = Color(0xFFC2F18D),
                onPrimaryContainer = Color(0xFF0F2000),
                secondary = Color(0xFF57624A),
                onSecondary = Color(0xFFFFFFFF),
                secondaryContainer = Color(0xFFDBE7C8),
                onSecondaryContainer = Color(0xFF151E0B),
                tertiary = Color(0xFF386663),
                onTertiary = Color(0xFFFFFFFF),
                tertiaryContainer = Color(0xFFBBECE8),
                onTertiaryContainer = Color(0xFF00201F),
                error = Color(0xFFBA1A1A),
                errorContainer = Color(0xFFFFDAD6),
                onError = Color(0xFFFFFFFF),
                onErrorContainer = Color(0xFF410002),
                background = Color(0xFFFDFCF5),
                onBackground = Color(0xFF1B1C18),
                surface = Color(0xFFFDFCF5),
                onSurface = Color(0xFF1B1C18),
                surfaceVariant = Color(0xFFE1E4D5),
                onSurfaceVariant = Color(0xFF44483D),
                outline = Color(0xFF75796C),
                inverseOnSurface = Color(0xFFF2F1E9),
                inverseSurface = Color(0xFF30312C),
                inversePrimary = Color(0xFFA7D474),
                surfaceTint = Color(0xFF436915),
                outlineVariant = Color(0xFFC5C8BA),
                scrim = Color(0xFF000000)
            ),
            darkColors = darkColorScheme(
                primary = Color(0xFFA7D474),
                onPrimary = Color(0xFF1E3700),
                primaryContainer = Color(0xFF2D5000),
                onPrimaryContainer = Color(0xFFC2F18D),
                secondary = Color(0xFFBFCBAD),
                onSecondary = Color(0xFF2A331E),
                secondaryContainer = Color(0xFF404A33),
                onSecondaryContainer = Color(0xFFDBE7C8),
                tertiary = Color(0xFFA0CFCC),
                onTertiary = Color(0xFF003735),
                tertiaryContainer = Color(0xFF1F4E4B),
                onTertiaryContainer = Color(0xFFBBECE8),
                error = Color(0xFFFFB4AB),
                errorContainer = Color(0xFF93000A),
                onError = Color(0xFF690005),
                onErrorContainer = Color(0xFFFFDAD6),
                background = Color(0xFF1B1C18),
                onBackground = Color(0xFFE3E3DB),
                surface = Color(0xFF1B1C18),
                onSurface = Color(0xFFE3E3DB),
                surfaceVariant = Color(0xFF44483D),
                onSurfaceVariant = Color(0xFFC5C8BA),
                outline = Color(0xFF8E9285),
                inverseOnSurface = Color(0xFF1B1C18),
                inverseSurface = Color(0xFFE3E3DB),
                inversePrimary = Color(0xFF436915),
                surfaceTint = Color(0xFFA7D474),
                outlineVariant = Color(0xFF44483D),
                scrim = Color(0xFF000000)
            )
        )
    },
    lazy {
        Palettes(
            lightColors = lightColorScheme(
                primary = Color(0xFF616200),
                onPrimary = Color(0xFFFFFFFF),
                primaryContainer = Color(0xFFE8E870),
                onPrimaryContainer = Color(0xFF1D1D00),
                secondary = Color(0xFF606043),
                onSecondary = Color(0xFFFFFFFF),
                secondaryContainer = Color(0xFFE6E4BF),
                onSecondaryContainer = Color(0xFF1D1D06),
                tertiary = Color(0xFF3D6657),
                onTertiary = Color(0xFFFFFFFF),
                tertiaryContainer = Color(0xFFBFECD8),
                onTertiaryContainer = Color(0xFF002117),
                error = Color(0xFFBA1A1A),
                errorContainer = Color(0xFFFFDAD6),
                onError = Color(0xFFFFFFFF),
                onErrorContainer = Color(0xFF410002),
                background = Color(0xFFFFFBFF),
                onBackground = Color(0xFF1C1C17),
                surface = Color(0xFFFFFBFF),
                onSurface = Color(0xFF1C1C17),
                surfaceVariant = Color(0xFFE6E3D1),
                onSurfaceVariant = Color(0xFF48473A),
                outline = Color(0xFF797869),
                inverseOnSurface = Color(0xFFF4F0E8),
                inverseSurface = Color(0xFF31312B),
                inversePrimary = Color(0xFFCCCC57),
                surfaceTint = Color(0xFF616200),
                outlineVariant = Color(0xFFCAC7B6),
                scrim = Color(0xFF000000)
            ),
            darkColors = darkColorScheme(
                primary = Color(0xFFCCCC57),
                onPrimary = Color(0xFF323200),
                primaryContainer = Color(0xFF494900),
                onPrimaryContainer = Color(0xFFE8E870),
                secondary = Color(0xFFCAC8A5),
                onSecondary = Color(0xFF323218),
                secondaryContainer = Color(0xFF48482D),
                onSecondaryContainer = Color(0xFFE6E4BF),
                tertiary = Color(0xFFA4D0BD),
                onTertiary = Color(0xFF0B372A),
                tertiaryContainer = Color(0xFF254E40),
                onTertiaryContainer = Color(0xFFBFECD8),
                error = Color(0xFFFFB4AB),
                errorContainer = Color(0xFF93000A),
                onError = Color(0xFF690005),
                onErrorContainer = Color(0xFFFFDAD6),
                background = Color(0xFF1C1C17),
                onBackground = Color(0xFFE6E2DA),
                surface = Color(0xFF1C1C17),
                onSurface = Color(0xFFE6E2DA),
                surfaceVariant = Color(0xFF48473A),
                onSurfaceVariant = Color(0xFFCAC7B6),
                outline = Color(0xFF939182),
                inverseOnSurface = Color(0xFF1C1C17),
                inverseSurface = Color(0xFFE6E2DA),
                inversePrimary = Color(0xFF616200),
                surfaceTint = Color(0xFFCCCC57),
                outlineVariant = Color(0xFF48473A),
                scrim = Color(0xFF000000)
            )
        )
    },
    lazy {
        Palettes(
            lightColors = lightColorScheme(
                primary = Color(0xFFA03F28),
                onPrimary = Color(0xFFFFFFFF),
                primaryContainer = Color(0xFFFFDAD2),
                onPrimaryContainer = Color(0xFF3D0700),
                secondary = Color(0xFF77574F),
                onSecondary = Color(0xFFFFFFFF),
                secondaryContainer = Color(0xFFFFDAD2),
                onSecondaryContainer = Color(0xFF2C1510),
                tertiary = Color(0xFF6D5D2E),
                onTertiary = Color(0xFFFFFFFF),
                tertiaryContainer = Color(0xFFF8E1A6),
                onTertiaryContainer = Color(0xFF241A00),
                error = Color(0xFFBA1A1A),
                errorContainer = Color(0xFFFFDAD6),
                onError = Color(0xFFFFFFFF),
                onErrorContainer = Color(0xFF410002),
                background = Color(0xFFFFFBFF),
                onBackground = Color(0xFF201A19),
                surface = Color(0xFFFFFBFF),
                onSurface = Color(0xFF201A19),
                surfaceVariant = Color(0xFFF5DDD8),
                onSurfaceVariant = Color(0xFF534340),
                outline = Color(0xFF85736F),
                inverseOnSurface = Color(0xFFFBEEEB),
                inverseSurface = Color(0xFF362F2D),
                inversePrimary = Color(0xFFFFB4A3),
                surfaceTint = Color(0xFFA03F28),
                outlineVariant = Color(0xFFD8C2BD),
                scrim = Color(0xFF000000)
            ),
            darkColors = darkColorScheme(
                primary = Color(0xFFFFB4A3),
                onPrimary = Color(0xFF611201),
                primaryContainer = Color(0xFF812813),
                onPrimaryContainer = Color(0xFFFFDAD2),
                secondary = Color(0xFFE7BDB3),
                onSecondary = Color(0xFF442A23),
                secondaryContainer = Color(0xFF5D3F38),
                onSecondaryContainer = Color(0xFFFFDAD2),
                tertiary = Color(0xFFDAC58C),
                onTertiary = Color(0xFF3C2F04),
                tertiaryContainer = Color(0xFF544519),
                onTertiaryContainer = Color(0xFFF8E1A6),
                error = Color(0xFFFFB4AB),
                errorContainer = Color(0xFF93000A),
                onError = Color(0xFF690005),
                onErrorContainer = Color(0xFFFFDAD6),
                background = Color(0xFF201A19),
                onBackground = Color(0xFFEDE0DD),
                surface = Color(0xFF201A19),
                onSurface = Color(0xFFEDE0DD),
                surfaceVariant = Color(0xFF534340),
                onSurfaceVariant = Color(0xFFD8C2BD),
                outline = Color(0xFFA08C88),
                inverseOnSurface = Color(0xFF201A19),
                inverseSurface = Color(0xFFEDE0DD),
                inversePrimary = Color(0xFFA03F28),
                surfaceTint = Color(0xFFFFB4A3),
                outlineVariant = Color(0xFF534340),
                scrim = Color(0xFF000000)
            )
        )
    }
)

val phaseColors = listOf(
    Color(0xFF80BF65),
    Color(0xFF6CA8E6),
    Color(0xFFCC6AA6),
    Color(0xFFD94C42),
    Color(0xFFB367DC)
)

val Int.rankRarity: Int
    get() = when {
        this > 39 -> 11
        this > 31 -> 10
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

@Stable
class RarityColors(
    val highLight: Color,//CenterTop
    val light: Color,//Top
    val middle: Color,//Bottom
    val dark: Color,//CenterBottom
    val deepDark: Color//Border
)

object RaritiesColors {
    private val r11: RarityColors by lazy {
        RarityColors(
            highLight = Color(0xFFEBFF8E),
            light = Color(0xFFE5FF6A),
            middle = Color(0xFFFFF761),
            dark = Color(0xFFADBD4A),
            deepDark = Color(0xFF818125)
        )
    }
    private val r10: RarityColors by lazy {
        RarityColors(
            highLight = Color(0xFFFFEED9),
            light = Color(0xFFFAB0B1),
            middle = Color(0xFFFFB8C5),
            dark = Color(0xFFFA9E83),
            deepDark = Color(0xFF9D453B)
        )
    }
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
        11 -> r11
        10 -> r10
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
