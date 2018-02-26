package me.yamlee.jsbridge.utils;

import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

import me.yamlee.jsbridge.R;


/**
 * 底部提示
 * <p>
 *
 * @author zcZhang
 * @author yamlee
 */
public class SnackBarUtils {
    public static void showLongSnackBar(View view, String msg, String actionTitle) {

        Snackbar.make(view, msg, Snackbar.LENGTH_LONG)
                .setAction(actionTitle, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
    }

    public static void showShortSnackBar(View view, String msg, String actionTitle) {
        Snackbar snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_SHORT);
        View snackbarView = snackbar.getView();
        TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setMaxLines(5);  // show multiple line
        snackbar.setAction(actionTitle, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }).show();
    }

    public static void showClickSnackBar(View view, String msg, String actionTitle,
                                         View.OnClickListener clickListener) {
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG)
                .setAction(actionTitle, clickListener).show();
    }

    public static void showClickSnackBarAlways(View view, String msg, String actionTitle,
                                               View.OnClickListener clickListener) {
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG)
                .setAction(actionTitle, clickListener).show();
    }

    public static void showShortSnackBar(View view, String msg) {
        showShortSnackBar(view, msg, view.getContext().getString(R.string.i_know_it));
    }
}
