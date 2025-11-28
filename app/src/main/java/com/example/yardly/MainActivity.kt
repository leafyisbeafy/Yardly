package com.example.yardly

import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.core.content.edit
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.example.yardly.ui.components.*
import com.example.yardly.ui.theme.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

// --- DATA: Helper Wrapper for legacy clicks ---
data class Ad(val name: String, val user: String)

// --- DATA: Master System List ---
private val systemPosts = listOf(
    UserPost(id = 1001L, title = "Air Force 1", price = "291.28", category = "Moving Out", description = "Good condition", location = "Campus", userName = "User 1"),
    UserPost(id = 1002L, title = "iPhone 13", price = "299.99", category = "Moving Out", description = "Unlocked", location = "Dorm A", userName = "User 2"),
    UserPost(id = 1003L, title = "PlayStation 4", price = "300.00", category = "Moving Out", description = "Comes with 2 controllers", location = "Northside", userName = "User 3"),
    UserPost(id = 1004L, title = "Macbook Air 13", price = "500.00", category = "Moving Out", description = "M1 Chip", location = "Library", userName = "User 4"),
    UserPost(id = 2001L, title = "Calculus Textbook", price = "45.00", category = "Textbook", description = "Calculus Early Transcendentals", location = "Library", userName = "User 7"),
    UserPost(id = 2002L, title = "Organic Chemistry", price = "60.00", category = "Textbook", description = "7th Edition", location = "Science Hall", userName = "User 12"),
    UserPost(id = 2003L, title = "Psych 101", price = "20.00", category = "Textbook", description = "Intro to Psychology", location = "East Hall", userName = "User 22"),
    UserPost(id = 3001L, title = "Golden Retriever Puppy", price = "Free", category = "Rescue", description = "Needs a loving home", location = "Southside", userName = "User 99"),
    UserPost(id = 3002L, title = "Cat for Adoption", price = "20.00", category = "Rescue", description = "Very friendly orange tabby", location = "East", userName = "User 33"),
    UserPost(id = 3003L, title = "Hamster Cage + Hamster", price = "Free", category = "Rescue", description = "Moving out, can't keep him", location = "West", userName = "User 41"),
    UserPost(id = 4001L, title = "Sublet: 1-Bed Room", price = "500.00", category = "Sublease", description = "Available for Summer", location = "Downtown", userName = "User 9"),
    UserPost(id = 4002L, title = "Luxury Apt Sublease", price = "800.00", category = "Sublease", description = "Aug-Dec", location = "The Lofts", userName = "User 44"),
    UserPost(id = 5001L, title = "Razer Gaming Chair", price = "222.00", category = "Moving Out", description = "Like new", location = "West", userName = "User 6"),
    UserPost(id = 5002L, title = "Mini Fridge (Black)", price = "50.00", category = "Moving Out", description = "Perfect for dorms", location = "Campus", userName = "User 8"),
    UserPost(id = 5003L, title = "IKEA Desk - White", price = "30.00", category = "Moving Out", description = "Sturdy desk", location = "Apts", userName = "User 9"),
    UserPost(id = 5004L, title = "Microwave", price = "25.00", category = "Moving Out", description = "Works great", location = "Dorm B", userName = "User 10")
)

sealed class ProfileScreenState {
    object Profile : ProfileScreenState()
    object Settings : ProfileScreenState()
    object Accessibility : ProfileScreenState()
    object DarkMode : ProfileScreenState()
    object EditProfile : ProfileScreenState()
    object AdDetail : ProfileScreenState()
}

const val PREFS_NAME = "yardly_settings"
const val KEY_DARK_MODE = "dark_mode_enabled"

class MainActivity : ComponentActivity() {
    private val sharedPreferences: SharedPreferences by lazy {
        getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
    }
    private lateinit var postStorage: PostStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postStorage = PostStorage(applicationContext)
        enableEdgeToEdge()
        val savedIsDarkMode = sharedPreferences.getBoolean(KEY_DARK_MODE, true)

