package com.example.dice.retrofit

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface CarNumService {
    @POST("/car")
    fun CarNums(@Body data: JsonObject) : Call<JsonObject>
}