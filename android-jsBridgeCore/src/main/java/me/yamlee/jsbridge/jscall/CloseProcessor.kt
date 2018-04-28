package me.yamlee.jsbridge.jscall


import org.json.JSONException
import org.json.JSONObject

import me.yamlee.jsbridge.BaseJsCallProcessor
import me.yamlee.jsbridge.JsCallData
import me.yamlee.jsbridge.NativeComponentProvider
import me.yamlee.jsbridge.ui.WebActionView

/**
 * Js调用界面关闭
 *
 * @author yamlee
 */
class CloseProcessor(componentProvider: NativeComponentProvider) : BaseJsCallProcessor(componentProvider) {
    private var nearInteraction: WebActionView.WebLogicListener = componentProvider.provideWebInteraction()

    override fun onHandleJsQuest(callData: JsCallData): Boolean {
        if (FUNC_NAME == callData.func) {
            try {
                val jsonObject = JSONObject(callData.params)
                val type = jsonObject.optString("type")
                if ("1" == type) {
                    nearInteraction.finishActivity()
                } else if ("2" == type) {
                    nearInteraction.clearTopWebActivity()
                } else {
                    nearInteraction.finishActivity()
                }
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

        const val FUNC_NAME = "close"
    }
}