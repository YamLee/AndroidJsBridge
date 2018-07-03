package me.yamlee.jsbridge

interface WVJBResponseCallback {
    var handled: Boolean

    fun callback(data: Any?)
}