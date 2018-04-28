package me.yamlee.jsbridge.jscall

import org.json.JSONException
import org.json.JSONObject

import me.yamlee.jsbridge.BaseJsCallProcessor
import me.yamlee.jsbridge.JsCallData
import me.yamlee.jsbridge.NativeComponentProvider
import me.yamlee.jsbridge.WVJBWebViewClient
import timber.log.Timber

/**
 * Js获取原生传过来的参数
 *
 * @author yamlee
 */
class SendParamsProcessor(componentProvider: NativeComponentProvider) : BaseJsCallProcessor(componentProvider) {

    override fun onHandleJsQuest(callData: JsCallData): Boolean {
        if (FUNC_NAME == callData.func) {
            val params = callData.params
            try {
                val jsonObject = JSONObject(params)
                val module = jsonObject.optString("module")
                Timber.i("module is$module")
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            return true
        }
        return false
    }

    override fun onResponse(callback: WVJBWebViewClient.WVJBResponseCallback): Boolean {
        callback.callback("")
        return true
    }

    override fun getFuncName(): String {
        return FUNC_NAME
    }

    companion object {
        const val FUNC_NAME = "getParams"
    }
}
