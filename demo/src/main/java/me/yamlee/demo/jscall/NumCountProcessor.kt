package me.yamlee.demo.jscall

import android.os.Handler
import me.yamlee.demo.bridge.CustomComponentProvider
import me.yamlee.jsbridge.BaseJsCallProcessor
import me.yamlee.jsbridge.BaseJsCallResponse
import me.yamlee.jsbridge.JsCallData
import me.yamlee.jsbridge.JsCallback

class NumCountProcessor(provider: CustomComponentProvider) : BaseJsCallProcessor(provider) {
    override fun getFuncName(): String {
        return "numCount"
    }

    override fun onHandleJsQuest(callData: JsCallData, callback: JsCallback): Boolean {
        val request = convertJsonToObject(callData.params ?: "", NumCountReq::class.java)
        val response = NumCountResponse()
        response.result = request.count
        response.ret = "ok"
        val handler = Handler()
        Thread(Runnable {
            Thread.sleep(2000)
            handler.post {
                callback.callback(response)
            }
        }).start()
        return true
    }

    override fun onHandleJsResponse(): Boolean {
        return true
    }

    internal class NumCountReq {
        var count: String = "0"
    }

    internal class NumCountResponse : BaseJsCallResponse() {
        var result: String = "0"
    }

}