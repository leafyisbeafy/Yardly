package com.example.yardly.ui.components

import android.content.Context
import com.example.yardly.UserPost
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.IOException

class PostStorage(private val context: Context) {

    private val postsFile = File(context.filesDir, "my_posts.json")

    // This function converts the list of posts to a JSON string and saves it.
    fun savePosts(posts: List<UserPost>) {
        try {
            val jsonString = Json.encodeToString(posts)
            postsFile.writeText(jsonString)
        } catch (e: IOException) {
            e.printStackTrace()
            // Handle error (e.g., show a toast)
        }
    }

    // This function reads the JSON file, converts it back to a list,
    // and returns it.
    fun loadPosts(): List<UserPost> {
        if (!postsFile.exists()) {
            return emptyList()
        }

        return try {
            val jsonString = postsFile.readText()
            Json.decodeFromString(jsonString)
        } catch (e: Exception) { // Catch both IOException and SerializationException
            e.printStackTrace()
            emptyList() // Return empty list if file is corrupt or unreadable
        }
    }
}