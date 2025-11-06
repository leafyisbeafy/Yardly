package com.example.yardly

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yardly.ui.components.AccessibilityScreen
import com.example.yardly.ui.components.AdCard
import com.example.yardly.ui.components.AdLoginSheet
import com.example.yardly.ui.components.ChooseCornerSheet
import com.example.yardly.ui.components.DarkModeScreen
import com.example.yardly.ui.components.FindNear
import com.example.yardly.ui.components.ListingScreen
import com.example.yardly.ui.components.ProfileContent
import com.example.yardly.ui.components.ProfilePopup
import com.example.yardly.ui.components.SettingsScreen
import com.example.yardly.ui.components.WatchlistScreen
import com.example.yardly.ui.theme.YardlyTheme
import androidx.compose.material3.ExperimentalMaterial3Api

sealed class ProfileScreenState {
    object Profile : ProfileScreenState()
    object Settings : ProfileScreenState()
    object Accessibility : ProfileScreenState()
    object DarkMode : ProfileScreenState()
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var isDarkMode by remember { mutableStateOf(false) }
            val onDarkModeToggle: (Boolean) -> Unit = { enabled ->
                isDarkMode = enabled
                Log.d("DarkModeToggle", "dark_mode_enabled: $enabled")
            }

            YardlyTheme(
                isDarkMode = isDarkMode,
                dynamicColor = false
            ) {
                YardlyApp(
                    isDarkMode = isDarkMode,
                    onDarkModeToggle = onDarkModeToggle
                )
            }
        }
    }
}

