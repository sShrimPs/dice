package com.example.dice.retrofit

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST


interface FlaskService {
    @POST("/signup")
    fun sendData(@Body data: JsonObject): Call<JsonObject>
}