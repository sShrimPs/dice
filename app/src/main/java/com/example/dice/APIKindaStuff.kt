package com.example.dice

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

class APIKindaStuff {

    interface APIService{

        @GET("/users/{user}")

        fun greetUser(@Path("user") user: String) : Call<ResponseBody>

        @Headers("Content-type: application/json")

        @POST("/user")

        fun getVectors(@Body body: JsonObject) : Call<ResponseBody>
    }


    //companion object{
       // private val retrofit = Retrofit.Builder().baseUrl("http://192.168.1.176:24/")
       //     .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create())).build()

        //var service = retrofit.create(APIService::class.java)
   // }
}