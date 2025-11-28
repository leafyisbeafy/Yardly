# Yardly: Hyperlocal Marketplace

Yardly isn't just another marketplace.

Yardly ditches the standard "radius" search. Users filter by specific neighborhood "corners"

Facebook Marketplace is not solving the problem; Craigslist is full of odd individuals. There are niche apps for subleasing and pet adoption, but I don’t think people want to keep dozens of apps on their phones.

It pains me to see people trapped in the cycle of subleasing for rooms and cars. Even if they want to transfer deposits and pay rent, they struggle to find someone.

Students cannot make money from their used textbooks

A student who kept pests is now unable to care for them and is trying to find someone who can take them in, but is struggling to find the right person.

I’m giving it a shot to fix the problems I see every other day.

---

## Tech Stack

- **Language:** Kotlin
- **UI Framework:** Jetpack Compose (Material 3)
- **Architecture:** Single-Activity with Composable-based navigation
- **Image Loading:** Coil
- **Image Cropping:** Android Image Cropper (CanHub)
- **Persistence:** SharedPreferences + JSON file storage
- **Theming:** Dynamic dark/light mode support

---

## Project Structure

```
app/src/main/java/com/example/yardly/
│
├── MainActivity.kt                 # App entry point, theme setup, edge-to-edge config
│
├── data/                           # Data layer
│   ├── model/
│   │   ├── Ad.kt                   # Ad data class (name, user, image resource)
│   │   └── UserPost.kt             # User-created post data class
│   ├── repository/
│   │   └── PostStorage.kt          # JSON-based persistence for user posts
│   └── SampleData.kt               # Mock data for development/testing
│
└── ui/                             # UI layer
    ├── YardlyApp.kt                # Main app composable, state management, navigation logic
    │
    ├── components/                 # Reusable UI components
    │   ├── AdCard.kt               # Grid item card for marketplace listings
    │   ├── BottomIconNavigation.kt # Bottom navigation bar (Home, Messenger, Watchlist, Profile)
    │   ├── ExitDialog.kt           # Confirmation dialog for exit actions
    │   ├── FindNear.kt             # Location-based search component
    │   ├── SectionNavigation.kt    # Category filter pills (horizontal scroll)
    │   ├── SettingsRow.kt          # Reusable settings list item
    │   ├── TopBar.kt               # App header with Channel button
    │   └── WatchlistCard.kt        # Saved item card for watchlist
    │
    ├── navigation/
    │   └── NavigationState.kt      # Navigation state definitions (ProfileScreenState)
    │
    ├── screens/                    # Full-screen composables
    │   ├── home/
    │   │   └── ContentArea.kt      # Main content router (grid, profile, settings, etc.)
    │   ├── messenger/
    │   │   └── MessengerScreen.kt  # Chat/messaging interface
    │   ├── settings/
    │   │   ├── AccessibilityScreen.kt  # Accessibility options
    │   │   ├── DarkModeScreen.kt       # Dark mode toggle screen
    │   │   └── SettingsScreen.kt       # Main settings menu
    │   └── watchlist/
    │       └── WatchlistScreen.kt  # Saved items list
    │
    ├── sheets/                     # Bottom sheet composables
    │   ├── AdDetailSheet.kt        # Post detail view (full info, save, contact)
    │   ├── AdLoginSheet.kt         # Login prompt modal
    │   ├── ChooseCornerSheet.kt    # Neighborhood/corner selector
    │   ├── CreatePostSheet.kt      # New listing creation form
    │   ├── EditProfileSheet.kt     # Profile editing form
    │   └── ProfilePopup.kt         # User profile preview popup
    │
    └── theme/                      # Theming & styling
        ├── Category.kt             # Category definitions with colors
        ├── Color.kt                # Color palette (light/dark)
        ├── Theme.kt                # YardlyTheme composable, Dimens object
        └── Type.kt                 # Typography definitions
```

---

## Features

- **Marketplace Grid** - Browse listings in a responsive grid layout
- **Category Filtering** - Filter by categories (Textbooks, Electronics, Furniture, etc.)
- **Corner-based Search** - Find items in specific neighborhood corners (unique to Yardly!)
- **User Profiles** - Create and edit your profile with custom images
- **Watchlist** - Save items for later
- **Create Listings** - Post items with photos, descriptions, and pricing
- **Dark Mode** - Full dark/light theme support
- **Messenger** - In-app messaging (UI ready)

---

## Getting Started

1. Clone the repository
2. Open in Android Studio (Hedgehog or newer recommended)
3. Sync Gradle dependencies
4. Run on emulator or physical device (API 24+)

---

## Future Roadmap

- [ ] Integrate backend services (Firebase) for user auth and database
- [ ] Real-time messaging with push notifications
- [ ] Map integration for corner-based location selection
- [ ] Search functionality
- [ ] User ratings and reviews
