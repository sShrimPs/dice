package com.example.dice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sbtn_1.setOnClickListener {
            val intent = Intent(this, SubActivity1::class.java)
            startActivity(intent)
        }
        sbtn_2.setOnClickListener {
            val intent = Intent(this, SubActivity2::class.java)
            startActivity(intent)
        }
        sbtn_3.setOnClickListener {
            val intent = Intent(this, SubActivity3::class.java)
            startActivity(intent)
        }
    }
}