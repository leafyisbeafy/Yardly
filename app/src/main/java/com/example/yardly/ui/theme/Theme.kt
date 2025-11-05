package com.example.yardly.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = AppBlack,                // Primary actions (like 'Rehome' button)
    onPrimary = AppWhite,              // Text on black buttons

    background = AppWhite,             // App background
    onBackground = AppBlack,           // Default text color

    surface = AppWhite,                // Card/Sheet backgrounds
    onSurface = AppBlack,              // Text on cards

    onSurfaceVariant = AppDarkGray,    // Secondary text color (gray)

    surfaceVariant = AppGray,          // Light gray (for selected buttons)

    // Other colors set to monochrome
    secondary = AppGray,
    onSecondary = AppBlack,
    tertiary = AppGray,
    onTertiary = AppBlack,
)

@Composable
fun YardlyTheme(
    darkTheme: Boolean = false, // Always force light theme
    dynamicColor: Boolean = false, // Disable dynamic colors
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}