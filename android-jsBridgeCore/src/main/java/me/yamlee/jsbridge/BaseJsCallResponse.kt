package me.yamlee.jsbridge

/**
 * Native return content
 * @author yamlee
 */
open class BaseJsCallResponse {
    var ret: String? = ResponseCode.SUCCESS
    var msg: String? = ""
}
