package com.example.feldmantest.model

/*open class Comment {
    var postId: Int = 0
    var id: Int = 0
    var name: String = ""
    var email: String = ""
    var body: String = ""
}*/

data class Comment(
    val postId: Int = 0,
    val id: Int = 0,
    val name: String = "",
    val email: String = "",
    val body: String = ""
) {
    companion object {
        val default = Comment()
    }
}


