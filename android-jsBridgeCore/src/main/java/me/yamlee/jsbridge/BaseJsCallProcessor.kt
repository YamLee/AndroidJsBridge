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
                         callback: JsCallback): Boolean {
        val handled = onHandleJsQuest(callData, callback)
        //如果子类处理这个请求但是没有做出回应，这里统一加上通用返回
        if (!onHandleJsResponse()) {
            val response = BaseJsCallResponse()
            response.ret = "OK"
            callback.callback(response)
        }
        return handled
    }

    fun destroy() {
        onDestroy()
    }

    abstract fun onHandleJsQuest(callData: JsCallData, callback: JsCallback): Boolean

    open fun onHandleJsResponse(): Boolean {
        return false
    }

    override fun onDestroy() {
        //do nothing now
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
