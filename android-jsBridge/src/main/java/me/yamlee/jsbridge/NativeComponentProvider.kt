package me.yamlee.jsbridge

import android.app.Activity
import android.content.Context
import android.webkit.WebChromeClient
import android.webkit.WebViewClient

import me.yamlee.jsbridge.ui.WebActionView


/**
 * Js调用处理类中需要使用类的提供者
 *
 * @author yamlee
 */
interface NativeComponentProvider {

    /**
     * 提供Web界面Fragment的View交互类
     *
     * @return Fragment交互接口
     */
    fun provideWebLogicView(): WebActionView

    /**
     * 提供Web界面Activity的交互类
     *
     * @return Activity交互类
     */
    fun provideWebInteraction(): WebActionView.WebLogicListener

    /**
     * 提供全局的Context对象
     *
     * @return 全局上下文
     */
    fun provideApplicationContext(): Context

    /**
     * 提供全Activity上下文
     *
     * @return
     */
    fun provideActivityContext(): Activity

    /**
     * 提供WebView使用的WebViewClient
     */
    fun onCreateWebViewClient(): WebViewClient

    /**
     * 提供WebView使用的ChromeClient
     */
    fun onCreateWebChromeClient(): WebChromeClient

    fun addJsCallProcessor(processor: JsCallProcessor)
}
