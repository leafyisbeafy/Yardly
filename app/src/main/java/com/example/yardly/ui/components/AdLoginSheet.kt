package com.example.yardly.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yardly.ui.theme.Dimens
import com.example.yardly.ui.theme.YardlyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdLoginSheet(
    showModal: Boolean,
    onDismiss: () -> Unit,
    onAppleClick: () -> Unit = {},
    onGoogleClick: () -> Unit = {},
    onEmailClick: () -> Unit = {}
) {
    if (showModal) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = rememberModalBottomSheetState(),
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            containerColor = MaterialTheme.colorScheme.surface,
            scrimColor = Color.Black.copy(alpha = 0.4f), // <-- ADDED
            dragHandle = null
        ) {
            // ... (Rest of the file is unchanged)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimens.SpacingXXLarge), // *** FIXED PADDING ***
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Circle placeholder
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = CircleShape
                        )
                )

                Spacer(modifier = Modifier.height(Dimens.SpacingXLarge)) // *** FIXED PADDING ***

                // Get Started text
                Text(
                    text = "Get Started",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(Dimens.SpacingXXXLarge)) // *** FIXED PADDING ***

                // Continue with Apple button
                Button(
                    onClick = onAppleClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.onBackground, // Black/White
                        contentColor = MaterialTheme.colorScheme.background // White/Black
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Continue with Apple",
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.height(Dimens.SpacingMedium)) // *** FIXED PADDING ***

                // Continue with Google button
                OutlinedButton(
                    onClick = onGoogleClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Continue with Google",
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.height(Dimens.SpacingMedium)) // *** FIXED PADDING ***

                // Continue with Email button
                Button(
                    onClick = onEmailClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Continue with Email",
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.height(Dimens.SpacingXLarge)) // *** FIXED PADDING ***

                // Terms footer
                Text(
                    text = "By continuing, you agree to Yardly's Terms of Use",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    lineHeight = 16.sp
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun AdLoginSheetPreview() {
    YardlyTheme(isDarkMode = true) {
        AdLoginSheet(showModal = true, onDismiss = {})
    }
}