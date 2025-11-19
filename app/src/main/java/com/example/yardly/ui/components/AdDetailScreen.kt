package com.example.yardly.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.ImageNotSupported
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.BookmarkAdd
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yardly.ui.theme.AppBlack
import com.example.yardly.ui.theme.DarkAccent
import com.example.yardly.ui.theme.Dimens
import com.example.yardly.ui.theme.YardlyTheme

@Composable
fun AdDetailScreen(
    title: String,
    description: String,
    isSaved: Boolean,
    saveCount: Int, // <--- NEW PARAMETER ADDED HERE
    onBackClick: () -> Unit,
    onUserClick: () -> Unit,
    onSaveClick: () -> Unit,
    onShareClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // 1. Top Bar
        Spacer(modifier = Modifier.statusBarsPadding())
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.ScreenPaddingHorizontal, vertical = Dimens.SpacingMedium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Back Button
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
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        // 2. Scrollable Content
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(Dimens.ScreenPaddingHorizontal),
            verticalArrangement = Arrangement.spacedBy(Dimens.SpacingXLarge)
        ) {
            // Title
            item {
                Text(
                    text = title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSystemInDarkTheme()) DarkAccent else AppBlack
                )
            }

            // Description
            item {
                Text(
                    text = description,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            // Full-width Image Placeholder
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ImageNotSupported,
                        contentDescription = "Image placeholder",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }

            // Button and Avatar Row
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = Dimens.SpacingMedium),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Avatar Icon
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .clickable { onUserClick() }
                    )

                    // Spacer pushes the buttons to the right
                    Spacer(Modifier.weight(1f))

                    // *** NEW LOGIC: SAVE COUNT DISPLAY ***
                    AnimatedVisibility(
                        visible = saveCount > 0,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Text(
                            text = saveCount.toString(),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(end = Dimens.SpacingSmall)
                        )
                    }

                    // Save Button
                    IconButton(onClick = onSaveClick) {
                        Icon(
                            imageVector = if (isSaved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkAdd,
                            contentDescription = if (isSaved) "Remove from saved" else "Save item",
                            tint = if (isSaved) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(Dimens.SpacingMedium))

                    // Share Button
                    IconButton(onClick = onShareClick) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AdDetailScreenPreviewLight() {
    YardlyTheme(isDarkMode = false) {
        AdDetailScreen(
            title = "Vintage T-Shirt",
            description = "This is a description.",
            isSaved = true,
            saveCount = 12, // Previewing with count
            onBackClick = {},
            onUserClick = {},
            onSaveClick = {},
            onShareClick = {}
        )
    }
}