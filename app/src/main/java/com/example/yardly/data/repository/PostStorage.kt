package com.example.yardly.data.repository

import android.content.Context
import com.example.yardly.data.model.UserPost
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Repository class for persisting user posts to SharedPreferences.
 * Handles saving and loading of user-created marketplace listings.
 */
class PostStorage(context: Context) {
    
    companion object {
        private const val PREFS_NAME = "yardly_posts"
        private const val KEY_POSTS = "user_posts"
    }
    
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val jsonParser = Json { ignoreUnknownKeys = true }

    fun savePosts(posts: List<UserPost>) {
        val json = jsonParser.encodeToString(posts)
        prefs.edit().putString(KEY_POSTS, json).apply()
    }

    fun loadPosts(): List<UserPost> {
        val json = prefs.getString(KEY_POSTS, null) ?: return emptyList()
        return try {
            jsonParser.decodeFromString(json)
        } catch (e: Exception) {
            emptyList()
        }
    }
}

