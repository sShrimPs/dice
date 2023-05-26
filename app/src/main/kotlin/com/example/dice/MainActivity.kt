package com.example.dice

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.dice.datamodel.DataModel
import com.example.dice.kakao.KakaoAddress
import com.example.dice.retrofit.RetrofitAPI
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    val TAG = "TAG_MainActivity"
    lateinit var pref: SharedPreferences
    lateinit var preid: SharedPreferences.Editor
    lateinit var mRetrofit :Retrofit
    lateinit var mRetrofitAPI: RetrofitAPI
    lateinit var mCallTodoList : retrofit2.Call<JsonObject>
    lateinit var test: String

    override fun onCreate(savedInstanceState: Bundle?) {
        var ids = intent?.getStringExtra("ids") ?: ""
        var maps = intent?.getStringExtra("roadAddress") ?: ""

        pref = getSharedPreferences("id",Context.MODE_PRIVATE)
        preid = pref.edit()
        var saveid = pref.getString("InputData","")
        ids = saveid.toString()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setRetrofit()

        button1.setOnClickListener {
            button1.visibility = View.INVISIBLE
            progressBar.visibility = View.VISIBLE
            callTodoList()
            myUID.text = "주소는" + maps
        }

        kakaoadd.setOnClickListener {
            val intent = Intent(this, Register_park::class.java)
            startActivity(intent)
        }

        mapbtn_1.setOnClickListener {
            val intent = Intent(this, NaverMap::class.java)
            startActivity(intent)
        }
        returnloginbtn_1.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            Toast.makeText(this@MainActivity, "로그아웃 성공!", Toast.LENGTH_SHORT).show()
        }
        ident_btn.setOnClickListener {
            Log.d(TAG, "로그인 $ids")
            val intent = Intent(this, Myinfo::class.java)
            intent.apply {
                this.putExtra("ids", ids)
            }
            startActivity(intent)
        }

    }

    private fun callTodoList() {
        mCallTodoList = mRetrofitAPI.getTodoList()
        mCallTodoList.enqueue(mRetrofitCallback)
    }


    private val mRetrofitCallback  = (object : retrofit2.Callback<JsonObject>{
        override fun onFailure(call: Call<JsonObject>, t: Throwable) {

            t.printStackTrace()
            Log.d(TAG, "에러코드. => ${t.message.toString()}")
            textView.text = "에러\n" + "다시 한번 새로고침을 해주세요"

            progressBar.visibility = View.GONE
            button1.visibility = View.VISIBLE
        }

        override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
            val result = response.body()
            Log.d(TAG, "수신왼료 $result")
            if (result != null) {
                // Response 데이터가 null이 아닌 경우 처리하는 코드 작성

                var mGson = Gson()
                val dataParsed1 = mGson.fromJson(result, DataModel::class.java)
                test = dataParsed1.Sonic

                test = test.replace("(","")
                test = test.replace(")","")
                test = test.replace(",","")
                test = test.replace("'","")
                test = test.replace("\\","")
                test = test.replace("r","")
                test = test.replace("n","")
                Log.d(TAG, "변환 데이터 $test")

                textView.text = "Dice 주차장 주차 현황\n" + test + "/6"

            } else {
                // Response 데이터가 null인 경우 처리하는 코드 작성
                Log.d(TAG, "에러 발생 NULL ")
            }

            progressBar.visibility = View.GONE
            button1.visibility = View.VISIBLE
        }
    })

    private fun setRetrofit(){
        var gson = GsonBuilder().setLenient().create()
        mRetrofit = Retrofit.Builder()
            .baseUrl(getString(R.string.baseUrl))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        mRetrofitAPI = mRetrofit.create(RetrofitAPI::class.java)
    }

}
