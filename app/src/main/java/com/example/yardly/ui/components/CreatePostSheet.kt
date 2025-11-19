package com.example.yardly.ui.components

import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import coil.compose.AsyncImage
import com.example.yardly.ui.theme.Category
import com.example.yardly.ui.theme.Dimens
import com.example.yardly.ui.theme.YardlyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostSheet(
    showModal: Boolean,
    onDismiss: () -> Unit,
    onPostListing: (title: String, desc: String, category: String, location: String, price: String, imageUri: String?) -> Unit,
    imageUri: Uri?,
    onSelectImageClick: () -> Unit
) {
    // Internal state for the sheet's fields
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<Category>(Category.Electronics) }
    var location by remember { mutableStateOf("Central Campus, Your City") }

    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    if (showModal) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false),
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            containerColor = MaterialTheme.colorScheme.surface,
            scrimColor = Color.Black.copy(alpha = 0.4f),
            dragHandle = null
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.ScreenPaddingHorizontal, vertical = Dimens.SpacingXLarge)
                    .navigationBarsPadding()
            ) {
                // 1. Avatar / User Row (unchanged)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingLarge)
                ) {
                    Box(
                        modifier = Modifier
                            .size(Dimens.SpacingXXXLarge)
                            .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                    )
                    Text(
                        text = "Jordan Lee",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(Dimens.SpacingXLarge))

                // --- IMAGE DISPLAY SECTION ---
                if (imageUri != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(bottom = Dimens.SpacingXLarge)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        // Display the selected image using Coil
                        AsyncImage(
                            model = imageUri,
                            contentDescription = "Selected Post Image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
                // --- END IMAGE DISPLAY ---


                // 2. Title Field (unchanged)
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
                                "What's selling?",
                                fontSize = 20.sp,
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
                        textStyle = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                    IconButton(onClick = {
                        Toast.makeText(context, "Grok Suggestions: 'Mint Condition iPhone!'", Toast.LENGTH_SHORT).show()
                    }) {
                        Icon(Icons.Default.Lightbulb, contentDescription = "Grok Suggestions")
                    }
                }

                // 3. Description Field (unchanged)
                TextField(
                    value = description,
                    onValueChange = { description = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            "What's the description? Need help with Grok?",
                            fontSize = 14.sp
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

                // 4. Price Field (unchanged)
                TextField(
                    value = price,
                    onValueChange = { price = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            "What is the price?",
                            fontSize = 14.sp
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
                        keyboardType = KeyboardType.Number
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        keyboardController?.hide()
                    }),
                    textStyle = TextStyle(
                        fontSize = 14.sp
                    )
                )

                // ... (Category Selector Pills)

                // 5. Media Bar (Updated to trigger the image picker)
                Spacer(modifier = Modifier.height(Dimens.SpacingXLarge))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    // CRITICAL FIX: Photo Library button launches the picker
                    IconButton(onClick = onSelectImageClick) {
                        Icon(Icons.Default.PhotoLibrary, "Photo Library", tint = if (imageUri != null) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    IconButton(onClick = { Toast.makeText(context, "Video recording not yet implemented.", Toast.LENGTH_SHORT).show() }) {
                        Icon(Icons.Default.PlayCircle, "Video")
                    }
                    IconButton(onClick = { Toast.makeText(context, "Camera not yet implemented.", Toast.LENGTH_SHORT).show() }) {
                        Icon(Icons.Default.CameraAlt, "Camera")
                    }
                    IconButton(onClick = { Toast.makeText(context, "Location picker not yet implemented.", Toast.LENGTH_SHORT).show() }) {
                        Icon(Icons.Default.LocationOn, "Location")
                    }
                    IconButton(onClick = { Toast.makeText(context, "More options not yet implemented.", Toast.LENGTH_SHORT).show() }) {
                        Icon(Icons.Default.MoreVert, "More")
                    }
                }

                // 6. Geo / Map Placeholder (unchanged)
                Spacer(modifier = Modifier.height(Dimens.SpacingXLarge))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Map,
                            contentDescription = "Map Placeholder",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(60.dp)
                        )
                        Text(
                            text = location,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // 7. Post Button
                Spacer(modifier = Modifier.height(Dimens.SpacingXXLarge))
                Button(
                    onClick = {
                        if (title.isBlank() || price.isBlank()) {
                            Toast.makeText(context, "Title and Price are required!", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        // CRITICAL: Pass the image URI string to the callback
                        onPostListing(
                            title,
                            description,
                            selectedCategory.label,
                            location,
                            price,
                            imageUri?.toString()
                        )

                        // Clear fields and dismiss
                        title = ""
                        description = ""
                        price = ""
                        onDismiss()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    // Enable only if required fields are filled
                    enabled = title.isNotBlank() && price.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text("Create Listing")
                }
            }
        }
    }
}