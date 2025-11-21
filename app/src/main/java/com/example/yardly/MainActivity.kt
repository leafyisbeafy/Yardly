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
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.example.yardly.ui.components.*
import com.example.yardly.ui.theme.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.UUID

// --- (All data classes and mock data) ---
data class Ad(val name: String, val user: String)

private val defaultAds = listOf(
    Ad("Air Force 1", "User 1"),
    Ad("iPhone 13", "User 2"),
    Ad("PlayStation 4", "User 3"),
    Ad("Macbook Air 13", "User 4"),
    Ad("Denim Jacket", "User 5"),
    Ad("Razer Gaming Chair", "User 6"),
    Ad("Calculus Textbook", "User 7"),
    Ad("Mini Fridge (Black)", "User 8"),
    Ad("IKEA Desk - White", "User 9"),
    Ad("Microwave", "User 10"),
    Ad("Study Lamp", "User 11"),
    Ad("Futon Sofa Bed", "User 12"),
    Ad("4K Monitor 27\"", "User 13"),
    Ad("Bose Speakers", "User 14"),
    Ad("Winter Boots (Size 10)", "User 15"),
    Ad("TI-84 Calculator", "User 16"),
    Ad("Keurig Coffee Maker", "User 17"),
    Ad("Wooden Bookshelf", "User 18"),
    Ad("Office Chair", "User 19"),
    Ad("Dining Table Set", "User 20"),
    Ad("Floor Lamp", "User 21"),
    Ad("Area Rug 5x7", "User 22"),
    Ad("Ninja Blender", "User 23"),
    Ad("Toaster Oven", "User 24"),
    Ad("Queen Bed Frame", "User 25"),
    Ad("TV Stand", "User 26")
)

private val allSubleaseAds = mapOf(
    "Room" to listOf(
        Ad("Sublet: 1-Bed Room", "User A"),
        Ad("Shared Room Downtown", "User B"),
        Ad("Summer Sublet, 2BR", "User S1"),
        Ad("Fall Semester Sublet", "User S2"),
        Ad("Luxury Apartment Sublet", "User S3"),
        Ad("Studio Near Campus", "User S4"),
        Ad("Furnished Room for Rent", "User S5"),
        Ad("Quiet Neighborhood Sublet", "User S6"),
        Ad("Sublet with Parking", "User S7"),
        Ad("Month-to-Month Sublet", "User S8"),
        Ad("Sublet, Utilities Included", "User S9"),
        Ad("Pet-Friendly Sublet", "User S10"),
        Ad("Sublet with Gym Access", "User S11"),
        Ad("Bright and Sunny Room", "User S12"),
        Ad("Sublet close to transit", "User S13"),
        Ad("4-Month Sublet available", "User S14"),
        Ad("Renovated Apartment Sublet", "User S15"),
        Ad("Spacious Room for Rent", "User S16"),
        Ad("Sublet with Balcony", "User S17"),
        Ad("Affordable Downtown Sublet", "User S18"),
        Ad("Cozy Room in Shared House", "User S19"),
        Ad("Sublet with Modern Kitchen", "User S20"),
        Ad("Private Room and Bath", "User S21"),
        Ad("Student Housing Sublet", "User S22"),
        Ad("Short-Term Sublet", "User S23"),
        Ad("Walk-in Closet Room", "User S24"),
        Ad("All-Inclusive Sublet", "User S25"),
        Ad("Rooftop Patio Access", "User S26"),
        Ad("Sublet with a View", "User S27"),
        Ad("Great for Commuters", "User S28"),
        Ad("Clean and Tidy Sublet", "User S29"),
        Ad("Secure Building Sublet", "User S30"),
        Ad("Loft-Style Apartment", "User S31"),
        Ad("Sublet in Historic Building", "User S32"),
        Ad("Close to Parks and Trails", "User S33"),
        Ad("Sublet with In-Suite Laundry", "User S34"),
        Ad("Charming Sublet", "User S35"),
        Ad("Large Window Room", "User S36"),
        Ad("Student-Friendly Sublet", "User S37"),
        Ad("Sublet with Bike Storage", "User S38")
    ),
    "Car" to listOf(Ad("Toyota Camry 2018", "User C"), Ad("Honda Civic Lease", "User D")),
    "Retail Store" to listOf(Ad("Pop-up Shop Space", "User E"), Ad("Small Retail Front", "User F"))
)

