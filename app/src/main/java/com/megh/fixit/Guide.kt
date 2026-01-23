package com.megh.fixit

data class Guide(
    val id: String,
    val name: String,        // Was 'title'
    val description: String, // Added this back
    val categoryId: String,
    val icon: String,
    val difficulty: String,
    val time: String         // Was 'durationMin'/'durationMax'
)