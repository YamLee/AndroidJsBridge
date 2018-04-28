package me.yamlee.jsbridge

import android.content.Intent

import com.google.gson.Gson

/**
 * JsCall基础处理类，具体的JsCall接口处理类集成此接口
 *
 * @author yamlee
 */
abstract class BaseJsCallProcessor(protected var componentProvider: NativeComponentProvider) : JsCallProcessor {
    private val gson: Gson = Gson()

    override fun process(callData: JsCallData,
                         callback: WVJBWebViewClient.WVJBResponseCallback): Boolean {
        val handled = onHandleJsQuest(callData)
        var onResponsed = false
        if (handled) {
            onResponsed = onResponse(callback)
        }
        //如果子类处理这个请求但是没有做出回应，这里统一加上通用返回
        if (!onResponsed && handled) {
            val response = BaseJsCallResponse()
            response.ret = "OK"
            callback.callback(response)
        }
        return handled
    }

    fun destroy() {
        onDestroy()
    }

    open fun onHandleJsQuest(callData: JsCallData): Boolean {
        return false
    }

    open fun onResponse(callback: WVJBWebViewClient.WVJBResponseCallback): Boolean {
        return false
    }

    fun onDestroy() {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent): Boolean {
        return false
    }

    fun <T> convertJsonToObject(json: String, tClass: Class<T>): T {
        return gson.fromJson(json, tClass)
    }

    fun convertObjectString(jsonObject: Any): String {
        return gson.toJson(jsonObject)
    }

}