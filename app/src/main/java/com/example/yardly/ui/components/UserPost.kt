package com.example.yardly

import kotlinx.serialization.Serializable

// 1. We define the UserPost data class
// 2. @Serializable allows it to be converted to/from JSON
@Serializable
data class UserPost(
    val id: Long = System.currentTimeMillis(), // Unique ID for list stability
    val title: String,
    val description: String,
    val price: String,
    val category: String,
    val location: String,
    val userName: String = "Jordan Lee" // Mocked user from the sheet
)