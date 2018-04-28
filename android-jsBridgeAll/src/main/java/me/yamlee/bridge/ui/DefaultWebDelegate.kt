package me.yamlee.bridge.ui

import android.app.Activity
import android.view.LayoutInflater
import me.yamlee.bridge.ui.dialog.BridgeDialogFactory
import me.yamlee.bridge.ui.view.WebHeaderView
import me.yamlee.jsbridge.ui.BridgeActivityDelegate
import me.yamlee.jsbridge.ui.WebHeader

open class DefaultWebDelegate(private val mActivity: Activity) : BridgeActivityDelegate(mActivity) {

    override fun onCreateWebHeader(inflater: LayoutInflater): WebHeader {
        val view = inflater.inflate(R.layout.layout_web_header, null, false)
        return view as WebHeaderView
    }


    override fun showAlert(title: String, content: String) {
        BridgeDialogFactory.getSingleBtnDialogBuilder()
                .setTitle(title)
                .setMsg(content)
                .build(mActivity).show()
    }

    override fun showLoading(msg: String) {
        if (loadingDialog != null && loadingDialog!!.isShowing) {
            loadingDialog!!.dismiss()
        }
        loadingDialog = BridgeDialogFactory.getLoadingDialogBuilder()
                .setMsg(msg)
                .setTouchOutDismiss(false)
                .build(mActivity)
        loadingDialog!!.show()
    }

}