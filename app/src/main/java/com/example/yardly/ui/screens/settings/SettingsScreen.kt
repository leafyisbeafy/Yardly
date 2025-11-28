package com.example.yardly.ui.screens.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yardly.ui.components.ExitDialog
import com.example.yardly.ui.components.SettingsRow
import com.example.yardly.ui.theme.Dimens
import com.example.yardly.ui.theme.YardlyTheme

/**
 * Settings screen with expandable options for account, accessibility, etc.
 */
@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    onAccessibilityClick: () -> Unit,
    onDarkModeClick: () -> Unit,
    onLogOutClick: () -> Unit = {}
) {
    val settingsOptions = listOf("Account", "Notification", "Security & Permission", "Privacy", "Accessibility", "About", "Log out")
    val specialItems = setOf("Log out", "About")
    var expandedSetting by remember { mutableStateOf<String?>(null) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        if (showLogoutDialog) {
            ExitDialog(onDismiss = { showLogoutDialog = false })
        }

        SettingsTopBar(onBackClick = onBackClick)

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(settingsOptions) { optionName ->
                val isExpanded = expandedSetting == optionName
                val isExpandable = optionName !in specialItems

                SettingsRow(
                    name = optionName,
                    onClick = {
                        when (optionName) {
                            "Log out" -> showLogoutDialog = true
                            else -> if (isExpandable) expandedSetting = if (isExpanded) null else optionName
                        }
                    },
                    isExpanded = isExpanded,
                    showArrow = isExpandable
                )

                AnimatedVisibility(
                    visible = isExpanded,
                    enter = expandVertically(animationSpec = tween(durationMillis = 300)),
                    exit = shrinkVertically(animationSpec = tween(durationMillis = 300))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = Dimens.ScreenPaddingHorizontal)
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                    ) {
                        when (optionName) {
                            "Account" -> {
                                SettingsRow(name = "Change password", onClick = { /* TODO */ }, showArrow = false)
                                SettingsRow(name = "Deactivate account", onClick = { /* TODO */ }, showArrow = false)
                            }
                            "Accessibility" -> {
                                SettingsRow(name = "Dark Mode", onClick = onDarkModeClick, showArrow = false)
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsTopBar(onBackClick: () -> Unit) {
    TopAppBar(
        title = { Text("Settings", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground) },
        navigationIcon = { IconButton(onClick = onBackClick) { Icon(Icons.Filled.ArrowBack, "Back", Modifier.size(28.dp)) } },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
    )
}

@Preview(showBackground = true, name = "Light Mode")
@Composable
fun SettingsScreenPreview() {
    YardlyTheme(isDarkMode = false) {
        SettingsScreen(onBackClick = {}, onAccessibilityClick = {}, onDarkModeClick = {})
    }
}

@Preview(showBackground = true, name = "Dark Mode")
@Composable
fun SettingsScreenDarkPreview() {
    YardlyTheme(isDarkMode = true) {
        SettingsScreen(onBackClick = {}, onAccessibilityClick = {}, onDarkModeClick = {})
    }
}