# Yardly: The Relocation Marketplace


Yardly isn't just another marketplace. It's a **relocation marketplace**, built from the ground up to solve the waste, cost, and stress of moving.

Think Marketplace, but laser-focused: **instant area-only drops, sublease swaps, and even pet rehomes**. No shipping, no landfill, no Craigslist weirdos. Yardly is a sublease + garage-sale mashup with a built-in conscience, turning dumping into deals and helping broke newcomers.

## 🚀 Core Features

This repository contains the native Android application for Yardly, built entirely with Kotlin and Jetpack Compose.

* **Hyper-Local Location Filtering:** Yardly ditches the standard "radius" search. Users filter by specific neighborhood "corners" (e.g., "SW, Cedar Rapids") or by "County" for broader, but still defined, accessibility.
* **Hyper-Local Location Filtering: Yardly ditches the standard "radius" search. Users filter by specific neighborhood "corners" (e.g., "SW, Cedar Rapids") or by "County" for broader, but still defined, accessibility.
* **Dual-Gesture Navigation:** The main `SectionNavigation` bar ("Aqua Swap," "Yard Sales," "Lease") is built for speed and power-users:
    * **Single-Tap:** Selects the main category.
    * **Double-Tap:** Reveals sub-options (like "Move Out" or "Room").
    * **Long-Press:** Triggers urgent, special-case actions like "Rehome".
* **Expanding Action Menu:** A multi-purpose Floating Action Button that expands to give users quick access to "Create Post" and "Choose Location".
* **"Saved" Watchlist with Price Drops:** A dedicated watchlist screen that uses a `WatchlistCard` to show saved items. It features a pulsing red icon to instantly alert users to price drops.
* **Component-Based UI:** A clean, scalable UI built with reusable, stateless components for everything from ad cards to login sheets and profiles.

## 🛠 Tech Stack & Architecture

* **Language:** 100% [Kotlin](https://kotlinlang.org/)
* **UI Toolkit:** [Jetpack Compose](https://developer.android.com/jetpack/compose)
* **Design System:** [Material 3](https://m3.material.io/)
* **Theme:** Custom `YardlyTheme` with full light and dark mode support.
* **Architecture:**
    * Single-Activity Model (`MainActivity.kt`).
    * Component-based UI layer (`ui/components` package).
    * Custom state-driven navigation (state is managed in `MainActivity.kt` and passed down to composables).

## 🏗️ Project Status

This project is in active development. The core UI, navigation, and state management foundation are in place.

### Future Roadmap
* Implement ViewModel layer to manage and hoist state out of composables.
* Integrate backend services (e.g., Firebase) for user auth and database.
* Build out the "Create Post" functionality.
* Add functionality to the "Create Collection" (plus) button on the Saved screen.
