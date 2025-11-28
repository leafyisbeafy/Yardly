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
 * Dark mode toggle screen with On/Off options.
 */
@Composable
fun DarkModeScreen(
    isDarkMode: Boolean,
    onToggle: (Boolean) -> Unit,
    onBackClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        DarkModeTopBar(onBackClick = onBackClick)
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            item {
                SettingsRow(name = "On", onClick = { onToggle(true) }, isSelected = isDarkMode)
                SettingsRow(name = "Off", onClick = { onToggle(false) }, isSelected = !isDarkMode)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DarkModeTopBar(onBackClick: () -> Unit) {
    TopAppBar(
        title = { Text("Dark Mode", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground) },
        navigationIcon = { IconButton(onClick = onBackClick) { Icon(Icons.Filled.ArrowBack, "Back", Modifier.size(28.dp)) } },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
    )
}

@Preview(showBackground = true, name = "Light Mode - Toggle On")
@Composable
fun DarkModeScreenPreview() {
    YardlyTheme(isDarkMode = false) {
        DarkModeScreen(isDarkMode = true, onToggle = {}, onBackClick = {})
    }
}

@Preview(showBackground = true, name = "Dark Mode - Toggle On")
@Composable
fun DarkModeScreenDarkPreview() {
    YardlyTheme(isDarkMode = true) {
        DarkModeScreen(isDarkMode = true, onToggle = {}, onBackClick = {})
    }
}