        val insetsController = WindowCompat.getInsetsController(window, window.decorView)
        insetsController.isAppearanceLightStatusBars = !savedIsDarkMode

        setContent {
            var isDarkMode by remember { mutableStateOf(savedIsDarkMode) }
            val onDarkModeToggle: (Boolean) -> Unit = { enabled ->
                isDarkMode = enabled

                val insetsController = WindowCompat.getInsetsController(window, window.decorView)
                insetsController.isAppearanceLightStatusBars = !enabled

                sharedPreferences.edit {
                    putBoolean(KEY_DARK_MODE, enabled)
                }
            }

            YardlyTheme(
                isDarkMode = isDarkMode,
                dynamicColor = false
            ) {
                YardlyApp(
                    isDarkMode = isDarkMode,
                    onDarkModeToggle = onDarkModeToggle,
                    postStorage = postStorage,
                    onSaveImagePermanently = ::saveImagePermanently
                )
            }
        }
    }

    private fun saveImagePermanently(tempUri: Uri): Uri? {
        val contentResolver = applicationContext.contentResolver
        val fileName = "profile_image_${System.currentTimeMillis()}.jpg"
        val destinationFile = File(applicationContext.filesDir, fileName)

        return try {
            contentResolver.openInputStream(tempUri)?.use { inputStream ->
                destinationFile.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
            Uri.fromFile(destinationFile)
        } catch (e: Exception) {
            Log.e("MainActivity", "Failed to save image permanently", e)
            null
        }
    }
}


