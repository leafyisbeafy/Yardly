package com.example.yardly.data.model

import kotlinx.serialization.Serializable

/**
 * Data class representing a user's marketplace listing.
 * Used for both user-created posts and system sample posts.
 */
@Serializable
data class UserPost(
    val id: Long = System.currentTimeMillis(),
    val title: String,
    val description: String,
    val price: String,
    val category: String,
    val location: String,
    val userName: String = "Jordan Lee",
    val imageUris: List<String> = emptyList()
)

