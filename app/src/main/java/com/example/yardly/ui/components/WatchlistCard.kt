package com.example.yardly.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.ImageNotSupported
import androidx.compose.material.icons.outlined.BookmarkAdd
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.yardly.ui.theme.Dimens
import com.example.yardly.ui.theme.PriceDropRed1
import com.example.yardly.ui.theme.PriceDropRed2
import com.example.yardly.ui.theme.YardlyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatchlistCard(
    itemName: String,
    price: String,
    imageUriString: String?,
    isSaved: Boolean,
    saveCount: Int,
    onItemClick: () -> Unit = {},
    onSaveClick: () -> Unit = {}
) {
    Card(
        onClick = onItemClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column {
            // 1. Image Display Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                if (imageUriString != null) {
                    AsyncImage(
                        model = imageUriString,
                        contentDescription = itemName,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.ImageNotSupported,
                        contentDescription = "Placeholder",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }

            // 2. Item Name and Save Button Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.SpacingMedium, vertical = Dimens.SpacingSmall),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = itemName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.weight(1f, fill = false)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingSmall)
                ) {
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
                    IconButton(
                        onClick = onSaveClick,
                        modifier = Modifier.size(Dimens.SpacingXXXLarge)
                    ) {
                        Icon(
                            imageVector = if (isSaved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkAdd,
                            contentDescription = "Save Advertisement",
                            tint = if (isSaved) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // 3. Price Row
            Row(
                modifier = Modifier.padding(horizontal = Dimens.SpacingMedium, vertical = Dimens.SpacingSmall),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingSmall)
            ) {
                val infiniteTransition = rememberInfiniteTransition(label = "pulse")
                val pulseColor by infiniteTransition.animateColor(
                    initialValue = PriceDropRed1,
                    targetValue = PriceDropRed2,
                    animationSpec = infiniteRepeatable(
                        animation = tween(1000),
                        repeatMode = RepeatMode.Reverse
                    ), label = "pulse"
                )
                Icon(
                    imageVector = Icons.Default.ArrowDownward,
                    contentDescription = "Price Drop",
                    tint = pulseColor,
                    modifier = Modifier.size(Dimens.SpacingXLarge)
                )
                Text(
                    text = price,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            Spacer(modifier = Modifier.height(Dimens.SpacingSmall))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WatchlistCardPreview() {
    YardlyTheme(isDarkMode = false) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            WatchlistCard(
                itemName = "Vintage Desk Lamp",
                price = "$25.00",
                imageUriString = null,
                isSaved = true,
                saveCount = 5,
                onItemClick = {},
                onSaveClick = {}
            )
            WatchlistCard(
                itemName = "Calculus Textbook",
                price = "$45.00",
                imageUriString = null,
                isSaved = false,
                saveCount = 0,
                onItemClick = {},
                onSaveClick = {}
            )
        }
    }
}