private val allTextbookAds = listOf(
    Ad("Calculus Early Transcendentals", "User G"),
    Ad("Organic Chemistry 3rd Ed", "User H"),
    Ad("Psychology 101", "User I"),
    Ad("Intro to Java Programming", "User J"),
    Ad("Microeconomics", "User K"),
    Ad("Physics for Scientists", "User L"),
    Ad("Data Strcutures and Alo", "User 22"),
    Ad("World Religions", "User 33"),
    Ad("RD Sharma", "User 34"),
    Ad("Linear Algebra and its Applications (5th Ed)", "User 31"),
    Ad("History of Western Art (10th Ed)", "User 32"),
    Ad("Data Structures and Algorithms (Mint)", "User 33"),
    Ad("Financial Accounting (Loose Leaf)", "User 34"),
    Ad("Public Speaking Today", "User 35"),
    Ad("Statistics for Beginners", "User 36"),
    Ad("Nursing Fundamentals Textbook", "User 37"),
    Ad("Engineering Mechanics: Statics", "User 38"),
    Ad("World Religions (2nd Edition)", "User 39"),
    Ad("The Art of Public Speaking", "User T1"),
    Ad("Biology: A Global Approach", "User T2"),
    Ad("Chemistry: The Central Science", "User T3"),
    Ad("Campbell Biology", "User T4"),
    Ad("Economics by Mankiw", "User T5"),
    Ad("Fundamentals of Physics", "User T6"),
    Ad("Introduction to Algorithms", "User T7"),
    Ad("Marketing Management", "User T8"),
    Ad("Principles of Marketing", "User T9"),
    Ad("Psychology by Myers", "User T10"),
    Ad("Sociology: A Brief Introduction", "User T11"),
    Ad("The Norton Anthology of English Literature", "User T12"),
    Ad("A People's History of the United States", "User T13"),
    Ad("Calculus by Stewart", "User T14"),
    Ad("Differential Equations", "User T15"),
    Ad("Genki I: An Integrated Course in Elementary Japanese", "User T16"),
    Ad("Gray's Anatomy for Students", "User T17"),
    Ad("Harrison's Principles of Internal Medicine", "User T18"),
    Ad("Janeway's Immunobiology", "User T19"),
    Ad("Lehninger Principles of Biochemistry", "User T20"),
    Ad("Molecular Biology of the Cell", "User T21"),
    Ad("The C Programming Language", "User T22"),
    Ad("The Clean Coder", "User T23"),
    Ad("The Pragmatic Programmer", "User T24"),
    Ad("Design Patterns: Elements of Reusable Object-Oriented Software", "User T25"),
    Ad("Compilers: Principles, Techniques, and Tools", "User T26"),
    Ad("Computer Networking: A Top-Down Approach", "User T27"),
    Ad("Database System Concepts", "User T28"),
    Ad("Modern Operating Systems", "User T29"),
    Ad("Structure and Interpretation of Computer Programs", "User T30"),
    Ad("The Lord of the Rings", "User T31"),
    Ad("Pride and Prejudice", "User T32"),
    Ad("The Great Gatsby", "User T33"),
    Ad("To Kill a Mockingbird", "User T34"),
    Ad("1984", "User T35"),
    Ad("The Catcher in the Rye", "User T36"),
    Ad("Brave New World", "User T37"),
    Ad("The Hobbit", "User T38")
)

