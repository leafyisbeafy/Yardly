package com.example.yardly.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yardly.ui.theme.Dimens
import com.example.yardly.ui.theme.YardlyTheme

@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    onAccessibilityClick: () -> Unit
) {
    val settingsOptions = listOf(
        "Account",
        "Notification",
        "Security & Permission",
        "Privacy",
        "Accessibility",
        "About",
        "Log out"
    )
    val nonExpandableItems = setOf("About", "Log out")

    var expandedSetting by remember { mutableStateOf<String?>(null) }

    var showLogoutDialog by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {

        if (showLogoutDialog) {
            ExitDialog(
                onDismiss = { showLogoutDialog = false }
            )
        }

        // 1. Top Bar
        SettingsTopBar(onBackClick = onBackClick)

        // 2. List of Options
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(settingsOptions) { optionName ->
                val isExpanded = expandedSetting == optionName
                val isExpandable = optionName !in nonExpandableItems

                SettingsRow(
                    name = optionName,
                    onClick = {
                        if (isExpandable) {
                            // Toggle expansion
                            expandedSetting = if (isExpanded) null else optionName
                        } else {
                            if (optionName == "Log out") {
                                showLogoutDialog = true // This shows the dialog
                            } else {
                                // Handle about section navigation
                            }
                        }
                    },
                    isExpanded = isExpanded,
                    showArrow = isExpandable // Show arrow only if expandable
                )

                AnimatedVisibility(
                    visible = isExpanded,
                    enter = expandVertically(animationSpec = tween(durationMillis = 300)),
                    exit = shrinkVertically(animationSpec = tween(durationMillis = 300))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = Dimens.ScreenPaddingHorizontal) // *** FIXED PADDING ***
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                    ) {
                        when (optionName) {
                            "Accessibility" -> {
                                SettingsRow(
                                    name = "Dark Mode",
                                    onClick = onAccessibilityClick
                                )
                            }
                            "Account", "Notification", "Security & Permission", "Privacy" -> {
                                Box(modifier = Modifier.height(1.dp))
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
private fun SettingsTopBar(
    onBackClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = "Settings",
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

@Preview
@Composable
fun SettingsScreenPreview() {
    YardlyTheme(isDarkMode = false) {
        SettingsScreen(
            onBackClick = {},
            onAccessibilityClick = {}
        )
    }
}