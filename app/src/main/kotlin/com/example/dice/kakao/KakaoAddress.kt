package com.example.dice.kakao

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import com.example.dice.R
import kotlinx.android.synthetic.main.activity_kakaoadd.*


class KakaoAddress : AppCompatActivity() {

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

        webView!!.loadUrl("file:///android_asset/www/index.html")
}


var client: WebViewClient = object : WebViewClient() {

    @TargetApi(Build.VERSION_CODES.N)
    override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
        return false
    }
    override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
        handler?.proceed() // Proceed with SSL certificate

    }


}



}
