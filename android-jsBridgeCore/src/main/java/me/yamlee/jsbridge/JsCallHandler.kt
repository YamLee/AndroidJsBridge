package me.yamlee.jsbridge

interface JsCallHandler {
    fun request(data: Any?, callback: JsCallback?)
}