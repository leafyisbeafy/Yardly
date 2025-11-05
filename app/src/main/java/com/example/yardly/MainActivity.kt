package com.example.yardly

import android.os.Bundle
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import com.example.yardly.ui.components.AccessibilityScreen // <-- ADDED IMPORT
import com.example.yardly.ui.components.AdCard
import com.example.yardly.ui.components.AdLoginSheet
import com.example.yardly.ui.components.ChooseCornerSheet
import com.example.yardly.ui.components.DarkModeScreen // <-- ADDED IMPORT
import com.example.yardly.ui.components.FindNear
import com.example.yardly.ui.components.ListingScreen
import com.example.yardly.ui.components.ProfileContent
import com.example.yardly.ui.components.ProfilePopup
import com.example.yardly.ui.components.SettingsScreen
import com.example.yardly.ui.components.WatchlistScreen
import com.example.yardly.ui.theme.YardlyTheme
import androidx.compose.material3.ExperimentalMaterial3Api

// *** NEW: State definition for profile tab navigation ***
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
            YardlyTheme(darkTheme = false, dynamicColor = false) {
                YardlyApp()
            }
        }
    }
}

@Composable
fun YardlyApp() {
    var selectedIconSection by remember { mutableStateOf("home") }
    var selectedNavSection by remember { mutableStateOf("lease") }
    var selectedSectionOptions by remember { mutableStateOf<String?>(null) }
    val buttonCoordinates = remember { mutableStateMapOf<String, Float>() }
    var showRehomeInAquaSwap by remember { mutableStateOf(false) }
    var showAdLoginModal by remember { mutableStateOf(false) }
    var showProfileSheet by remember { mutableStateOf(false) }
    var showChooseCornerSheet by remember { mutableStateOf(false) }

    // *** REPLACED boolean with new state class ***
    var profileScreenState by remember { mutableStateOf<ProfileScreenState>(ProfileScreenState.Profile) }

    val showHeaderAndNav = selectedIconSection == "home"

    // Scroll state detection
    val listState = rememberLazyListState()
    var previousIndex by remember { mutableStateOf(listState.firstVisibleItemIndex) }
    var previousOffset by remember { mutableStateOf(listState.firstVisibleItemScrollOffset) }

    val isFabVisible by remember {
        derivedStateOf {
            val currentIndex = listState.firstVisibleItemIndex
            val currentOffset = listState.firstVisibleItemScrollOffset

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

    // *** UPDATED: Navigation logic for settings ***
    val navigateToSettings = {
        showProfileSheet = false
        selectedIconSection = "profile"
        profileScreenState = ProfileScreenState.Settings
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
                // Content Area (LazyColumn or Profile Page)
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .then(
                            // Add padding unless it's a full-screen page
                            if (selectedIconSection == "home" || selectedIconSection == "yardly")
                                Modifier.padding(20.dp)
                            else Modifier
                        )
                ) {
                    ContentArea(
                        listState = listState,
                        selectedIconSection = selectedIconSection,
                        selectedNavSection = selectedNavSection,
                        profileScreenState = profileScreenState, // <-- PASS STATE
                        onAdClick = { showAdLoginModal = true },
                        onBackClick = { selectedIconSection = "home" }, // For Watchlist
                        onSettingsBackClick = { profileScreenState = ProfileScreenState.Profile }, // Settings -> Profile
                        onAccessibilityBackClick = { profileScreenState = ProfileScreenState.Settings }, // Access -> Settings
                        onDarkModeBackClick = { profileScreenState = ProfileScreenState.Accessibility }, // DarkMode -> Access
                        onUserClick = { showProfileSheet = true },
                        onMenuClick = navigateToSettings, // For Profile -> Settings
                        onAccessibilityClick = { profileScreenState = ProfileScreenState.Accessibility }, // Settings -> Access
                        onDarkModeClick = { profileScreenState = ProfileScreenState.DarkMode } // Access -> DarkMode
                    )
                }

                // Section Options (Overlaid on top, bottom-start)
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

                // Only show the FindNear button if we are on the "home" screen
                if (selectedIconSection == "home") {
                    FindNear(
                        isVisible = isFabVisible,
                        onClick = { showChooseCornerSheet = true },
                        modifier = Modifier.align(Alignment.BottomEnd)
                    )
                }
            }

            // Section Navigation (only show when home is selected)
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
                    profileScreenState = ProfileScreenState.Profile // <-- Reset on tab change
                }
            )
        }

        // Ad Login Modal
        AdLoginSheet(
            showModal = showAdLoginModal,
            onDismiss = { showAdLoginModal = false }
        )

        // Profile Sheet (POPUP VERSION)
        ProfilePopup(
            showModal = showProfileSheet,
            onDismiss = { showProfileSheet = false },
            onBackClick = { showProfileSheet = false },
            onMenuClick = navigateToSettings // <-- Pass action
        )

        // NEW: Call to ChooseCornerSheet
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
        // Transparent status bar area
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
        )

        // Content area with background
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.surfaceVariant, // Light Gray
                    RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp)
                )
                .padding(horizontal = 20.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            // Right Side: Icon Buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Notification Button
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
                // Messenger Button
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
                            // STYLE: Selected = light gray, Unselected = transparent
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
                        // STYLE: Both selected and unselected text is black
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
                        // STYLE: Selected = light gray, Unselected = transparent
                        containerColor = if (isSelected) MaterialTheme.colorScheme.surfaceVariant else Color.Transparent,
                        // STYLE: Both selected and unselected text is black
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
                MaterialTheme.colorScheme.background, // White
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
                    // STYLE: Selected = light gray, Unselected = transparent
                    containerColor = if (isSelected) MaterialTheme.colorScheme.surfaceVariant else Color.Transparent,
                    // STYLE: Both selected and unselected text is black
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
                        // Handle rehome action
                        println("Rehome option selected")
                    } else {
                        // Handle other button clicks
                        println("$name option selected")
                    }
                },
                modifier = Modifier
                    .width(110.dp)
                    .height(44.dp),
                colors = ButtonDefaults.buttonColors(
                    // STYLE: "Rehome" = black, others = light gray
                    containerColor = if (name == "Rehome") MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.surfaceVariant,
                    // STYLE: "Rehome" = white, others = black
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
    listState: LazyListState,
    selectedIconSection: String,
    selectedNavSection: String,
    profileScreenState: ProfileScreenState, // <-- CHANGED
    onAdClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
    onSettingsBackClick: () -> Unit = {},
    onAccessibilityBackClick: () -> Unit = {}, // <-- NEW
    onDarkModeBackClick: () -> Unit = {}, // <-- NEW
    onUserClick: () -> Unit = {},
    onMenuClick: () -> Unit = {},
    onAccessibilityClick: () -> Unit = {}, // <-- NEW
    onDarkModeClick: () -> Unit = {} // <-- NEW
) {
    when (selectedIconSection) {
        "home" -> {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(10) { index ->
                    AdCard(
                        advertisementName = "Advertisement ${index + 1}",
                        userName = "User ${index + 1}",
                        onAdClick = onAdClick,
                        onUserClick = onUserClick
                    )
                }
            }
        }
        "yardly" -> {
            ListingScreen()
        }
        "watchlist" -> {
            WatchlistScreen(onBackClick = onBackClick)
        }
        "profile" -> {
            // *** NEW: Navigation logic for profile tab ***
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
                    onBackClick = onDarkModeBackClick
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
    YardlyApp()
}