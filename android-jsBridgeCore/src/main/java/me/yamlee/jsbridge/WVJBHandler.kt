package me.yamlee.jsbridge

interface WVJBHandler {
    fun request(data: Any?, callback: WVJBResponseCallback?)
}