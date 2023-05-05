package com.example.dice

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.dice.databinding.ActivitySub5Binding
import com.example.dice.datamodel.milesInfo
import com.example.dice.retrofit.milesService
import kotlinx.android.synthetic.main.activity_sub5.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



class Myinfo : AppCompatActivity() {
    lateinit var pref: SharedPreferences
    lateinit var preid: SharedPreferences.Editor
    var milesmony: String = ""
    val TAG: String = "Price"
    var isExistBlank = false
    var money: String = ""
    private val binding by lazy {
        ActivitySub5Binding.inflate(layoutInflater)
    }
    private val AppId = "640ab855755e27001c6921c2"

    //companion object {
    //private var instance: SubActivity3? = null
    //fun getInstance(): SubActivity3? {
    //    return instance
    // }
    // }

    override fun onCreate(savedInstanceState: Bundle?) {
        var ids = intent?.getStringExtra("ids") ?: ""
        val buygood = intent?.getStringExtra("buygood") ?: ""
        pref = getSharedPreferences("id", Context.MODE_PRIVATE)
        preid = pref.edit()
        var saveid = pref.getString("InputData","")
        ids = saveid.toString()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setContentView(R.layout.activity_sub5)
        //renow_point.setText("갱신 전 포인트 :"+nowpoint)

        point_renewal.setOnClickListener {

            val jsonObject = JsonObject().apply {
                addProperty("id", ids)
                addProperty("mile", 0)
            }
            Log.d(TAG, "로그인 전송중 $jsonObject")
            sendDataToServer(jsonObject)
            dialog("success")

        } //잔여포인트 갱신버튼

        ac_charge.setOnClickListener {
            var price = edit_price.text.toString()
            Log.d(TAG, "가격 전송중 $price")
            Log.d(TAG, "로그인 $ids")
            val intent = Intent(this, PointchargeActivity::class.java)
            intent.apply {
                this.putExtra("price", edit_price.text.toString())
                this.putExtra("ids", ids)
            }
            startActivity(intent)
        } //충전할 금액 확정버튼

        mainbtn_2.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        } //메인메뉴로 이동


        nfcbtn.setOnClickListener {
            val intent = Intent(this, cardread::class.java)
            intent.apply {
                this.putExtra("ids", ids)
            }
            startActivity(intent)
        } //nfc메뉴로 이동


        parking_info.setOnClickListener {
            val intent = Intent(this, ParkinfoActivity::class.java)
            startActivity(intent)
        } //주차정보로 이동


    }

    private fun sendDataToServer(jsonObject: JsonObject) {

        var gson = GsonBuilder().setLenient().create()
        val retrofit = Retrofit.Builder()
            .baseUrl(getString(R.string.baseUrl))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        val apiService = retrofit.create(milesService::class.java)

        val call = apiService.miles(jsonObject)
        Log.d(TAG, "전송중")

        Thread{
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    Log.d("Response 완료", response.body().toString())
                    val results: JsonObject? = response.body()
                    Log.d(TAG, "전송 과정 중 response $results")
                    var mGson = Gson()
                    val miler = mGson.fromJson(results, milesInfo::class.java)
                    milesmony = miler.mile
                    if (milesmony == "1") {
                        Log.d(TAG, "0원")
                        money = "0원"
                    } else {
                        Log.d(TAG, "변환 결과 $milesmony")
                        money = milesmony
                    }


                } else {
                    Log.d("Response 완료", response.errorBody().toString())
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.d("전송 실패", t.message.toString())

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

    fun dialog(type: String) {
        var dialog = AlertDialog.Builder(this)

        if (type.equals("success")) {
            dialog.setTitle("잔액은")
            dialog.setMessage(money)
        } else if (type.equals("fail")) {
            dialog.setTitle("잔액확인실패")
            dialog.setMessage("다시 한번 눌려주세요")
        }

        var dialog_listener = object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
                when (which) {
                    DialogInterface.BUTTON_POSITIVE ->
                        Log.d(TAG, "")
                }
            }
        }

        dialog.setPositiveButton("확인",dialog_listener)
        dialog.show()


    }
}