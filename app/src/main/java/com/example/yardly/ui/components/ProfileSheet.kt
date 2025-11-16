package com.example.yardly.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yardly.ui.theme.Dimens
import com.example.yardly.ui.theme.YardlyTheme
import androidx.compose.material.icons.filled.Edit
import com.example.yardly.ui.components.WatchlistCard

// The ModalBottomSheet logic has been moved to ProfilePopup.kt

@Composable
fun ProfileContent(
    name: String,
    username: String,
    bio: String,
    onBackClick: () -> Unit,
    onEditClick: () -> Unit,
    onMenuClick: () -> Unit,
    onDummyListingClick: () -> Unit
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                // *** FIXED PADDING ***
                .padding(horizontal = Dimens.ScreenPaddingHorizontal, vertical = Dimens.SpacingXLarge)
        ) {
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
                            text = "Peyton",
                            fontSize = 12.sp,
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
                            // *** FIXED PADDING ***
                            .offset(x = Dimens.SpacingSmall, y = Dimens.SpacingSmall)
                    )
                }
            }

            Spacer(modifier = Modifier.height(Dimens.ScreenPaddingVertical)) // *** FIXED PADDING ***

            // --- (Bio is unchanged) ---
            Text(
                text = bio,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onBackground,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(Dimens.SpacingXLarge)) // *** FIXED PADDING ***

            // --- (Channel Button is unchanged) ---
            Surface(
                modifier = Modifier.border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                color = Color.Transparent
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = Dimens.SpacingLarge, vertical = Dimens.SpacingMedium), // *** FIXED PADDING ***
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Channel: Peyton Used Stuff",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            // --- (TAB ROW UI is unchanged) ---
            Spacer(modifier = Modifier.height(Dimens.SpacingXXLarge)) // *** FIXED PADDING ***

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

            Spacer(modifier = Modifier.height(Dimens.SpacingXLarge)) // *** FIXED PADDING ***
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = Dimens.SpacingXLarge), // *** FIXED PADDING ***
                contentAlignment = Alignment.TopCenter
            ) {
                // Show content based on the selected tab
                when (selectedTab) {
                    "Available" -> {
                        // Use WatchlistCard, as it supports a price
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            WatchlistCard(
                                itemName = "My Dummy Listing",
                                price = "$100.00",
                                isSaved = false,
                                saveCount = 0,
                                onItemClick = onDummyListingClick, // <-- Hook up lambda
                                onSaveClick = { /* TODO */ }
                            )
                        }
                    }
                    "Gone" -> {
                        Text(
                            text = "Content for $selectedTab",
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    "Pick it up" -> {
                        Text(
                            text = "Content for $selectedTab",
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
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
            onBackClick = {},
            onEditClick = {},
            onMenuClick = {},
            onDummyListingClick = {}
        )
    }
}
