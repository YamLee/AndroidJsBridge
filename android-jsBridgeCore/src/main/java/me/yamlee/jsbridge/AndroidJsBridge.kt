package me.yamlee.jsbridge

import java.util.HashMap

object AndroidJsBridge {
    private var jsCallProcessors: MutableMap<String, JsCallProcessor>? = null
    internal var mDebug: Boolean = false

    fun registerJsCallProcessor(callHandler: JsCallProcessor) {
        if (jsCallProcessors == null) {
            jsCallProcessors = HashMap()
        }
        jsCallProcessors!![callHandler.getFuncName()] = callHandler
    }

    /**
     * if set debug true,JsBridge framework will log
     * some debug info
     */
    fun setDebug(isDebug: Boolean): AndroidJsBridge {
        mDebug = isDebug
        return this
    }
}