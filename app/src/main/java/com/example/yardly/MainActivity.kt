package com.example.yardly

import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.content.edit
import androidx.core.view.WindowCompat
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.yardly.data.repository.PostStorage
import com.example.yardly.ui.YardlyApp
import com.example.yardly.ui.theme.YardlyTheme
import java.io.File

const val PREFS_NAME = "yardly_settings"
const val KEY_DARK_MODE = "dark_mode_enabled"

class MainActivity : ComponentActivity() {
    private val sharedPreferences: SharedPreferences by lazy {
        getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
    }
    private lateinit var postStorage: PostStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postStorage = PostStorage(applicationContext)
        enableEdgeToEdge()
        val savedIsDarkMode = sharedPreferences.getBoolean(KEY_DARK_MODE, true)

        val insetsController = WindowCompat.getInsetsController(window, window.decorView)
        insetsController.isAppearanceLightStatusBars = !savedIsDarkMode

        setContent {
            var isDarkMode by remember { mutableStateOf(savedIsDarkMode) }
            val onDarkModeToggle: (Boolean) -> Unit = { enabled ->
                isDarkMode = enabled

                val insetsController = WindowCompat.getInsetsController(window, window.decorView)
                insetsController.isAppearanceLightStatusBars = !enabled

                sharedPreferences.edit {
                    putBoolean(KEY_DARK_MODE, enabled)
                }
            }

            YardlyTheme(
                isDarkMode = isDarkMode,
                dynamicColor = false
            ) {
                YardlyApp(
                    isDarkMode = isDarkMode,
                    onDarkModeToggle = onDarkModeToggle,
                    postStorage = postStorage,
                    onSaveImagePermanently = ::saveImagePermanently
                )
            }
        }
    }

    private fun saveImagePermanently(tempUri: Uri): Uri? {
        val contentResolver = applicationContext.contentResolver
        val fileName = "profile_image_${System.currentTimeMillis()}.jpg"
        val destinationFile = File(applicationContext.filesDir, fileName)

        return try {
            contentResolver.openInputStream(tempUri)?.use { inputStream ->
                destinationFile.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
            Uri.fromFile(destinationFile)
        } catch (e: Exception) {
            Log.e("MainActivity", "Failed to save image permanently", e)
            null
        }
    }
}

