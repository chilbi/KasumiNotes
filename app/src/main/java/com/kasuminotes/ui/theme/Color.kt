package com.kasuminotes.ui.theme

import androidx.compose.material.Colors
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix

val PrimaryColor = Color(0xFF5F96F5)
val PrimaryVariantColor = Color(0xFF5B88E6)
val SecondaryColor = Color(0xFFF55F96)
val SecondaryVariantColor = Color(0xFFF2397E)
val ImmersiveSysUi = Color(0x33000000)
val ShadowColor = Color(0x775E6FA2)
val LightPlace = Color(0xFFE0E0E0)
val DarkPlace = Color(0xFF3F3F3F)
val BorderColor = Color(0x335E6FA2)
val UniqueColor = Color(0xFFD34BEF)
val Positive = Color(0xFF0097A7)
val Negative = Color(0xFFE64A19)
val LightError = Color(0xFFFFCDD2)
val DarkError = Color(0xFFE53935)
val LightWarning = Color(0xFFFFE082)
val DarkWarning = Color(0xFFFFA000)
val LightInfo = Color(0xFFB3E5FC)
val DarkInfo = Color(0xFF039BE5)
val LightSuccess = Color(0xFFA5D6A7)
val DarkSuccess = Color(0xFF388E3C)

val Colors.place: Color
    get() = if (isLight) LightPlace else DarkPlace

val Colors.selected: Color
    get() = if (isLight) LightWarning else DarkWarning

val GrayFilter by lazy {
    ColorFilter.colorMatrix(
        ColorMatrix().apply {
            setToSaturation(0f)
        }
    )
}

val LightColorPalette = lightColors(
    primary = PrimaryColor,
    primaryVariant = PrimaryVariantColor,
    secondary = SecondaryColor,
    secondaryVariant = SecondaryVariantColor,
//    background = Color.White,
//    surface = Color.White,
//    error = Color(0xFFB00020),
    onPrimary = Color.White,
    onSecondary = Color.White,
//    onBackground = Color.Black,
//    onSurface = Color.Black,
//    onError = Color.White
)

val DarkColorPalette = darkColors(
    primary = PrimaryColor,
    primaryVariant = PrimaryVariantColor,
    secondary = SecondaryColor,
    secondaryVariant = SecondaryVariantColor,
//    background = Color(0xFF121212),
//    surface = Color(0xFF121212),
//    error = Color(0xFFCF6679),
    onPrimary = Color.White,
    onSecondary = Color.White,
//    onBackground = Color.White,
//    onSurface = Color.White,
//    onError = Color.Black
)

val Int.rankRarity: Int
    get() = when {
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
    val highLight: Color,
    val light: Color,
    val middle: Color,
    val dark: Color,
    val deepDark: Color
)

object RaritiesColors {
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
            highLight = Color(0xFF88DDFF),
            light = Color(0xFF51BDF6),
            middle = Color(0xFF7FB0EA),
            dark = Color(0xFF4882DC),
            deepDark = Color(0xFF3B6EB2)
        )
    }

    fun getRarityColors(rankRarity: Int): RarityColors = when (rankRarity) {
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
