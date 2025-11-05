package com.example.yardly.ui.components

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
fun DarkModeScreen(
    isDarkMode: Boolean, // <-- ADDED
    onToggle: (Boolean) -> Unit, // <-- ADDED
    onBackClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        // 1. Top Bar
        DarkModeTopBar(onBackClick = onBackClick)

        // 2. List of Options
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                SettingsRow(
                    name = "On",
                    onClick = { onToggle(true) }, // <-- ADDED ACTION
                    isSelected = isDarkMode     // <-- ADDED STATE
                )
                SettingsRow(
                    name = "Off",
                    onClick = { onToggle(false) }, // <-- ADDED ACTION
                    isSelected = !isDarkMode    // <-- ADDED STATE
                )
            }
        }
    }
}

@Composable
private fun DarkModeTopBar(
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
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Dark Mode",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Preview
@Composable
fun DarkModeScreenPreview() {
    YardlyTheme(isDarkMode = false) {
        DarkModeScreen(
            isDarkMode = true,
            onToggle = {},
            onBackClick = {}
        )
    }
}