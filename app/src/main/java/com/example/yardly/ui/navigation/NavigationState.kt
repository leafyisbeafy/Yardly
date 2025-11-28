package com.example.yardly.ui.navigation

/**
 * Sealed class representing all possible screen states in the profile section.
 * Used for navigation management within the profile tab.
 */
sealed class ProfileScreenState {
    data object Profile : ProfileScreenState()
    data object Settings : ProfileScreenState()
    data object Accessibility : ProfileScreenState()
    data object DarkMode : ProfileScreenState()
    data object EditProfile : ProfileScreenState()
    data object AdDetail : ProfileScreenState()
}

