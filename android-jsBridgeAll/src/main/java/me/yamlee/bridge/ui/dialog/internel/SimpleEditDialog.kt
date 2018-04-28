package me.yamlee.bridge.ui.dialog.internel

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.text.InputFilter
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.TextView

import me.yamlee.bridge.ui.R
import timber.log.Timber

/**
 * 简单的带编辑提示框,基本样式为:标题,输入框,确认按钮
 *
 * @author yamlee
 */
class SimpleEditDialog : Dialog {
    private var mContext: Context? = null

    constructor(context: Context) : super(context) {}

    constructor(context: Context, themeResId: Int) : super(context, themeResId) {}

    protected constructor(context: Context, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener) : super(context, cancelable, cancelListener) {}

    fun setEditMsg(msg: String) {
        try {
            val editText = findViewById<EditText>(R.id.et_dialog_simple_edit_content)
            if (editText != null) {
                if (!TextUtils.isEmpty(msg)) {
                    editText.setText(msg)
                    editText.setSelection(msg.length)
                } else {
                    editText.setText("")

                }
            }
        } catch (e: IndexOutOfBoundsException) {
            //如果设置的edit最大长度setSelection可能会包indexOutOfBound异常
            Timber.e(e)
        }

    }

    fun setDialogTitlle(msg: String) {
        val title = findViewById<TextView>(R.id.tv_dialog_simple_edit_title)
        if (title != null) {
            if (!TextUtils.isEmpty(msg)) {
                title.text = msg
            }
        }
    }

    private fun initView(context: Context) {
        mContext = context
    }

    class Builder {
        private var title: String? = null
        private var confirmBtnText = ""
        private var editTextHint = ""
        private var canTouchOutDismiss: Boolean = false
        internal var mDialog: SimpleEditDialog? = null
        private var maxLength: Int = 0
        private var confirmClickListener: OnConfirmClickListener? = null


        fun setTouchOutDismiss(canTouchOutDismiss: Boolean): Builder {
            this.canTouchOutDismiss = canTouchOutDismiss
            return this
        }

        fun setEditInputMaxLength(length: Int): Builder {
            this.maxLength = length
            return this
        }

        fun setTitle(title: String): Builder {
            this.title = title
            return this
        }

        fun setEditHint(hintMsg: String): Builder {
            this.editTextHint = hintMsg
            return this
        }


        fun setConfirmBtnText(confirmBtnText: String): Builder {
            this.confirmBtnText = confirmBtnText
            return this

        }

        fun setConfirmClickListener(confirmClickListener: OnConfirmClickListener): Builder {
            this.confirmClickListener = confirmClickListener
            return this
        }

        fun build(context: Context): SimpleEditDialog {

            val tvTitle: TextView
            val tvConfirm: TextView

            val mDialogView: View
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            mDialog = SimpleEditDialog(context, R.style.publish_dialog)
            mDialogView = inflater.inflate(
                    R.layout.view_dialog_simple_edit, null)
            val editText = mDialogView.findViewById<View>(R.id.et_dialog_simple_edit_content) as EditText
            if (maxLength != 0) {
                val inputFilters = arrayOfNulls<InputFilter>(1)
                inputFilters[0] = InputFilter.LengthFilter(maxLength)
                editText.filters = inputFilters
            }
            if (!TextUtils.isEmpty(editTextHint)) {
                editText.hint = editTextHint
            }
            tvTitle = mDialogView.findViewById<View>(R.id.tv_dialog_simple_edit_title) as TextView
            if (TextUtils.isEmpty(title)) {
                title = context.getString(R.string.common_dialog_title)
            }
            tvTitle.text = title

            tvConfirm = mDialogView.findViewById<View>(R.id.tv_dialog_simple_edit_confirm) as TextView
            if (TextUtils.isEmpty(confirmBtnText)) {
                confirmBtnText = context.getString(R.string.confirm)
            }
            tvConfirm.text = confirmBtnText
            tvConfirm.setOnClickListener {
                if (confirmClickListener != null) {
                    confirmClickListener!!.onConfirm(editText.text.toString())
                }
            }

            mDialog!!.setContentView(mDialogView)
            mDialog!!.setCanceledOnTouchOutside(canTouchOutDismiss)
            return mDialog!!
        }

        interface OnConfirmClickListener {
            fun onConfirm(editMsg: String)
        }
    }

    companion object {

        fun builder(): SimpleEditDialog.Builder {
            return Builder()
        }
    }

}
