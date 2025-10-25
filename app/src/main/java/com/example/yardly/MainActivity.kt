package com.example.yardly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
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
    var selectedNavSection by remember { mutableStateOf("lease") }
    var selectedSectionOptions by remember { mutableStateOf<String?>(null) }
    val buttonCoordinates = remember { mutableStateMapOf<String, Float>() }

    val showHeaderAndNav = selectedIconSection == "home"

    val sectionOptions = mapOf(
        "aqua-swap" to listOf("Tank", "Substrate", "live plants", "live fish swap", "Equipment"),
        "yard-sales" to listOf("Move Out", "Garage Sale"),
        "lease" to listOf("Room", "Retail", "Car")
    )

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
                TopBar(
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    onMessengerClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.surfaceVariant,
                RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)
            )
            .statusBarsPadding()
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // App Title
        Text(
            text = "Yardly",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Messenger Button
        IconButton(
            onClick = onMessengerClick,
            modifier = Modifier
                .size(40.dp)
                .background(MaterialTheme.colorScheme.surface, CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = "Messenger",
                tint = MaterialTheme.colorScheme.onSurface,
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
        "yard-sales" to "Yard Sales",
        "lease" to "Lease",
        "auction" to "Auction"
    )

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
                onClick = { /* Handle button click */ },
                modifier = Modifier
                    .width(110.dp)
                    .height(44.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.primary
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
    selectedNavSection: String
) {
    when (selectedIconSection) {
        "home" -> {
            val content = when (selectedNavSection) {
                "aqua-swap" -> "Welcome to the Aqua Swap Section"
                "yard-sales" -> "Welcome to the Yard Sales Section"
                "lease" -> "Welcome to the Lease Section"
                "auction" -> "Welcome to the Auction Section"
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
            text = "Profile section is under construction.",
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
