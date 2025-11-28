package com.example.yardly.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yardly.UserPost
import com.example.yardly.ui.theme.Dimens
import com.example.yardly.ui.theme.YardlyTheme

@Composable
fun WatchlistScreen(
    onBackClick: () -> Unit,
    savedItems: Map<String, Boolean>,
    saveCounts: Map<String, Int>,
    onSaveClick: (String) -> Unit,
    allPosts: List<UserPost>,
    // *** FIX: Parameter is now explicitly named onPostClick ***
    onPostClick: (UserPost) -> Unit
) {

    val dynamicallySavedItems = remember(allPosts, savedItems) {
        allPosts.filter { post ->
            savedItems.getOrDefault(post.title, false)
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        WatchlistTopBar(onBackClick = onBackClick)

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(Dimens.ScreenPaddingHorizontal),
            verticalArrangement = Arrangement.spacedBy(Dimens.SpacingLarge),
            horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingLarge)
        ) {
            items(dynamicallySavedItems, key = { it.id }) { post ->
                val saveCount = saveCounts.getOrDefault(post.title, 0)
                WatchlistCard(
                    itemName = post.title,
                    price = "$${post.price}",
                    imageUriString = post.imageUriString,
                    isSaved = true,
                    saveCount = saveCount,
                    // *** FIX: Pass the click event correctly ***
                    onItemClick = { onPostClick(post) },
                    onSaveClick = { onSaveClick(post.title) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WatchlistTopBar(
    onBackClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = "Saved",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier.size(28.dp)
                )
            }
        },
        actions = {
            IconButton(onClick = { /* Add new item */ }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}

@Preview
@Composable
fun WatchlistScreenPreview() {
    YardlyTheme(isDarkMode = false) {
        WatchlistScreen(
            onBackClick = {},
            savedItems = mapOf("Jordan" to true),
            saveCounts = mapOf("Jordan" to 8),
            onSaveClick = {},
            allPosts = listOf(
                UserPost(title = "Jordan", price = "10.00", category = "Shoes", description = "Good", location = "Campus", imageUriString = null)
            ),
            onPostClick = {}
        )
    }
}