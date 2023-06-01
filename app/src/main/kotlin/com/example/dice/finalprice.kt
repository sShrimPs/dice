package com.example.dice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_fianlprice.back_btn
import kotlinx.android.synthetic.main.activity_fianlprice.parking_location
import kotlinx.android.synthetic.main.activity_fianlprice.renewal_info

class finalprice : AppCompatActivity() {
    val TAG: String = "Test"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fianlprice)

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