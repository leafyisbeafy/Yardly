package com.example.yardly.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.ImageNotSupported
import androidx.compose.material.icons.outlined.BookmarkAdd
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yardly.ui.theme.YardlyTheme
import androidx.compose.ui.text.style.TextOverflow

/**
 * AdCard - the heart of our marketplace UI!
 * This component represents each item in the grid feed
 * Went through many iterations to get the spacing and interactions just right
 * 
 * Note: Removed price display for cleaner look - users can see price in detail view
 */
@Composable
fun AdCard(
    advertisementName: String = "Name of the advertisement",
    userName: String = "First Last",
    saveCount: Int,
    isSaved: Boolean,
    onSaveClick: () -> Unit = {},
    onAdClick: () -> Unit = {},
    onUserClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface // Clean white background
        ),
        shape = RoundedCornerShape(16.dp),  // Rounded corners for modern feel
        elevation = CardDefaults.cardElevation(0.dp)  // Flat design - no shadows
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .clickable(onClick = onAdClick)  // Entire card is clickable
        ) {
            // Title section - the item name
            // Using minLines/maxLines to ensure consistent card heights in grid
            Text(
                text = advertisementName,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                minLines = 2,  // Forces consistent height
                maxLines = 2,  // Prevents overly long titles
                overflow = TextOverflow.Ellipsis  // Graceful text cutoff
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Image placeholder - will eventually support actual photos
            // Square aspect ratio ensures consistent grid layout
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)  // Perfect square for grid consistency
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                // TODO: Replace with actual image loading (probably use Coil library)
                Icon(
                    imageVector = Icons.Filled.ImageNotSupported,
                    contentDescription = "Image coming soon!",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(48.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Bottom row - user info on left, save functionality on right
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Seller information section
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .clickable(onClick = onUserClick), // Click to view user profile
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Profile picture placeholder - simple circle for now
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                shape = CircleShape
                            )
                    )
                    Text(
                        text = userName,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Save functionality - count and button
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Save count with smooth animation - users love seeing popularity!
                    AnimatedVisibility(
                        visible = saveCount > 0,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Text(
                            text = saveCount.toString(),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // The save/bookmark button - visual feedback is crucial here
                    IconButton(
                        onClick = onSaveClick,
                        modifier = Modifier
                            .size(32.dp)
                            .padding(end = 0.dp)
                    ) {
                        Icon(
                            imageVector = if (isSaved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkAdd,
                            contentDescription = if (isSaved) "Remove from saved" else "Save item",
                            tint = if (isSaved) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AdCardPreview() {
    YardlyTheme(isDarkMode = false) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            AdCard(
                advertisementName = "Short Name",
                // price = "100.00", // <-- Removed from preview
                saveCount = 1,
                isSaved = true
            )
            AdCard(
                advertisementName = "This is a Very Long Advertisement Name That Wraps to Two Lines",
                // price = "25.50", // <-- Removed from preview
                saveCount = 1,
                isSaved = true
            )
        }
    }
}