package com.example.yardly.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = AppBlack,
    onPrimary = AppWhite,
    background = AppWhite,
    onBackground = AppBlack,
    surface = AppWhite,
    onSurface = AppBlack,
    onSurfaceVariant = AppDarkGray,
    surfaceVariant = AppGray,
    secondary = AppGray,
    onSecondary = AppBlack,
    tertiary = AppGray,
    onTertiary = AppBlack,
    outline = DarkDivider // Use dark divider for light theme too
)

// *** NEW: Dark Color Scheme ***
private val DarkColorScheme = darkColorScheme(
    primary = DarkAccent,             // Accent color for main buttons
    onPrimary = DarkMainText,         // Text on accent buttons
    background = DarkBackground,      // Soft black
    onBackground = DarkMainText,      // Off-white text
    surface = DarkBackground,         // Cards/Sheets are same as background
    onSurface = DarkMainText,         // Text on cards
    onSurfaceVariant = DarkSecondaryText, // Gray text
    surfaceVariant = DarkDivider,     // Dividers / selected light gray
    secondary = DarkDivider,
    onSecondary = DarkMainText,
    tertiary = DarkDivider,
    onTertiary = DarkMainText,
    outline = DarkDivider             // Subtle borders
)

@Composable
fun YardlyTheme(
    isDarkMode: Boolean, // <-- CHANGED
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    // *** NEW: Logic to switch themes ***
    val colorScheme = if (isDarkMode) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}