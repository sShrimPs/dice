package com.example.dice

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.dice.retrofit.finalinfo
import com.example.dice.retrofit.milesService
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_fianlprice.back_btn
import kotlinx.android.synthetic.main.activity_fianlprice.final_pay
import kotlinx.android.synthetic.main.activity_fianlprice.final_prices
import kotlinx.android.synthetic.main.activity_fianlprice.renewal_info
import kr.co.bootpay.android.Bootpay
import kr.co.bootpay.android.events.BootpayEventListener
import kr.co.bootpay.android.models.BootExtra
import kr.co.bootpay.android.models.BootItem
import kr.co.bootpay.android.models.BootUser
import kr.co.bootpay.android.models.Payload
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory





class finalprice : AppCompatActivity() {

    val TAG = "TAG_MainActivity"
    private val AppId = "640ab855755e27001c6921c2"
    var buygood:String = ""
    lateinit var pref: SharedPreferences
    lateinit var preid: SharedPreferences.Editor
    var backKeyPressedTime : Long = 0


    override fun onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2500){
            finish()
            val intent = Intent(this, Myinfo::class.java)
            startActivity(intent)
        }

    }



    override fun onCreate(savedInstanceState: Bundle?) {
        var fianlprice = 10000
        var price = intent?.getStringExtra("price") ?: ""
        var ids = intent?.getStringExtra("ids") ?: ""
        pref = getSharedPreferences("id", Context.MODE_PRIVATE)
        preid = pref.edit()
        var saveid = pref.getString("InputData","")
        ids = saveid.toString()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fianlprice)

        back_btn.setOnClickListener {
            val intent = Intent(this, Myinfo::class.java)
            startActivity(intent)
        } //메인메뉴로 이동

        renewal_info.setOnClickListener {
            Toast.makeText(applicationContext, "갱신 중입니다.", Toast.LENGTH_SHORT).show()
            val jsonObject = JsonObject().apply {
                addProperty("id", ids)
            }
            Log.d(TAG, "갱신 중 $jsonObject")
            sendDataToServer(jsonObject)

        } //주차정보갱신 버튼

        final_pay.setOnClickListener {
            val intent = Intent(this, fianlpay::class.java)
            startActivity(intent)
        }


    }



    private fun sendDataToServer(jsonObject: JsonObject) {

        var gson = GsonBuilder().setLenient().create()
        val retrofit = Retrofit.Builder()
            .baseUrl(getString(R.string.baseUrl))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        val apiService = retrofit.create(finalinfo::class.java)

        val call = apiService.fininfo(jsonObject)
        Log.d(TAG, "전송중")
        Thread{
            call.enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        Log.d("Response 완료", response.body().toString())
                        final_prices.text = response.body().toString()
                        Toast.makeText(applicationContext,"수신 완료!", Toast.LENGTH_SHORT).show()

                    } else {
                        Log.d("Response 완료", response.errorBody().toString())
                        final_prices.text = response.body().toString()
                        Toast.makeText(applicationContext,"수신 완료!", Toast.LENGTH_SHORT).show()

                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.d("전송 실패", t.message.toString())
                    Toast.makeText(applicationContext,"수신 실패! 서버 연결을 확인해 주세요.", Toast.LENGTH_SHORT).show()

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