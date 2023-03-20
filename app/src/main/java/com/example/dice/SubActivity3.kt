package com.example.dice

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_sub3.*
import kotlinx.android.synthetic.main.activity_sub3.edit_id
import kotlinx.android.synthetic.main.activity_sub3.edit_pw
import kotlinx.android.synthetic.main.activity_sub4.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SubActivity3 : AppCompatActivity() {
    val TAG: String = "SubActivity3"

    lateinit var mRetrofit :Retrofit
    lateinit var mRetrofitAPI: memberid
    lateinit var mCallTodoList : retrofit2.Call<JsonObject>
    var result: String = ""
    var loginst: String = ""
    var memNum: String = ""


    companion object {
        private var instance: SubActivity3? = null
        fun getInstance(): SubActivity3? {
            return instance
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub3)
        val subactivity5 = SubActivity5()

        // 로그인 버튼
        btn_login.setOnClickListener {

            //editText로부터 입력된 값을 받아온다
            var id = edit_id.text.toString()
            var pw = edit_pw.text.toString()

            val jsonObject = JsonObject().apply {
                addProperty("id", edit_id.text.toString())
                addProperty("pw", edit_pw.text.toString())
            }
            Log.d(TAG, "로그인 전송중 $jsonObject")
            sendDataToServer(jsonObject)
            Log.d(TAG, "senddata 전송완료")
            login_process()
            Log.d(TAG, "loginprocess")

            // 유저가 입력한 id, pw값과 쉐어드로 불러온 id, pw값 비교
            if(memNum.equals("1")){
                // 로그인 성공 다이얼로그 보여주기
                dialog("로그인 성공 회원정보 $memNum")
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }

            else{
                // 로그인 실패 다이얼로그 보여주기
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
                    val results = response.body()
                    Log.d(TAG, "전송과정중 response $results")

                    var mGson = Gson()
                    val receive_pass = mGson.fromJson(results, Loginserver::class.java)
                    val receive_mem = mGson.fromJson(results, NumInfo::class.java)

                    loginst = receive_pass.id
                    memNum = receive_mem.check
                    Log.d(TAG, "변환 $loginst , $memNum ")


                } else {
                    Log.d("Response 완료", response.errorBody().toString())
                    val results = response.body()
                    Log.d(TAG, "전송과정중error response $results")
                    var mGson = Gson()
                    val receive_pass = mGson.fromJson(results, Loginserver::class.java)
                    val receive_mem = mGson.fromJson(results, NumInfo::class.java)

                    loginst = receive_pass.id
                    memNum = receive_mem.check
                    Log.d(TAG, "변환 $loginst , $memNum ")

                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.d("실패", t.message.toString())
            }

        }
        )
        Log.d(TAG, "전송완료")



    }
    /*private fun callTodoList() {
        mCallTodoList = mRetrofitAPI.memid()
        mCallTodoList.enqueue(mRetrofitCallback)
    }*/

    private fun login_process(){
        var gson = GsonBuilder().setLenient().create()
        mRetrofit = Retrofit.Builder()
            .baseUrl(getString(R.string.baseUrl))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        mRetrofitAPI = mRetrofit.create(memberid::class.java)
    }
   /* private val mRetrofitCallback  = (object : retrofit2.Callback<JsonObject>{
        override fun onFailure(call: Call<JsonObject>, t: Throwable) {

            t.printStackTrace()
            Log.d(TAG, "에러코드. => ${t.message.toString()}")

        }

        override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
            val result = response.body()
            Log.d(TAG, "로그인 정보 수신 완료 $result")

            var mGson = Gson()
            val receive_pass = mGson.fromJson(result, Loginserver::class.java)
            val receive_mem = mGson.fromJson(result, NumInfo::class.java)
            //val dataParsed3 = mGson.fromJson(result, DataModel.TodoInfo3::class.java)
            loginst = receive_pass.Login_chk
            memNum = receive_mem.memID

            //var sever_pw = receive_pw.Log_pw


            loginst = loginst.replace("(","")
            loginst = loginst.replace(")","")
            loginst = loginst.replace(",","")
            loginst = loginst.replace("'","")
            loginst = loginst.replace("\\","")
            loginst = loginst.replace("r","")
            loginst = loginst.replace("n","")

            memNum = memNum.replace("(","")
            memNum = memNum.replace(")","")
            memNum = memNum.replace(",","")
            memNum = memNum.replace("'","")
            memNum = memNum.replace("\\","")
            memNum = memNum.replace("r","")
            memNum = memNum.replace("n","")
            Log.d(TAG, "회원정보 일치 정보 $loginst 회원번호 $memNum")

        }
    })*/


}