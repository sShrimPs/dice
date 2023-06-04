package com.example.dice.retrofit

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.GET

interface finalpp {

    @GET("/finalpp")//서버에 GET 요청을 할 주소를 입력
    fun finalprice() : Call<JsonObject>

}