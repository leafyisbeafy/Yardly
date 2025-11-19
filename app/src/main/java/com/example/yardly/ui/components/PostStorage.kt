package com.example.yardly

import android.content.Context
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.IOException

// CRITICAL FIX: Configure Json instance once for safety
private val json = Json {
    // Allows old data (without imageUriString) to load without crashing
    ignoreUnknownKeys = true
    isLenient = true
    prettyPrint = true
}

class PostStorage(private val context: Context) {

    private val postsFile = File(context.filesDir, "my_posts.json")

    fun savePosts(posts: List<UserPost>) {
        try {
            val jsonString = json.encodeToString(posts) // Use configured json
            postsFile.writeText(jsonString)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun loadPosts(): List<UserPost> {
        if (!postsFile.exists()) {
            return emptyList()
        }

        return try {
            val jsonString = postsFile.readText()
            json.decodeFromString(jsonString) // Use configured json
        } catch (e: Exception) {
            e.printStackTrace()
            // Optional but recommended: delete corrupt file to prevent continuous errors
            postsFile.delete()
            emptyList()
        }
    }
}