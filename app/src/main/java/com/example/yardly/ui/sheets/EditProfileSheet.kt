package com.example.yardly.ui.sheets

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

/**
 * Full-screen edit profile form with image cropping support.
 */
@Composable
fun EditProfileScreen(
    currentName: String,
    currentUsername: String,
    currentBio: String,
    currentImageUri: Uri?,
    onBackClick: () -> Unit,
    onSaveClick: (name: String, username: String, bio: String, imageUri: Uri?) -> Unit,
    onSaveImagePermanently: (Uri) -> Uri?
) {
    var name by remember { mutableStateOf(currentName) }
    var username by remember { mutableStateOf(currentUsername) }
    var bio by remember { mutableStateOf(currentBio) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(currentImageUri) }

    val imageCropLauncher = rememberLauncherForActivityResult(CropImageContract()) { result: CropImageView.CropResult ->
        if (result.isSuccessful) {
            result.uriContent?.let { tempUri ->
                selectedImageUri = onSaveImagePermanently(tempUri)
            }
        }
    }

    val imagePickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        uri?.let {
            imageCropLauncher.launch(CropImageContractOptions(it, CropImageOptions(
                imageSourceIncludeGallery = false, imageSourceIncludeCamera = false,
                cropShape = CropImageView.CropShape.OVAL, fixAspectRatio = true, aspectRatioX = 1, aspectRatioY = 1
            )))
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        EditProfileTopBar(onBackClick = onBackClick, onDoneClick = { onSaveClick(name, username, bio, selectedImageUri) })

        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = Dimens.ScreenPaddingHorizontal, vertical = Dimens.SpacingXLarge),
            verticalArrangement = Arrangement.spacedBy(Dimens.SpacingXLarge)
        ) {
            ProfilePictureSection(currentImageUri = selectedImageUri, onClick = {
                imagePickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            })
            OutlinedTextField(value = name, onValueChange = { name = it }, modifier = Modifier.fillMaxWidth(), label = { Text("Name") }, singleLine = true)
            OutlinedTextField(value = username, onValueChange = { username = it }, modifier = Modifier.fillMaxWidth(), label = { Text("Username") }, singleLine = true)
            OutlinedTextField(value = bio, onValueChange = { bio = it }, modifier = Modifier.fillMaxWidth().height(150.dp), label = { Text("Bio") })
        }
    }
}

@Composable
fun ProfilePictureSection(currentImageUri: Uri?, onClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(Dimens.SpacingMedium)) {
        Box(modifier = Modifier.size(80.dp).clip(CircleShape).background(MaterialTheme.colorScheme.surfaceVariant).clickable { onClick() }, contentAlignment = Alignment.Center) {
            if (currentImageUri != null) {
                AsyncImage(model = currentImageUri, contentDescription = "Profile Picture", modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
            } else {
                Icon(Icons.Default.CameraAlt, "Change Photo", tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(32.dp))
            }
        }
        Text("Change Photo", color = MaterialTheme.colorScheme.primary, fontSize = 16.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun EditProfileTopBar(onBackClick: () -> Unit, onDoneClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().statusBarsPadding().padding(horizontal = Dimens.ScreenPaddingHorizontal, vertical = Dimens.SpacingMedium),
        horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) { Icon(Icons.Default.KeyboardArrowLeft, "Back", Modifier.size(28.dp)) }
        Text("Edit profile", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        TextButton(onClick = onDoneClick) { Text("Done", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary) }
    }
}

@Preview(showBackground = true, name = "Light Mode")
@Composable
fun EditProfileScreenPreview() {
    YardlyTheme(isDarkMode = false) {
        EditProfileScreen(currentName = "Osh", currentUsername = "oshonik.xd", currentBio = "This is a bio.", currentImageUri = null, onBackClick = {}, onSaveClick = { _, _, _, _ -> }, onSaveImagePermanently = { it })
    }
}