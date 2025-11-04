package com.example.yardly.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable // Make sure this is imported
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ImageNotSupported
import androidx.compose.material.icons.outlined.BookmarkAdd
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yardly.ui.theme.YardlyTheme

@Composable
fun AdCard(
    advertisementName: String = "Name of the advertisement",
    userName: String = "First Last",
    onSaveClick: () -> Unit = {},
    onAdClick: () -> Unit = {},
    onUserClick: () -> Unit = {}, // <-- THIS IS THE PARAMETER
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(350.dp)
            .wrapContentHeight(),
        // onClick = onAdClick, // (Removed from here)
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .clickable(onClick = onAdClick) // (This handles the ad click)
        ) {
            // Top section - Advertisement name
            Text(
                text = advertisementName,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Middle section - Image placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(
                        color = Color(0xFFF5F5F5),
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                // Placeholder for advertisement image
                Icon(
                    imageVector = Icons.Filled.ImageNotSupported,
                    contentDescription = "Advertisement Image Placeholder",
                    tint = Color(0xFFCCCCCC),
                    modifier = Modifier.size(48.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Bottom section - User info and save button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // NEW: Wrap Avatar and Name in a clickable Row
                Row(
                    modifier = Modifier
                        .weight(1f) // Takes up available space
                        .clickable(onClick = onUserClick), // <-- THIS TRIGGERS IT
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left - Circular avatar placeholder
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                color = Color(0xFFE0E0E0),
                                shape = CircleShape
                            )
                    )

                    Spacer(modifier = Modifier.width(8.dp)) // Added spacer

                    // Center - User full name
                    Text(
                        text = userName,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFF666666)
                    )
                }

                // Right - Save icon (now separate from the user click)
                IconButton(
                    onClick = onSaveClick,
                    modifier = Modifier
                        .size(32.dp)
                        .padding(end = 0.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.BookmarkAdd,
                        contentDescription = "Save Advertisement",
                        tint = Color(0xFF888888),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AdCardPreview() {
    YardlyTheme {
        AdCard()
    }
}