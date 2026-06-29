package com.ferret.app.model

import androidx.compose.ui.graphics.Color


val sampleArticles = listOf(
    Article(
        1,
        "Mastering Jetpack Compose Animations",
        "Deep dive into AnimatedVisibility, animate*AsState, and Transition APIs",
        "Priya Nair",
        "Android",
        8,
        342,
        Color(0xFF6C63FF)
    ),
    Article(
        2,
        "Kotlin Coroutines in Production",
        "Handling complex async flows without callback hell",
        "Rohan Mehta",
        "Kotlin",
        12,
        215,
        Color(0xFF00BFA5)
    ),
    Article(
        3,
        "Clean Architecture with MVI",
        "Scalable patterns for large Android apps",
        "Anjali Singh",
        "Architecture",
        10,
        478,
        Color(0xFFFF6B6B)
    ),
    Article(
        4,
        "StateFlow vs SharedFlow",
        "When to use which reactive primitive in your ViewModel",
        "Karan Patel",
        "Kotlin",
        6,
        189,
        Color(0xFFFFA000)
    ),
    Article(
        5,
        "Compose Navigation Deep Dives",
        "Nested graphs, arguments, and deep links explained",
        "Meera Iyer",
        "Android",
        9,
        261,
        Color(0xFF26A69A)
    ),
    Article(
        6,
        "Hilt Dependency Injection Guide",
        "From @Inject to custom modules — a practical walkthrough",
        "Vikram Rao",
        "Architecture",
        11,
        394,
        Color(0xFFE91E63)
    ),
    Article(
        7,
        "Testing Composable Functions",
        "Unit, snapshot, and UI tests for Compose screens",
        "Deepa Krishnan",
        "Testing",
        7,
        302,
        Color(0xFF5C6BC0)
    ),
    Article(
        8,
        "Room Database Best Practices",
        "Entity relationships, migrations, and type converters",
        "Arjun Sharma",
        "Android",
        14,
        418,
        Color(0xFF43A047)
    ),
    Article(
        9,
        "Accompanist to Compose 1.x",
        "Migrating pager, permissions, and system UI controllers",
        "Neha Gupta",
        "Android",
        5,
        134,
        Color(0xFFFF7043)
    ),
    Article(
        10,
        "ProGuard & R8 Optimization",
        "Shrinking and obfuscating your release builds effectively",
        "Siddharth Kumar",
        "Build",
        8,
        221,
        Color(0xFF8D6E63)
    ),
)

val sampleChips = listOf(
    CategoryChip("All", "🗂️"),
    CategoryChip("Android", "🤖"),
    CategoryChip("Kotlin", "💜"),
    CategoryChip("Architecture", "🏗️"),
    CategoryChip("Testing", "🧪"),
)