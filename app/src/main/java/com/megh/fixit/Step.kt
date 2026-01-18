package com.megh.fixit

data class Step(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val order: Long = 0,
    val tip: String = "",     // Your DB has this useful field
    val type: String = "step" // "check" or "step"
)