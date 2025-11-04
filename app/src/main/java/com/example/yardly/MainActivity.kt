package com.example.yardly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.yardly.ui.components.AdCard
import com.example.yardly.ui.components.AdLoginSheet
import com.example.yardly.ui.components.ProfileSheet
import com.example.yardly.ui.theme.YardlyTheme

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

    val showHeaderAndNav = selectedIconSection == "home"

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

            // Content Area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(20.dp)
            ) {
                ContentArea(
                    selectedIconSection = selectedIconSection,
                    selectedNavSection = selectedNavSection,
                    onAdClick = { showAdLoginModal = true }
                )
            }

            // Section Options
            selectedSectionOptions?.let { section ->
                sectionOptions[section]?.let { options ->
                    val xOffset = buttonCoordinates[section] ?: 0f
                    SectionOptions(options = options, xOffset = xOffset)
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
                    // Show ProfileSheet when profile section is selected
                    if (section == "profile") {
                        showProfileSheet = true
                    }
                }
            )
        }

        // Ad Login Modal
        AdLoginSheet(
            showModal = showAdLoginModal,
            onDismiss = { showAdLoginModal = false }
        )

        // Profile Sheet
        ProfileSheet(
            showModal = showProfileSheet,
            onDismiss = { 
                showProfileSheet = false
                // Reset to home section when profile sheet is dismissed
                selectedIconSection = "home"
            },
            onBackClick = { 
                showProfileSheet = false
                selectedIconSection = "home"
            }
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
                    MaterialTheme.colorScheme.surfaceVariant,
                    RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp)
                )
                .padding(horizontal = 10.dp, vertical = 32.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            // Title removed - keeping TopBar structure for future modifications
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
                            color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
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
                        color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.primary
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
                        containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
                        contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.primary
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
        "save" to "Saved Items",
        "profile" to "Profile"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.surfaceVariant,
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
                    containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
                    contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.primary
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
fun SectionOptions(options: List<String>, xOffset: Float) {
    val density = LocalDensity.current
    val xOffsetDp = with(density) { xOffset.toDp() }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = xOffsetDp, bottom = 16.dp),
        horizontalAlignment = Alignment.Start
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
                    containerColor = if (name == "Rehome") MaterialTheme.colorScheme.primary else Color.Transparent,
                    contentColor = if (name == "Rehome") Color.White else MaterialTheme.colorScheme.primary
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
    selectedIconSection: String,
    selectedNavSection: String,
    onAdClick: () -> Unit = {}
) {
    when (selectedIconSection) {
        "home" -> {
            // Show scrollable list of AdCards
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(10) { index ->
                    AdCard(
                        advertisementName = "Advertisement ${index + 1}",
                        userName = "User ${index + 1}",
                        onAdClick = onAdClick
                    )
                }
            }
        }
        "yardly" -> Text(
            text = "Welcome to Yardly!",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp
        )
        "save" -> Text(
            text = "Saved Items section is under construction.",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp
        )
        "profile" -> Text(
            text = "Profile section - tap to open profile sheet",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp
        )
        "messenger" -> {
            val content = """
                Messages

                Your conversations and messages will appear here

                John Doe
                Hey! Are you still interested in the pet adoption?

                Sarah Wilson
                Thanks for the lease swap info!
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
    YardlyTheme(darkTheme = false, dynamicColor = false) { // Forced light theme
        YardlyApp()
    }
}
