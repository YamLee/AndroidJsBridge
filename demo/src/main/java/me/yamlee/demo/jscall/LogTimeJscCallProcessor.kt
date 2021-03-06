package me.yamlee.demo.jscall

import me.yamlee.jsbridge.BaseJsCallProcessor
import me.yamlee.jsbridge.JsCallData
import me.yamlee.jsbridge.NativeComponentProvider
import me.yamlee.jsbridge.JsCallback

/**
 * 打印时间的Js接口处理器
 * @author YamLee
 */
class LogTimeJscCallProcessor(componentProvider: NativeComponentProvider)
    : BaseJsCallProcessor(componentProvider) {

    companion object {
        const val FUNC_NAME = "logTime"
    }

    override fun getFuncName(): String {
        return FUNC_NAME
    }

    override fun onHandleJsRequest(callData: JsCallData, callback: JsCallback): Boolean {
        if (FUNC_NAME == callData.func) {
            val alertRequest = convertJsonToObject(callData.params!!, LogTimeRequest::class.java)
            componentProvider.provideWebLogicView().showAlert("Js时间", alertRequest.time!!)
            return true
        }
        return false
    }


    internal class LogTimeRequest {

        var time: String? = null
    }

}