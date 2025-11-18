package com.example.yardly.ui.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.yardly.UserPost
import com.example.yardly.ui.theme.YardlyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfilePopup(
    name: String,
    username: String,
    bio: String,
    userPosts: List<UserPost>, // <--- NEW: Accepts the list
    showModal: Boolean,
    onDismiss: () -> Unit,
    onBackClick: () -> Unit = {},
    onEditClick: () -> Unit = {},
    onMenuClick: () -> Unit = {},
    onDummyListingClick: () -> Unit,
    onSaveClick: (String) -> Unit // <--- NEW: Accepts save action
) {
    if (showModal) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = rememberModalBottomSheetState(),
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            containerColor = MaterialTheme.colorScheme.surface,
            scrimColor = Color.Black.copy(alpha = 0.4f),
            dragHandle = null
        ) {
            // Pass the data down to the actual UI content
            ProfileContent(
                name = name,
                username = username,
                bio = bio,
                userPosts = userPosts, // <--- Passing it down
                onBackClick = onBackClick,
                onEditClick = onEditClick,
                onMenuClick = onMenuClick,
                onDummyListingClick = onDummyListingClick,
                onSaveClick = onSaveClick // <--- Passing it down
            )
        }
    }
}

@Preview
@Composable
fun ProfilePopupPreview() {
    YardlyTheme(isDarkMode = false) {
        ProfilePopup(
            name = "Preview Name",
            username = "preview_user",
            bio = "This is a preview bio for the popup.",
            userPosts = emptyList(), // Preview with empty list
            showModal = true,
            onDismiss = {},
            onDummyListingClick = {},
            onSaveClick = {}
        )
    }
}