private val allMovingOutAds = listOf(
    Ad("Moving Sale: Everything Must Go", "User M"),
    Ad("Couch for Sale", "User N"),
    Ad("Dining Table", "User O"),
    Ad("Queen Bed Frame", "User P"),
    Ad("Patio Furniture Set (4 pieces)", "User 40"),
    Ad("Free Moving Boxes (Tons)", "User 41"),
    Ad("Toolbox and Starter Tools", "User 42"),
    Ad("Brand New Curtains (Blackout)", "User 43"),
    Ad("Box of Kitchen Utensils", "User 44"),
    Ad("Vintage Bicycle (Needs work)", "User 45"),
    Ad("Desk Chair", "User M1"),
    Ad("Bookshelf", "User M2"),
    Ad("Dresser", "User M3"),
    Ad("Nightstand", "User M4"),
    Ad("Coffee Table", "User M5"),
    Ad("TV Stand", "User M6"),
    Ad("Lamps", "User M7"),
    Ad("Microwave", "User M8"),
    Ad("Toaster", "User M9"),
    Ad("Blender", "User M10"),
    Ad("Dish Set", "User M11"),
    Ad("Silverware", "User M12"),
    Ad("Pots and Pans", "User M13"),
    Ad("Baking Sheets", "User M14"),
    Ad("Mixing Bowls", "User M15"),
    Ad("Measuring Cups", "User M16"),
    Ad("Wall Art", "User M17"),
    Ad("Throw Pillows", "User M18"),
    Ad("Blankets", "User M19"),
    Ad("Towels", "User M20"),
    Ad("Shower Curtain", "User M21"),
    Ad("Bathroom Rug", "User M22"),
    Ad("Trash Cans", "User M23"),
    Ad("Laundry Baskets", "User M24"),
    Ad("Iron and Ironing Board", "User M25"),
    Ad("Vacuum Cleaner", "User M26"),
    Ad("Broom and Dustpan", "User M27"),
    Ad("Mop and Bucket", "User M28"),
    Ad("Cleaning Supplies", "User M29"),
    Ad("Storage Bins", "User M30"),
    Ad("Hangers", "User M31"),
    Ad("Shoe Rack", "User M32"),
    Ad("Full-Length Mirror", "User M33"),
    Ad("House Plants", "User M34"),
    Ad("Outdoor Chairs", "User M35"),
    Ad("Grill", "User M36"),
    Ad("Lawn Mower", "User M37"),
    Ad("Garden Tools", "User M38")
)

private val allGarageSaleAds = listOf(
    Ad("Neighborhood Garage Sale", "User Q"),
    Ad("Antique Sale", "User R"),
    Ad("Box of Vinyl Records", "User S"),
    Ad("Old Tools Bundle", "User T")
)

