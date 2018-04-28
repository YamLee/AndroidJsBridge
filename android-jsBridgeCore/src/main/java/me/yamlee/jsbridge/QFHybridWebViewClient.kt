package me.yamlee.jsbridge

import android.content.Intent
import android.webkit.WebView

import org.json.JSONObject

import java.util.HashMap

import me.yamlee.jsbridge.jscall.AlertProcessor
import me.yamlee.jsbridge.jscall.CloseProcessor
import me.yamlee.jsbridge.jscall.DefaultProcessor
import me.yamlee.jsbridge.jscall.SetHeaderProcessor
import me.yamlee.jsbridge.jscall.ToastProcessor
import timber.log.Timber

/**
 * 包含JsBridge功能的WebViewClient
 *
 * @author yamlee
 */
open class QFHybridWebViewClient(webView: WebView, wvjbHandler: WVJBWebViewClient.WVJBHandler,
                                 componentProvider: NativeComponentProvider) : WVJBWebViewClient(webView, wvjbHandler) {
    private var jsCallProcessors: MutableMap<String, JsCallProcessor>? = null

    init {
        registerCallProcessors(componentProvider)
        registerHandler(BRIDGE_HANDLER_NAME,object:WVJBWebViewClient.WVJBHandler{
            override fun request(data: Any?, callback: WVJBResponseCallback?) {
                val jsonObject = data as JSONObject
                callback?.let {
                    handleData(jsonObject, callback)
                }
            }
        })
    }

    private fun registerCallProcessors(componentProvider: NativeComponentProvider) {
        registerJsCallProcessor(DefaultProcessor(componentProvider))
        registerJsCallProcessor(AlertProcessor(componentProvider))
        registerJsCallProcessor(CloseProcessor(componentProvider))
        registerJsCallProcessor(ToastProcessor(componentProvider))
        registerJsCallProcessor(SetHeaderProcessor(componentProvider))
    }


    fun registerJsCallProcessor(callHandler: JsCallProcessor) {
        if (jsCallProcessors == null) {
            jsCallProcessors = HashMap()
        }
        jsCallProcessors!![callHandler.getFuncName()] = callHandler
    }

    private fun handleData(jsonObject: JSONObject, callback: WVJBWebViewClient.WVJBResponseCallback) {
        val jsCallData = JsCallData()
        try {
            jsCallData.func = jsonObject.optString("func")
            jsCallData.params = jsonObject.optJSONObject("params").toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        proceed(jsCallData, callback)
    }

    fun proceed(callData: JsCallData, callback: WVJBWebViewClient.WVJBResponseCallback) {
        if (jsCallProcessors == null) return
        val msg: String
        val jsCallProcessor = jsCallProcessors!![callData.func]
        if (jsCallProcessor != null) {
            val handled = jsCallProcessor.process(callData, callback)
            msg = if (handled)
                String.format("%s Processor handled js call", jsCallProcessor.getFuncName())
            else
                String.format("%s Processor have not handled target js call", jsCallProcessor.getFuncName())
            Timber.i(msg)
        } else {
            val defaultProcessor = jsCallProcessors!![DefaultProcessor.FUNC_NAME]
            defaultProcessor?.process(callData, callback)
            msg = String.format("No JsCallProcessor can handle jsCall %s ", callData.func)
            Timber.e(msg)
        }
    }

    fun handleActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (jsCallProcessors == null) return
        for (processor in jsCallProcessors!!.values) {
            val handled = processor.onActivityResult(requestCode, resultCode, data)

            if (handled) {
                val msg = String.format("Processor %s handled ActivityResult",
                        processor.getFuncName())
                Timber.i(msg)
                return
            }

        }
    }

    companion object {

        val BRIDGE_HANDLER_NAME = "JsBridgeCall"
    }


}