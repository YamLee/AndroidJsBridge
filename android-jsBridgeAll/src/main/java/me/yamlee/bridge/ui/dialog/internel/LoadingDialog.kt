package me.yamlee.bridge.ui.dialog.internel

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView

import me.yamlee.bridge.ui.R


/**
 * 自定义loading,默认为包外不可直接访问,需要通过DialogFactory来使用
 *
 * @author yamlee
 */
class LoadingDialog : Dialog {
    private var mContentView: View? = null

    constructor(context: Context, view: View) : super(context) {
        initView(context, view)
    }

    constructor(context: Context, theme: Int, view: View) : super(context, theme) {
        initView(context, view)
    }

    private fun initView(context: Context, view: View) {
        mContentView = view
    }

    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        if (mContentView != null) {
            setContentView(mContentView!!)
        }
    }

    class Builder {
        private var msg = ""
        private var canTouchOutDismiss = false

        fun setMsg(msg: String): Builder {
            this.msg = msg
            return this
        }

        fun setTouchOutDismiss(canTouchOutDismiss: Boolean): Builder {
            this.canTouchOutDismiss = canTouchOutDismiss
            return this
        }

        fun build(context: Context): Dialog {
            val view = LayoutInflater.from(context).inflate(R.layout.layout_loading_dialog, null)
            val tvMsg = view.findViewById<View>(R.id.tv_loading_msg) as TextView
            tvMsg.text = this.msg
            val loadingDialog = LoadingDialog(context, R.style.Theme_Near_CustomProgressDialog, view)
            loadingDialog.setCanceledOnTouchOutside(canTouchOutDismiss)
            return loadingDialog
        }
    }

    companion object {

        fun builder(): LoadingDialog.Builder {
            return Builder()
        }
    }
}
