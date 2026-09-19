package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = QuinceDarkPrimary,
    onPrimary = Color.White,
    primaryContainer = QuinceDarkSurfaceVariant,
    onPrimaryContainer = QuinceRoseLight,
    secondary = QuinceDarkGold,
    onSecondary = Color.Black,
    secondaryContainer = QuinceGoldDark,
    onSecondaryContainer = QuinceGoldLight,
    tertiary = QuinceDarkGold,
    background = QuinceDarkBackground,
    onBackground = QuinceDarkTextPrimary,
    surface = QuinceDarkSurface,
    onSurface = QuinceDarkTextPrimary,
    surfaceVariant = QuinceDarkSurfaceVariant,
    onSurfaceVariant = QuinceDarkTextSecondary,
    outline = Color(0xFF6B4D58)
)

private val LightColorScheme = lightColorScheme(
    primary = QuinceRose,
    onPrimary = Color.White,
    primaryContainer = QuinceRoseLight,
    onPrimaryContainer = QuinceRoseDark,
    secondary = QuinceGold,
    onSecondary = Color.White,
    secondaryContainer = QuinceGoldLight,
    onSecondaryContainer = QuinceGoldDark,
    tertiary = QuinceGoldDark,
    background = QuinceBackground,
    onBackground = QuinceTextPrimary,
    surface = QuinceSurface,
    onSurface = QuinceTextPrimary,
    surfaceVariant = QuinceSurfaceVariant,
    onSurfaceVariant = QuinceTextSecondary,
    outline = QuinceBorder
)

@Composable
fun Mis15Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