@Composable
fun YardlyApp(
    isDarkMode: Boolean,
    onDarkModeToggle: (Boolean) -> Unit,
    postStorage: PostStorage,
    onSaveImagePermanently: (Uri) -> Uri?
) {
    var selectedIconSection by remember { mutableStateOf("home") }
    var selectedNavSection by remember { mutableStateOf("home-default") }

    // --- APP STATE ---
    var isLoggedIn by remember { mutableStateOf(false) }
    var showAdLoginModal by remember { mutableStateOf(false) }
    var showProfileSheet by remember { mutableStateOf(false) }
    var showChooseCornerSheet by remember { mutableStateOf(false) }
    var showCreatePostSheet by remember { mutableStateOf(false) }

    // *** NEW STATE: Ad Detail Sheet ***
    var showAdDetailSheet by remember { mutableStateOf(false) }

    var profileScreenState by remember { mutableStateOf<ProfileScreenState>(ProfileScreenState.Profile) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val categoryScrollPositions = remember { mutableStateMapOf<String, Pair<Int, Int>>() }
    val cropImage = rememberLauncherForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            val savedUri = onSaveImagePermanently(result.uriContent!!)
            imageUri = savedUri
        }
    }

    var selectedPost by remember { mutableStateOf<UserPost?>(null) }

    val saveCounts = remember { mutableStateMapOf<String, Int>() }
    val savedItems = remember { mutableStateMapOf<String, Boolean>() }
    val showHeaderAndNav = selectedIconSection == "home"
    val gridState = rememberLazyGridState()

    // --- Scroll Detection Logic ---
    var previousIndex by remember(gridState) { mutableIntStateOf(0) }
    var previousOffset by remember(gridState) { mutableIntStateOf(0) }

    val isControlsVisible by remember {
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

    var userPosts by remember { mutableStateOf<List<UserPost>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()
    var suppressNavShow by remember { mutableStateOf(false) }

    // --- PROFILE STATE ---
    var profileName by remember { mutableStateOf("Peyton Venzeee") }
    var profileUsername by remember { mutableStateOf("peyton") }
    var profileBio by remember { mutableStateOf("just another broke college student...") }
    var profileImageUri by remember { mutableStateOf<Uri?>(null) }

    // --- BACK HANDLER LOGIC ---
    val isHome = selectedIconSection == "home"
    val isDefaultHome = selectedNavSection == "home-default"

    BackHandler(enabled = !isHome || !isDefaultHome) {
        if (!isHome) {
            selectedIconSection = "home"
        } else if (!isDefaultHome) {
            selectedNavSection = "home-default"
        }
    }

    // --- (LaunchedEffects) ---
    LaunchedEffect(Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            val loadedPosts = postStorage.loadPosts()
            withContext(Dispatchers.Main) {
                userPosts = loadedPosts
            }
        }
    }
    LaunchedEffect(userPosts) {
        if (userPosts.isNotEmpty()) {
            coroutineScope.launch(Dispatchers.IO) {
                postStorage.savePosts(userPosts)
            }
        }
    }

    LaunchedEffect(selectedNavSection) {
        if (selectedIconSection == "home") {
            val (index, offset) = categoryScrollPositions[selectedNavSection] ?: (0 to 0)
            gridState.scrollToItem(index, offset)
        }
    }

    LaunchedEffect(isControlsVisible) {
        if (!isControlsVisible) {
            suppressNavShow = false
        }
    }

    // --- (navigation lambdas) ---
    val navigateToSettings = {
        showProfileSheet = false
        selectedIconSection = "profile"
        profileScreenState = ProfileScreenState.Settings
    }
    val navigateToEditProfile = {
        showProfileSheet = false
        selectedIconSection = "profile"
        profileScreenState = ProfileScreenState.EditProfile
    }

    val onSaveClick: (String) -> Unit = { adName ->
        val currentCount = saveCounts.getOrDefault(adName, 0)
        val isSaved = savedItems.getOrDefault(adName, false)
        if (isSaved) {
            if (currentCount > 0) saveCounts[adName] = currentCount - 1
            savedItems[adName] = false
        } else {
            saveCounts[adName] = currentCount + 1
            savedItems[adName] = true
        }
    }

    val dynamicAdList = emptyList<Ad>()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            if (showHeaderAndNav) {
                TopBar()
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                ContentArea(
                    userPosts = userPosts,
                    ads = dynamicAdList,
                    gridState = gridState,
                    selectedIconSection = selectedIconSection,
                    selectedNavSection = selectedNavSection,
                    profileScreenState = profileScreenState,
                    isDarkMode = isDarkMode,
                    saveCounts = saveCounts,
                    savedItems = savedItems,
                    selectedPost = selectedPost,

                    profileName = profileName,
                    profileUsername = profileUsername,
                    profileBio = profileBio,
                    profileImageUri = profileImageUri,

                    onSaveProfile = { newName, newUsername, newBio, newImageUri ->
                        profileName = newName
                        profileUsername = newUsername
                        profileBio = newBio
                        if (newImageUri != null) {
                            profileImageUri = newImageUri
                        }
                        profileScreenState = ProfileScreenState.Profile
                    },
                    onSaveImagePermanently = onSaveImagePermanently,

                    onAdClick = { ad ->
                        if (isLoggedIn) {
                            val newPost = UserPost(
                                title = ad.name,
                                description = "Description for ${ad.name}",
                                category = "General",
                                location = "Campus",
                                price = "Contact for Price",
                                userName = ad.user,
                                imageUriString = null
                            )
                            selectedPost = newPost
                            // *** UPDATED: Show sheet instead of changing screen state
                            showAdDetailSheet = true
                        } else {
                            showAdLoginModal = true
                        }
                    },
                    onBackClick = { selectedIconSection = "home" },
                    onSettingsBackClick = { profileScreenState = ProfileScreenState.Profile },
                    onAccessibilityBackClick = { profileScreenState = ProfileScreenState.Settings },
                    onDarkModeBackClick = { profileScreenState = ProfileScreenState.Accessibility },
                    onEditProfileBackClick = { profileScreenState = ProfileScreenState.Profile },
                    onAdDetailBackClick = { profileScreenState = ProfileScreenState.Profile },

                    onEditClick = navigateToEditProfile,
                    onUserClick = { showProfileSheet = true },
                    onMenuClick = navigateToSettings,

                    onAccessibilityClick = { profileScreenState = ProfileScreenState.Accessibility },
                    onDarkModeClick = { profileScreenState = ProfileScreenState.DarkMode },

                    onDarkModeToggle = onDarkModeToggle,
                    onSaveClick = onSaveClick,

                    onPostClick = { post ->
                        selectedPost = post
                        // *** UPDATED: Show sheet instead of changing screen state
                        showAdDetailSheet = true
                    }
                )

                // --- FABs ---
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(Dimens.SpacingXLarge),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(Dimens.SpacingXLarge)
                ) {
                    AnimatedVisibility(
                        visible = isFabMenuExpanded && isControlsVisible && selectedIconSection == "home",
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        FloatingActionButton(
                            onClick = {
                                showChooseCornerSheet = true
                                isFabMenuExpanded = false
                            },
                            shape = CircleShape,
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.onSecondary
                        ) {
                            Icon(
                                Icons.Filled.LocationOn,
                                contentDescription = "Choose Location"
                            )
                        }
                    }
                    AnimatedVisibility(
                        visible = isFabMenuExpanded && isControlsVisible && selectedIconSection == "home",
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        FloatingActionButton(
                            onClick = {
                                if (!isLoggedIn) {
                                    showAdLoginModal = true
                                } else {
                                    showCreatePostSheet = true
                                    isFabMenuExpanded = false
                                }
                            },
                            shape = CircleShape,
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ) {
                            Icon(
                                Icons.Filled.CameraAlt,
                                contentDescription = "Create Post"
                            )
                        }
                    }
                    AnimatedVisibility(
                        visible = isControlsVisible && selectedIconSection == "home",
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
            }

            // --- Navigation Logic ---
            val isBottomNavVisible = if (selectedIconSection != "home") {
                true
            } else {
                isControlsVisible && !suppressNavShow
            }

            if (showHeaderAndNav) {
                SectionNavigation(
                    selectedSection = selectedNavSection,
                    onSectionSelected = { section ->
                        categoryScrollPositions[selectedNavSection] = gridState.firstVisibleItemIndex to gridState.firstVisibleItemScrollOffset
                        if (!isBottomNavVisible) {
                            suppressNavShow = true
                        }
                        selectedNavSection = section
                    },
                    modifier = if (!isBottomNavVisible) Modifier.navigationBarsPadding() else Modifier
                )
            }

            AnimatedVisibility(
                visible = isBottomNavVisible,
                enter = slideInVertically(initialOffsetY = { it }) + expandVertically(),
                exit = slideOutVertically(targetOffsetY = { it }) + shrinkVertically()
            ) {
                BottomIconNavigation(
                    selectedSection = selectedIconSection,
                    onSectionSelected = { section ->
                        selectedIconSection = section

                        if (section == "home") {
                            selectedNavSection = "home-default"
                        }

                        if (section != "profile") {
                            profileScreenState = ProfileScreenState.Profile
                        }
                    }
                )
            }
        }

        // --- (Modals) ---
        AdLoginSheet(
            showModal = showAdLoginModal,
            onDismiss = { showAdLoginModal = false },
            onLoginSuccess = {
                isLoggedIn = true
                showAdLoginModal = false
            }
        )

        ProfilePopup(
            name = profileName,
            username = profileUsername,
            bio = profileBio,
            imageUri = profileImageUri,
            userPosts = userPosts,
            saveCounts = saveCounts,
            savedItems = savedItems,
            showModal = showProfileSheet,
            onDismiss = { showProfileSheet = false },
            onBackClick = { showProfileSheet = false },
            onEditClick = navigateToEditProfile,
            onMenuClick = navigateToSettings,
            onNavigateToAdDetail = { post ->
                showProfileSheet = false
                selectedPost = post
                // *** UPDATED: Show sheet instead of changing screen state
                showAdDetailSheet = true
            },
            onSaveClick = onSaveClick
        )

        ChooseCornerSheet(
            showModal = showChooseCornerSheet,
            onDismiss = { showChooseCornerSheet = false }
        )
        CreatePostSheet(
            userName = profileName,
            showModal = showCreatePostSheet,
            onDismiss = { showCreatePostSheet = false },
            onPostListing = { title, desc, category, location, price, imageUriString ->
                val newPost = UserPost(
                    title = title,
                    description = desc,
                    category = category,
                    location = location,
                    price = price,
                    imageUriString = imageUriString
                )
                userPosts = listOf(newPost) + userPosts
                Log.d("CreatePostSheet", "New Post Saved: $newPost")
                coroutineScope.launch(Dispatchers.IO) {
                    postStorage.savePosts(userPosts)
                }
                imageUri = null
            },
            imageUri = imageUri,
            onSelectImageClick = {
                cropImage.launch(
                    CropImageContractOptions(
                        uri = null,
                        cropImageOptions = CropImageOptions()
                    )
                )
            }
        )

        // *** NEW: Ad Detail Sheet ***
        AdDetailSheet(
            post = selectedPost,
            showModal = showAdDetailSheet,
            onDismiss = { showAdDetailSheet = false },
            isSaved = if (selectedPost != null) savedItems.getOrDefault(selectedPost!!.title, false) else false,
            saveCount = if (selectedPost != null) saveCounts.getOrDefault(selectedPost!!.title, 0) else 0,
            onSaveClick = {
                if (selectedPost != null) {
                    onSaveClick(selectedPost!!.title)
                }
            },
            onUserClick = {
                showAdDetailSheet = false
                showProfileSheet = true
            }
        )
    }
}


