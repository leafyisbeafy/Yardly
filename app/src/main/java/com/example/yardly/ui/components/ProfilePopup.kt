package com.example.yardly.ui.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfilePopup(
    showModal: Boolean,
    onDismiss: () -> Unit,
    onBackClick: () -> Unit = {},
    onEditClick: () -> Unit = {},
    onMenuClick: () -> Unit = {}
) {
    if (showModal) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = rememberModalBottomSheetState(),
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            containerColor = Color.White,
            dragHandle = null
        ) {
            // This calls the shared UI from ProfileSheet.kt
            ProfileContent(
                onBackClick = onBackClick,
                onEditClick = onEditClick,
                onMenuClick = onMenuClick
            )
        }
    }
}