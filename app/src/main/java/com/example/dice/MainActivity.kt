package com.example.dice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    fun main(){
        // 서버 역할을 하기 위해 객체 생성
        val server = java.net.ServerSocket(55555)
        print("사용자 접속대기")
        val socket = server.accept()
        println(socket)

        // 서버 종료
        socket.close()
    }
}