package com.example.yardly.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yardly.ui.theme.Dimens
import com.example.yardly.ui.theme.YardlyTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class LoginStep {
    SELECTION,
    EMAIL_INPUT
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdLoginSheet(
    showModal: Boolean,
    onDismiss: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    // Internal state for the "Wizard of Oz" flow
    var currentStep by remember { mutableStateOf(LoginStep.SELECTION) }
    val scope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current

    // Form State
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }

    // Reset state when modal opens/closes
    LaunchedEffect(showModal) {
        if (showModal) {
            currentStep = LoginStep.SELECTION
            email = ""
            password = ""
            isLoading = false
        }
    }

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
                    .padding(Dimens.SpacingXXLarge)
                    .navigationBarsPadding() // Safety for bottom nav
                    .imePadding() // Push up for keyboard
            ) {

                // Header / Back Button Area
                Box(modifier = Modifier.fillMaxWidth()) {
                    if (currentStep == LoginStep.EMAIL_INPUT) {
                        IconButton(
                            onClick = { currentStep = LoginStep.SELECTION },
                            modifier = Modifier.align(Alignment.CenterStart)
                        ) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(Dimens.SpacingMedium))

                // Animated Content Switcher
                AnimatedContent(
                    targetState = currentStep,
                    transitionSpec = {
                        if (targetState == LoginStep.EMAIL_INPUT) {
                            slideInHorizontally { width -> width } + fadeIn() togetherWith
                                    slideOutHorizontally { width -> -width } + fadeOut()
                        } else {
                            slideInHorizontally { width -> -width } + fadeIn() togetherWith
                                    slideOutHorizontally { width -> width } + fadeOut()
                        }
                    },
                    label = "LoginFlow"
                ) { step ->
                    when (step) {
                        LoginStep.SELECTION -> {
                            SelectionView(
                                onEmailClick = { currentStep = LoginStep.EMAIL_INPUT },
                                onAppleClick = {
                                    // Mock instant success
                                    onLoginSuccess()
                                },
                                onGoogleClick = {
                                    // Mock instant success
                                    onLoginSuccess()
                                }
                            )
                        }
                        LoginStep.EMAIL_INPUT -> {
                            EmailInputView(
                                email = email,
                                onEmailChange = { email = it },
                                password = password,
                                onPasswordChange = { password = it },
                                passwordVisible = passwordVisible,
                                onVisibilityChange = { passwordVisible = !passwordVisible },
                                isLoading = isLoading,
                                onLoginClick = {
                                    keyboardController?.hide()
                                    scope.launch {
                                        isLoading = true
                                        delay(1000) // Fake network delay
                                        isLoading = false
                                        onLoginSuccess()
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SelectionView(
    onEmailClick: () -> Unit,
    onAppleClick: () -> Unit,
    onGoogleClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
        )

        Spacer(modifier = Modifier.height(Dimens.SpacingXLarge))

        Text(
            text = "Get Started",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(Dimens.SpacingXXXLarge))

        Button(
            onClick = onAppleClick,
            modifier = Modifier.fillMaxWidth().height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onBackground,
                contentColor = MaterialTheme.colorScheme.background
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Continue with Apple", fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(Dimens.SpacingMedium))

        OutlinedButton(
            onClick = onGoogleClick,
            modifier = Modifier.fillMaxWidth().height(48.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Continue with Google", fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(Dimens.SpacingMedium))

        Button(
            onClick = onEmailClick,
            modifier = Modifier.fillMaxWidth().height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Continue with Email", fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(Dimens.SpacingXLarge))

        Text(
            text = "By continuing, you agree to Yardly's Terms of Use",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            lineHeight = 16.sp
        )
    }
}

@Composable
fun EmailInputView(
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    passwordVisible: Boolean,
    onVisibilityChange: () -> Unit,
    isLoading: Boolean,
    onLoginClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Log In",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(Dimens.SpacingXXLarge))

        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(Dimens.SpacingMedium))

        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(onClick = onVisibilityChange) {
                    Icon(imageVector = image, contentDescription = "Toggle Password")
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = { onLoginClick() }),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(Dimens.SpacingXXXLarge))

        Button(
            onClick = onLoginClick,
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth().height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
            } else {
                Text("Log In", fontSize = 16.sp)
            }
        }
    }
}

@Preview
@Composable
fun AdLoginSheetPreview() {
    YardlyTheme(isDarkMode = false) {
        AdLoginSheet(showModal = true, onDismiss = {}, onLoginSuccess = {})
    }
}