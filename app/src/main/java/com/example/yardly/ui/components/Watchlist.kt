package com.example.yardly.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yardly.UserPost
import com.example.yardly.ui.theme.YardlyTheme
import com.example.yardly.ui.components.WatchlistCard

// Dummy data class for the grid
private data class WatchlistItem(val id: Int, val name: String, val price: String)

// This list now acts as the "database" of all possible items
private val dummyItems = listOf(
    // Original Items
    WatchlistItem(1, "Air Force 1", "$291.28"),
    WatchlistItem(2, "iPhone 13", "$299.99"),
    WatchlistItem(3, "PlayStation 4", "$300.00"),
    WatchlistItem(4, "Macbook Air 13", "$300.00"),
    WatchlistItem(5, "Denim Jacket", "$100.00"),
    WatchlistItem(6, "Razer Gaming Chair", "$222.00"),
    WatchlistItem(7, "Laptop", "$190.00"),
    WatchlistItem(8, "Monitor", "$200.00"),

    // Lease, Yard Sale, Aqua Swap Items
    WatchlistItem(9, "Sublet: 1-Bed Room", "$500.00"),
    WatchlistItem(10, "Shared Room Downtown", "$350.00"),
    WatchlistItem(11, "Moving Sale: Everything Must Go", "$1.00"),
    WatchlistItem(12, "Couch for Sale", "$150.00"),
    WatchlistItem(13, "Used 50g Filter", "$25.00"),
    WatchlistItem(14, "Zoanthid Frag", "$20.00"),
    WatchlistItem(15, "Goldfish needs home", "$0.00"),
    WatchlistItem(16, "Rare Coin Auction", "$1000.00"),

    // NEW Clothing Items
    WatchlistItem(17, "Vintage T-Shirt", "$45.00"),
    WatchlistItem(18, "Designer Jeans", "$120.00"),
    WatchlistItem(19, "Winter Coat", "$80.00"),

    // NEW Sneaker Items
    WatchlistItem(20, "Jordan 1s", "$250.00"),
    WatchlistItem(21, "Yeezy 350", "$220.00"),
    WatchlistItem(22, "New Balance 550", "$110.00"),

    // NEW Electronics Items
    WatchlistItem(23, "Sony Headphones", "$150.00"),
    WatchlistItem(24, "Dell Monitor", "$200.00"),
    WatchlistItem(25, "GoPro Hero 8", "$180.00"),

    // NEW Gaming Items
    WatchlistItem(26, "Nintendo Switch", "$280.00"),
    WatchlistItem(27, "PS5 Controller", "$60.00"),
    WatchlistItem(28, "Logitech Mouse", "$40.00")
)

@Composable
fun WatchlistScreen(
    onBackClick: () -> Unit,
    savedItems: Map<String, Boolean>,
    saveCounts: Map<String, Int>,
    onSaveClick: (String) -> Unit,
    userPosts: List<UserPost> = emptyList()
) {
    val userWatchlistItems = userPosts.map { post ->
        WatchlistItem(
            id = post.id.toInt(),
            name = post.title,
            price = "$${post.price}"
        )
    }

    val combinedItems = userWatchlistItems + dummyItems

    val dynamicallySavedItems = combinedItems.filter { item ->
        savedItems.getOrDefault(item.name, false)
    }

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
            // We now use the new 'dynamicallySavedItems' list
            items(dynamicallySavedItems) { item ->
                val isSaved = savedItems.getOrDefault(item.name, false)
                val saveCount = saveCounts.getOrDefault(item.name, 0)

                WatchlistCard(
                    itemName = item.name,
                    price = item.price,
                    isSaved = isSaved,
                    saveCount = saveCount,
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
            // Back Arrow
            Button(
                onClick = onBackClick,
                shape = CircleShape,
                modifier = Modifier.size(40.dp),
                contentPadding = PaddingValues(0.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.onBackground
                )
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Title
            Text(
                text = "Saved",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.weight(1f))

            // Plus Icon
            IconButton(onClick = { /* Add new item to watchlist */ }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

@Preview
@Composable
fun WatchlistScreenPreview() {
    YardlyTheme(isDarkMode = false) {
        WatchlistScreen(
            onBackClick = {},
            savedItems = mapOf("Air Force 1" to true, "Jordan 1s" to true),
            saveCounts = mapOf("Air Force 1" to 3, "Jordan 1s" to 1),
            onSaveClick = {},
            userPosts = emptyList()
        )
    }
}

@Preview
@Composable
private fun WatchlistTopBarPreview() {
    YardlyTheme(isDarkMode = false) {
        WatchlistTopBar(onBackClick = {})
    }
}