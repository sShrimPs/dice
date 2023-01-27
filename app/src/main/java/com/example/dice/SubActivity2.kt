package com.example.dice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_sub1.*
import kotlinx.android.synthetic.main.activity_sub2.*

class SubActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub2)

        lbtn_2.setOnClickListener {
            finish()
        }
    }
}