package com.example.feldmantest.network

import com.example.feldmantest.model.Album
import com.example.feldmantest.model.Comment
import com.example.feldmantest.model.Photo
import retrofit2.http.*

interface AppApi {

    @GET(WebService.COMMENT)
    suspend fun getCommentsList(): List<Comment>

    @GET(WebService.ALBUM)
    suspend fun getAlbumsList(): List<Album>

    @GET(WebService.PHOTO)
    suspend fun getPhotosList(): List<Photo>
}

