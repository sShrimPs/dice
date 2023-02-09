package com.example.dice

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
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

    lateinit var mRetrofit :Retrofit
    lateinit var mRetrofitAPI: RetrofitAPI
    lateinit var mCallTodoList : retrofit2.Call<JsonObject>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setRetrofit()


        button1.setOnClickListener {
            button1.visibility = View.INVISIBLE
            progressBar.visibility = View.VISIBLE
            callTodoList()
        }

        sbtn_1.setOnClickListener {
            val intent = Intent(this, SubActivity1::class.java)
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


            var mGson = Gson()
            val dataParsed1 = mGson.fromJson(result, DataModel::class.java)
            //val dataParsed2 = mGson.fromJson(result, DataModel.TodoInfo2::class.java)
            //val dataParsed3 = mGson.fromJson(result, DataModel.TodoInfo3::class.java)
            var test = dataParsed1.Sonic

            test = test.replace("(","")
            test = test.replace(")","")
            test = test.replace(",","")
            test = test.replace("'","")
            test = test.replace("\\","")
            test = test.replace("r","")
            test = test.replace("n","")
            Log.d(TAG, "변환 데이터 $test")

            //textView.text = "주차장 자리 현황\n" + dataParsed1.todo1.task+"\n"+dataParsed2.todo2.task +"\n"+dataParsed3.todo3.task
            textView.text = "Dice 주차장 주차 현황\n" + test + "/6"
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
