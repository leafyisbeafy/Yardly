package com.example.yardly

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun GoogleLogo(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val center = Offset(size.width / 2, size.height / 2)
        val radius = size.minDimension / 3
        val strokeWidth = radius / 4

        // Google colors
        val blueColor = Color(0xFF4285F4)
        val redColor = Color(0xFFEA4335)
        val yellowColor = Color(0xFFFBBC05)
        val greenColor = Color(0xFF34A853)

        // Draw the main circle (blue arc)
        drawArc(
            color = blueColor,
            startAngle = -45f,
            sweepAngle = 180f,
            useCenter = false,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
            topLeft = Offset(center.x - radius, center.y - radius),
            size = Size(radius * 2, radius * 2)
        )

        // Draw red arc (top right)
        drawArc(
            color = redColor,
            startAngle = 135f,
            sweepAngle = 90f,
            useCenter = false,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
            topLeft = Offset(center.x - radius, center.y - radius),
            size = Size(radius * 2, radius * 2)
        )

        // Draw yellow arc (top left)
        drawArc(
            color = yellowColor,
            startAngle = 225f,
            sweepAngle = 90f,
            useCenter = false,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
            topLeft = Offset(center.x - radius, center.y - radius),
            size = Size(radius * 2, radius * 2)
        )

        // Draw green arc (bottom left)
        drawArc(
            color = greenColor,
            startAngle = 315f,
            sweepAngle = 90f,
            useCenter = false,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
            topLeft = Offset(center.x - radius, center.y - radius),
            size = Size(radius * 2, radius * 2)
        )

        // Draw horizontal line (Google G opening)
        drawLine(
            color = blueColor,
            start = Offset(center.x, center.y),
            end = Offset(center.x + radius * 0.7f, center.y),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )

        // Draw small vertical line
        drawLine(
            color = blueColor,
            start = Offset(center.x + radius * 0.7f, center.y),
            end = Offset(center.x + radius * 0.7f, center.y - radius * 0.3f),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
    }
}

@Composable
fun DraggableChatHead(
    onChatClick: () -> Unit,
    modifier: Modifier = Modifier,
    topBoundary: Float = 0f,
    bottomBoundary: Float = Float.MAX_VALUE
) {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current

    // Screen dimensions in dp
    val screenWidthDp = configuration.screenWidthDp.dp
    val screenHeightDp = configuration.screenHeightDp.dp

    // Convert to pixels for calculations
    val screenWidthPx = with(density) { screenWidthDp.toPx() }
    val screenHeightPx = with(density) { screenHeightDp.toPx() }

    // Chat head size
    val chatHeadSize = 56.dp
    val chatHeadSizePx = with(density) { chatHeadSize.toPx() }

    // Calculate effective boundaries
    val effectiveBottomBoundary = if (bottomBoundary == Float.MAX_VALUE) screenHeightPx else bottomBoundary
    val effectiveTopBoundary = topBoundary

    // Initial position (bottom right corner with some padding within boundaries)
    val initialX = screenWidthPx - chatHeadSizePx - with(density) { 16.dp.toPx() }
    val initialY = (effectiveBottomBoundary - chatHeadSizePx - with(density) { 16.dp.toPx() }).coerceAtLeast(effectiveTopBoundary)

    var offsetX by remember { mutableStateOf(initialX) }
    var offsetY by remember { mutableStateOf(initialY) }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        FloatingActionButton(
            onClick = onChatClick,
            modifier = Modifier
                .size(chatHeadSize)
                .offset {
                    IntOffset(
                        x = offsetX.roundToInt(),
                        y = offsetY.roundToInt()
                    )
                }
                .shadow(8.dp, CircleShape)
                .pointerInput(Unit) {
                    detectDragGestures { change, _ ->
                        val newX = offsetX + change.position.x
                        val newY = offsetY + change.position.y

                        // Constrain to defined bounds
                        offsetX = newX.coerceIn(0f, screenWidthPx - chatHeadSizePx)
                        offsetY = newY.coerceIn(effectiveTopBoundary, effectiveBottomBoundary - chatHeadSizePx)
                    }
                },
            containerColor = Color.White,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            elevation = FloatingActionButtonDefaults.elevation(8.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Send,
                contentDescription = "Chat",
                modifier = Modifier.size(24.dp),
                tint = Color.White
            )
        }
    }
}

@Preview
@Composable
fun DraggableChatHeadPreview() {
    DraggableChatHead(
        onChatClick = {}
    )
}
@Composable
fun ChatOverlay(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (isVisible) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .fillMaxHeight(0.8f)
                    .clip(MaterialTheme.shapes.large),
                elevation = CardDefaults.cardElevation(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Messages",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        IconButton(onClick = onDismiss) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = MaterialTheme.colorScheme.outline
                    )

                    // Chat content
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Your conversations and messages will appear here",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Sample messages
                        MessageItem("John Doe", "Hey! Are you still interested in the pet adoption?")
                        Spacer(modifier = Modifier.height(8.dp))
                        MessageItem("Sarah Wilson", "Thanks for the lease swap info!")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun ChatOverlayPreview() {
    ChatOverlay(
        isVisible = true,
        onDismiss = {}
    )
}

@Composable
private fun MessageItem(sender: String, message: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = sender,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Preview
@Composable
private fun MessageItemPreview() {
    MessageItem(sender = "Jane Doe", message = "This is a preview message.")
}