// --- (TopBar) ---
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
                    MaterialTheme.colorScheme.background,
                    RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp)
                )
                .padding(horizontal = Dimens.ScreenPaddingHorizontal, vertical = Dimens.SpacingLarge),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = { /* TODO: Handle notifications */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onBackground
                ),
                shape = RoundedCornerShape(20.dp),
                contentPadding = PaddingValues(horizontal = Dimens.SpacingLarge)
            ) {
                Text(
                    text = "Channel",
                    fontSize = 12.sp
                )
            }
        }
    }
}

// --- SectionNavigation ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SectionNavigation(
    selectedSection: String,
    onSectionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // This will now include the "Home" category if you updated Category.kt
    val categories = Category.all

    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = Dimens.SpacingMedium),
        horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingSmall),
        contentPadding = PaddingValues(horizontal = Dimens.ScreenPaddingHorizontal)
    ) {
        items(categories) { category ->
            val isSelected = selectedSection == category.id

            val containerColor by animateColorAsState(
                targetValue = if (isSelected) MaterialTheme.colorScheme.surfaceVariant else Color.Transparent,
                label = "containerColor"
            )

            val contentColor by animateColorAsState(
                targetValue = if (isSelected) category.onColor else MaterialTheme.colorScheme.onBackground,
                label = "contentColor"
            )

            val borderColor by animateColorAsState(
                targetValue = if (isSelected) Color.Transparent else category.color,
                label = "borderColor"
            )

            Box(
                modifier = Modifier
                    .height(40.dp)
                    .border(
                        width = if (isSelected) 0.dp else 2.dp,
                        color = borderColor,
                        shape = RoundedCornerShape(50)
                    )
                    .background(
                        color = containerColor,
                        shape = RoundedCornerShape(50)
                    )
                    .clip(RoundedCornerShape(50))
                    .clickable { onSectionSelected(category.id) }
                    .padding(horizontal = Dimens.SpacingXLarge),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = category.label,
                    fontSize = 14.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = contentColor
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
        "messenger" to "Messenger",
        "notification" to "Notification",
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
            .padding(vertical = Dimens.SpacingLarge, horizontal = Dimens.SpacingMedium),
        horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingMedium),
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
                contentPadding = PaddingValues(horizontal = Dimens.SpacingSmall)
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

// --- ContentArea ---
@Composable
fun ContentArea(
    userPosts: List<UserPost>,
    ads: List<Ad>,
    gridState: LazyGridState,
    selectedIconSection: String,
    selectedNavSection: String,
    profileScreenState: ProfileScreenState,
    isDarkMode: Boolean,
    saveCounts: Map<String, Int>,
    savedItems: Map<String, Boolean>,
    selectedPost: UserPost?,

    profileName: String,
    profileUsername: String,
    profileBio: String,
    profileImageUri: Uri?,
    onSaveProfile: (String, String, String, Uri?) -> Unit,
    onSaveImagePermanently: (Uri) -> Uri?,

    onAdClick: (Ad) -> Unit = {},
    onBackClick: () -> Unit = {},
    onSettingsBackClick: () -> Unit = {},
    onAccessibilityBackClick: () -> Unit = {},
    onDarkModeBackClick: () -> Unit = {},
    onEditProfileBackClick: () -> Unit = {},
    onAdDetailBackClick: () -> Unit = {},
    onUserClick: () -> Unit = {},
    onMenuClick: () -> Unit = {},
    onEditClick: () -> Unit = {},
    onAccessibilityClick: () -> Unit = {},
    onDarkModeClick: () -> Unit = {},
    onDarkModeToggle: (Boolean) -> Unit,
    onSaveClick: (String) -> Unit,
    onPostClick: (UserPost) -> Unit
) {
    // 1. COMBINE DATA Sources
    val allPosts = remember(userPosts) { userPosts + systemPosts }

    // 2. FILTERING LOGIC
    val currentCategoryLabel = Category.getLabelById(selectedNavSection)

    val visiblePosts = remember(selectedNavSection, allPosts) {
        if (selectedNavSection == "home-default") {
            allPosts
        } else {
            allPosts.filter { it.category == currentCategoryLabel }
        }
    }

    when (selectedIconSection) {
        "home" -> {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                state = gridState,
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(Dimens.SpacingLarge),
                horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingLarge),
                contentPadding = PaddingValues(
                    horizontal = Dimens.ScreenPaddingHorizontal,
                    vertical = Dimens.ScreenPaddingVertical
                )
            ) {
                items(visiblePosts, key = { it.id }) { post ->
                    val saveCount = saveCounts.getOrDefault(post.title, 0)
                    val isSaved = savedItems.getOrDefault(post.title, false)

                    AdCard(
                        advertisementName = post.title,
                        userName = post.userName,
                        saveCount = saveCount,
                        isSaved = isSaved,
                        onAdClick = { onAdClick(Ad(post.title, post.userName)) },
                        onUserClick = onUserClick,
                        onSaveClick = { onSaveClick(post.title) }
                    )
                }
            }
        }
        "watchlist" -> {
            WatchlistScreen(
                onBackClick = onBackClick,
                savedItems = savedItems,
                saveCounts = saveCounts,
                onSaveClick = onSaveClick,
                allPosts = allPosts,
                // *** FIX: Use onPostClick parameter name ***
                onPostClick = onPostClick
            )
        }
        "profile" -> {
            when (profileScreenState) {
                ProfileScreenState.Profile -> ProfileContent(
                    name = profileName,
                    username = profileUsername,
                    bio = profileBio,
                    imageUri = profileImageUri,
                    userPosts = userPosts,
                    saveCounts = saveCounts,
                    savedItems = savedItems,
                    onBackClick = onBackClick,
                    onEditClick = onEditClick,
                    onMenuClick = onMenuClick,
                    onNavigateToAdDetail = onPostClick,
                    onSaveClick = onSaveClick,
                    isEditMode = false
                )
                ProfileScreenState.Settings -> SettingsScreen(
                    onBackClick = onSettingsBackClick,
                    onAccessibilityClick = onAccessibilityClick,
                    onDarkModeClick = onDarkModeClick
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
                ProfileScreenState.EditProfile -> EditProfileScreen(
                    currentName = profileName,
                    currentUsername = profileUsername,
                    currentBio = profileBio,
                    currentImageUri = profileImageUri,
                    onBackClick = onEditProfileBackClick,
                    onSaveClick = onSaveProfile,
                    onSaveImagePermanently = onSaveImagePermanently
                )
                ProfileScreenState.AdDetail -> {
                    // Placeholder for AdDetail if accessed via legacy state
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Ad Detail is now a popup sheet.")
                    }
                }
            }
        }
        "messenger" -> {
            MessengerScreen(
                onBackClick = onBackClick
            )
        }

        "notification" -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Notifications",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
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