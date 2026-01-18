package com.megh.fixit

data class Guide(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val difficulty: String = "", // e.g. "beginner"
    val durationMin: Int = 0,
    val durationMax: Int = 0,
    val rating: Double = 0.0,
    val categoryId: String = ""
)