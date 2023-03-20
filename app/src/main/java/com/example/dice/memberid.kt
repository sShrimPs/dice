package com.example.dice

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.GET

interface memberid {
    @GET("/login")
    fun memid() : Call<JsonObject> //MainActivity에서 불러와서 이 함수에 큐를 만들고 대기열에 콜백을 넣어주면 그거갖고 요청하는거임.
}