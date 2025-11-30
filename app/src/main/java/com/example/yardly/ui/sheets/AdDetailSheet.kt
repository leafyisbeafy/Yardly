package com.example.yardly.ui.sheets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ImageNotSupported
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.BookmarkAdd
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import com.example.yardly.ui.theme.Dimens
import com.example.yardly.ui.theme.YardlyTheme


/**
 * Bottom sheet for displaying detailed information about a marketplace listing.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdDetailSheet(
    post: UserPost?,
    showModal: Boolean,
    onDismiss: () -> Unit,
    isSaved: Boolean,
    saveCount: Int,
    onSaveClick: () -> Unit,
    onUserClick: () -> Unit
) {
    if (showModal && post != null) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            containerColor = MaterialTheme.colorScheme.surface,
            scrimColor = Color.Black.copy(alpha = 0.4f),
            dragHandle = null
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.9f)
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                // Top Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Dimens.ScreenPaddingHorizontal, vertical = Dimens.SpacingMedium),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                            .size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            modifier = Modifier.size(20.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Scrollable Content
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = Dimens.ScreenPaddingHorizontal)
                ) {
                    // Image Carousel (Horizontal Pager)
                    val images = post?.imageUris ?: emptyList()

                    if (images.isNotEmpty()) {
                        val pagerState = rememberPagerState(pageCount = { images.size })
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f) // 1:1 Square Aspect Ratio (Threads style)
                                // .aspectRatio(16f / 9f) // Uncomment for 16:9
                        ) {
                            HorizontalPager(
                                state = pagerState,
                                modifier = Modifier.fillMaxSize(),
                                pageSpacing = 12.dp, // Gap between images
                                contentPadding = PaddingValues(horizontal = 32.dp) // Show next/prev cards
                            ) { page ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(RoundedCornerShape(16.dp)) // Individual rounded corners
                                        .background(MaterialTheme.colorScheme.surfaceVariant)
                                ) {
                                    AsyncImage(
                                        model = images[page],
                                        contentDescription = post?.title,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                            }
                            
                            // Page Indicator
                            Text(
                                text = "${pagerState.currentPage + 1}/${images.size}",
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(12.dp)
                                    .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
                                    .padding(horizontal = 10.dp, vertical = 5.dp),
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ImageNotSupported,
                                contentDescription = "No Image",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(64.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(Dimens.SpacingXLarge))

                    // Title & Price
                    Text(
                        text = post.title,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(Dimens.SpacingSmall))
                    Text(
                        text = "$${post.price}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(Dimens.SpacingLarge))

                    // User Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                                RoundedCornerShape(12.dp)
                            )
                            .padding(Dimens.SpacingMedium),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // LEFT SIDE: Profile Picture Placeholder
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .clickable { onUserClick() }
                        )

                        // RIGHT SIDE: Action Buttons
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // 1. Save Button (Pill Shaped)
                            Button(
                                onClick = onSaveClick,
                                shape = CircleShape,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isSaved) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.primary,
                                    contentColor = if (isSaved) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary
                                ),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                                modifier = Modifier.height(40.dp)
                            ) {
                                Icon(
                                    imageVector = if (isSaved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkAdd,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = if (isSaved) "Saved" else "Save")
                            }

                            // 2. Share Button (Icon Only, Pill/Circle)
                            FilledTonalButton(
                                onClick = { /* TODO Share */ },
                                shape = CircleShape,
                                contentPadding = PaddingValues(0.dp),
                                modifier = Modifier.size(40.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = "Share",
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(Dimens.SpacingLarge))

                    // Description
                    Text(
                        text = "Description",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(Dimens.SpacingSmall))
                    Text(
                        text = post.description,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 20.sp
                    )
                    Spacer(modifier = Modifier.height(Dimens.SpacingXXXLarge))
                }

                // Bottom Action Bar removed
            }
        }
    }
}

@Preview(showBackground = true, name = "Light Mode")
@Composable
fun AdDetailSheetPreview() {
    val mockPost = UserPost(
        title = "Vintage T-Shirt",
        description = "A very cool vintage t-shirt from the 90s. Barely used.",
        price = "25.00",
        category = "Clothing",
        location = "Downtown",
        userName = "CoolSeller123"
    )
    YardlyTheme(isDarkMode = false) {
        AdDetailSheet(
            post = mockPost,
            showModal = true,
            onDismiss = {},
            isSaved = false,
            saveCount = 12,
            onSaveClick = {},
            onUserClick = {}
        )
    }
}
