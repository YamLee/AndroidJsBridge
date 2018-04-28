package me.yamlee.jsbridge

import android.content.Context
import android.util.Log
import android.webkit.ConsoleMessage
import android.webkit.GeolocationPermissions
import android.webkit.JsResult
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView

import timber.log.Timber

/**
 * Add some additional operation for WebChromeClient
 * @author yamlee
 */
open class BridgeWebChromeClient(private val mContext: Context) : WebChromeClient() {
    private val mUploadMsg: ValueCallback<*>? = null
    private val cameraFilePath = ""
    private var mResult: JsResult? = null

    override fun onConsoleMessage(cm: ConsoleMessage): Boolean {
        Log.i("--Js Console.log:--", cm.message() + " -- From line "
                + cm.lineNumber() + " of "
                + cm.sourceId())
        return true
    }

    override fun onJsAlert(view: WebView, url: String, message: String, result: JsResult): Boolean {
        mResult = result
        return super.onJsAlert(view, url, message, result)

    }


    fun onStop() {
        if (mResult != null) {
            mResult!!.cancel()
        }
    }


    override fun onProgressChanged(view: WebView, newProgress: Int) {
        super.onProgressChanged(view, newProgress)
        Timber.v("web load progress$newProgress")
    }

    //配置H5请求地理位置权限
    override fun onGeolocationPermissionsShowPrompt(origin: String,
                                                    callback: GeolocationPermissions.Callback) {
        // 请求定位信息
        callback.invoke(origin, true, false)
    }

    companion object {
        val FILECHOOSER_RESULTCODE = 10001
    }

}
