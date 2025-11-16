package com.example.yardly.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
                .padding(Dimens.SpacingMedium), // *** FIXED PADDING ***
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            // Back Arrow
            Button(
                onClick = onBackClick,
                shape = CircleShape,
                modifier = Modifier.size(40.dp),
                contentPadding = PaddingValues(0.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.onBackground
                )
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(Dimens.SpacingXLarge)) // *** FIXED PADDING ***

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