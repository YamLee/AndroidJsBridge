package me.yamlee.demo.jscall

import me.yamlee.jsbridge.BaseJsCallProcessor
import me.yamlee.jsbridge.JsCallData
import me.yamlee.jsbridge.NativeComponentProvider

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

    override fun onHandleJsQuest(callData: JsCallData): Boolean {
        if (FUNC_NAME == callData.func) {
            val alertRequest = convertJsonToObject(callData.params!!, LogTimeRequest::class.java)
            componentProvider.provideWebLogicView().showAlert("Js时间", alertRequest.time!!)
            return true
        }
        return false
    }


    internal class LogTimeRequest {
        /**
         * title : title
         * msg : 测试弹出消息
         */
        var time: String? = null
    }

}