package com.example.yardly.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp // <-- ADDED DP IMPORT

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
    isDarkMode: Boolean,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
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

// --- *** NEW: DIMENSIONS OBJECT ADDED HERE *** ---

/**
 * A central object to hold all standard spacing dimensions for the app.
 * This ensures a consistent look and feel across all screens.
 */
object Dimens {
    // Standard horizontal and vertical padding for main screen content
    val ScreenPaddingHorizontal = 20.dp
    val ScreenPaddingVertical = 20.dp

    // Standard spacing scale
    val SpacingSmall = 4.dp
    val SpacingMedium = 8.dp
    val SpacingLarge = 12.dp
    val SpacingXLarge = 16.dp
    val SpacingXXLarge = 24.dp
    val SpacingXXXLarge = 32.dp
}