private val allAquaSwapAds = mapOf(
    "Equipment" to listOf(Ad("Used 50g Filter", "User K"), Ad("Heater", "User L")),
    "Coral" to listOf(Ad("Zoanthid Frag", "User M"), Ad("Hammer Coral", "User N")),
    "Tank" to listOf(Ad("40 Gallon Tank", "User O"), Ad("10g Betta Tank", "User P")),
    "Rehome" to listOf(
        Ad("Goldfish needs home", "User Q"),
        Ad("Betta Fish (Free)", "User R"),
        Ad("Guppies for Sale", "User A1"),
        Ad("Pleco needs new tank", "User A2"),
        Ad("Corydoras Catfish", "User A3"),
        Ad("Angelfish Pair", "User A4"),
        Ad("Discus Fish", "User A5"),
        Ad("German Blue Rams", "User A6"),
        Ad("Kribensis Cichlids", "User A7"),
        Ad("Electric Blue Acara", "User A8"),
        Ad("Red Cherry Shrimp", "User A9"),
        Ad("Amano Shrimp", "User A10"),
        Ad("Nerite Snails", "User A11"),
        Ad("Mystery Snails", "User A12"),
        Ad("Java Fern", "User A13"),
        Ad("Anubias Nana", "User A14"),
        Ad("Amazon Sword", "User A15"),
        Ad("Cryptocoryne Wendtii", "User A16"),
        Ad("Java Moss", "User A17"),
        Ad("Duckweed", "User A18"),
        Ad("Water Wisteria", "User A19"),
        Ad("Hornwort", "User A20"),
        Ad("Driftwood", "User A21"),
        Ad("Lava Rock", "User A22"),
        Ad("Seiryu Stone", "User A23"),
        Ad("Dragon Stone", "User A24"),
        Ad("Aquarium Gravel", "User A25"),
        Ad("Aquarium Sand", "User A26"),
        Ad("Fish Food", "User A27"),
        Ad("Water Conditioner", "User A28"),
        Ad("Fertilizer", "User A29"),
        Ad("CO2 System", "User A30"),
        Ad("LED Light", "User A31"),
        Ad("Canister Filter", "User A32"),
        Ad("Sponge Filter", "User A33"),
        Ad("Air Pump", "User A34"),
        Ad("Heater", "User A35"),
        Ad("Thermometer", "User A36"),
        Ad("Fish Net", "User A37"),
        Ad("Gravel Vacuum", "User A38")
    )
)

private val allSneakerAds = listOf(
    Ad("Jordan 1s", "User X"),
    Ad("Yeezy 350", "User Y"),
    Ad("New Balance 550", "User Z")
)
private val allElectronicsAds = listOf(
    Ad("Sony Headphones", "User AA"),
    Ad("Dell Monitor", "User BB"),
    Ad("GoPro Hero 8", "User CC")
)

sealed class ProfileScreenState {
    object Profile : ProfileScreenState()
    object Settings : ProfileScreenState()
    object Accessibility : ProfileScreenState()
    object DarkMode : ProfileScreenState()
    object EditProfile : ProfileScreenState()
    object AdDetail : ProfileScreenState()
}
private const val PREFS_NAME = "yardly_settings"
private const val KEY_DARK_MODE = "dark_mode_enabled"

class MainActivity : ComponentActivity() {
    private val sharedPreferences: SharedPreferences by lazy {
        getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
    }
    private lateinit var postStorage: PostStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postStorage = PostStorage(applicationContext)
        enableEdgeToEdge()
        val savedIsDarkMode = sharedPreferences.getBoolean(KEY_DARK_MODE, false)

        val insetsController = WindowCompat.getInsetsController(window, window.decorView)
        insetsController.isAppearanceLightStatusBars = !savedIsDarkMode

