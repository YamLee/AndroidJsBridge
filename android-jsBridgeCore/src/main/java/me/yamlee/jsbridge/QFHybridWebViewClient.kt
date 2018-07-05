package me.yamlee.jsbridge

import android.content.Intent
import android.webkit.WebView
import me.yamlee.jsbridge.jscall.*
import me.yamlee.jsbridge.utils.LogUtil
import org.json.JSONObject
import java.util.*

/**
 * 包含JsBridge功能的WebViewClient
 *
 * @author yamlee
 */
open class QFHybridWebViewClient(webView: WebView, jsCallHandler: JsCallHandler,
                                 componentProvider: NativeComponentProvider) : BridgeWebViewClient(webView, jsCallHandler) {
    private var jsCallProcessors: MutableMap<String, JsCallProcessor>? = null

    init {
        registerCallProcessors(componentProvider)
        registerHandler(BRIDGE_HANDLER_NAME, object : JsCallHandler {
            override fun request(data: Any?, callback: JsCallback?) {
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

    private fun handleData(jsonObject: JSONObject, callback: JsCallback) {
        val jsCallData = JsCallData()
        try {
            jsCallData.func = jsonObject.optString("func")
            jsCallData.params = jsonObject.optJSONObject("params").toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        proceed(jsCallData, callback)
    }

    fun proceed(callData: JsCallData, callback: JsCallback) {
        if (jsCallProcessors == null) return
        val msg: String
        val jsCallProcessor = jsCallProcessors!![callData.func]
        if (jsCallProcessor != null) {
            val handled = jsCallProcessor.process(callData, callback)
            msg = if (handled)
                String.format("%s Processor handled js call", jsCallProcessor.getFuncName())
            else
                String.format("%s Processor have not handled target js call", jsCallProcessor.getFuncName())
            LogUtil.info(msg)
        } else {
            msg = String.format("No JsCallProcessor can handle jsCall %s ", callData.func)
            LogUtil.error(msg)
            val defaultProcessor = jsCallProcessors!!["default"]
            if (defaultProcessor is DefaultProcessor) {
                defaultProcessor.setMsg(msg)
            }
            defaultProcessor?.process(callData, callback)
        }
    }

    fun handleActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (jsCallProcessors == null) return
        for (processor in jsCallProcessors!!.values) {
            val handled = processor.onActivityResult(requestCode, resultCode, data)

            if (handled) {
                val msg = String.format("Processor %s handled ActivityResult",
                        processor.getFuncName())
                LogUtil.info(msg)
                return
            }

        }
    }

    companion object {

        val BRIDGE_HANDLER_NAME = "JsBridgeCall"
    }


}
