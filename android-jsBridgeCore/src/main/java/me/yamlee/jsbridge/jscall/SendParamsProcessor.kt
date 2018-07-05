package me.yamlee.jsbridge.jscall

import me.yamlee.jsbridge.BaseJsCallProcessor
import me.yamlee.jsbridge.JsCallData
import me.yamlee.jsbridge.JsCallback
import me.yamlee.jsbridge.NativeComponentProvider
import me.yamlee.jsbridge.utils.LogUtil
import org.json.JSONException
import org.json.JSONObject

/**
 * Js获取原生传过来的参数
 *
 * @author yamlee
 */
class SendParamsProcessor(componentProvider: NativeComponentProvider) : BaseJsCallProcessor(componentProvider) {

    override fun onHandleJsQuest(callData: JsCallData, callback: JsCallback): Boolean {
        if (FUNC_NAME == callData.func) {
            val params = callData.params
            try {
                val jsonObject = JSONObject(params)
                val module = jsonObject.optString("module")
                LogUtil.info("module is$module")
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            return true
        }
        return false
    }

    override fun getFuncName(): String {
        return FUNC_NAME
    }

    companion object {
        const val FUNC_NAME = "getParams"
    }
}
