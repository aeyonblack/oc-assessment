package com.example.feldmantest

import retrofit2.Call
import retrofit2.http.*

interface AppApi {
    @GET("posts/1/comments")
    fun getComments(): Call<Comment>

    @GET("users/1/albums")
    fun getAlbums(): Call<Album>

    @GET("albums/1/photos")
    fun getPhotos(): Call<Photo>
}

