package me.yamlee.jsbridge.utils

import android.app.Activity
import android.content.ClipData
import android.content.Context
import android.os.Build
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 * 输入法工具类,如:打开关闭软键盘,复制,粘帖文字等
 *
 * @author yamlee
 */
object InputTypeUtil {
    private val TAG = "InputTypeUtil"

    /**
     * 保存数据到剪切板
     *
     * @param context
     * @param str
     */
    fun saveClipBoard(context: Context?, str: String) {
        if (context == null) {
            return
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {

            val cmb = context
                    .getSystemService(Activity.CLIPBOARD_SERVICE) as android.text.ClipboardManager
            cmb.text = str
        } else {
            val clipboard = context
                    .getSystemService(Activity.CLIPBOARD_SERVICE) as android.content.ClipboardManager
            val clip = ClipData.newPlainText("honey", str)
            clipboard.primaryClip = clip
        }
    }

    /**
     * 打开软件盘
     *
     * @param context
     * @param view
     */
    fun openSoftKeyBoard(context: Context, view: View?) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (view != null) {

            LogUtil.info("-----open soft key board------")
            imm.showSoftInput(view, InputMethodManager.SHOW_FORCED)
        }
    }

    /**
     * 关闭软键盘
     *
     * @param context
     * @param view
     */
    fun closeSoftKeyBoard(context: Context, view: View?) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (view != null) {
            if (view.windowToken != null) {
                imm.hideSoftInputFromWindow(view.applicationWindowToken, 0)
            }
        }
    }


}
