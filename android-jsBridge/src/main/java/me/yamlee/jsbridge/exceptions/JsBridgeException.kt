package me.yamlee.jsbridge.exceptions

class JsBridgeException(val msg: String) : Exception() {
    override val message: String?
        get() = msg
}