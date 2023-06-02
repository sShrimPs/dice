package com.example.dice

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.dice.retrofit.milesService
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_fianlprice.back_btn
import kotlinx.android.synthetic.main.activity_fianlprice.final_pay
import kotlinx.android.synthetic.main.activity_fianlprice.renewal_info
import kotlinx.android.synthetic.main.activity_pointcharge.ac_charge2
import kotlinx.android.synthetic.main.activity_pointcharge.returnbtn
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

    override fun onCreate(savedInstanceState: Bundle?) {

        var fianlprice = 10000
        var price = intent?.getStringExtra("price") ?: ""
        var ids = intent?.getStringExtra("ids") ?: ""
        pref = getSharedPreferences("id", Context.MODE_PRIVATE)
        preid = pref.edit()
        var saveid = pref.getString("InputData","")
        ids = saveid.toString()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pointcharge)

        back_btn.setOnClickListener {
            val intent = Intent(this, Myinfo::class.java)
            startActivity(intent)
        } //메인메뉴로 이동
        renewal_info.setOnClickListener {


        } //주차정보갱신 버튼
        final_pay.setOnClickListener {

        } //주차자리 위치확인 버튼

        ac_charge2.setOnClickListener {
            Log.d(TAG, "현재 $price")
            val user = BootUser().setPhone("010-1234-5678") // 구매자 정보
            val extra = BootExtra()
                .setCardQuota("0,2,3") // 일시불, 2개월, 3개월 할부 허용, 할부는 최대 12개월까지 사용됨 (5만원 이상 구매시 할부허용 범위)

            val prices = price.toDouble() / 100

            val items: MutableList<BootItem> = ArrayList()

            val payload = Payload()
            payload.setApplicationId(AppId)
                .setOrderName("Dice 결제")
                .setOrderId("1234")
                .setPrice(prices)
                .setUser(user)
                .setExtra(extra).items = items

            val map: MutableMap<String, Any> = HashMap()
            map["1"] = "abcdef"
            map["2"] = "abcdef55"
            map["3"] = 1234
            payload.metadata = map

            Bootpay.init(supportFragmentManager, applicationContext)
                .setPayload(payload)
                .setEventListener(object : BootpayEventListener {
                    override fun onCancel(data: String) {
                        Log.d("bootpay", "cancel: $data")
                    }

                    override fun onError(data: String) {
                        Log.d("bootpay", "error: $data")
                    }

                    override fun onClose(data: String) {
                        Log.d("bootpay", "close: $data")
                        Bootpay.removePaymentWindow()
                    }

                    override fun onIssued(data: String) {
                        Log.d("bootpay", "issued: $data")
                    }

                    override fun onConfirm(data: String): Boolean {
                        Log.d("bootpay", "confirm: $data")
                        //                        Bootpay.transactionConfirm(data); //재고가 있어서 결제를 진행하려 할때 true (방법 1)
                        return true //재고가 있어서 결제를 진행하려 할때 true (방법 2)
                        //                        return false; //결제를 진행하지 않을때 false
                    }

                    override fun onDone(data: String) {
                        Log.d("done", data)
                        buygood = price
                        Log.d(TAG, "결제 결과 $buygood")
                        val jsonObject = JsonObject().apply {
                            addProperty("id", ids)
                            addProperty("mile", buygood)
                        }
                        Log.d(TAG, "결제 결과 전송중 $jsonObject")
                        sendDataToServer(jsonObject)
                    }
                }).requestPayment()
        }


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
}