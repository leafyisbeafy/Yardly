package com.example.yardly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector // Added for type checking
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource // Added for custom drawable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yardly.ui.theme.Theme
import com.example.yardly.ui.theme.ThemeViewModel
import com.example.yardly.ui.theme.YardlyTheme

class MainActivity : ComponentActivity() {
    private val themeViewModel: ThemeViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val theme by themeViewModel.theme.collectAsState()
            val darkTheme = when (theme) {
                Theme.LIGHT -> false
                Theme.DARK -> true
                Theme.SYSTEM -> isSystemInDarkTheme()
            }
            YardlyTheme(darkTheme = darkTheme, dynamicColor = false) {
                YardlyApp()
            }
        }
    }
}

@Composable
fun YardlyApp() {
    var selectedIconSection by remember { mutableStateOf("home") }
    var selectedNavSection by remember { mutableStateOf("garage-sale") }
    var searchText by remember { mutableStateOf("") }
    var selectedSectionOptions by remember { mutableStateOf<String?>(null) }
    val buttonCoordinates = remember { mutableStateMapOf<String, Float>() }

    val showHeaderAndNav = selectedIconSection == "home"

    val sectionOptions = mapOf(
        "aqua-swap" to listOf("Freshwater Fish", "Saltwater Fish", "Coral", "Aquarium Plants"),
        "garage-sale" to listOf("Furniture", "Appliances", "Clothing", "Toys")
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background) // Changed to theme background
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Top Bar (Header)
            if (showHeaderAndNav) {
                TopBar(
                    searchText = searchText,
                    onSearchTextChange = { searchText = it },
                    onMessengerClick = {
                        selectedIconSection = "messenger"
                    }
                )
            }

            // Content Area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                ContentArea(
                    selectedIconSection = selectedIconSection,
                    selectedNavSection = selectedNavSection
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
                        selectedSectionOptions = if (sectionOptions.containsKey(section) && selectedSectionOptions != section) section else null
                    },
                    onButtonPositioned = { key, x ->
                        buttonCoordinates[key] = x
                    }
                )
            }

            // Bottom Icon Navigation
            BottomIconNavigation(
                selectedSection = selectedIconSection,
                onSectionSelected = {
                    selectedIconSection = it
                    selectedSectionOptions = null
                }
            )
        }
    }
}

@Composable
fun TopBar(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    onMessengerClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.surfaceVariant, // Changed to theme surfaceVariant
                RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)
            )
            .statusBarsPadding()
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Search Container
        Box(
            modifier = Modifier
                .weight(1f)
                .height(40.dp)
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(20.dp)) // Changed to theme surface
                .padding(horizontal = 15.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant, // Changed to theme onSurfaceVariant
                    modifier = Modifier.size(20.dp)
                )

                BasicTextField(
                    value = searchText,
                    onValueChange = onSearchTextChange,
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = androidx.compose.ui.text.TextStyle(
                        color = MaterialTheme.colorScheme.onSurface, // Changed to theme onSurface
                        fontSize = 16.sp
                    ),
                    decorationBox = { innerTextField ->
                        if (searchText.isEmpty()) {
                            Text(
                                text = "Search...",
                                color = MaterialTheme.colorScheme.onSurfaceVariant, // Changed to theme onSurfaceVariant
                                fontSize = 16.sp
                            )
                        }
                        innerTextField()
                    }
                )
            }
        }

        // Messenger Button
        IconButton(
            onClick = onMessengerClick,
            modifier = Modifier
                .size(40.dp)
                .background(MaterialTheme.colorScheme.surface, CircleShape) // Changed to theme surface
        ) {
            Icon(
                painter = painterResource(id = R.drawable.msg), // Changed to R.drawable.msg
                contentDescription = "Messenger",
                tint = MaterialTheme.colorScheme.onSurface, // Changed to theme onSurface
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun SectionNavigation(
    selectedSection: String,
    onSectionSelected: (String) -> Unit,
    onButtonPositioned: (String, Float) -> Unit
) {
    val sections = listOf(
        "aqua-swap" to "Aqua Swap",
        "garage-sale" to "Garage Sale",
    )

    androidx.compose.foundation.lazy.LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        items(sections.size) { index ->
            val (sectionKey, sectionName) = sections[index]
            val isSelected = selectedSection == sectionKey

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
                    // Text color is now handled by ButtonDefaults.buttonColors contentColor
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
fun BottomIconNavigation(
    selectedSection: String,
    onSectionSelected: (String) -> Unit
) {
    val iconSections = listOf(
        Triple("home",
            "Home",
            R.drawable.home as Any),

        Triple("marketplace",
            "Marketplace",
            R.drawable.marketplace as Any),

        Triple(
            "create",
            "Create",
            R.drawable.create as Any
        ),
        Triple("save",
            "saved",
            R.drawable.save as Any),

        Triple("profile",
            "Profile",
            R.drawable.profile as Any)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.surfaceVariant, // Changed to theme surfaceVariant
                RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
            )
            .navigationBarsPadding()
            .padding(vertical = 10.dp, horizontal = 4.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        iconSections.forEach { (key, label, iconAsset) ->
            val isSelected = selectedSection == key

            IconButton(
                onClick = { onSectionSelected(key) },
                modifier = Modifier
                    .size(50.dp) // Added size
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surfaceContainerHighest,
                        CircleShape // Changed to CircleShape
                    )
                // Removed .padding(horizontal = 10.dp, vertical = 0.25.dp)
            ) {
                val iconTint = if (isSelected) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
                when (iconAsset) {
                    is ImageVector -> Icon(
                        imageVector = iconAsset,
                        contentDescription = label,
                        tint = iconTint, // Changed to theme-aware tint
                        modifier = Modifier.size(30.dp)
                    )
                    is Int -> Icon(
                        painter = painterResource(id = iconAsset),
                        contentDescription = label,
                        tint = iconTint, // Changed to theme-aware tint
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun SectionOptions(options: List<String>, xOffset: Float) {
    val density = LocalDensity.current
    val xOffsetDp = with(density) { xOffset.toDp() }
    Column(
        modifier = Modifier.fillMaxWidth().padding(start = xOffsetDp, bottom = 16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        options.forEach { name ->
            Button(
                onClick = { /* Handle button click */ },
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Text(text = name)
            }
        }
    }
}

@Composable
fun ContentArea(
    selectedIconSection: String,
    selectedNavSection: String
) {
    when (selectedIconSection) {
        "home" -> {
            val content = when (selectedNavSection) {
                "aqua-swap" -> "Welcome to the Aqua Swap Section"
                "garage-sale" -> "Welcome to the Garage Sale Section"
                else -> "Welcome to your home dashboard"
            }
            Text(
                text = content,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                lineHeight = 24.sp
            )
        }
        "create" -> Text(
            text = "Create\n\nCreate new content, posts, or items here",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp
        )
        "profile" -> Text(
            text = "Profile section is under construction.",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp
        )
        "messenger" -> {
            val content = buildString {
                append("Messages\n\n")
                append("Your conversations and messages will appear here\n\n")
                append("John Doe\n")
                append("Hey! Are you still interested in the pet adoption?\n\n")
                append("Sarah Wilson\n")
                append("Thanks for the lease swap info!")
            }
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
