package com.example.dice.kakao

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

    inner class AndroidBridge {
        @JavascriptInterface
        @SuppressWarnings("unused")
        fun processDATA(roadAdd: String) {
            val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            sharedPreferences.edit().putString("roadAddress", roadAdd).apply()
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

        webView!!.addJavascriptInterface(AndroidBridge(), "AndroidBridge")
        webView!!.loadUrl(getString(R.string.baseUrl))
}


var client: WebViewClient = object : WebViewClient() {

    @TargetApi(Build.VERSION_CODES.N)
    override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
        return false
    }
    override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
        handler?.proceed() // Proceed with SSL certificate

    }
    override fun onPageFinished(view: WebView?, url: String?) {
        webView?.loadUrl("javascript:DaumPostcode")
    }


}



}
