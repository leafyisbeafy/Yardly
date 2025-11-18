# Gemini Project Primer: Yardly

This document provides context for the Gemini CLI to assist with the development of the Yardly Android application.

## 1. Project Overview

- **Project Name**: Yardly
- **Platform**: Android (Jetpack Compose)
- **Core Functionality**: Yardly is a mobile application that appears to be a dashboard-style app. Key features include a home screen, a watchlist, a portfolio section, user profile management, and settings. It uses a bottom navigation bar for main screen transitions.

## 2. Technology Stack & Key Libraries

- **UI Framework**: Jetpack Compose
- **Primary Language**: Kotlin
- **Architecture**: The project follows a basic MVI/MVVM pattern within `MainActivity.kt`, managing state with `mutableStateOf`. The UI is organized into composable functions.
- **Key Jetpack Libraries**:
    - `androidx.compose.material3` (Material Design 3)
    - `androidx.compose.ui`
    - `androidx.lifecycle.viewmodel` (Implied for future state management)
    - `androidx.navigation.compose` (Likely used for screen routing)

## 3. Code Structure & Conventions

- **Main Entry Point**: `MainActivity.kt` contains the primary UI structure, including `TopBar`, `BottomNavigationBar`, and `ContentArea`.
- **State Management**: Simple state is managed in `MainActivity.kt` using `remember { mutableStateOf(...) }`. The `selectedIconSection` variable controls which main screen is displayed.
- **Composable Naming**: Composable functions should be named using PascalCase (e.g., `TopBar`, `ContentArea`).
- **UI Logic**: The main screen content is determined by a `when` block inside the `ContentArea` composable, which switches based on the `selectedIconSection` state.

## 4. How to Help: Assistant Guidelines

- **Code Generation**: When generating new composables, follow the existing style. Use Material 3 components (`androidx.compose.material3.*`) and structure them cleanly.
- **Refactoring**: A key area for improvement is refactoring `MainActivity.kt`. When asked, focus on breaking down large composables (like `ContentArea`) into smaller, more specific functions placed in their own files (e.g., `HomeScreen.kt`, `WatchlistScreen.kt`).
- **Answering Questions**: Base your explanations on the code found in the project. For example, when asked about `ContentArea`, explain how it acts as a router for the main app screens.
- **Bug Fixes**: Assume that state hoisting is a preferred pattern. If a composable manages its own state but shouldn't, suggest lifting the state up.

