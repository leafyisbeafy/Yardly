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
                        text = "University of Pennsylvania",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "uofpenn",
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
                            text = "PENN",
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
                text = "The University of Pennsylvania is a private Ivy League research university in Philadelphia, Pennsylvania. Founded in 1740, it is one of the oldest institutions of higher education in the United States and among the most prestigious universities in the world.",
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onBackground,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Tag/Pill
            Surface(
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = Color(0xFFE0E0E0),
                        shape = RoundedCornerShape(16.dp)
                    ),
                shape = RoundedCornerShape(16.dp),
                color = Color.Transparent
            ) {
                Text(
                    text = "PennGrad",
                    fontSize = 14.sp,
                    color = Color(0xFF666666),
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Followers and link
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "70.5K followers",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = " • ",
                    fontSize = 14.sp,
                    color = Color(0xFF666666)
                )
                Text(
                    text = "upenn.edu",
                    fontSize = 14.sp,
                    color = Color(0xFF0066CC),
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { /* Following action */ },
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE0E0E0),
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Following",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                OutlinedButton(
                    onClick = { /* Message action */ },
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Message",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
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
