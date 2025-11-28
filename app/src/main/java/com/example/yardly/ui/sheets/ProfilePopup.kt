package com.example.yardly.ui.sheets

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.yardly.data.model.UserPost
import com.example.yardly.ui.components.WatchlistCard
import com.example.yardly.ui.theme.Dimens
import com.example.yardly.ui.theme.YardlyTheme

/**
 * Bottom sheet for displaying user profile with their listings.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfilePopup(
    name: String,
    username: String,
    bio: String,
    imageUri: Uri?,
    userPosts: List<UserPost>,
    saveCounts: Map<String, Int>,
    savedItems: Map<String, Boolean>,
    showModal: Boolean,
    onDismiss: () -> Unit,
    onBackClick: () -> Unit = {},
    onEditClick: () -> Unit = {},
    onMenuClick: () -> Unit = {},
    onNavigateToAdDetail: (UserPost) -> Unit,
    onSaveClick: (String) -> Unit,
    isEditMode: Boolean = false
) {
    if (showModal) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = rememberModalBottomSheetState(),
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            containerColor = MaterialTheme.colorScheme.surface,
            scrimColor = Color.Black.copy(alpha = 0.4f),
            dragHandle = null
        ) {
            ProfileContent(
                name = name, username = username, bio = bio, imageUri = imageUri,
                userPosts = userPosts, saveCounts = saveCounts, savedItems = savedItems,
                onBackClick = onBackClick, onEditClick = onEditClick, onMenuClick = onMenuClick,
                onNavigateToAdDetail = onNavigateToAdDetail, onSaveClick = onSaveClick, isEditMode = isEditMode
            )
        }
    }
}

@Composable
fun ProfileContent(
    name: String, username: String, bio: String, imageUri: Uri?,
    userPosts: List<UserPost>, saveCounts: Map<String, Int>, savedItems: Map<String, Boolean>,
    onBackClick: () -> Unit, onEditClick: () -> Unit, onMenuClick: () -> Unit,
    onNavigateToAdDetail: (UserPost) -> Unit, onSaveClick: (String) -> Unit, isEditMode: Boolean
) {
    var selectedTab by remember { mutableStateOf("Available") }
    val tabs = listOf("Available", "Gone", "Pick it up")

    Column(modifier = Modifier.fillMaxWidth().fillMaxHeight(0.95f)) {
        TopProfileBar(onBackClick = onBackClick, onEditClick = onEditClick, onMenuClick = onMenuClick, isEditMode = isEditMode)

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = Dimens.ScreenPaddingHorizontal, vertical = Dimens.SpacingXLarge),
            verticalArrangement = Arrangement.spacedBy(Dimens.SpacingLarge)
        ) {
            item {
                Column {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(name, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                            Text(username, fontSize = 16.sp, color = Color(0xFF666666))
                        }
                        Box(modifier = Modifier.size(80.dp)) {
                            Box(modifier = Modifier.fillMaxSize().clip(CircleShape).background(Color(0xFF0F1B3C)), contentAlignment = Alignment.Center) {
                                if (imageUri != null) {
                                    AsyncImage(model = imageUri, contentDescription = "Profile Picture", modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                                } else {
                                    Text(name.take(1).uppercase(), fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }
                            Icon(Icons.Default.CheckCircle, "Verified", tint = Color(0xFF1DA1F2), modifier = Modifier.size(24.dp).align(Alignment.BottomEnd).offset(x = Dimens.SpacingSmall, y = Dimens.SpacingSmall))
                        }
                    }
                    Spacer(modifier = Modifier.height(Dimens.ScreenPaddingVertical))
                    Text(bio, fontSize = 15.sp, color = MaterialTheme.colorScheme.onBackground, lineHeight = 20.sp)
                    Spacer(modifier = Modifier.height(Dimens.SpacingXLarge))
                    Surface(modifier = Modifier.border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(16.dp)), shape = RoundedCornerShape(16.dp), color = Color.Transparent) {
                        Row(modifier = Modifier.padding(horizontal = Dimens.SpacingLarge, vertical = Dimens.SpacingMedium), verticalAlignment = Alignment.CenterVertically) {
                            Text("Channel: $username's Shop", fontSize = 14.sp, color = MaterialTheme.colorScheme.onBackground)
                        }
                    }
                    Spacer(modifier = Modifier.height(Dimens.SpacingXXLarge))
                    TabRow(
                        selectedTabIndex = tabs.indexOf(selectedTab), containerColor = Color.Transparent, contentColor = MaterialTheme.colorScheme.onBackground,
                        indicator = { tabPositions -> TabRowDefaults.SecondaryIndicator(Modifier.tabIndicatorOffset(tabPositions[tabs.indexOf(selectedTab)]), 2.dp, MaterialTheme.colorScheme.onBackground) },
                        divider = {}
                    ) {
                        tabs.forEach { tabTitle ->
                            Tab(selected = selectedTab == tabTitle, onClick = { selectedTab = tabTitle }, text = {
                                Text(tabTitle, fontWeight = if (selectedTab == tabTitle) FontWeight.Bold else FontWeight.Normal, color = if (selectedTab == tabTitle) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onSurfaceVariant)
                            })
                        }
                    }
                    Spacer(modifier = Modifier.height(Dimens.SpacingLarge))
                }
            }

            if (selectedTab == "Available") {
                if (userPosts.isEmpty()) {
                    item { Box(modifier = Modifier.fillMaxWidth().padding(top = 40.dp), contentAlignment = Alignment.Center) { Text("No listings yet.", color = MaterialTheme.colorScheme.onSurfaceVariant) } }
                } else {
                    items(userPosts) { post ->
                        WatchlistCard(
                            itemName = post.title, price = "$${post.price}", imageUriString = post.imageUriString,
                            isSaved = savedItems.getOrDefault(post.title, false), saveCount = saveCounts.getOrDefault(post.title, 0),
                            onItemClick = { onNavigateToAdDetail(post) }, onSaveClick = { onSaveClick(post.title) }
                        )
                        Spacer(modifier = Modifier.height(Dimens.SpacingMedium))
                    }
                }
            } else {
                item { Text("Content for $selectedTab", fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 20.dp)) }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopProfileBar(onBackClick: () -> Unit, onEditClick: () -> Unit, onMenuClick: () -> Unit, isEditMode: Boolean) {
    TopAppBar(
        title = { },
        navigationIcon = { IconButton(onClick = onBackClick) { Icon(Icons.Filled.ArrowBack, "Back", Modifier.size(28.dp), MaterialTheme.colorScheme.onBackground) } },
        actions = {
            IconButton(onClick = onEditClick) { Icon(Icons.Filled.Edit, "Edit", tint = if (isEditMode) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground, modifier = Modifier.size(24.dp)) }
            IconButton(onClick = onMenuClick) { Icon(Icons.Default.MoreVert, "More options", Modifier.size(24.dp), MaterialTheme.colorScheme.onBackground) }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
    )
}

@Preview(showBackground = true, name = "Light Mode")
@Composable
fun ProfilePopupPreview() {
    YardlyTheme(isDarkMode = false) {
        ProfilePopup(name = "Preview Name", username = "preview_user", bio = "This is a preview bio.", imageUri = null, userPosts = emptyList(), saveCounts = emptyMap(), savedItems = emptyMap(), showModal = true, onDismiss = {}, onNavigateToAdDetail = {}, onSaveClick = {})
    }
}

@Preview(showBackground = true, name = "Dark Mode")
@Composable
fun ProfilePopupDarkPreview() {
    YardlyTheme(isDarkMode = true) {
        ProfilePopup(name = "Dark Mode User", username = "dark_user", bio = "Testing dark mode preview.", imageUri = null, userPosts = emptyList(), saveCounts = emptyMap(), savedItems = emptyMap(), showModal = true, onDismiss = {}, onNavigateToAdDetail = {}, onSaveClick = {})
    }
}