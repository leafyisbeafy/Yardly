package com.example.yardly.data

import com.example.yardly.data.model.UserPost

/**
 * Sample data for the Yardly marketplace.
 * Contains pre-populated posts for demonstration purposes.
 * Note: Category values must match the labels in Category.kt (e.g., "Moving Out", "Rescue", "Sublease", "Textbook")
 */
object SampleData {

    val systemPosts = listOf(
        UserPost(
            id = 1001L,
            title = "Air Force 1",
            price = "291.28",
            category = "Moving Out",
            description = "Good condition, barely worn. Size 10.",
            location = "Campus",
            userName = "Jordan Lee",
            userProfileImageUri = "https://randomuser.me/api/portraits/men/32.jpg",
            imageUris = listOf("https://picsum.photos/seed/shoe1/400/400", "https://picsum.photos/seed/shoe2/400/400")
        ),
        UserPost(
            id = 1002L,
            title = "iPhone 13",
            price = "299.99",
            category = "Moving Out",
            description = "Unlocked, 128GB. Battery health 90%.",
            location = "Dorm A",
            userName = "Sarah Chen",
            userProfileImageUri = "https://randomuser.me/api/portraits/women/44.jpg",
            imageUris = listOf("https://picsum.photos/seed/iphone1/400/400", "https://picsum.photos/seed/iphone2/400/400", "https://picsum.photos/seed/iphone3/400/400")
        ),
        UserPost(
            id = 1003L,
            title = "PlayStation 4",
            price = "300.00",
            category = "Moving Out",
            description = "Comes with 2 controllers and 3 games.",
            location = "Northside",
            userName = "Mike Ross",
            userProfileImageUri = "https://randomuser.me/api/portraits/men/85.jpg",
            imageUris = listOf("https://picsum.photos/seed/ps4/600/400", "https://picsum.photos/seed/ps4controller/400/400") // Wide then Square
        ),
        UserPost(
            id = 1004L,
            title = "Macbook Air 13",
            price = "500.00",
            category = "Moving Out",
            description = "M1 Chip, 8GB RAM, 256GB SSD. Space Gray.",
            location = "Library",
            userName = "Emily Blunt",
            userProfileImageUri = "https://randomuser.me/api/portraits/women/68.jpg",
            imageUris = listOf("https://picsum.photos/seed/macbook/500/500")
        ),
        UserPost(
            id = 2001L,
            title = "Calculus Textbook",
            price = "45.00",
            category = "Textbook",
            description = "Calculus Early Transcendentals, 8th Edition.",
            location = "Library",
            userName = "David Kim",
            imageUris = listOf("https://picsum.photos/seed/book1/400/600") // Portrait
        ),
        UserPost(id = 2002L, title = "Organic Chemistry", price = "60.00", category = "Textbook", description = "7th Edition", location = "Science Hall", userName = "User 12"),
        UserPost(id = 2003L, title = "Psych 101", price = "20.00", category = "Textbook", description = "Intro to Psychology", location = "East Hall", userName = "User 22"),
        UserPost(id = 3001L, title = "Golden Retriever Puppy", price = "Free", category = "Rescue", description = "Needs a loving home. 8 weeks old.", location = "Southside", userName = "User 99", imageUris = listOf("https://picsum.photos/seed/puppy/500/500")),
        UserPost(id = 3002L, title = "Cat for Adoption", price = "20.00", category = "Rescue", description = "Very friendly orange tabby", location = "East", userName = "User 33"),
        UserPost(id = 3003L, title = "Hamster Cage + Hamster", price = "Free", category = "Rescue", description = "Moving out, can't keep him", location = "West", userName = "User 41"),
        UserPost(id = 4001L, title = "Sublet: 1-Bed Room", price = "500.00", category = "Sublease", description = "Available for Summer. Fully furnished.", location = "Downtown", userName = "User 9", imageUris = listOf("https://picsum.photos/seed/room/800/600")),
        UserPost(id = 4002L, title = "Luxury Apt Sublease", price = "800.00", category = "Sublease", description = "Aug-Dec. Pool and Gym included.", location = "The Lofts", userName = "User 44", imageUris = listOf("https://picsum.photos/seed/apt/800/600", "https://picsum.photos/seed/pool/800/600")),
        UserPost(id = 5001L, title = "Razer Gaming Chair", price = "222.00", category = "Moving Out", description = "Like new", location = "West", userName = "User 6"),
        UserPost(id = 5002L, title = "Mini Fridge (Black)", price = "50.00", category = "Moving Out", description = "Perfect for dorms", location = "Campus", userName = "User 8"),
        UserPost(id = 5003L, title = "IKEA Desk - White", price = "30.00", category = "Moving Out", description = "Sturdy desk", location = "Apts", userName = "User 9"),
        UserPost(id = 5004L, title = "Microwave", price = "25.00", category = "Moving Out", description = "Works great", location = "Dorm B", userName = "User 10")
    )
}

