package com.example.dice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.dice.R
import kotlinx.android.synthetic.main.activity_parkinfo.*

class ParkinfoActivity : AppCompatActivity() {
    var carnum: String = "" //차량번호
    var parkname: String = "" //주차한 주차장명
    var parktime: String = "" //총 주차시간
    var allprice: String = "" //전체금액
    var ifprice: String = "" //할인적용시 금액
    var nowprice: String = "" //예상결제금액
    //nowpoint 현재 포인트를 활용해 띄우기

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parkinfo)

        renewal_info.setOnClickListener {

        } //주차정보갱신 버튼
        parking_location.setOnClickListener {

        } //주차자리 위치확인 버튼
    }
}