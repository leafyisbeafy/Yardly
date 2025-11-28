package com.example.yardly.ui.screens.home

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.yardly.data.SampleData
import com.example.yardly.data.model.Ad
import com.example.yardly.data.model.UserPost
import com.example.yardly.ui.components.AdCard
import com.example.yardly.ui.navigation.ProfileScreenState
import com.example.yardly.ui.screens.messenger.MessengerScreen
import com.example.yardly.ui.screens.settings.AccessibilityScreen
import com.example.yardly.ui.screens.settings.DarkModeScreen
import com.example.yardly.ui.screens.settings.SettingsScreen
import com.example.yardly.ui.screens.watchlist.WatchlistScreen
import com.example.yardly.ui.sheets.EditProfileScreen
import com.example.yardly.ui.sheets.ProfileContent
import com.example.yardly.ui.theme.Category
import com.example.yardly.ui.theme.Dimens

@Composable
fun ContentArea(
    userPosts: List<UserPost>,
    ads: List<Ad>,
    gridState: LazyGridState,
    selectedIconSection: String,
    selectedNavSection: String,
    profileScreenState: ProfileScreenState,
    isDarkMode: Boolean,
    saveCounts: Map<String, Int>,
    savedItems: Map<String, Boolean>,
    selectedPost: UserPost?,

    profileName: String,
    profileUsername: String,
    profileBio: String,
    profileImageUri: Uri?,
    onSaveProfile: (String, String, String, Uri?) -> Unit,
    onSaveImagePermanently: (Uri) -> Uri?,

    onAdClick: (Ad) -> Unit = {},
    onBackClick: () -> Unit = {},
    onSettingsBackClick: () -> Unit = {},
    onAccessibilityBackClick: () -> Unit = {},
    onDarkModeBackClick: () -> Unit = {},
    onEditProfileBackClick: () -> Unit = {},
    onAdDetailBackClick: () -> Unit = {},
    onUserClick: () -> Unit = {},
    onMenuClick: () -> Unit = {},
    onEditClick: () -> Unit = {},
    onAccessibilityClick: () -> Unit = {},
    onDarkModeClick: () -> Unit = {},
    onDarkModeToggle: (Boolean) -> Unit,
    onSaveClick: (String) -> Unit,
    onPostClick: (UserPost) -> Unit
) {
    // 1. COMBINE DATA Sources
    val allPosts = remember(userPosts) { userPosts + SampleData.systemPosts }

    // 2. FILTERING LOGIC
    val currentCategoryLabel = Category.getLabelById(selectedNavSection)

    val visiblePosts = remember(selectedNavSection, allPosts) {
        if (selectedNavSection == "home-default") {
            allPosts
        } else {
            allPosts.filter { it.category == currentCategoryLabel }
        }
    }

    when (selectedIconSection) {
        "home" -> {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                state = gridState,
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(Dimens.SpacingLarge),
                horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingLarge),
                contentPadding = PaddingValues(
                    horizontal = Dimens.ScreenPaddingHorizontal,
                    vertical = Dimens.ScreenPaddingVertical
                )
            ) {
                items(visiblePosts, key = { it.id }) { post ->
                    val saveCount = saveCounts.getOrDefault(post.title, 0)
                    val isSaved = savedItems.getOrDefault(post.title, false)

                    AdCard(
                        advertisementName = post.title,
                        userName = post.userName,
                        saveCount = saveCount,
                        isSaved = isSaved,
                        onAdClick = { onAdClick(Ad(post.title, post.userName)) },
                        onUserClick = onUserClick,
                        onSaveClick = { onSaveClick(post.title) }
                    )
                }
            }
        }
        "watchlist" -> {
            WatchlistScreen(
                onBackClick = onBackClick,
                savedItems = savedItems,
                saveCounts = saveCounts,
                onSaveClick = onSaveClick,
                allPosts = allPosts,
                onPostClick = onPostClick
            )
        }
        "profile" -> {
            when (profileScreenState) {
                ProfileScreenState.Profile -> ProfileContent(
                    name = profileName,
                    username = profileUsername,
                    bio = profileBio,
                    imageUri = profileImageUri,
                    userPosts = userPosts,
                    saveCounts = saveCounts,
                    savedItems = savedItems,
                    onBackClick = onBackClick,
                    onEditClick = onEditClick,
                    onMenuClick = onMenuClick,
                    onNavigateToAdDetail = onPostClick,
                    onSaveClick = onSaveClick,
                    isEditMode = false
                )
                ProfileScreenState.Settings -> SettingsScreen(
                    onBackClick = onSettingsBackClick,
                    onAccessibilityClick = onAccessibilityClick,
                    onDarkModeClick = onDarkModeClick
                )
                ProfileScreenState.Accessibility -> AccessibilityScreen(
                    onBackClick = onAccessibilityBackClick,
                    onDarkModeClick = onDarkModeClick
                )
                ProfileScreenState.DarkMode -> DarkModeScreen(
                    onBackClick = onDarkModeBackClick,
                    isDarkMode = isDarkMode,
                    onToggle = onDarkModeToggle
                )
                ProfileScreenState.EditProfile -> EditProfileScreen(
                    currentName = profileName,
                    currentUsername = profileUsername,
                    currentBio = profileBio,
                    currentImageUri = profileImageUri,
                    onBackClick = onEditProfileBackClick,
                    onSaveClick = onSaveProfile,
                    onSaveImagePermanently = onSaveImagePermanently
                )
                ProfileScreenState.AdDetail -> {
                    // Placeholder for AdDetail if accessed via legacy state
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Ad Detail is now a popup sheet.")
                    }
                }
            }
        }
        "messenger" -> {
            MessengerScreen(
                onBackClick = onBackClick
            )
        }

        "notification" -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Notifications",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        else -> Text(
            text = "Welcome",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp
        )
    }
}

