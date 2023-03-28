package com.example.dice

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_cardread.*
import kotlinx.android.synthetic.main.activity_sub5.*

class cardread : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cardread)


        returnbtn.setOnClickListener {
            val intent = Intent(this, SubActivity5::class.java)
            startActivity(intent)
        } //메인메뉴로 이동


    }



}