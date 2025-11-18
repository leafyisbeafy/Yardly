package com.example.yardly.ui.components

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yardly.UserPost
import com.example.yardly.ui.theme.Dimens
import com.example.yardly.ui.theme.YardlyTheme

@Composable
fun ProfileContent(
    name: String,
    username: String,
    bio: String,
    userPosts: List<UserPost>, // <--- NEW: The list arrives here
    onBackClick: () -> Unit,
    onEditClick: () -> Unit,
    onMenuClick: () -> Unit,
    onDummyListingClick: () -> Unit,
    onSaveClick: (String) -> Unit // <--- NEW: Save action arrives here
) {
    var isEditMode by remember { mutableStateOf(false) }

    var selectedTab by remember { mutableStateOf("Available") }
    val tabs = listOf("Available", "Gone", "Pick it up")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.95f) // This allows the sheet to be expandable
    ) {
        TopProfileBar(
            onBackClick = onBackClick,
            onEditClick = {
                isEditMode = !isEditMode
                onEditClick()
            },
            onMenuClick = onMenuClick,
            isEditMode = isEditMode
        )

        // We use LazyColumn for the whole content so it scrolls as one unit
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = Dimens.ScreenPaddingHorizontal, vertical = Dimens.SpacingXLarge),
            verticalArrangement = Arrangement.spacedBy(Dimens.SpacingLarge)
        ) {
            // --- HEADER SECTION ---
            item {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Left side - Title and username
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = name,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = username,
                                fontSize = 16.sp,
                                color = Color(0xFF666666)
                            )
                        }

                        // Right side - Logo with verified checkmark
                        Box(
                            modifier = Modifier.size(80.dp)
                        ) {
                            // University logo placeholder
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape)
                                    .background(Color(0xFF0F1B3C)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = name.take(1), // Initials
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }

                            // Verified checkmark
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Verified",
                                tint = Color(0xFF1DA1F2),
                                modifier = Modifier
                                    .size(24.dp)
                                    .align(Alignment.BottomEnd)
                                    .offset(x = Dimens.SpacingSmall, y = Dimens.SpacingSmall)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(Dimens.ScreenPaddingVertical))

                    Text(
                        text = bio,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(Dimens.SpacingXLarge))

                    Surface(
                        modifier = Modifier.border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp),
                        color = Color.Transparent
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = Dimens.SpacingLarge, vertical = Dimens.SpacingMedium),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Channel: $username's Shop",
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(Dimens.SpacingXXLarge))

                    // TAB ROW
                    TabRow(
                        selectedTabIndex = tabs.indexOf(selectedTab),
                        containerColor = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.onBackground,
                        indicator = { tabPositions ->
                            TabRowDefaults.SecondaryIndicator(
                                modifier = Modifier.tabIndicatorOffset(tabPositions[tabs.indexOf(selectedTab)]),
                                height = 2.dp,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        },
                        divider = {}
                    ) {
                        tabs.forEach { tabTitle ->
                            Tab(
                                selected = selectedTab == tabTitle,
                                onClick = { selectedTab = tabTitle },
                                text = {
                                    Text(
                                        text = tabTitle,
                                        fontWeight = if (selectedTab == tabTitle) FontWeight.Bold else FontWeight.Normal,
                                        color = if (selectedTab == tabTitle) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(Dimens.SpacingLarge))
                }
            }

            // --- CONTENT SECTION ---
            if (selectedTab == "Available") {
                if (userPosts.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 40.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No listings yet.",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else {
                    // Dynamic List of Posts
                    items(userPosts) { post ->
                        WatchlistCard(
                            itemName = post.title,
                            price = "$${post.price}",
                            isSaved = false, // Logic can be added later
                            saveCount = 0,
                            onItemClick = onDummyListingClick,
                            onSaveClick = { onSaveClick(post.title) }
                        )
                        Spacer(modifier = Modifier.height(Dimens.SpacingMedium))
                    }
                }
            } else {
                item {
                    Text(
                        text = "Content for $selectedTab",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 20.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopProfileBar(
    onBackClick: () -> Unit,
    onEditClick: () -> Unit,
    onMenuClick: () -> Unit,
    isEditMode: Boolean
) {
    TopAppBar(
        title = { },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier.size(28.dp),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        },
        actions = {
            IconButton(onClick = onEditClick) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit",
                    tint = if (isEditMode) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(24.dp)
                )
            }
            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More options",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}

@Preview(showBackground = true, heightDp = 800)
@Composable
fun ProfileContentPreview() {
    YardlyTheme(isDarkMode = false) {
        ProfileContent(
            name = "Peyton Venzeee",
            username = "peyton",
            bio = "This is a preview bio.",
            userPosts = emptyList(),
            onBackClick = {},
            onEditClick = {},
            onMenuClick = {},
            onDummyListingClick = {},
            onSaveClick = {}
        )
    }
}