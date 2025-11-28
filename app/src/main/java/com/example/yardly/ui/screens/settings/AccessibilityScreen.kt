package com.example.yardly.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yardly.ui.components.SettingsRow
import com.example.yardly.ui.theme.YardlyTheme

/**
 * Accessibility settings screen with options like dark mode.
 */
@Composable
fun AccessibilityScreen(
    onBackClick: () -> Unit,
    onDarkModeClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        AccessibilityTopBar(onBackClick = onBackClick)
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            item {
                SettingsRow(name = "Dark Mode", onClick = onDarkModeClick)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AccessibilityTopBar(onBackClick: () -> Unit) {
    TopAppBar(
        title = { Text("Accessibility", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground) },
        navigationIcon = { IconButton(onClick = onBackClick) { Icon(Icons.Filled.ArrowBack, "Back", Modifier.size(28.dp)) } },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
    )
}

@Preview(showBackground = true, name = "Light Mode")
@Composable
fun AccessibilityScreenPreview() {
    YardlyTheme(isDarkMode = false) {
        AccessibilityScreen(onBackClick = {}, onDarkModeClick = {})
    }
}

@Preview(showBackground = true, name = "Dark Mode")
@Composable
fun AccessibilityScreenDarkPreview() {
    YardlyTheme(isDarkMode = true) {
        AccessibilityScreen(onBackClick = {}, onDarkModeClick = {})
    }
}