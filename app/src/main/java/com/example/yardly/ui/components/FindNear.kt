package com.example.yardly.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.example.yardly.ui.theme.Dimens
import com.example.yardly.ui.theme.YardlyTheme

@Composable
fun FindNear(
    isVisible: Boolean,
    onClick: () -> Unit, // <-- ADDED THIS PARAMETER
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = modifier // Apply the modifier to the wrapper
    ) {
        FloatingActionButton(
            onClick = onClick, // <-- USED IT HERE
            modifier = Modifier
                .padding(Dimens.SpacingXLarge), // *** FIXED PADDING ***
            shape = CircleShape,
            containerColor = MaterialTheme.colorScheme.primary, // Black
            contentColor = MaterialTheme.colorScheme.onPrimary // White
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Find Near")
        }
    }
}

@Preview(showBackground = true, name = "Visible")
@Composable
fun FindNearPreview() {
    YardlyTheme(isDarkMode = false) {
        FindNear(isVisible = true, onClick = {})
    }
}

@Preview(showBackground = true, name = "Dark Mode")
@Composable
fun FindNearDarkPreview() {
    YardlyTheme(isDarkMode = true) {
        FindNear(isVisible = true, onClick = {})
    }
}