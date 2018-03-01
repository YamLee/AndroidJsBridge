package me.yamlee.jsbridge.ui

import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import me.yamlee.jsbridge.JsCallProcessor

/**
 * Created by yamlee on 01/03/2018.
 */
interface BridgeWebAction {

    /**
     * 根据指定的url加载网页
     */
    fun loadUrl(url: String)


    fun onCreateWebViewClient(): WebViewClient


    fun onCreateWebChromeClient(): WebChromeClient

    fun addJsCallProcessor(processor: JsCallProcessor)

}