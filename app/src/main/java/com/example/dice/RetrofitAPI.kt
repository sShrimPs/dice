package com.example.dice

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitAPI {
    @GET("/sonic")//서버에 GET 요청을 할 주소를 입력
    //fun getTodoList() : Call<JsonObject> //MainActivity에서 불러와서 이 함수에 큐를 만들고 대기열에 콜백을 넣어주면 그거갖고 요청하는거임.
    fun getTodoList() : Call<JsonObject> //MainActivity에서 불러와서 이 함수에 큐를 만들고 대기열에 콜백을 넣어주면 그거갖고 요청하는거임.
}