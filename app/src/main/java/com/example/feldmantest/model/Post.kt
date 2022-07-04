package com.example.feldmantest.model

data class Post(
    val userId: Int = 0,
    val id: Int = 0,
    val title: String = "",
    val body: String = "",
) {
    val comments: MutableList<Comment> = mutableListOf()
    var user: User? = null
}