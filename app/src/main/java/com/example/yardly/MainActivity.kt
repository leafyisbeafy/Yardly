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
import androidx.core.content.edit // Added this import
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.example.yardly.ui.components.*
import com.example.yardly.ui.theme.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

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
        Ad("Sublet: 1-Bed Room Downtown", "User A"),
        Ad("Shared Room Near Campus (Summer)", "User B"),
        Ad("Furnished Master Bedroom w/ Private Bath", "SubletPro1"),
        Ad("Short-term Room for May-July", "StudentSublet2"),
        Ad("Quiet Room in 3-Bed Apartment", "GradStudent3"),
        Ad("Loft-style Room Available Immediately", "UrbanDweller4"),
        Ad("Basement Room with Separate Entrance", "Homeowner5")
    ),
    "Apartment" to listOf(
        Ad("1-Bedroom Apartment Sublet (Full)", "LeaseHolder6"),
        Ad("Studio Apartment Sublease - 6 months", "CityLiving7"),
        Ad("2-Bed, 2-Bath Condo Sublet", "CondoOwner8"),
        Ad("Modern Downtown Loft Sublease", "LoftLover9"),
        Ad("Pet-Friendly 1-Bed Apt Sublet", "PetFriendly10")
    ),
    "Parking" to listOf(
        Ad("Underground Parking Spot Near University", "Parker11"),
        Ad("Covered Parking Spot for Rent", "Commuter12"),
        Ad("Driveway Parking Space (Monthly)", "Homeowner13")
    )
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
    Ad("Moving Sale: Everything Must Go!", "User M"),
    Ad("L-Shaped Sectional Couch (Grey)", "User N"),
    Ad("Solid Wood Dining Table w/ 4 Chairs", "User O"),
    Ad("Queen Bed Frame and Mattress", "User P"),
    Ad("IKEA Kallax Shelving Unit (White)", "StudentSeller1"),
    Ad("Standing Desk (Electric)", "WFH_Professional2"),
    Ad("Samsung 55\" 4K Smart TV", "Techie3"),
    Ad("Mini Fridge - Perfect for Dorms", "DormDweller4"),
    Ad("Bookshelf (5 shelves, dark wood)", "Bookworm5"),
    Ad("Microwave and Toaster Oven Bundle", "KitchenSeller6"),
    Ad("Dresser with 6 Drawers", "FurnitureFlipper7"),
    Ad("Set of 4 Bar Stools", "Homeowner8"),
    Ad("LG Washer and Dryer Set", "ApplianceSeller9"),
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
    Ad("Garden Tools", "User M38"),
    Ad("Full Kitchenware Set (Pots, Pans, Utensils)", "GraduatingSenior10"),
    Ad("Floor Lamp with Adjustable Head", "LightingNeeds11")
)

private val allAquaSwapAds = mapOf(
    "Equipment" to listOf(
        Ad("Used 50g Filter", "User K"),
        Ad("Heater", "User L"),
        Ad("AI Prime 16HD Reef Light", "ReefTanker1"),
        Ad("Fluval FX6 Canister Filter", "CichlidFan2"),
        Ad("RO/DI Unit - 4 Stage", "PureWater3"),
        Ad("Eheim Jager Heater 150W", "HeaterSeller4"),
        Ad("Hanna Salinity Checker", "SaltyDog5"),
        Ad("BRS 2-Part Doser", "CalciumPro6")
    ),
    "Coral" to listOf(
        Ad("Zoanthid Frag", "User M"),
        Ad("Hammer Coral", "User N"),
        Ad("GSP Frag (Green Star Polyp)", "CoralGrower7"),
        Ad("Torch Coral (Indo Gold)", "LPSLover8"),
        Ad("Acan Lord Colony", "AcanKing9"),
        Ad("Montipora Digitata Frag", "SPSFreak10"),
        Ad("Mushroom Coral Rock", "SoftieFan11")
    ),
    "Tank" to listOf(
        Ad("40 Gallon Tank", "User O"),
        Ad("10g Betta Tank", "User P"),
        Ad("Waterbox 20 Gallon Cube", "NanoReefer12"),
        Ad("75 Gallon with Stand & Sump", "BigTankBob13"),
        Ad("Fluval Spec V Shrimp Tank", "ShrimpKeeper14")
    ),
    "Rehome" to listOf(
        Ad("Goldfish needs home", "User Q"),
        Ad("Betta Fish (Free)", "User R"),
        Ad("Pair of Clownfish", "NemoFinder15"),
        Ad("Bristlenose Pleco", "AlgaeEater16"),
        Ad("Cherry Shrimp Colony (20+)", "ShrimpBreeder17"),
        Ad("Pea Puffer Group (x5)", "PufferPal18")
    ),
    "Plants & Decor" to listOf(
        Ad("Anubias Nana Petite", "PlantScaper19"),
        Ad("Java Fern on Driftwood", "LowTechTank20"),
        Ad("Dragon Stone Aquascape Rocks (10lbs)", "IwagumiLife21"),
        Ad("Bag of CaribSea Sand", "SubstrateSwap22"),
        Ad("Bucephalandra Pack", "RarePlantGuy23")
    )
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
        val savedIsDarkMode = sharedPreferences.getBoolean(KEY_DARK_MODE, false)

        val insetsController = WindowCompat.getInsetsController(window, window.decorView)
        insetsController.isAppearanceLightStatusBars = !savedIsDarkMode

        setContent {
            var isDarkMode by remember { mutableStateOf(savedIsDarkMode) }
            val onDarkModeToggle: (Boolean) -> Unit = { enabled ->
                isDarkMode = enabled

                val insetsController = WindowCompat.getInsetsController(window, window.decorView)
                insetsController.isAppearanceLightStatusBars = !enabled

                sharedPreferences.edit { // Modified line
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
    var suppressNavShow by remember { mutableStateOf(false) }

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

    LaunchedEffect(selectedNavSection) {
        if (selectedIconSection == "home") {
            gridState.scrollToItem(0)
        }
    }

    LaunchedEffect(isControlsVisible) {
        if (isControlsVisible) {
            if (isControlsVisible) {
                suppressNavShow = false
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
            else -> defaultAds
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
                    // selectedNavSection = selectedNavSection, // Removed this parameter
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
                isControlsVisible && !suppressNavShow
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
            .navigationBarsPadding()              .padding(vertical = Dimens.SpacingLarge, horizontal = Dimens.SpacingMedium),
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
    // selectedNavSection: String, // Removed this parameter
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

                items(ads) {
 ad ->
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