package me.yamlee.jsbridge.jscall

import android.net.Uri
import android.text.TextUtils
import android.view.View
import me.yamlee.jsbridge.BaseJsCallProcessor
import me.yamlee.jsbridge.JsCallData
import me.yamlee.jsbridge.NativeComponentProvider

/**
 * 设置Web界面头部UI
 * @author YamLee
 */
class SetHeaderMenuProcessor(provider: NativeComponentProvider) : BaseJsCallProcessor(provider) {
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
                headerView.setTitle(request.title)
            }

            if (!TextUtils.isEmpty(request.rightBtn)) {
                if (request.rightBtn.startsWith("http://")) {
                    headerView.showRightBtn(Uri.parse(request.rightBtn), View.OnClickListener {})
                } else {

                }
            } else if (request.rightMenus.size > 0) {

            } else {
                headerView.hideRightBtn()
            }
            return true
        }
        return false
    }

    inner class SetHeaderRequest {
        var layout: String = ""
        var title: String = ""
        var showBackBtn: String = "true"
        var showCloseBtn: String = "false"
        var rightMenus: ArrayList<String> = ArrayList()
        var rightBtn: String = ""
    }

}