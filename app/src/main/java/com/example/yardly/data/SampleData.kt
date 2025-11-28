package com.example.yardly.data

import com.example.yardly.data.model.UserPost

/**
 * Sample data for the Yardly marketplace.
 * Contains pre-populated posts for demonstration purposes.
 * Note: Category values must match the labels in Category.kt (e.g., "Moving Out", "Rescue", "Sublease", "Textbook")
 */
object SampleData {

    val systemPosts = listOf(
        UserPost(id = 1001L, title = "Air Force 1", price = "291.28", category = "Moving Out", description = "Good condition", location = "Campus", userName = "User 1"),
        UserPost(id = 1002L, title = "iPhone 13", price = "299.99", category = "Moving Out", description = "Unlocked", location = "Dorm A", userName = "User 2"),
        UserPost(id = 1003L, title = "PlayStation 4", price = "300.00", category = "Moving Out", description = "Comes with 2 controllers", location = "Northside", userName = "User 3"),
        UserPost(id = 1004L, title = "Macbook Air 13", price = "500.00", category = "Moving Out", description = "M1 Chip", location = "Library", userName = "User 4"),
        UserPost(id = 2001L, title = "Calculus Textbook", price = "45.00", category = "Textbook", description = "Calculus Early Transcendentals", location = "Library", userName = "User 7"),
        UserPost(id = 2002L, title = "Organic Chemistry", price = "60.00", category = "Textbook", description = "7th Edition", location = "Science Hall", userName = "User 12"),
        UserPost(id = 2003L, title = "Psych 101", price = "20.00", category = "Textbook", description = "Intro to Psychology", location = "East Hall", userName = "User 22"),
        UserPost(id = 3001L, title = "Golden Retriever Puppy", price = "Free", category = "Rescue", description = "Needs a loving home", location = "Southside", userName = "User 99"),
        UserPost(id = 3002L, title = "Cat for Adoption", price = "20.00", category = "Rescue", description = "Very friendly orange tabby", location = "East", userName = "User 33"),
        UserPost(id = 3003L, title = "Hamster Cage + Hamster", price = "Free", category = "Rescue", description = "Moving out, can't keep him", location = "West", userName = "User 41"),
        UserPost(id = 4001L, title = "Sublet: 1-Bed Room", price = "500.00", category = "Sublease", description = "Available for Summer", location = "Downtown", userName = "User 9"),
        UserPost(id = 4002L, title = "Luxury Apt Sublease", price = "800.00", category = "Sublease", description = "Aug-Dec", location = "The Lofts", userName = "User 44"),
        UserPost(id = 5001L, title = "Razer Gaming Chair", price = "222.00", category = "Moving Out", description = "Like new", location = "West", userName = "User 6"),
        UserPost(id = 5002L, title = "Mini Fridge (Black)", price = "50.00", category = "Moving Out", description = "Perfect for dorms", location = "Campus", userName = "User 8"),
        UserPost(id = 5003L, title = "IKEA Desk - White", price = "30.00", category = "Moving Out", description = "Sturdy desk", location = "Apts", userName = "User 9"),
        UserPost(id = 5004L, title = "Microwave", price = "25.00", category = "Moving Out", description = "Works great", location = "Dorm B", userName = "User 10")
    )
}

