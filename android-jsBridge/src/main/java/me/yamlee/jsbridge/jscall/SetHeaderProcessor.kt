package me.yamlee.jsbridge.jscall

import android.graphics.Color
import android.text.TextUtils
import me.yamlee.jsbridge.BaseJsCallProcessor
import me.yamlee.jsbridge.JsCallData
import me.yamlee.jsbridge.NativeComponentProvider

/**
 * 设置Web界面头部UI
 * @author YamLee
 */
class SetHeaderProcessor(provider: NativeComponentProvider) : BaseJsCallProcessor(provider) {
    companion object {
        const val LAYOUT_LEFT = "left"
        const val LAYOUT_MIDDLE = "middle"
        const val TRUE = "true"
        const val FALSE = "false"

        const val FUNC_NAME = "setHeader"

    }


    override fun getFuncName(): String {
        return FUNC_NAME
    }

    override fun onHandleJsQuest(callData: JsCallData?): Boolean {
        if (callData?.func == FUNC_NAME) {
            val request = convertJsonToObject(callData.params, SetHeaderRequest::class.java)
            val headerView = componentProvider.provideWebLogicView().headerView
            if (request.layout == LAYOUT_LEFT) {
                headerView.setLayoutStyleLeft()
            } else if (request.layout == LAYOUT_MIDDLE) {
                headerView.setLayoutStyleMiddle()
            }
            if (!TextUtils.isEmpty(request.title)) {
                if (request.title.startsWith("u")) {
                    val result = unicodeToString(request.title)
                    request.title = result
                }
                headerView.title = request.title
            }
            if (!TextUtils.isEmpty(request.titleColor)) {
                headerView.setTitleColor(Color.parseColor(request.titleColor))
            }

            headerView.showCloseBtn(request.showCloseBtn == TRUE)
            if (!TextUtils.isEmpty(request.closeBtnColor)) {
                headerView.setCloseBtnColor(Color.parseColor(request.closeBtnColor))
            }

            headerView.showBackBtn(request.showBackBtn == TRUE)
            if (!TextUtils.isEmpty(request.backBtnColor)) {
                headerView.setBackBtnColor(Color.parseColor(request.backBtnColor))
            }


            if (!TextUtils.isEmpty(request.bgColor)) {
                headerView.setBackgroundColor(Color.parseColor(request.bgColor))
            }

            if (!TextUtils.isEmpty(request.dividerColor)) {
                headerView.setDivideColor(Color.parseColor(request.dividerColor))
            }
            return true
        }
        return false
    }

    /**
     * 将unicode的汉字码转换成utf-8格式的汉字
     * @param unicode
     * @return
     */
    private fun unicodeToString(unicode: String): String {
        val string = StringBuffer()
        unicode.replace("0x", "\\")
                .split("u")
                .filter { it.isNotEmpty() }
                .forEach {
                    val data = Integer.parseInt(it, 16)
                    string.append(data.toChar())
                }
        return string.toString()
    }

    inner class SetHeaderRequest {
        var layout: String = ""
        var title: String = ""
        var titleColor: String = ""
        var showBackBtn: String = "true"
        var backBtnColor = ""
        var showCloseBtn: String = "false"
        var closeBtnColor = ""
        var bgColor: String = ""
        var dividerColor: String = ""
    }

}