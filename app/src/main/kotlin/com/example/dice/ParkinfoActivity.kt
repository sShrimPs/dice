package com.example.dice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.dice.datamodel.CarNumInfo
import com.example.dice.datamodel.Loginserver
import com.example.dice.datamodel.NumInfo
import com.example.dice.retrofit.CarNumService
import com.example.dice.retrofit.cardService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_parkinfo.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ParkinfoActivity : AppCompatActivity() {
    val TAG: String = "Test"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parkinfo)

        back_btn.setOnClickListener {
            val intent = Intent(this, Myinfo::class.java)
            startActivity(intent)
        } //메인메뉴로 이동
        renewal_info.setOnClickListener {


        } //주차정보갱신 버튼
        parking_location.setOnClickListener {

        } //주차자리 위치확인 버튼
    }



}