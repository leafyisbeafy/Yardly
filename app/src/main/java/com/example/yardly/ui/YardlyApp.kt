package com.example.yardly.ui

import android.net.Uri
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.example.yardly.data.model.Ad
import com.example.yardly.data.model.UserPost
import com.example.yardly.data.repository.PostStorage
import com.example.yardly.ui.components.BottomIconNavigation
import com.example.yardly.ui.components.SectionNavigation
import com.example.yardly.ui.components.TopBar
import com.example.yardly.ui.navigation.ProfileScreenState
import com.example.yardly.ui.screens.home.ContentArea
import com.example.yardly.ui.sheets.AdDetailSheet
import com.example.yardly.ui.sheets.AdLoginSheet
import com.example.yardly.ui.sheets.ChooseCornerSheet
import com.example.yardly.ui.sheets.CreatePostSheet
import com.example.yardly.ui.sheets.ProfilePopup
import com.example.yardly.ui.theme.Dimens
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

    // --- LaunchedEffects ---
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

    // --- Navigation lambdas ---
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
                        showAdDetailSheet = true
                    }
                )

                // --- FABs ---
                FabMenu(
                    isFabMenuExpanded = isFabMenuExpanded,
                    isControlsVisible = isControlsVisible,
                    selectedIconSection = selectedIconSection,
                    onLocationClick = {
                        showChooseCornerSheet = true
                        isFabMenuExpanded = false
                    },
                    onCameraClick = {
                        if (!isLoggedIn) {
                            showAdLoginModal = true
                        } else {
                            showCreatePostSheet = true
                            isFabMenuExpanded = false
                        }
                    },
                    onToggleMenu = { isFabMenuExpanded = !isFabMenuExpanded },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(Dimens.SpacingXLarge)
                )
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

        // --- Modals ---
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

        AdDetailSheet(
            post = selectedPost,
            showModal = showAdDetailSheet,
            onDismiss = { showAdDetailSheet = false },
            onUserClick = {
                showAdDetailSheet = false
                showProfileSheet = true
            }
        )
    }
}

@Composable
private fun FabMenu(
    isFabMenuExpanded: Boolean,
    isControlsVisible: Boolean,
    selectedIconSection: String,
    onLocationClick: () -> Unit,
    onCameraClick: () -> Unit,
    onToggleMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(Dimens.SpacingXLarge)
    ) {
        AnimatedVisibility(
            visible = isFabMenuExpanded && isControlsVisible && selectedIconSection == "home",
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            FloatingActionButton(
                onClick = onLocationClick,
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
                onClick = onCameraClick,
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
                onClick = onToggleMenu,
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

