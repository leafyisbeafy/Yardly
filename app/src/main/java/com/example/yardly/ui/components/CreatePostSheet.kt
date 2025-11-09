package com.example.yardly.ui.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yardly.ui.theme.YardlyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostSheet(
    showModal: Boolean,
    onDismiss: () -> Unit,
    // --- *** CHANGE 1: Added 'price' to the callback *** ---
    onPostListing: (title: String, desc: String, category: String, location: String, price: String) -> Unit
) {
    // Internal state for the sheet's fields
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    // --- *** CHANGE 2: Add state for price *** ---
    var price by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Electronics") } // Mocked default as requested
    var location by remember { mutableStateOf("Central Campus, Your City") } // Mocked location

    // --- *** CHANGE 3: Removed 'isTitleInEditMode' state *** ---
    // var isTitleInEditMode by remember { mutableStateOf(true) }

    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    if (showModal) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false), // Standard sheet
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            containerColor = MaterialTheme.colorScheme.surface,
            scrimColor = Color.Black.copy(alpha = 0.4f),
            dragHandle = null
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
                    .navigationBarsPadding() // Add padding for nav bar
            ) {
                // 1. Avatar / User Row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                    )
                    Text(
                        text = "Jordan Lee", // Mocked user as requested
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // --- *** CHANGE 4: Simplified the Title/Desc/Price section *** ---

                // 2. Title Field
                Row(
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextField(
                        value = title,
                        onValueChange = { title = it },
                        modifier = Modifier.weight(1f),
                        placeholder = {
                            Text(
                                "What's selling?", // New placeholder
                                fontSize = 20.sp, // Larger font size
                                fontWeight = FontWeight.Medium,
                                fontStyle = FontStyle.Italic
                            )
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        ),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        // Removed keyboard action
                        textStyle = TextStyle(
                            fontSize = 20.sp, // Larger font size
                            fontWeight = FontWeight.Medium
                        )
                    )
                    // "Grok" Button
                    IconButton(onClick = {
                        Toast.makeText(context, "Grok Suggestions: 'Mint Condition iPhone!'", Toast.LENGTH_SHORT).show()
                    }) {
                        Icon(Icons.Default.Lightbulb, contentDescription = "Grok Suggestions")
                    }
                }

                // 3. Description Field
                TextField(
                    value = description,
                    onValueChange = { description = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            "What's the description? Need help with Grok?", // New placeholder
                            fontSize = 14.sp // 14sp
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    textStyle = TextStyle(
                        fontSize = 14.sp
                    )
                )

                // 4. Price Field (NEW)
                TextField(
                    value = price,
                    onValueChange = { price = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            "What is the price?", // New placeholder
                            fontSize = 14.sp // 14sp
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Number // Set keyboard for price
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        keyboardController?.hide()
                    }),
                    textStyle = TextStyle(
                        fontSize = 14.sp
                    )
                )

                // --- *** END OF CHANGE 4 *** ---


                // 5. Media Bar
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(Icons.Default.PhotoLibrary, "Photo Library")
                    }
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(Icons.Default.PlayCircle, "Video")
                    }
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(Icons.Default.CameraAlt, "Camera")
                    }
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(Icons.Default.LocationOn, "Location")
                    }
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(Icons.Default.MoreVert, "More")
                    }
                }

                // 6. Geo / Map Placeholder
                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp) // 200.dp height as requested
                        .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Map,
                            contentDescription = "Map Placeholder",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(80.dp)
                        )
                        Text(
                            text = location,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // 7. Post Button
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = {
                        // --- *** CHANGE 5: Pass new 'price' and clear it *** ---
                        // Hoist the state
                        onPostListing(title, description, category, location, price)

                        // Clear fields and dismiss
                        title = ""
                        description = ""
                        price = ""
                        // isTitleInEditMode = true (No longer needed)
                        onDismiss()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary, // Primary color
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text("Create Listing") // "Create Listing" text
                }
            }
        }
    }
}

// --- Previews ---

@Preview(showBackground = true)
@Composable
fun CreatePostSheetPreviewLight() {
    YardlyTheme(isDarkMode = false) {
        CreatePostSheet(
            showModal = true,
            onDismiss = {},
            onPostListing = {_,_,_,_,_ -> }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CreatePostSheetPreviewDark() {
    YardlyTheme(isDarkMode = true) {
        CreatePostSheet(
            showModal = true,
            onDismiss = {},
            onPostListing = {_,_,_,_,_ -> }
        )
    }
}