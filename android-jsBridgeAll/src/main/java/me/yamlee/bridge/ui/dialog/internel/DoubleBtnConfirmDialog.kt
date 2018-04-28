package me.yamlee.bridge.ui.dialog.internel

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Paint
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView

import me.yamlee.bridge.ui.R
import me.yamlee.jsbridge.utils.ScreenUtil


/**
 * 通用确认和取消按钮提示弹框
 *
 * @author yamlee
 */
class DoubleBtnConfirmDialog : Dialog {
    constructor(context: Context) : super(context) {}

    constructor(context: Context, themeResId: Int) : super(context, themeResId) {}


    class Builder {
        private var title: String? = null
        private var msg: String? = null
        private var confirmBtnText = ""
        private var cancelBtnText = ""
        private var canTouchOutDismiss = true
        private var doubleBtnClickListener: DoubleBtnClickListener? = null
        private var mDialog: Dialog? = null
        private var enhanceConfirm = EnhanceBtn.ENHANCE_CONFIRM
        private var mDialogView: View? = null

        fun setTouchOutDismiss(canTouchOutDismiss: Boolean): Builder {
            this.canTouchOutDismiss = canTouchOutDismiss
            return this
        }

        fun setDoubleBtnClickListener(doubleBtnClickListener: DoubleBtnClickListener): Builder {
            this.doubleBtnClickListener = doubleBtnClickListener
            return this
        }

        fun setTitle(title: String): Builder {
            this.title = title
            return this
        }

        fun setMsg(msg: String): Builder {
            this.msg = msg
            return this
        }

        fun setConfirmBtnText(confirmBtnText: String): Builder {
            this.confirmBtnText = confirmBtnText
            return this
        }

        fun setCancelBtnText(cancelBtnText: String): Builder {
            this.cancelBtnText = cancelBtnText
            return this
        }

        fun setEnhanceConfirm(@EnhanceBtn.EnhanceBtnDef enhanceState: Int): Builder {
            this.enhanceConfirm = enhanceState
            return this
        }

        fun setDialogView(mDialogView: View): Builder {
            this.mDialogView = mDialogView
            return this
        }

        fun build(context: Context): Dialog {
            val tvTitle: TextView
            val tvMsg: TextView
            val tvConfirm: TextView
            val tvCancel: TextView
            if (TextUtils.isEmpty(confirmBtnText)) {
                confirmBtnText = context.getString(R.string.text_confirm)
            }
            if (TextUtils.isEmpty(cancelBtnText)) {
                cancelBtnText = context.getString(R.string.text_cancel)
            }
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            mDialog = Dialog(context, R.style.publish_dialog)
            if (mDialogView == null) {
                mDialogView = inflater.inflate(R.layout.dialog_common_double, null)
            }
            tvTitle = mDialogView!!.findViewById<View>(R.id.dialog_tv_title) as TextView
            if (TextUtils.isEmpty(title)) {
                tvTitle.visibility = View.GONE
            }
            tvTitle.text = title
            tvMsg = mDialogView!!.findViewById<View>(R.id.dialog_tv_content) as TextView
            val msgPaint = tvMsg.paint
            if (msgPaint != null) {
                val msgLength = msgPaint.measureText(msg.toString())
                val tvMsgWidth = ScreenUtil.getScreenWidth(context) - ScreenUtil.dip2px(context, 40f)
                if (msgLength < tvMsgWidth) {
                    tvMsg.gravity = Gravity.CENTER or Gravity.CENTER_VERTICAL
                }
            }
            tvMsg.text = msg
            tvConfirm = mDialogView!!.findViewById<View>(R.id.dialog_tv_confirm) as TextView
            tvConfirm.text = confirmBtnText
            tvConfirm.setOnClickListener {
                if (doubleBtnClickListener != null) {
                    doubleBtnClickListener!!.onConfirm()
                }
                mDialog!!.dismiss()
            }
            tvCancel = mDialogView!!.findViewById<View>(R.id.dialog_tv_cancel) as TextView
            tvCancel.text = cancelBtnText
            tvCancel.setOnClickListener {
                if (doubleBtnClickListener != null) {
                    doubleBtnClickListener!!.onCancel()
                }
                mDialog!!.dismiss()
            }
            when (enhanceConfirm) {
                EnhanceBtn.ENHANCE_NONE -> {
                    tvConfirm.setBackgroundResource(R.drawable.shape_orange_corner_edge)
                    tvConfirm.setTextColor(context.resources.getColor(R.color.palette_orange))
                    tvCancel.setBackgroundResource(R.drawable.shape_orange_corner_edge)
                    tvCancel.setTextColor(context.resources.getColor(R.color.palette_orange))
                }
                EnhanceBtn.ENHANCE_CONFIRM -> {
                    tvConfirm.setBackgroundResource(R.drawable.shape_orange_corner_solid)
                    tvConfirm.setTextColor(context.resources.getColor(R.color.palette_white))
                    tvCancel.setBackgroundResource(R.drawable.shape_orange_corner_edge)
                    tvCancel.setTextColor(context.resources.getColor(R.color.palette_orange))
                }
                EnhanceBtn.ENHANCE_CANCEL -> {
                    tvConfirm.setBackgroundResource(R.drawable.shape_orange_corner_edge)
                    tvConfirm.setTextColor(context.resources.getColor(R.color.palette_orange))
                    tvCancel.setBackgroundResource(R.drawable.shape_orange_corner_solid)
                    tvCancel.setTextColor(context.resources.getColor(R.color.palette_white))
                }
            }
            mDialog!!.setCanceledOnTouchOutside(canTouchOutDismiss)
            mDialog!!.setContentView(mDialogView!!)
            return mDialog!!
        }
    }

    interface DoubleBtnClickListener {
        fun onConfirm()

        fun onCancel()
    }

    companion object {

        fun builder(): DoubleBtnConfirmDialog.Builder {
            return Builder()
        }
    }
}
