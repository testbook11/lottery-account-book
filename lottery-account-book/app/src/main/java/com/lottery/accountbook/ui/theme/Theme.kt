package com.lottery.accountbook.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = AccentBlue,
    onPrimary = PaperBackground,
    secondary = InkGray,
    onSecondary = PaperBackground,
    background = PaperBackground,
    onBackground = InkBlack,
    surface = PaperDark,
    onSurface = InkBlack,
    error = WarningRed,
    onError = PaperBackground
)

@Composable
fun LotteryTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}