        setContent {
            var isDarkMode by remember { mutableStateOf(savedIsDarkMode) }
            val onDarkModeToggle: (Boolean) -> Unit = { enabled ->
                isDarkMode = enabled

                val insetsController = WindowCompat.getInsetsController(window, window.decorView)
                insetsController.isAppearanceLightStatusBars = !enabled

                with(sharedPreferences.edit()) {
                    putBoolean(KEY_DARK_MODE, enabled)
                    apply()
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
                    // *** NEW: Pass the image saving logic down ***
                    onSaveImagePermanently = ::saveImagePermanently
                )
            }
        }
    }

    // *** NEW HELPER: Persist image to internal storage to fix disappearance bug ***
    private fun saveImagePermanently(tempUri: Uri): Uri? {
        val contentResolver = applicationContext.contentResolver
        // Create a unique file name
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
    // *** NEW PARAMETER ***
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
    var profileScreenState by remember { mutableStateOf<ProfileScreenState>(ProfileScreenState.Profile) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }


    val cropImage = rememberLauncherForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            // For creating posts, we also want to persist the image
            // We can reuse the same logic passed in
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

    // --- PROFILE STATE ---
    var profileName by remember { mutableStateOf("Peyton Venzeee") }
    var profileUsername by remember { mutableStateOf("peyton") }
    var profileBio by remember { mutableStateOf("just another broke college student...") }
    // *** NEW: Profile Image State ***
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
        saveCounts[adName] = currentCount + 1
        savedItems[adName] = true
    }

    val dynamicAdList = remember(selectedNavSection) {
        when (selectedNavSection) {
            "home-default" -> defaultAds
            Category.Rehome.id -> allAquaSwapAds.values.flatten()
            Category.Sublease.id -> allSubleaseAds.values.flatten()
            Category.Textbook.id -> allTextbookAds
            Category.MovingOut.id -> allMovingOutAds
            Category.GarageSale.id -> allGarageSaleAds
            Category.Sneaker.id -> allSneakerAds
            Category.Electronics.id -> allElectronicsAds
            else -> defaultAds
        }
    } ?: defaultAds


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
                    // *** NEW: Pass profile image ***
                    profileImageUri = profileImageUri,

                    onSaveProfile = { newName, newUsername, newBio, newImageUri ->
                        profileName = newName
                        profileUsername = newUsername
                        profileBio = newBio
                        // Update the image if a new one was selected (otherwise keep old)
                        if (newImageUri != null) {
                            profileImageUri = newImageUri
                        }
                        profileScreenState = ProfileScreenState.Profile
                    },
                    // *** NEW: Pass persistence function ***
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
                            selectedIconSection = "profile"
                            profileScreenState = ProfileScreenState.AdDetail
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
                        profileScreenState = ProfileScreenState.AdDetail
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
                isControlsVisible
            }

            if (showHeaderAndNav) {
                SectionNavigation(
                    selectedSection = selectedNavSection,
                    onSectionSelected = { section ->
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
            // *** NEW: Pass Image ***
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
                selectedIconSection = "profile"
                profileScreenState = ProfileScreenState.AdDetail
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
                    text = "Notification",
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
                targetValue = if (isSelected) category.color else Color.Transparent,
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
        "home" to "Home",
        "messenger" to "Messenger",
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
    // *** NEW: Accept profile image ***
    profileImageUri: Uri?,
    onSaveProfile: (String, String, String, Uri?) -> Unit, // Updated signature
    onSaveImagePermanently: (Uri) -> Uri?, // New persistence func

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
                items(userPosts, key = { it.id }) { post ->
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

                items(ads) { ad ->
                    val saveCount = saveCounts.getOrDefault(ad.name, 0)
                    val isSaved = savedItems.getOrDefault(ad.name, false)
                    AdCard(
                        advertisementName = ad.name,
                        userName = ad.user,
                        saveCount = saveCount,
                        isSaved = isSaved,
                        onAdClick = { onAdClick(ad) },
                        onUserClick = onUserClick,
                        onSaveClick = { onSaveClick(ad.name) }
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
                userPosts = userPosts
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
                    if (selectedPost != null) {
                        AdDetailScreen(
                            title = selectedPost.title,
                            description = selectedPost.description + "\n\nPrice: " + selectedPost.price + "\nLocation: " + selectedPost.location,
                            isSaved = savedItems.getOrDefault(selectedPost.title, false),
                            saveCount = saveCounts.getOrDefault(selectedPost.title, 0),
                            onBackClick = onAdDetailBackClick,
                            onUserClick = { /* Stay on profile */ },
                            onSaveClick = { onSaveClick(selectedPost.title) },
                            onShareClick = { /* TODO */ }
                        )
                    } else {
                        Text("No post selected")
                    }
                }
            }
        }
        "messenger" -> {
            MessengerScreen(
                onBackClick = onBackClick
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

// --- (Previews) ---
@Preview(showBackground = true)
@Composable
fun YardlyAppPreview() {
    val context = androidx.compose.ui.platform.LocalContext.current
    YardlyTheme(isDarkMode = false) {
        YardlyApp(
            isDarkMode = false,
            onDarkModeToggle = {},
            postStorage = PostStorage(context),
            onSaveImagePermanently = { it }
        )
    }
}