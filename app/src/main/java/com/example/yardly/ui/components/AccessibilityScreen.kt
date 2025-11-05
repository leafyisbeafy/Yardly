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
fun AccessibilityScreen(
    onBackClick: () -> Unit,
    onDarkModeClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        // 1. Top Bar
        AccessibilityTopBar(onBackClick = onBackClick)

        // 2. List of Options
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                // Uses the public SettingsRow
                SettingsRow(
                    name = "Dark Mode",
                    onClick = onDarkModeClick
                )
            }
        }
    }
}

@Preview
@Composable
fun AccessibilityScreenPreview() {
    YardlyTheme(darkTheme = false, dynamicColor = false) {
        AccessibilityScreen(
            onBackClick = {},
            onDarkModeClick = {}
        )
    }
}

@Composable
private fun AccessibilityTopBar(
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding() // <-- THIS WAS THE FIX
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
                text = "Accessibility",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Preview
@Composable
private fun AccessibilityTopBarPreview() {
    YardlyTheme(darkTheme = false, dynamicColor = false) {
        AccessibilityTopBar(
            onBackClick = {}
        )
    }
}