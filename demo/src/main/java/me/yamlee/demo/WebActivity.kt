package me.yamlee.demo

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import com.tbruyelle.rxpermissions.RxPermissions
import me.yamlee.jsbridge.BridgeWebChromeClient
import me.yamlee.jsbridge.NativeComponentProvider
import me.yamlee.jsbridge.QFHybridWebViewClient
import me.yamlee.jsbridge.WVJBWebViewClient
import me.yamlee.jsbridge.model.ListIconTextModel
import me.yamlee.jsbridge.ui.BridgeWebActivity

/**
 * Created by yamlee on 24/02/2018.
 */
class WebActivity : BridgeWebActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setWebView()
        loadUrl("http://www.baidu.com")
    }

    /**
     * 设置默认的client
     */
    private fun setWebView() {
        val wvBridgeHandler = object : WVJBWebViewClient.WVJBHandler {
            override fun request(data: Any?, callback: WVJBWebViewClient.WVJBResponseCallback?) {

            }

        }
        val chromeClient: WebChromeClient = MyChromeClient(applicationContext)
        webView.webChromeClient = chromeClient

        val webViewClient = MyWebViewClient(webView, wvBridgeHandler, this)

        webViewClient.enableLogging()
        webView.webViewClient = webViewClient

    }

    override fun onClickErrorView() {
    }

    override fun onClickTitleRight(clickUri: String?) {
    }

    override fun onClickMoreMenuItem(listIconTextModel: ListIconTextModel) {
    }

    inner class MyWebViewClient(webView: WebView, wvjbHandler: WVJBWebViewClient.WVJBHandler,
                                componentProvider: NativeComponentProvider)
        : QFHybridWebViewClient(webView, wvjbHandler, componentProvider) {
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            showProgress()
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            hideProgress()
        }

    }

    inner class MyChromeClient(context: Context) : BridgeWebChromeClient(context) {
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
            renderWebViewLoadProgress(newProgress)
        }
    }


}