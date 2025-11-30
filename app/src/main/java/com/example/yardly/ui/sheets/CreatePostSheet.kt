package com.example.yardly.ui.sheets

import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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

/**
 * Bottom sheet for creating a new marketplace listing.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostSheet(
    userName: String,
    showModal: Boolean,
    onDismiss: () -> Unit,
    onPostListing: (title: String, desc: String, category: String, location: String, price: String, imageUris: List<String>) -> Unit,
    imageUris: List<Uri>,
    onSelectImageClick: () -> Unit,
    onRemoveImage: (Uri) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<Category>(Category.Rehome) }
    var location by remember { mutableStateOf("Central Campus, Your City") }

    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    if (showModal) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
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
                // User Row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingLarge)
                ) {
                    Box(
                        modifier = Modifier
                            .size(Dimens.SpacingXXXLarge)
                            .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                    )
                    Text(text = userName, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
                }

                Spacer(modifier = Modifier.height(Dimens.SpacingXLarge))

                // Image Display (Horizontal Scroll)
                if (imageUris.isNotEmpty() || imageUris.size < 6) {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingMedium),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .padding(bottom = Dimens.SpacingXLarge)
                    ) {
                        // Display Selected Images
                        items(imageUris) { uri ->
                            Box(
                                modifier = Modifier
                                    .size(120.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                            ) {
                                AsyncImage(
                                    model = uri,
                                    contentDescription = "Selected Post Image",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                                // Remove Button (X)
                                IconButton(
                                    onClick = { onRemoveImage(uri) },
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(4.dp)
                                        .size(24.dp)
                                        .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Remove Image",
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }

                        // Add Image Button (if less than 6)
                        if (imageUris.size < 6) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .size(120.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(MaterialTheme.colorScheme.surfaceVariant)
                                        .clickable { onSelectImageClick() },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Icon(
                                            imageVector = Icons.Default.Add,
                                            contentDescription = "Add Image",
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.size(32.dp)
                                        )
                                        Text(
                                            text = "${imageUris.size}/6",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    }
                } else {
                    // Initial State: Big "Add Photo" Box if no images
                     Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(bottom = Dimens.SpacingXLarge)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .clickable { onSelectImageClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.AddPhotoAlternate, "Add Photo", tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(48.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Add Photos (Max 6)", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }

                // Title Field
                Row(verticalAlignment = Alignment.Top, modifier = Modifier.fillMaxWidth()) {
                    TextField(
                        value = title, onValueChange = { title = it }, modifier = Modifier.weight(1f),
                        placeholder = { Text("What's selling?", fontSize = 20.sp, fontWeight = FontWeight.Medium) },
                        colors = TextFieldDefaults.colors(focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent, disabledContainerColor = Color.Transparent, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent, disabledIndicatorColor = Color.Transparent),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        textStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Medium)
                    )
                    IconButton(onClick = { Toast.makeText(context, "Grok Suggestions: 'Mint Condition iPhone!'", Toast.LENGTH_SHORT).show() }) {
                        Icon(Icons.Default.Lightbulb, contentDescription = "Grok Suggestions")
                    }
                }

                // Description Field
                TextField(
                    value = description, onValueChange = { description = it }, modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("What's the description? Need help with Grok?", fontSize = 14.sp) },
                    colors = TextFieldDefaults.colors(focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent, disabledContainerColor = Color.Transparent, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent, disabledIndicatorColor = Color.Transparent),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    textStyle = TextStyle(fontSize = 14.sp)
                )

                // Price Field
                TextField(
                    value = price, onValueChange = { price = it }, modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("What is the price?", fontSize = 14.sp) },
                    colors = TextFieldDefaults.colors(focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent, disabledContainerColor = Color.Transparent, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent, disabledIndicatorColor = Color.Transparent),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done, keyboardType = KeyboardType.Number),
                    keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
                    textStyle = TextStyle(fontSize = 14.sp)
                )

                // Category Selector
                Spacer(modifier = Modifier.height(Dimens.SpacingXLarge))
                Text("Category", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.height(Dimens.SpacingSmall))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingSmall)) {
                    items(Category.all) { category ->
                        val isSelected = selectedCategory == category
                        val backgroundColor by animateColorAsState(if (isSelected) category.color.copy(alpha = 0.1f) else Color.Transparent, label = "bg")
                        val borderColor by animateColorAsState(if (isSelected) category.color else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f), label = "border")
                        val textColor by animateColorAsState(if (isSelected) category.color else MaterialTheme.colorScheme.onSurface, label = "text")
                        Surface(shape = RoundedCornerShape(8.dp), border = BorderStroke(1.dp, borderColor), color = backgroundColor, modifier = Modifier.clickable { selectedCategory = category }) {
                            Text(category.label, style = MaterialTheme.typography.labelMedium, color = textColor, modifier = Modifier.padding(horizontal = Dimens.SpacingMedium, vertical = Dimens.SpacingSmall))
                        }
                    }
                }

                // Media Bar (Simplified as we have the main image area)
                Spacer(modifier = Modifier.height(Dimens.SpacingXLarge))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                    IconButton(onClick = onSelectImageClick) { Icon(Icons.Default.PhotoLibrary, "Photo Library", tint = if (imageUris.isNotEmpty()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant) }
                    IconButton(onClick = { Toast.makeText(context, "Video recording not yet implemented.", Toast.LENGTH_SHORT).show() }) { Icon(Icons.Default.PlayCircle, "Video") }
                    IconButton(onClick = { Toast.makeText(context, "Camera not yet implemented.", Toast.LENGTH_SHORT).show() }) { Icon(Icons.Default.CameraAlt, "Camera") }
                    IconButton(onClick = { Toast.makeText(context, "Location picker not yet implemented.", Toast.LENGTH_SHORT).show() }) { Icon(Icons.Default.LocationOn, "Location") }
                    IconButton(onClick = { Toast.makeText(context, "More options not yet implemented.", Toast.LENGTH_SHORT).show() }) { Icon(Icons.Default.MoreVert, "More") }
                }

                // Map Placeholder
                Spacer(modifier = Modifier.height(Dimens.SpacingXLarge))
                Box(
                    modifier = Modifier.fillMaxWidth().height(150.dp).background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Map, "Map Placeholder", tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(60.dp))
                        Text(location, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }

                // Post Button
                Spacer(modifier = Modifier.height(Dimens.SpacingXXLarge))
                Button(
                    onClick = {
                        if (title.isBlank() || price.isBlank()) {
                            Toast.makeText(context, "Title and Price are required!", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        // Sort images: Square (1:1) images first
                        val sortedUris = imageUris.sortedByDescending { uri ->
                            isSquareImage(context, uri)
                        }

                        onPostListing(title, description, selectedCategory.label, location, price, sortedUris.map { it.toString() })
                        title = ""; description = ""; price = ""
                        onDismiss()
                    },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    enabled = title.isNotBlank() && price.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary, contentColor = MaterialTheme.colorScheme.onPrimary)
                ) { Text("Create Listing") }
            }
        }
    }
}

private fun isSquareImage(context: android.content.Context, uri: Uri): Boolean {
    return try {
        val options = android.graphics.BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            android.graphics.BitmapFactory.decodeStream(inputStream, null, options)
        }
        val width = options.outWidth
        val height = options.outHeight
        if (width > 0 && height > 0) {
            val ratio = width.toFloat() / height.toFloat()
            ratio in 0.95f..1.05f // Tolerance for "square"
        } else {
            false
        }
    } catch (e: Exception) {
        false
    }
}

@Preview(showBackground = true, name = "Light Mode")
@Composable
fun CreatePostSheetPreview() {
    YardlyTheme(isDarkMode = false) {
        CreatePostSheet(
            userName = "Preview User",
            showModal = true,
            onDismiss = {},
            onPostListing = { _, _, _, _, _, _ -> },
            imageUris = listOf(Uri.parse("http://example.com/1"), Uri.parse("http://example.com/2")),
            onSelectImageClick = {},
            onRemoveImage = {}
        )
    }
}