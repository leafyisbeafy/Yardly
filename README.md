# Yardly: The Relocation Marketplace

Yardly isn't just another marketplace.

Facebook Marketplace is not solving the problem; Craigslist is full of odd individuals. There are niche apps for subleasing and pet adoption, but I don’t think people want to keep dozens of apps on their phones.

It pains me to see people trapped in the cycle of subleasing for rooms and cars. Even if they want to transfer deposits and pay rent, they struggle to find someone.

Students cannot make money from their used textbooks, and the goodwill of stationary is too far out of reach.

A student who kept pests is now unable to care for them and is trying to find someone who can take them in, but is struggling to find the right person.

I’m giving it a shot to fix the problems I see every other day.

## Core Features

This repository contains the native Android application for Yardly, built entirely with Kotlin and Jetpack Compose.

* **Hyper-Local Location Filtering:** Yardly ditches the standard "radius" search. Users filter by specific neighborhood "corners" (e.g., "SW, Cedar Rapids") or by "County" for broader, but still defined, accessibility.
* **Dual-Gesture Navigation:** The main `SectionNavigation` bar is built for speed and power-users:
    * **Single-Tap:** Selects the main category.
    * **Double-Tap:** Reveals sub-options (like "Move Out" or "Room").
    * **Long-Press:** Triggers urgent, special-case actions like "Rehome".
* **"Saved" Watchlist with Price Drops:** A dedicated watchlist screen that uses a `WatchlistCard` to show saved items. It will keep the student engaged by encouraging them to check back for notifications about price drops. Everyone, at some point, wants to get rid of their items to make some money, often by lowering the price. This creates a loop of dopamine similar to social media, though not exactly the same.
* **Component-Based UI:** A clean, scalable UI built with reusable, stateless components for everything from ad cards to login sheets and profiles.

## Tech Stack

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
* Integrate backend services (e.g., Firebase) for user auth and database.
* Build out the "Create Post" functionality.


