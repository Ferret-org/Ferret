package com.ferret.app.model

const val ALL_CATEGORY = "All"

/** Static category filters shown in the top chip row. */
val categoryChips: List<CategoryChip> = listOf(
    CategoryChip(ALL_CATEGORY, "✨"),
    CategoryChip("Android", "🤖"),
    CategoryChip("Kotlin", "💜"),
    CategoryChip("Architecture", "🏗️"),
    CategoryChip("Testing", "🧪"),
    CategoryChip("Build", "🛠️"),
    CategoryChip("KMP", "🌐"),
)
