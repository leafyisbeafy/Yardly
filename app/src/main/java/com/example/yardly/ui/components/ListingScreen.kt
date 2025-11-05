package com.example.yardly.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight // <-- THIS IS THE FIX
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yardly.ui.theme.YardlyTheme

// Dummy list of categories
private val categories = listOf(
    "Women",
    "Men",
    "Electronics",
    "Toys & Collectibles",
    "Home",
    "Beauty",
    "Kids",
    "Vintage & collectibles",
    "Sports & outdoors",
    "Handmade",
    "Arts & Crafts"
)

@Composable
fun ListingScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // 1. Top Bar with Search
            ListingTopBar()

            // 2. Category List
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f) // Fills the remaining space
            ) {
                item {
                    Text(
                        text = "Categories",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 16.dp)
                    )
                }
                items(categories) { categoryName ->
                    CategoryRow(name = categoryName, onClick = { /* TODO */ })
                }
            }
        }

        // Camera Button (Stays at the bottom)
        FloatingActionButton(
            onClick = { /* TODO: Add post action */ },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            shape = CircleShape,
            containerColor = MaterialTheme.colorScheme.primary, // Black
            contentColor = MaterialTheme.colorScheme.onPrimary // White
        ) {
            Icon(Icons.Filled.CameraAlt, contentDescription = "Create Post")
        }
    }
}

@Composable
private fun CategoryRow(
    name: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp), // Padding for the row
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Category Name
            Text(
                text = name,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            // No icon or arrow
        }
        Divider(
            color = MaterialTheme.colorScheme.surfaceVariant, // Light gray
            modifier = Modifier.padding(horizontal = 20.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ListingTopBar() {
    var searchText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface) // White background
    ) {
        // Spacer for the system status bar
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
        )

        // Search Bar Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = searchText,
                onValueChange = { searchText = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        text = "Search listings...",
                        color = MaterialTheme.colorScheme.onSurfaceVariant // Gray
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = MaterialTheme.colorScheme.onBackground // Black
                    )
                },
                shape = RoundedCornerShape(24.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant, // Light gray
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant, // Light gray
                    disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant, // Light gray

                    focusedTextColor = MaterialTheme.colorScheme.onBackground, // Black
                    unfocusedTextColor = MaterialTheme.colorScheme.onBackground, // Black

                    disabledIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
        }
    }
}

// Previews
@Preview
@Composable
fun ListingScreenPreview() {
    YardlyTheme {
        ListingScreen()
    }
}

@Preview
@Composable
fun ListingTopBarPreview() {
    YardlyTheme {
        ListingTopBar()
    }
}