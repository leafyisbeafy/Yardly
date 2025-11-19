package com.example.yardly

import kotlinx.serialization.Serializable

@Serializable
data class UserPost(
    val id: Long = System.currentTimeMillis(),
    val title: String,
    val description: String,
    val price: String,
    val category: String,
    val location: String,
    val userName: String = "Jordan Lee",
    // CRITICAL FIX: Add field to store the image reference
    val imageUriString: String? = null
)