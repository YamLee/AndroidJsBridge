package me.yamlee.demo

import android.os.Bundle
import android.webkit.WebChromeClient
import kotlinx.android.synthetic.main.activity_main.*
import me.yamlee.jsbridge.BridgeWebChromeClient
import me.yamlee.jsbridge.QFHybridWebViewClient
import me.yamlee.jsbridge.WVJBWebViewClient
import me.yamlee.jsbridge.ui.BridgeWebActivity

class MainActivity : BridgeWebActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    /**
     * 设置默认的client
     */
    private fun setWebView() {
        val wvBridgeHandler = object : WVJBWebViewClient.WVJBHandler {
            override fun request(data: Any?, callback: WVJBWebViewClient.WVJBResponseCallback?) {

            }

        }
        val chromeClient: WebChromeClient = BridgeWebChromeClient(applicationContext)
        webView.webChromeClient = chromeClient

        val webViewClient = QFHybridWebViewClient(webView, wvBridgeHandler, this)
        webViewClient.enableLogging()
        webView.webViewClient = webViewClient

    }
}
