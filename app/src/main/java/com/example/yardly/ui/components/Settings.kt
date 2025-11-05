package com.example.yardly.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yardly.ui.theme.YardlyTheme

@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    onAccessibilityClick: () -> Unit
) {
    val settingsOptions = listOf(
        "Accessibility",
        "Privacy",
        "Notification",
        "Invite friend and refer",
        "Location"
    )

    Column(modifier = Modifier.fillMaxSize()) {
        // 1. Top Bar
        SettingsTopBar(onBackClick = onBackClick)

        // 2. List of Options
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(settingsOptions) { optionName ->
                // Uses the new public SettingsRow
                SettingsRow(
                    name = optionName,
                    onClick = {
                        if (optionName == "Accessibility") {
                            onAccessibilityClick()
                        } else {
                            // TODO: Handle other clicks
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun SettingsTopBar(
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            // Back Arrow
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Title
            Text(
                text = "Settings",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

// *** The private SettingsRow composable was REMOVED from here ***

@Preview
@Composable
fun SettingsScreenPreview() {
    // *** THIS IS THE FIX ***
    // Replaced darkTheme/dynamicColor with isDarkMode
    YardlyTheme(isDarkMode = false) {
        SettingsScreen(
            onBackClick = {},
            onAccessibilityClick = {}
        )
    }
}