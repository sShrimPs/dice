package com.example.dice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.dice.kakao.KakaoAddress
import kotlinx.android.synthetic.main.activity_registerpark.*

class Register_park : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registerpark)

        search_addr.setOnClickListener {
            val intent = Intent(this, KakaoAddress::class.java)
            startActivity(intent)
        }
    }




}