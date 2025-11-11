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
import androidx.compose.ui.res.painterResource // <-- 1. ADD THIS IMPORT
import com.example.yardly.R // <-- 2. ADD THIS IMPORT

// The ModalBottomSheet logic has been moved to ProfilePopup.kt

@Composable
fun ProfileContent(
    onBackClick: () -> Unit,
    onEditClick: () -> Unit,
    onMenuClick: () -> Unit
) {
    var isEditMode by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.95f) // This allows the sheet to be expandable
    ) {
        // Top Bar
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
                .padding(24.dp)
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
                    Text(
                        text = "Peyton Venzeee",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground // Kept theme color
                    )
                    Text(
                        text = "peyton",
                        fontSize = 16.sp,
                        color = Color(0xFF666666) // <-- KEPT ORIGINAL COLOR
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
                            .background(Color(0xFF0F1B3C)), // <-- KEPT ORIGINAL COLOR
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Peyton",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White // <-- KEPT ORIGINAL COLOR
                        )
                    }

                    // Verified checkmark
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Verified",
                        tint = Color(0xFF1DA1F2), // <-- KEPT ORIGINAL COLOR
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.BottomEnd)
                            .offset(x = 4.dp, y = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Bio
            Text(
                text = "just another broke college student trying to make some extra cash by selling off my old books, clothes I totally don’t wear anymore, and a few electronics that are just collecting dust. Honestly, it’s wild how much stuff we accumulate over time!",
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onBackground, // Kept theme color
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                modifier = Modifier.border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(16.dp)), // <-- KEPT ORIGINAL COLOR
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
                        color = MaterialTheme.colorScheme.onBackground // Kept theme color
                    )
                }
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
            .padding(horizontal = 8.dp, vertical = 8.dp), // <-- Adjusted padding
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
                // --- *** 3. THIS IS THE CHANGE *** ---
                Icon(
                    // This now points to your profilesheet_edit.xml file
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
        ProfileContent(
            onBackClick = {},
            onEditClick = {},
            onMenuClick = {}
        )
    }
}