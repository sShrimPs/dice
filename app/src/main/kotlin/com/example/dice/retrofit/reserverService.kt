package com.example.dice.retrofit

import com.example.dice.datamodel.reservers
import com.google.gson.JsonObject
import retrofit2.http.GET
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Path
import com.google.gson.annotations.SerializedName

interface reserverService {
    @GET("/reservation")//서버에 GET 요청을 할 주소를 입력
    fun getReservers(): Call<JsonObject>
}