package com.example.yardly.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yardly.ui.theme.Dimens
import com.example.yardly.ui.theme.YardlyTheme

/**
 * Full-screen composable for the Messenger/Channels feature.
 */
@Composable
fun MessengerScreen(
    onBackClick: () -> Unit
) {
    // --- FIX APPLIED HERE: "Channels" removed from the list ---
    val channels = listOf("Chats", "Unread")

    Column(modifier = Modifier.fillMaxSize()) {
        // 1. Top Bar
        MessengerTopBar(onBackClick = onBackClick)

        // 2. Channel Buttons
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = Dimens.ScreenPaddingHorizontal, vertical = Dimens.SpacingMedium),
            horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingMedium)
        ) {
            items(channels) { channelName ->
                Button(
                    onClick = { /* TODO: Handle channel click */ },
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    elevation = ButtonDefaults.buttonElevation(0.dp)
                ) {
                    Text(text = channelName)
                }
            }
        }

        // 3. Profile Avatar and Name
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.ScreenPaddingHorizontal),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingLarge)
        ) {
            // Placeholder Avatar
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )

            // Name
            Text(
                text = "Peyton Venzeee", // Mocked name
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        // TODO: Add the list of message threads here
    }
}

/**
 * Simple top bar for the Messenger screen with a back button and title.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MessengerTopBar(
    onBackClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = "Messenger",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier.size(28.dp)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}

// --- Previews ---

@Preview(showBackground = true)
@Composable
private fun MessengerScreenPreviewLight() {
    YardlyTheme(isDarkMode = false) {
        MessengerScreen(onBackClick = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun MessengerScreenPreviewDark() {
    YardlyTheme(isDarkMode = true) {
        MessengerScreen(onBackClick = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun MessengerTopBarPreview() {
    YardlyTheme(isDarkMode = false) {
        MessengerTopBar(onBackClick = {})
    }
}