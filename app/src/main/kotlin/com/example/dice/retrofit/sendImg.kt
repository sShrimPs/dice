package com.example.dice.retrofit

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface sendImg {
    @Multipart
    @POST("/image")
    fun sendImage(
        @Part imageFile: MultipartBody.Part
    ): Call<String>
}
