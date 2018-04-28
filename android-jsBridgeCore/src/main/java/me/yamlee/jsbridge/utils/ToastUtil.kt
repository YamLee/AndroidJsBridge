package me.yamlee.jsbridge.utils

import android.app.Activity
import android.content.Context
import android.widget.Toast

/**
 * toast显示工具类
 *
 * @author yamlee
 */
object ToastUtil {

    var isShowToast = true
    private var mToast: Toast? = null

    /**
     * 显示toast,时间为LENGTH_LONG
     *
     * @param context
     * @param string
     */
    fun showLong(context: Context?, string: String) {
        if (isShowToast) {
            if (mToast == null) {
                mToast = Toast.makeText(context!!.applicationContext, string, Toast.LENGTH_LONG)
            } else {
                mToast!!.duration = Toast.LENGTH_LONG
                mToast!!.setText(string)
            }
            mToast!!.show()
        }
    }

    /**
     * 显示toast,时间为LENGTH_SHORT
     *
     * @param context
     * @param string
     */
    fun showShort(context: Context, string: String) {
        if (isShowToast) {
            if (mToast == null) {
                mToast = Toast.makeText(context.applicationContext, string, Toast.LENGTH_SHORT)
            } else {
                mToast!!.duration = Toast.LENGTH_SHORT
                mToast!!.setText(string)
            }
            mToast!!.show()
        }

    }

    /**
     * 在UI线程运行弹出toast
     *
     * @param ctx
     * @param text
     */
    fun showToastOnUiThread(ctx: Activity?, text: String) {
        ctx?.runOnUiThread { showLong(ctx, text) }
    }

}