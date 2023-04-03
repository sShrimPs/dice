package com.example.dice

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_sub3.*
import kotlinx.android.synthetic.main.activity_sub3.edit_id
import kotlinx.android.synthetic.main.activity_sub3.edit_pw
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SubActivity3 : AppCompatActivity() {
    val TAG: String = "SubActivity3"
    lateinit var pref: SharedPreferences
    lateinit var preid: SharedPreferences.Editor
    lateinit var mRetrofit :Retrofit
    lateinit var mRetrofitAPI: memberid
    lateinit var mCallTodoList : retrofit2.Call<JsonObject>
    var result: String = ""
    private var loginst: String = ""
    var memNum: String = ""
    var ok:Int = 0



    //companion object {
       // private var instance: SubActivity3? = null
       // fun getInstance(): SubActivity3? {
       //     return instance
     //   }
   // }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub3)
        pref = getSharedPreferences("id",Context.MODE_PRIVATE)
        preid = pref.edit()
        // 로그인 버튼
        btn_login.setOnClickListener {
            var autologin = pref.getString("success","")
            var ids = edit_id.text.toString()
            preid.putString("InputData",ids)
            preid.apply()
            var savebefore = pref.getString("InputData","")
            var beforeid = savebefore.toString()
            if(beforeid != ids){
                preid.clear()
            }
            var i = 0
            val jsonObject = JsonObject().apply {
                addProperty("id", edit_id.text.toString())
                addProperty("pw", edit_pw.text.toString())
            }
            Log.d(TAG, "로그인 전송중 $jsonObject")
            sendDataToServer(jsonObject)
            Log.d(TAG, "login process 진행중")
            //login_process()
            // 유저가 입력한 id, pw값과 쉐어드로 불러온 id, pw값 비교
            Log.d(TAG, "Value : $ok")
            if((ok == 1)||(autologin == "1")) {
                    if((memNum == "1")||(autologin == "1")){
                        // 로그인 성공 다이얼로그 보여주기
                        autologin = "1"
                        preid.putString("InputData",ids)
                        preid.putString("success", autologin)
                        preid.apply()
                        val intent = Intent(this, MainActivity::class.java)
                        intent.apply {
                              this.putExtra("ids", ids)
                        }
                        startActivity(intent)
                    }
                    else{
                        // 로그인 실패 다이얼로그 보여주기
                        dialog("fail")
                    }
                }
            else if(ok == 2){
                    dialog("fail")
            }
        }

        // 회원가입 버튼
        signup_btn.setOnClickListener {
            val intent = Intent(this, SubActivity4::class.java)
            startActivity(intent)
        }

    }
    // 로그인 성공/실패 시 다이얼로그를 띄워주는 메소드
    fun dialog(type: String){
        var dialog = AlertDialog.Builder(this)

        if(type.equals("success")){
            dialog.setTitle("로그인 성공")
            dialog.setMessage("로그인 성공!")
        }
        else if(type.equals("fail")){
            dialog.setTitle("로그인 실패")
            dialog.setMessage("아이디와 비밀번호를 확인해주세요")
        }

        var dialog_listener = object: DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                when(which){
                    DialogInterface.BUTTON_POSITIVE ->
                        Log.d(TAG, "")
                }
            }
        }

        dialog.setPositiveButton("확인",dialog_listener)
        dialog.show()
    }

    private fun sendDataToServer(jsonObject: JsonObject) {

        var gson = GsonBuilder().setLenient().create()
        val retrofit = Retrofit.Builder()
            .baseUrl(getString(R.string.baseUrl))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        val apiService = retrofit.create(loginService::class.java)

        val call = apiService.login(jsonObject)
        Log.d(TAG, "전송중")

        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {

                    Log.d("Response 완료", response.body().toString())
                    val results: JsonObject? = response.body()
                    Log.d(TAG, "전송 과정 중 response $results")

                    var mGson = Gson()
                    val receive_pass = mGson.fromJson(results, Loginserver::class.java)
                    val receive_mem = mGson.fromJson(results, NumInfo::class.java)

                    loginst = receive_pass.id
                    memNum = receive_mem.check
                    Log.d(TAG, "변환 결과 $loginst , $memNum ")
                    runBlocking {
                        delay(500L)
                    }
                    ok = 1

                } else {
                    Log.d("Response 완료", response.errorBody().toString())
                    val results: JsonObject? = response.body()
                    Log.d(TAG, "전송 error response $results")
                    if (results != null) {
                        // Response 데이터가 null이 아닌 경우 처리하는 코드 작성
                        var mGson = Gson()
                        val receive_pass = mGson.fromJson(results, Loginserver::class.java)
                        val receive_mem = mGson.fromJson(results, NumInfo::class.java)
                        loginst = receive_pass.id
                        memNum = receive_mem.check
                        Log.d(TAG, "변환 결과 $loginst , $memNum ")
                        runBlocking {
                            delay(500L)
                        }
                        ok = 1
                    } else {
                        // Response 데이터가 null인 경우 처리하는 코드 작성
                        Log.d(TAG, "에러 발생 NULL ")
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {

                Log.d("전송 실패", t.message.toString())
                ok = 2
            }

        }
        )

        Log.d(TAG, "전송완료")
    }
   // private fun login_process(){
     //   var gson = GsonBuilder().setLenient().create()
       // mRetrofit = Retrofit.Builder()
         //   .baseUrl(getString(R.string.baseUrl))
           // .addConverterFactory(GsonConverterFactory.create(gson))
            //.build()
        //mRetrofitAPI = mRetrofit.create(memberid::class.java)
    //}

}