package com.example.feldmantest.model

/*
open class Photo {
    var userId: Int = 0
    var id: Int = 0
    var title: String = ""
}*/

data class Photo(
    val userId: Int = 0,
    val id: Int = 0,
    val title: String = ""
) {
    companion object {
        val default = Photo()
    }
}