package com.example.dice

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.dice.retrofit.CarNumService
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_carnum.backs_btn
import kotlinx.android.synthetic.main.activity_carnum.car_edit
import kotlinx.android.synthetic.main.activity_carnum.car_register
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class Carnum : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carnum)
        var ids = intent?.getStringExtra("ids") ?: ""

        car_register.setOnClickListener {
            var cars = car_edit.text.toString()
            val jsonObject = JsonObject().apply {
                addProperty("id", ids)
                addProperty("carnum", cars)
            }
            Log.d(TAG, "차량정보 전송중$cars")
            sendDataToServer(jsonObject)
            Log.d(TAG, "차량정보 전송중 $jsonObject")
            Toast.makeText(applicationContext, "차량정보 등록 전송중!" , Toast.LENGTH_SHORT).show()
        }

        backs_btn.setOnClickListener {
            val intent = Intent(this, Myinfo::class.java)
            startActivity(intent)
        }



    }

    private fun sendDataToServer(jsonObject: JsonObject) {

        var gson = GsonBuilder().setLenient().create()
        val retrofit = Retrofit.Builder()
            .baseUrl(getString(R.string.baseUrl))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        val apiService = retrofit.create(CarNumService::class.java)

        val call = apiService.CarNums(jsonObject)
        Log.d(TAG, "전송중")

        Thread{
            call.enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        Log.d("Response 완료", response.body().toString())
                        val results: JsonObject? = response.body()
                        Log.d(TAG, "전송 과정 중 response $results")
                        Toast.makeText(applicationContext, "차량정보 등록 완료!" , Toast.LENGTH_SHORT).show()

                    } else {
                        Log.d("Response 완료", response.errorBody().toString())
                        Toast.makeText(applicationContext, "차량정보 등록 실패 재시도 해주세요!", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.d("전송 실패", t.message.toString())
                    Toast.makeText(applicationContext, "차량정보 등록 실패!! 서버가 불안정 합니다.", Toast.LENGTH_SHORT).show()

                }

            }
            )}.start()
        try {
            Thread.sleep(40)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        Log.d(TAG, "전송완료")
    }
}