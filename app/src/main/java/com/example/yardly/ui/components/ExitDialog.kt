package com.example.yardly.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yardly.ui.theme.YardlyTheme // <-- *** THIS IS THE MISSING IMPORT ***

@Composable
fun ExitDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(16.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        titleContentColor = MaterialTheme.colorScheme.onSurface,
        textContentColor = MaterialTheme.colorScheme.onSurface,

        modifier = Modifier.width(200.dp), // Your width

        // Title
        title = {
            Text(
                text = "Log out of Yardly?", // Your title
                modifier = Modifier.fillMaxWidth(),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                fontSize = 20.sp // Font size fix
            )
        },

        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // "Log out" (Red) Button
                TextButton(
                    onClick = onDismiss
                ) {
                    Text(
                        text = "Log out",
                        color = MaterialTheme.colorScheme.error
                    )
                }

                // "Cancel" Button
                TextButton(
                    onClick = onDismiss
                ) {
                    Text(text = "Cancel")
                }
            }
        },

        confirmButton = {},
        dismissButton = {}
    )
}

@Preview
@Composable
fun ExitDialogPreview() {
    // This preview function needs the YardlyTheme import to work
    YardlyTheme(isDarkMode = false) {
        ExitDialog(onDismiss = {})
    }
}