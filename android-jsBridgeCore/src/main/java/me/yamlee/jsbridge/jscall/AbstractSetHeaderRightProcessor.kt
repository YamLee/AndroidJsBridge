package me.yamlee.jsbridge.jscall

import android.net.Uri
import android.text.TextUtils
import android.view.View
import me.yamlee.jsbridge.*

/**
 * 设置Web界面头部UI
 * @author YamLee
 */
abstract class AbstractSetHeaderRightProcessor(provider: NativeComponentProvider) : BaseJsCallProcessor(provider) {
    companion object {

        const val FUNC_NAME = "setHeaderRight"
    }

    private var mCallback: WVJBWebViewClient.WVJBResponseCallback? = null

    override fun getFuncName(): String {
        return FUNC_NAME
    }

    override fun onHandleJsQuest(callData: JsCallData?): Boolean {
        if (callData?.func == FUNC_NAME) {
            val request = convertJsonToObject(callData.params, SetHeaderRightRequest::class.java)
            val headerView = componentProvider.provideWebLogicView().headerView

            if (!TextUtils.isEmpty(request.rightBtn)) {
                if (request.rightBtn.startsWith("http://")) {
                    headerView.showRightBtn(Uri.parse(request.rightBtn), View.OnClickListener { onClickRightBtn(request.clickJumpUrl) })
                } else {
                    headerView.showRightBtn(request.rightBtn, View.OnClickListener { onClickRightBtn(request.clickJumpUrl) })
                }
            }
            return true
        }
        return false
    }

    abstract fun onClickRightBtn(clickJumpUrl: String)


    inner class SetHeaderRightRequest {
        var rightBtn: String = ""
        var clickJumpUrl: String = ""
    }

}