package com.example.feldmantest.network

import com.example.feldmantest.model.*
import retrofit2.http.*

interface AppApi {

    @GET(WebService.COMMENT)
    suspend fun getCommentsList(): List<Comment>

    @GET(WebService.ALBUM)
    suspend fun getAlbumsList(): List<Album>

    @GET(WebService.PHOTO)
    suspend fun getPhotosList(): List<Photo>

    @GET(WebService.ALL_USERS)
    suspend fun getAllUsers(): List<User>

    @GET(WebService.ALL_POSTS)
    suspend fun getAllPosts(): List<Post>

    @GET(WebService.ALL_COMMENTS)
    suspend fun getAllComments(): List<Comment>

}

