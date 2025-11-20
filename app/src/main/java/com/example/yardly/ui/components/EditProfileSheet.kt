package com.example.yardly.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.example.yardly.ui.theme.Dimens
import com.example.yardly.ui.theme.YardlyTheme

@Composable
fun EditProfileScreen(
    currentName: String,
    currentUsername: String,
    currentBio: String,
    // *** NEW: Accept current image URI ***
    currentImageUri: Uri?,
    onBackClick: () -> Unit,
    // *** UPDATED: Callback now includes Uri? ***
    onSaveClick: (name: String, username: String, bio: String, imageUri: Uri?) -> Unit,
    // *** NEW: Function to save image permanently ***
    onSaveImagePermanently: (Uri) -> Uri?
) {
    var name by remember { mutableStateOf(currentName) }
    var username by remember { mutableStateOf(currentUsername) }
    var bio by remember { mutableStateOf(currentBio) }

    // State to hold the final cropped image URI
    var selectedImageUri by remember { mutableStateOf<Uri?>(currentImageUri) }

    // 1. Define the Cropper Launcher
    val imageCropLauncher = rememberLauncherForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            val tempUri = result.uriContent
            // *** CRITICAL FIX: Persist the image immediately ***
            if (tempUri != null) {
                val savedUri = onSaveImagePermanently(tempUri)
                selectedImageUri = savedUri
            }
        } else {
            // Handle error (optional)
            val exception = result.error
        }
    }

    // 2. Define the Photo Picker Launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            // If user picked an image, launch the cropper immediately
            val cropOptions = CropImageContractOptions(
                uri,
                CropImageOptions(
                    imageSourceIncludeGallery = false, // Already picked from gallery
                    imageSourceIncludeCamera = false,
                    cropShape = CropImageView.CropShape.OVAL, // Force Circular Crop guide
                    fixAspectRatio = true, // Force 1:1 aspect ratio
                    aspectRatioX = 1,
                    aspectRatioY = 1
                )
            )
            imageCropLauncher.launch(cropOptions)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top Bar
        EditProfileTopBar(
            onBackClick = onBackClick,
            onDoneClick = { onSaveClick(name, username, bio, selectedImageUri) }
        )

        // Form Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = Dimens.ScreenPaddingHorizontal, vertical = Dimens.SpacingXLarge),
            verticalArrangement = Arrangement.spacedBy(Dimens.SpacingXLarge)
        ) {
            // Profile Picture Editor
            ProfilePictureSection(
                currentImageUri = selectedImageUri,
                onClick = {
                    // Trigger the Native Photo Picker
                    imagePickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }
            )

            // Name Field
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Name") },
                singleLine = true
            )

            // Username Field
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Username") },
                singleLine = true
            )

            // Bio Field
            OutlinedTextField(
                value = bio,
                onValueChange = { bio = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                label = { Text("Bio") }
            )
        }
    }
}

@Composable
fun ProfilePictureSection(
    currentImageUri: Uri?,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens.SpacingMedium)
    ) {
        // Big Circle (80dp) with Camera Icon or Selected Image
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            if (currentImageUri != null) {
                // Use Coil to display the selected, cropped image
                AsyncImage(
                    model = currentImageUri,
                    contentDescription = "Profile Picture",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                // Placeholder Camera Icon
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "Change Photo",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        // Centered Blue Text
        Text(
            text = "Change Photo",
            color = MaterialTheme.colorScheme.primary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

// --- Top Bar (Unchanged) ---
@Composable
private fun EditProfileTopBar(
    onBackClick: () -> Unit,
    onDoneClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = Dimens.ScreenPaddingHorizontal, vertical = Dimens.SpacingMedium),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowLeft,
                contentDescription = "Back",
                modifier = Modifier.size(28.dp)
            )
        }

        Text(
            text = "Edit profile",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        TextButton(onClick = onDoneClick) {
            Text(
                text = "Done",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditProfileScreenPreview() {
    YardlyTheme(isDarkMode = false) {
        EditProfileScreen(
            currentName = "Osh",
            currentUsername = "oshonik.xd",
            currentBio = "This is a bio.",
            currentImageUri = null,
            onBackClick = {},
            onSaveClick = {_,_,_,_ ->},
            onSaveImagePermanently = { it }
        )
    }
}