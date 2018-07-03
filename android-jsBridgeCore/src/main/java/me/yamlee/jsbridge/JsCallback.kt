package me.yamlee.jsbridge

interface JsCallback {
    var handled: Boolean

    fun callback(data: Any?)
}