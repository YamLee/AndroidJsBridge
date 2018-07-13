package me.yamlee.jsbridge.jscall

import me.yamlee.jsbridge.*

/**
 * 默认JsBridge call处理，当没有找到指定func_name的处理者是，使用默认处理
 * @author LiYan
 */
class DefaultProcessor(val provider: NativeComponentProvider) : BaseJsCallProcessor(provider) {
    private var msg: String = ""

    override fun getFuncName(): String {
        return "default"
    }

    fun setMsg(msg: String) {
        this.msg = msg
    }

    override fun onHandleJsRequest(callData: JsCallData, callback: JsCallback): Boolean {
        val response = BaseJsCallResponse()
        response.ret = ResponseCode.UNSUPPORTED
        response.msg = this.msg
        callback.callback(response)
        return true
    }

    override fun onHandleJsResponse(): Boolean {
        return true
    }

}