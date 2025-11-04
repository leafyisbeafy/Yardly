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
import androidx.compose.material.icons.filled.Edit
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSheet(
    showModal: Boolean,
    onDismiss: () -> Unit,
    onBackClick: () -> Unit = {},
    onEditClick: () -> Unit = {},
    onMenuClick: () -> Unit = {}
) {
    if (showModal) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = rememberModalBottomSheetState(),
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            containerColor = Color.White,
            dragHandle = null
        ) {
            ProfileContent(
                onBackClick = onBackClick,
                onEditClick = onEditClick,
                onMenuClick = onMenuClick
            )
        }
    }
}

@Composable
private fun ProfileContent(
    onBackClick: () -> Unit,
    onEditClick: () -> Unit,
    onMenuClick: () -> Unit
) {
    var isEditMode by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.95f)
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
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "peyton",
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
                            .background(Color(0xFF0F1B3C)), // Penn blue
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
                        tint = Color(0xFF1DA1F2), // Twitter blue
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
                color = MaterialTheme.colorScheme.onBackground,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

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
        }
    }
}

@Composable
private fun TopProfileBar(
    onBackClick: () -> Unit,
    onEditClick: () -> Unit,
    onMenuClick: () -> Unit,
    isEditMode: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Back arrow
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

        // Right side icons
        Row {
            IconButton(onClick = onEditClick) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit",
                    tint = if (isEditMode) MaterialTheme.colorScheme.primary 
                          else MaterialTheme.colorScheme.onBackground
                )
            }
            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More options",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

@Preview(showBackground = true, heightDp = 800)
@Composable
fun ProfileContentPreview() {
    MaterialTheme {
        ProfileContent(
            onBackClick = {},
            onEditClick = {},
            onMenuClick = {}
        )
    }
}