@Composable
fun YardlyApp(
    isDarkMode: Boolean,
    onDarkModeToggle: (Boolean) -> Unit
) {
    var selectedIconSection by remember { mutableStateOf("home") }
    var selectedNavSection by remember { mutableStateOf("lease") }
    var selectedSectionOptions by remember { mutableStateOf<String?>(null) }
    val buttonCoordinates = remember { mutableStateMapOf<String, Float>() }
    var showRehomeInAquaSwap by remember { mutableStateOf(false) }
    var showAdLoginModal by remember { mutableStateOf(false) }
    var showProfileSheet by remember { mutableStateOf(false) }
    var showChooseCornerSheet by remember { mutableStateOf(false) }
    var profileScreenState by remember { mutableStateOf<ProfileScreenState>(ProfileScreenState.Profile) }
    val saveCounts = remember { mutableStateMapOf<String, Int>() }
    val savedItems = remember { mutableStateMapOf<String, Boolean>() }
    val showHeaderAndNav = selectedIconSection == "home"
    val gridState = rememberLazyGridState()
    var previousIndex by remember(gridState) { mutableStateOf(gridState.firstVisibleItemIndex) }
    var previousOffset by remember(gridState) { mutableStateOf(gridState.firstVisibleItemScrollOffset) }

    val isFabVisible by remember {
        derivedStateOf {
            val currentIndex = gridState.firstVisibleItemIndex
            val currentOffset = gridState.firstVisibleItemScrollOffset

            val isScrollingUp = if (currentIndex != previousIndex) {
                currentIndex < previousIndex
            } else {
                currentOffset < previousOffset
            }

            previousIndex = currentIndex
            previousOffset = currentOffset
            isScrollingUp || currentIndex == 0
        }
    }

    var isFabMenuExpanded by remember { mutableStateOf(false) }

    val navigateToSettings = {
        showProfileSheet = false
        selectedIconSection = "profile"
        profileScreenState = ProfileScreenState.Settings
    }

    // *** THIS IS THE FIX: Create onSaveClick ***
    val onSaveClick: (String) -> Unit = { adName ->
        val currentCount = saveCounts.getOrDefault(adName, 0)
        saveCounts[adName] = currentCount + 1
        savedItems[adName] = true
    }

    val baseSectionOptions = mapOf(
        "aqua-swap" to listOf("Equipment", "Coral", "Plants", "Substrate", "Tank"),
        "yard-sales" to listOf("Move Out", "Garage Sale"),
        "lease" to listOf("Room", "Car", "Retail Store")
    )

    val sectionOptions = baseSectionOptions.mapValues { (key, options) ->
        if (key == "aqua-swap" && showRehomeInAquaSwap) {
            listOf("Rehome")
        } else {
            options
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Top Bar (Header)
            if (showHeaderAndNav) {
                TopBar()
            }

            // Content Area and Section Options Overlap
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                ContentArea(
                    gridState = gridState,
                    selectedIconSection = selectedIconSection,
                    selectedNavSection = selectedNavSection,
                    profileScreenState = profileScreenState,
                    isDarkMode = isDarkMode,
                    saveCounts = saveCounts,
                    savedItems = savedItems,
                    onAdClick = { showAdLoginModal = true },
                    onBackClick = { selectedIconSection = "home" },
                    onSettingsBackClick = { profileScreenState = ProfileScreenState.Profile },
                    onAccessibilityBackClick = { profileScreenState = ProfileScreenState.Settings },
                    onDarkModeBackClick = { profileScreenState = ProfileScreenState.Accessibility },
                    onUserClick = { showProfileSheet = true },
                    onMenuClick = navigateToSettings,
                    onAccessibilityClick = { profileScreenState = ProfileScreenState.Accessibility },
                    onDarkModeClick = { profileScreenState = ProfileScreenState.DarkMode },
                    onDarkModeToggle = onDarkModeToggle,
                    onSaveClick = onSaveClick // <-- Pass it here
                )

                // Section Options
                selectedSectionOptions?.let { section ->
                    sectionOptions[section]?.let { options ->
                        val xOffset = buttonCoordinates[section] ?: 0f
                        SectionOptions(
                            options = options,
                            xOffset = xOffset,
                            modifier = Modifier.align(Alignment.BottomStart)
                        )
                    }
                }

                // *** START: NEW EXPANDING FAB MENU ***
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // 1. "Camera" Button
                    AnimatedVisibility(
                        visible = isFabMenuExpanded && isFabVisible && selectedIconSection == "home",
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        FloatingActionButton(
                            onClick = {
                                // TODO: Handle create post navigation
                                isFabMenuExpanded = false
                            },
                            shape = CircleShape,
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.onSecondary
                        ) {
                            Icon(
                                Icons.Filled.CameraAlt,
                                contentDescription = "Create Post"
                            )
                        }
                    }

                    // 2. "Location" Button
                    AnimatedVisibility(
                        visible = isFabMenuExpanded && isFabVisible && selectedIconSection == "home",
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        FloatingActionButton(
                            onClick = {
                                showChooseCornerSheet = true
                                isFabMenuExpanded = false
                            },
                            shape = CircleShape,
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ) {
                            Icon(
                                Icons.Filled.LocationOn,
                                contentDescription = "Choose Location"
                            )
                        }
                    }

                    // 3. Main FAB (The "plus" / "cross" button)
                    AnimatedVisibility(
                        visible = isFabVisible && selectedIconSection == "home",
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        FloatingActionButton(
                            onClick = { isFabMenuExpanded = !isFabMenuExpanded },
                            shape = CircleShape,
                            containerColor = if (isFabMenuExpanded) {
                                MaterialTheme.colorScheme.surfaceVariant
                            } else {
                                MaterialTheme.colorScheme.primary
                            },
                            contentColor = if (isFabMenuExpanded) {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            } else {
                                MaterialTheme.colorScheme.onPrimary
                            }
                        ) {
                            Icon(
                                imageVector = if (isFabMenuExpanded) Icons.Filled.Close else Icons.Filled.Add,
                                contentDescription = "Toggle Menu"
                            )
                        }
                    }
                }
                // *** END: NEW EXPANDING FAB MENU ***
            }

            // Section Navigation
            if (showHeaderAndNav) {
                SectionNavigation(
                    selectedSection = selectedNavSection,
                    onSectionSelected = { section ->
                        selectedNavSection = section
                        showRehomeInAquaSwap = false
                        selectedSectionOptions = if (sectionOptions.containsKey(section) && selectedSectionOptions != section) section else null
                    },
                    onButtonPositioned = { key, x ->
                        buttonCoordinates[key] = x
                    },
                    onLongPress = { section ->
                        if (section == "aqua-swap") {
                            showRehomeInAquaSwap = true
                            selectedSectionOptions = section
                        }
                    },
                    showRehomeInAquaSwap = showRehomeInAquaSwap,
                    onRehomeStateChange = { newState ->
                        showRehomeInAquaSwap = newState
                    }
                )
            }

            // Bottom Icon Navigation
            BottomIconNavigation(
                selectedSection = selectedIconSection,
                onSectionSelected = { section ->
                    selectedIconSection = section
                    selectedSectionOptions = null
                    profileScreenState = ProfileScreenState.Profile
                }
            )
        }

        // Modals
        AdLoginSheet(
            showModal = showAdLoginModal,
            onDismiss = { showAdLoginModal = false }
        )

        ProfilePopup(
            showModal = showProfileSheet,
            onDismiss = { showProfileSheet = false },
            onBackClick = { showProfileSheet = false },
            onMenuClick = navigateToSettings
        )

        ChooseCornerSheet(
            showModal = showChooseCornerSheet,
            onDismiss = { showChooseCornerSheet = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.surfaceVariant,
                    RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp)
                )
                .padding(horizontal = 20.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Button(
                    onClick = { /* TODO: Handle notifications */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onBackground
                    ),
                    shape = RoundedCornerShape(20.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp)
                ) {
                    Text(
                        text = "Notification",
                        fontSize = 12.sp
                    )
                }
                Button(
                    onClick = { /* TODO: Handle messenger */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onBackground
                    ),
                    shape = RoundedCornerShape(20.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp)
                ) {
                    Text(
                        text = "Messenger",
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
fun SectionNavigation(
    selectedSection: String,
    onSectionSelected: (String) -> Unit,
    onButtonPositioned: (String, Float) -> Unit,
    onLongPress: (String) -> Unit = {},
    showRehomeInAquaSwap: Boolean,
    onRehomeStateChange: (Boolean) -> Unit
) {
    val sections = listOf(
        "aqua-swap" to "Aqua Swap",
        "yard-sales" to "Yard Sales",
        "lease" to "Lease",
        "auction" to "Auction"
    )

    val hapticFeedback = LocalHapticFeedback.current
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        items(sections.size) { index ->
            val (sectionKey, sectionName) = sections[index]
            val isSelected = selectedSection == sectionKey
            if (sectionKey == "aqua-swap") {
                Box(
                    modifier = Modifier
                        .width(110.dp)
                        .height(44.dp)
                        .onGloballyPositioned {
                            onButtonPositioned(sectionKey, it.positionInParent().x)
                        }
                        .background(
                            color = if (isSelected) MaterialTheme.colorScheme.surfaceVariant else Color.Transparent,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .combinedClickable(
                            onClick = {
                                onRehomeStateChange(false)
                                onSectionSelected(sectionKey)
                            },
                            onLongClick = {
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                onLongPress(sectionKey)
                            }
                        )
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = sectionName,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            } else {
                Button(
                    onClick = { onSectionSelected(sectionKey) },
                    modifier = Modifier
                        .width(110.dp)
                        .height(44.dp)
                        .onGloballyPositioned {
                            onButtonPositioned(sectionKey, it.positionInParent().x)
                        },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected) MaterialTheme.colorScheme.surfaceVariant else Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.onBackground
                    ),
                    shape = RoundedCornerShape(20.dp),
                    contentPadding = PaddingValues(12.dp)
                ) {
                    Text(
                        text = sectionName,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Composable
fun BottomIconNavigation(
    selectedSection: String,
    onSectionSelected: (String) -> Unit
) {
    val sections = listOf(
        "home" to "Home",
        "yardly" to "Yardly",
        "watchlist" to "Watchlist",
        "profile" to "Profile"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.background,
                RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
            )
            .navigationBarsPadding()
            .padding(vertical = 10.dp, horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        sections.forEach { (sectionKey, sectionName) ->
            val isSelected = selectedSection == sectionKey
            Button(
                onClick = { onSectionSelected(sectionKey) },
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSelected) MaterialTheme.colorScheme.surfaceVariant else Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.onBackground
                ),
                shape = RoundedCornerShape(20.dp),
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                Text(
                    text = sectionName,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
fun SectionOptions(
    options: List<String>,
    xOffset: Float,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    val xOffsetDp = with(density) { xOffset.toDp() }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = xOffsetDp, bottom = 16.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        options.forEach { name ->
            Button(
                onClick = {
                    if (name == "Rehome") {
                        println("Rehome option selected")
                    } else {
                        println("$name option selected")
                    }
                },
                modifier = Modifier
                    .width(110.dp)
                    .height(44.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (name == "Rehome") MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = if (name == "Rehome") MaterialTheme.colorScheme.onPrimary
                    else MaterialTheme.colorScheme.onBackground
                ),
                shape = RoundedCornerShape(20.dp),
                contentPadding = PaddingValues(12.dp)
            ) {
                Text(
                    text = name,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
fun ContentArea(
    gridState: LazyGridState,
    selectedIconSection: String,
    selectedNavSection: String,
    profileScreenState: ProfileScreenState,
    isDarkMode: Boolean,
    saveCounts: Map<String, Int>,
    savedItems: Map<String, Boolean>,
    onAdClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
    onSettingsBackClick: () -> Unit = {},
    onAccessibilityBackClick: () -> Unit = {},
    onDarkModeBackClick: () -> Unit = {},
    onUserClick: () -> Unit = {},
    onMenuClick: () -> Unit = {},
    onAccessibilityClick: () -> Unit = {},
    onDarkModeClick: () -> Unit = {},
    onDarkModeToggle: (Boolean) -> Unit,
    onSaveClick: (String) -> Unit
) {
    when (selectedIconSection) {
        "home" -> {
            val adNames = listOf(
                "Air Force 1",
                "iPhone 13",
                "PlayStation 4",
                "Macbook Air 13",
                "Denim Jacket",
                "Razer Gaming Chair"
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                state = gridState,
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 20.dp)
            ) {
                itemsIndexed(adNames) { index, adName ->
                    val userName = "User ${index + 1}"
                    val saveCount = saveCounts.getOrDefault(adName, 0)
                    val isSaved = savedItems.getOrDefault(adName, false)
                    AdCard(
                        advertisementName = adName,
                        userName = userName,
                        saveCount = saveCount,
                        isSaved = isSaved,
                        onAdClick = onAdClick,
                        onUserClick = onUserClick,
                        onSaveClick = { onSaveClick(adName) }
                    )
                }
            }
        }
        "yardly" -> {
            ListingScreen()
        }
        "watchlist" -> {
            WatchlistScreen(
                onBackClick = onBackClick,
                savedItems = savedItems,
                saveCounts = saveCounts,
                onSaveClick = onSaveClick  // ← THIS IS THE FIX
            )
        }
        "profile" -> {
            when (profileScreenState) {
                ProfileScreenState.Profile -> ProfileContent(
                    onBackClick = onBackClick,
                    onEditClick = { /* Handle Edit */ },
                    onMenuClick = onMenuClick
                )
                ProfileScreenState.Settings -> SettingsScreen(
                    onBackClick = onSettingsBackClick,
                    onAccessibilityClick = onAccessibilityClick
                )
                ProfileScreenState.Accessibility -> AccessibilityScreen(
                    onBackClick = onAccessibilityBackClick,
                    onDarkModeClick = onDarkModeClick
                )
                ProfileScreenState.DarkMode -> DarkModeScreen(
                    onBackClick = onDarkModeBackClick,
                    isDarkMode = isDarkMode,
                    onToggle = onDarkModeToggle
                )
            }
        }
        "messenger" -> {
            val content = """
                Messages
                Your conversations and messages will appear here
                John Doe
                Hey! Are you still interested in the pet adoption?
                Sarah Wilson
                Thanks for the lease swap.
            """.trimIndent()
            Text(
                text = content,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                lineHeight = 24.sp
            )
        }
        else -> Text(
            text = "Welcome",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun YardlyAppPreview() {
    YardlyTheme(isDarkMode = false) {
        YardlyApp(isDarkMode = false, onDarkModeToggle = {})
    }
}

@Preview(showBackground = true)
@Composable
fun YardlyAppPreviewDark() {
    YardlyTheme(isDarkMode = true) {
        YardlyApp(isDarkMode = true, onDarkModeToggle = {})
    }
}
