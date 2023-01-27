package com.example.dice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_sub1.*
import kotlinx.android.synthetic.main.activity_sub3.*

class SubActivity3 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub3)

        lbtn_3.setOnClickListener {
            finish()
        }
    }
}