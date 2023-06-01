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
        @Part("fulladd") fulladd: String,
        @Part("fulladds") fulladds: String,
        @Part("postcode") postcode: String,
        @Part("id") id: String,
        @Part imageFile: MultipartBody.Part
    ): Call<String>
}
