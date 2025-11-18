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

    // 2. Lease (Slate - White Text)
    data object Lease : Category(
        id = "lease",
        label = "Lease",
        color = BtnSlateEmber,
        onColor = Color.White
    )

    // 3. Yardly (Forest - White Text)
    data object YardSales : Category(
        id = "yard-sales",
        label = "Yardly",
        color = BtnForestGlow,
        onColor = Color.White
    )

    // 4. Clothing (Terracotta - White Text)
    data object Clothing : Category(
        id = "clothing",
        label = "Clothing",
        color = BtnTerracotta,
        onColor = Color.White
    )

    // 5. Sneaker (Electric Lime - BLACK Text)
    data object Sneaker : Category(
        id = "sneaker",
        label = "Sneaker",
        color = BtnElectricLime,
        onColor = Color.Black
    )

    // 6. Electronics (Neon Azure - White Text)
    data object Electronics : Category(
        id = "electronics",
        label = "Electronics",
        color = BtnNeonAzure,
        onColor = Color.White
    )

    // 7. Gaming (Magenta - White Text)
    data object Gaming : Category(
        id = "gaming",
        label = "Gaming",
        color = BtnMagentaShock,
        onColor = Color.White
    )

    companion object {
        // The list used to generate UI buttons
        val all = listOf(
            Rehome, Lease, YardSales, Clothing, Sneaker, Electronics, Gaming
        )

        // Helper to safely get a category label by ID, useful for filtering if needed
        fun getLabelById(id: String): String {
            return all.find { it.id == id }?.label ?: "Home"
        }
    }
}