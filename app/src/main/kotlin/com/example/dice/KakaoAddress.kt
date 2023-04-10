package com.example.dice

import android.content.SharedPreferences
import android.annotation.TargetApi
import android.app.Dialog
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Message
import android.view.WindowManager
import android.webkit.*
import android.webkit.WebView.WebViewTransport
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_kakaoadd.*


class KakaoAddress : AppCompatActivity() {
    private var webView: WebView? = null
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kakaoadd)

        webView = webview
        WebView.setWebContentsDebuggingEnabled(true)
        webView!!.apply {
            settings.javaScriptEnabled = true
            settings.javaScriptCanOpenWindowsAutomatically = true
            settings.setSupportMultipleWindows(true)
            webViewClient = client

        }
        webView!!.loadUrl(getString(R.string.baseUrl))


    }

    var client: WebViewClient = object : WebViewClient() {

        @TargetApi(Build.VERSION_CODES.N)
        override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
            return false
        }

    }

}