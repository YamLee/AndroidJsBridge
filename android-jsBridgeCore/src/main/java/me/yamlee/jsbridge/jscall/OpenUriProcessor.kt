package me.yamlee.jsbridge.jscall


import me.yamlee.jsbridge.BaseJsCallProcessor
import me.yamlee.jsbridge.JsCallData
import me.yamlee.jsbridge.NativeComponentProvider
import me.yamlee.jsbridge.JsCallback
import org.json.JSONException
import org.json.JSONObject

/**
 * Activity will go to the activity which the uri target
 * @author yamlee
 */
class OpenUriProcessor(componentProvider: NativeComponentProvider) : BaseJsCallProcessor(componentProvider) {

    override fun onHandleJsQuest(callData: JsCallData, callback: JsCallback): Boolean {
        if (FUNC_NAME == callData.func) {
            var jsonObject: JSONObject? = null
            try {
                jsonObject = JSONObject(callData.params)
                val uri = jsonObject.get("uri") as String
                val interaction = componentProvider.provideWebInteraction()
                val context = componentProvider.provideApplicationContext()
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

        const val FUNC_NAME = "openUri"
    }
}
