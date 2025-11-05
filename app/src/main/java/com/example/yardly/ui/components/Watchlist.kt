package com.example.yardly.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yardly.ui.theme.YardlyTheme
import com.example.yardly.ui.components.WatchlistCard

// Dummy data class for the grid
private data class WatchlistItem(val id: Int, val name: String, val price: String)

private val dummyItems = listOf(
    WatchlistItem(1, "Air Force 1", "$291.28"),
    WatchlistItem(2, "iPhone 13", "$299.99"),
    WatchlistItem(3, "PlayStation 4", "$300.00"),
    WatchlistItem(4, "Macbook Air 13", "$300.00"),
    WatchlistItem(5, "Denim Jacket", "$100.00"),
    WatchlistItem(6, "Razer Gaming Chair", "$222.00"),
    WatchlistItem(7, "Laptop", "$190.00"),
    WatchlistItem(8, "Monitor", "$200.00"),
)

@Composable
fun WatchlistScreen(
    onBackClick: () -> Unit,
    savedItems: Map<String, Boolean>,
    saveCounts: Map<String, Int>, // <-- ADDED
    onSaveClick: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        // 1. New Top Bar
        WatchlistTopBar(onBackClick = onBackClick)

        // 2. Two-Column Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(dummyItems) { item ->
                val isSaved = savedItems.getOrDefault(item.name, false)
                val saveCount = saveCounts.getOrDefault(item.name, 0) // <-- GET COUNT

                WatchlistCard(
                    itemName = item.name,
                    price = item.price,
                    isSaved = isSaved,
                    saveCount = saveCount, // <-- PASS COUNT
                    onSaveClick = { onSaveClick(item.name) }
                )
            }
        }
    }
}

@Composable
private fun WatchlistTopBar(
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Watchlist",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Preview
@Composable
fun WatchlistScreenPreview() {
    YardlyTheme(isDarkMode = false) {
        WatchlistScreen(
            onBackClick = {},
            savedItems = mapOf("Air Force 1" to true),
            saveCounts = mapOf("Air Force 1" to 3), // <-- ADDED
            onSaveClick = {}
        )
    }
}