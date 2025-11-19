package com.example.yardly.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Single source of truth for Yardly Categories.
 * This handles the specific Color and Text Color (Black/White) for each section.
 */
sealed class Category(
    val id: String,
    val label: String,
    val color: Color,
    val onColor: Color // The text color to use when the button is filled
) {
    // 1. Rehome (Teal - White Text)
    data object Rehome : Category(
        id = "rehome",
        label = "Rehome",
        color = BtnTealPulse,
        onColor = Color.White
    )

    // 2. Sublease (Slate - White Text)
    data object Sublease : Category(
        id = "sublease",
        label = "Sublease",
        color = BtnSlateEmber,
        onColor = Color.White
    )

    // 3. Textbook (Forest - White Text)
    data object Textbook : Category(
        id = "textbook",
        label = "Textbook",
        color = BtnForestGlow,
        onColor = Color.White
    )

    // 4. Moving Out (Terracotta - White Text)
    data object MovingOut : Category(
        id = "moving_out",
        label = "Moving Out",
        color = BtnTerracotta,
        onColor = Color.White
    )

    // 5. Garage Sale (Dark Orange - White Text)
    data object GarageSale : Category(
        id = "garage_sale",
        label = "Garage Sale",
        color = BtnDarkOrange,
        onColor = Color.White
    )

    // 6. Sneaker (Electric Lime - BLACK Text)
    data object Sneaker : Category(
        id = "sneaker",
        label = "Sneaker",
        color = BtnElectricLime,
        onColor = Color.Black
    )

    // 7. Electronics (Neon Azure - White Text)
    data object Electronics : Category(
        id = "electronics",
        label = "Electronics",
        color = BtnNeonAzure,
        onColor = Color.White
    )

    companion object {
        // The list used to generate UI buttons
        val all = listOf(
            Rehome, Sublease, Textbook, MovingOut, GarageSale, Sneaker, Electronics
        )

        // Helper to safely get a category label by ID
        fun getLabelById(id: String): String {
            return all.find { it.id == id }?.label ?: "Home"
        }
    }
}