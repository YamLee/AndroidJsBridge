package me.yamlee.jsbridge

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Build
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.google.gson.Gson
import me.yamlee.jsbridge.jscall.*
import me.yamlee.jsbridge.utils.LogUtil
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
import java.util.*

/**
 * 构造器
 *
 * @param messageHandler 默认handler
 */
@SuppressLint("SetJavaScriptEnabled", "NewApi")
open class BridgeWebViewClient constructor(private val webView: WebView,
                                           private val messageHandler: JsCallHandler?,
                                           private val componentProvider: NativeComponentProvider) : WebViewClient() {
    //native发送给h5的数据以队列形式存放
    private var startupMessageQueue: ArrayList<WVJBMessage>? = null
    private var responseCallbacks: MutableMap<String, JsCallback>? = null
    private var messageHandlers: MutableMap<String, JsCallHandler>? = null
    private var uniqueId: Long = 0
    private val myInterface = MyJavascriptInterface()
    private var jsCallProcessors: MutableMap<String, JsCallProcessor>? = null

    init {
        this.webView.settings.javaScriptEnabled = true
        //此处不应加版本判断
        //        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        this.webView.addJavascriptInterface(myInterface, kInterface)
        //        }
        this.responseCallbacks = HashMap()
        this.messageHandlers = HashMap()
        this.startupMessageQueue = ArrayList()
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


    fun enableLogging() {
        logging = true
    }

    /**
     * 注册H5向native发请求Handler(native等待h5指令做出响应)
     *
     * @param handlerName 双方协议handler名称
     * @param handler     请求handler
     */
    protected fun registerHandler(handlerName: String?, handler: JsCallHandler?) {
        if (handlerName == null || handlerName.length == 0 || handler == null)
            return
        messageHandlers!![handlerName] = handler
    }

    /**
     * onPageFinished前发送，页面初始化未完成前发送数据，以队列形式存储到startupMessageQueue待onPageFinished统一dispatch
     * onPageFinished后发送，直接dispatch
     *
     * @param message 发送的数据
     */
    private fun queueMessage(message: WVJBMessage) {
        if (startupMessageQueue != null) {
            startupMessageQueue!!.add(message)
        } else {
            dispatchMessage(message)
        }
    }

    private fun dispatchMessage(message: WVJBMessage) {
        val messageJSON = message2JSONObject(message).toString()
                .replace("\\\\".toRegex(), "\\\\\\\\")
                .replace("\"".toRegex(), "\\\\\"")
                .replace("\'".toRegex(), "\\\\\'")
                .replace("\n".toRegex(), "\\\\\n")
                .replace("\r".toRegex(), "\\\\\r")
                //kotlin unSupport "\f" using unicode for replace
                .replace("\\u000C".toRegex(), "\\\\\\u000C")

        log("SEND", messageJSON)
        executeJavascript("WebViewJavascriptBridge._handleMessageFromObjC('"
                + messageJSON + "');")
    }

    private fun flushMessageQueue() {
        val script = "WebViewJavascriptBridge._fetchQueue()"
        executeJavascript(script, object : JavascriptCallback {
            override fun onReceiveValue(messageQueueString: String?) {
                val bytes = Base64.decode(messageQueueString!!.toByteArray(Charset.forName("UTF-8")), Base64.DEFAULT)
                var data = ""
                try {
                    data = String(bytes, Charset.forName("UTF-8"))
                } catch (e: UnsupportedEncodingException) {
                    e.printStackTrace()
                }

                if (TextUtils.isEmpty(data)) return
                processQueueMessage(data)
            }
        })
    }

    private fun processQueueMessage(messageQueueString: String) {
        try {
            val messages = JSONArray(messageQueueString)
            for (i in 0 until messages.length()) {
                val jo = messages.getJSONObject(i)
                log("RCVD", jo)
                val message = JSONObject2WVJBMessage(jo)
                if (message.responseId != null) {
                    val responseCallback = responseCallbacks!!.remove(message.responseId.toString())
                    responseCallback?.callback(message.responseData)
                } else {
                    var responseCallback: JsCallback? = null
                    if (message.callbackId != null) {
                        val callbackId = message.callbackId
                        responseCallback = object : JsCallback {
                            override var handled: Boolean = false

                            override fun callback(data: Any?) {
                                val msg = WVJBMessage()
                                msg.responseId = callbackId
                                //返回的data需要时string类型
                                if (data is String || data is JSONObject) {
                                    msg.responseData = data
                                } else {
                                    val gson = Gson()
                                    msg.responseData = gson.toJson(data)
                                }
                                queueMessage(msg)
                            }
                        }
                    }
                    val handler: JsCallHandler?
                    if (message.handlerName != null) {
                        handler = messageHandlers!![message.handlerName!!]
                    } else {
                        handler = messageHandler
                    }
                    handler?.request(message.data, responseCallback)
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

    }

    /**
     * 执行JS语句
     *
     * @param script   待执行的js
     * @param callback js执行完之后返回的结果
     */
    private fun executeJavascript(script: String,
                                  callback: JavascriptCallback? = null) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.evaluateJavascript(script) { value ->
                var value = value
                if (callback != null) {
                    if (value != null && value.startsWith("\"")
                            && value.endsWith("\"")) {
                        value = value.substring(1, value.length - 1)
                                .replace("\\\\".toRegex(), "")
                    }
                    callback.onReceiveValue(value)
                }
            }
        } else {
            if (callback != null) {
                myInterface.addCallback((++uniqueId).toString() + "", callback)
                //JS调用Java window.jsInterfaceName.methodName(parameterValues)
                //Java调用JS loadUrl(javascript:methodName(parameterValues))
                webView.loadUrl("javascript:window." + kInterface
                        + ".onResultForScript(" + uniqueId + "," + script + ")")
            }//执行WebViewJavascriptBridge的js代码
            else {
                webView.loadUrl("javascript:$script")
            }

        }
    }

    override fun onPageFinished(view: WebView, url: String) {
        super.onPageFinished(view, url)
        LoadBridgeAsyncTask(webView.context).execute()
    }

    override fun onLoadResource(view: WebView, url: String) {
        super.onLoadResource(view, url)
    }

    private inner class LoadBridgeAsyncTask internal constructor(private val context: Context) : AsyncTask<Void, Void, String>() {

        override fun doInBackground(vararg params: Void): String? {
            try {
                val `is` = context.assets
                        .open("WebViewJavascriptBridge.js")
                val size = `is`.available()
                val buffer = ByteArray(size)
                `is`.read(buffer)
                `is`.close()
                return String(buffer)
            } catch (e: IOException) {
                return null
            }

        }

        override fun onPostExecute(jsBridge: String?) {
            super.onPostExecute(jsBridge)
            if (jsBridge != null) {
                executeJavascript(jsBridge)
                if (startupMessageQueue != null) {
                    for (i in startupMessageQueue!!.indices) {
                        dispatchMessage(startupMessageQueue!![i])
                    }
                    startupMessageQueue = null
                }
            } else {
                Toast.makeText(context, "加载js引擎出错,请退出页面重试!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //接收h5发送的数据
    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        if (url.startsWith(kCustomProtocolScheme)) {
            if (url.indexOf(kQueueHasMessage) > 0) {
                flushMessageQueue()
            }
            return true
        }
        return super.shouldOverrideUrlLoading(view, url)
    }

    private fun message2JSONObject(message: WVJBMessage): JSONObject {
        val jo = JSONObject()
        try {
            if (message.callbackId != null) {
                jo.put("callbackId", message.callbackId)
            }
            if (message.data != null) {
                jo.put("data", message.data)
            }
            if (message.handlerName != null) {
                jo.put("handlerName", message.handlerName)
            }
            if (message.responseId != null) {
                jo.put("responseId", message.responseId)
            }
            if (message.responseData != null) {
                jo.put("responseData", message.responseData)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return jo
    }

    private fun JSONObject2WVJBMessage(jo: JSONObject): WVJBMessage {
        val message = WVJBMessage()
        try {
            if (jo.has("callbackId")) {
                message.callbackId = jo.getString("callbackId")
            }
            if (jo.has("data")) {
                message.data = jo.get("data")
            }
            if (jo.has("handlerName")) {
                message.handlerName = jo.getString("handlerName")
            }
            if (jo.has("responseId")) {
                message.responseId = jo.getString("responseId")
            }
            if (jo.has("responseData")) {
                message.responseData = jo.get("responseData")
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return message
    }

    private inner class WVJBMessage {
        internal var data: Any? = null
        internal var callbackId: String? = null
        internal var handlerName: String? = null
        internal var responseId: String? = null
        internal var responseData: Any? = null
    }

    private inner class MyJavascriptInterface {
        internal var map: MutableMap<String, JavascriptCallback> = HashMap()

        fun addCallback(key: String, callback: JavascriptCallback) {
            map[key] = callback
        }

        @JavascriptInterface
        fun onResultForScript(key: String, value: String) {
            Log.i(kTag, "onResultForScript: $value")
            (webView.context as Activity).runOnUiThread {
                val callback = map.remove(key)
                callback?.onReceiveValue(value)
            }
        }
    }

    private interface JavascriptCallback {
        fun onReceiveValue(value: String?)

    }

    private fun log(action: String, json: Any) {
        if (!logging)
            return
        val jsonString = json.toString()
        if (jsonString.length > 500) {
            Log.i(kTag, action + ": " + jsonString.substring(0, 500) + " [...]")
        } else {
            Log.i(kTag, "$action: $jsonString")
        }
    }

    companion object {
        private val kTag = "WVJB"
        private val kInterface = kTag + "Interface"
        private val kCustomProtocolScheme = "wvjbscheme"
        private val kQueueHasMessage = "__WVJB_QUEUE_MESSAGE__"
        private var logging = false
        val BRIDGE_HANDLER_NAME = "JsBridgeCall"
    }
}
