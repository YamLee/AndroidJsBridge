package me.yamlee.jsbridge.jscall

import org.json.JSONException
import org.json.JSONObject

import me.yamlee.jsbridge.BaseJsCallProcessor
import me.yamlee.jsbridge.JsCallData
import me.yamlee.jsbridge.NativeComponentProvider
import me.yamlee.jsbridge.JsCallback
import me.yamlee.jsbridge.ui.WebActionView

/**
 * Support Js call native toast
 *
 * @author yamlee
 */
class ToastProcessor(componentProvider: NativeComponentProvider) : BaseJsCallProcessor(componentProvider) {
    private var view: WebActionView? = null

    override fun onHandleJsRequest(callData: JsCallData, callback: JsCallback): Boolean {
        if (FUNC_NAME == callData.func) {
            view = componentProvider.provideWebLogicView()
            var jsonObject: JSONObject? = null
            try {
                jsonObject = JSONObject(callData.params)
                val msg = jsonObject.get("msg") as String
                view!!.showToast(msg)
                return true
            } catch (e: JSONException) {
                e.printStackTrace()
            }

        }
        return false
    }

    override fun getFuncName(): String {
        return FUNC_NAME
    }

    companion object {
        const val FUNC_NAME = "toast"
    }
}
