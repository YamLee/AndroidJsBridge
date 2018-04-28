package me.yamlee.bridge.ui.util

import android.support.design.widget.Snackbar
import android.view.View
import android.widget.TextView

import me.yamlee.bridge.ui.R


/**
 * 底部提示
 *
 *
 *
 * @author zcZhang
 * @author yamlee
 */
object SnackBarUtils {
    fun showLongSnackBar(view: View, msg: String, actionTitle: String) {

        Snackbar.make(view, msg, Snackbar.LENGTH_LONG)
                .setAction(actionTitle) { }.show()
    }

    @JvmOverloads
    fun showShortSnackBar(view: View, msg: String, actionTitle: String = view.context.getString(R.string.i_know_it)) {
        val snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_SHORT)
        val snackbarView = snackbar.view
        val textView = snackbarView.findViewById<View>(android.support.design.R.id.snackbar_text) as TextView
        textView.maxLines = 5  // show multiple line
        snackbar.setAction(actionTitle) { }.show()
    }

    fun showClickSnackBar(view: View, msg: String, actionTitle: String,
                          clickListener: View.OnClickListener) {
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG)
                .setAction(actionTitle, clickListener).show()
    }

    fun showClickSnackBarAlways(view: View, msg: String, actionTitle: String,
                                clickListener: View.OnClickListener) {
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG)
                .setAction(actionTitle, clickListener).show()
    }
}
