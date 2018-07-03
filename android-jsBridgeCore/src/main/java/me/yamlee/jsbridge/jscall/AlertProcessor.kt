package me.yamlee.jsbridge.jscall


import me.yamlee.jsbridge.BaseJsCallProcessor
import me.yamlee.jsbridge.JsCallData
import me.yamlee.jsbridge.NativeComponentProvider
import me.yamlee.jsbridge.JsCallback
import me.yamlee.jsbridge.ui.WebActionView

/**
 * Js调用原生显示弹框处理
 *
 * @author yamlee
 */
class AlertProcessor(componentProvider: NativeComponentProvider) : BaseJsCallProcessor(componentProvider) {
    private val nearWebLogicView: WebActionView = componentProvider.provideWebLogicView()

    override fun onHandleJsQuest(callData: JsCallData, callback: JsCallback): Boolean {
        if (FUNC_NAME == callData.func) {
            val alertRequest = convertJsonToObject(callData.params!!, AlertRequest::class.java)
            nearWebLogicView.showAlert(alertRequest.title!!, alertRequest.msg!!)
            return true
        }
        return false
    }

    override fun getFuncName(): String {
        return FUNC_NAME
    }


    internal class AlertRequest {
        /**
         * title : title
         * msg : 测试弹出消息
         */
        var title: String? = null
        var msg: String? = null
    }

    companion object {
        const val FUNC_NAME = "alert"
    }

}
