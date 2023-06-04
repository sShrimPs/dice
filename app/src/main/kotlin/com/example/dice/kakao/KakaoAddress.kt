package com.example.dice.kakao

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import com.example.dice.Myinfo
import com.example.dice.R
import com.example.dice.Register_park
import kotlinx.android.synthetic.main.activity_kakaoadd.*


class KakaoAddress : AppCompatActivity() {

    var backKeyPressedTime : Long = 0

    override fun onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2500){
            finish()
            val intent = Intent(this, Register_park::class.java)
            startActivity(intent)
        }

    }

    inner class AndroidBridge {
        @JavascriptInterface
        fun processDATA(roadAdd: String, postcode: String) {
            val sharedPreferences = getSharedPreferences("processDATA", Context.MODE_PRIVATE)
            sharedPreferences.edit().putString("roadAddress", roadAdd).apply()
            sharedPreferences.edit().putString("postcode", postcode).apply()
            Log.d("주소명",roadAdd)
            Log.d("우편번호",postcode)
            finish()
        }
    }

    private var webView: WebView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kakaoadd)

        webView = webview
        WebView.setWebContentsDebuggingEnabled(true)
        webView!!.apply {
            settings.javaScriptEnabled = true
            settings.javaScriptCanOpenWindowsAutomatically = true
            settings.setSupportMultipleWindows(true)
            webViewClient = client
            settings.setDomStorageEnabled(true)
            settings.setAllowContentAccess(true);

        }
            webView!!.webChromeClient = object : WebChromeClient() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                override fun onPermissionRequest(request: PermissionRequest) {
                    request.grant(request.resources)
                }
            }

            webView!!.addJavascriptInterface(AndroidBridge(), "dice")
            webView!!.loadUrl(getString(R.string.baseUrl))
    }
    override fun onStop() {
        super.onStop()
        webView?.loadUrl("javascript:self.close();");

    }

    override fun onResume() {
        super.onResume()
        webView = webview
        WebView.setWebContentsDebuggingEnabled(true)
        webView!!.apply {
            settings.javaScriptEnabled = true
            settings.javaScriptCanOpenWindowsAutomatically = true
            settings.setSupportMultipleWindows(true)
            webViewClient = client
            settings.setDomStorageEnabled(true)
            settings.setAllowContentAccess(true);

        }
        webView!!.webChromeClient = object : WebChromeClient() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            override fun onPermissionRequest(request: PermissionRequest) {
                request.grant(request.resources)
            }
        }
        webView!!.addJavascriptInterface(AndroidBridge(), "dice")
        webView!!.loadUrl(getString(R.string.baseUrl))
    }


    var client: WebViewClient = object : WebViewClient() {

        @TargetApi(Build.VERSION_CODES.N)
        override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
            view.loadUrl(getString(R.string.baseUrl))
            return true
        }
        override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
            handler?.proceed() // Proceed with SSL certificate

        }
        override fun onPageFinished(view: WebView?, url: String?) {
            webView?.loadUrl("javascript:daum.postcode.load()")
        }


}



}
