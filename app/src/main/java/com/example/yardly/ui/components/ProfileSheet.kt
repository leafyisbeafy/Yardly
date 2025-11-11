package com.example.yardly.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
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
import com.example.yardly.ui.theme.YardlyTheme
import androidx.compose.ui.res.painterResource
import com.example.yardly.R

// The ModalBottomSheet logic has been moved to ProfilePopup.kt

@Composable
fun ProfileContent(
    // --- 1. *** THIS IS THE FIX *** ---
    // These parameters are added to accept the data
    name: String,
    username: String,
    bio: String,
    // --- *** END OF FIX *** ---
    onBackClick: () -> Unit,
    onEditClick: () -> Unit,
    onMenuClick: () -> Unit
) {
    var isEditMode by remember { mutableStateOf(false) }

    var selectedTab by remember { mutableStateOf("Available") }
    val tabs = listOf("Available", "Gone", "Pick it up")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.95f) // This allows the sheet to be expandable
    ) {
        // Top Bar (Unchanged)
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
                // Use a smaller padding so the tabs fit well
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            // University Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left side - Title and username
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    // --- 2. *** THIS IS THE FIX *** ---
                    // Now using the 'name' parameter
                    Text(
                        text = name,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    // Now using the 'username' parameter
                    Text(
                        text = username,
                        fontSize = 16.sp,
                        color = Color(0xFF666666)
                    )
                    // --- *** END OF FIX *** ---
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
                            .offset(x = 4.dp, y = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Bio
            // --- 3. *** THIS IS THE FIX *** ---
            // Now using the 'bio' parameter
            Text(
                text = bio,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onBackground,
                lineHeight = 20.sp
            )
            // --- *** END OF FIX *** ---

            Spacer(modifier = Modifier.height(16.dp))

            // Channel Button (Unchanged)
            Surface(
                modifier = Modifier.border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                color = Color.Transparent
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Channel: Peyton Used Stuff",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            // --- TAB ROW UI (Unchanged) ---
            Spacer(modifier = Modifier.height(24.dp))

            TabRow(
                selectedTabIndex = tabs.indexOf(selectedTab),
                containerColor = Color.Transparent, // No background for the row itself
                contentColor = MaterialTheme.colorScheme.onBackground,
                indicator = { tabPositions ->
                    // This draws the underline
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[tabs.indexOf(selectedTab)]),
                        height = 2.dp,
                        color = MaterialTheme.colorScheme.onBackground // Underline color
                    )
                },
                divider = {
                    // No divider
                }
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

            // --- (Optional) CONTENT AREA FOR TABS (Unchanged) ---
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Text(
                    text = "Content for $selectedTab",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun TopProfileBar(
    onBackClick: () -> Unit,
    onEditClick: () -> Unit,
    onMenuClick: () -> Unit,
    isEditMode: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Back arrow
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
                imageVector = Icons.Default.KeyboardArrowLeft,
                contentDescription = "Back",
                modifier = Modifier.size(28.dp)
            )
        }

        // Right side icons
        Row {
            IconButton(onClick = onEditClick) {
                Icon(
                    painter = painterResource(id = R.drawable.profilesheet_edit),
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
        }
    }
}

@Preview(showBackground = true, heightDp = 800)
@Composable
fun ProfileContentPreview() {
    YardlyTheme(isDarkMode = false) {
        // --- 4. *** THIS IS THE FIX *** ---
        // Preview now provides the new parameters
        ProfileContent(
            name = "Peyton Venzeee",
            username = "peyton",
            bio = "This is a preview bio.",
            onBackClick = {},
            onEditClick = {},
            onMenuClick = {}
        )
        // --- *** END OF FIX *** ---
    }
}