package com.megh.fixit

data class Category(
    val id: String = "",
    val name: String = "",
    val subtitle: String = "",
    val icon: String = "",
    val order: Long = 9999,
    val active: Boolean = false
)