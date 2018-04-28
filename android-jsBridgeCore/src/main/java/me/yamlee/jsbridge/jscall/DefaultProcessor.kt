package me.yamlee.jsbridge.jscall

import me.yamlee.jsbridge.BaseJsCallProcessor
import me.yamlee.jsbridge.NativeComponentProvider

/**
 * 默认JsBridge call处理，当没有找到指定func_name的处理者是，使用默认处理
 * @author LiYan
 */
class DefaultProcessor(val provider: NativeComponentProvider) : BaseJsCallProcessor(provider) {
    companion object {
        const val FUNC_NAME = "default"
    }

    override fun getFuncName(): String {
        return FUNC_NAME
    }

}