package com.example.dice

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface FlaskService {
    @POST("/member")
    @GET("/member")
    fun sendData(@Body data: JsonObject): Call<JsonObject>
}