package me.yamlee.jsbridge.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

/**
 * toast显示工具类
 *
 * @author yamlee
 * @author cfy
 */
public class ToastUtil {

    public static boolean isShowToast = true;
    private static Toast mToast;

    /**
     * 显示toast,时间为LENGTH_LONG
     *
     * @param context
     * @param string
     */
    public static void showLong(Context context, String string) {
        if (isShowToast) {
            if (mToast == null) {
                mToast = Toast.makeText(context.getApplicationContext(), string, Toast.LENGTH_LONG);
            } else {
                mToast.setDuration(Toast.LENGTH_LONG);
                mToast.setText(string);
            }
            mToast.show();
        }
    }

    /**
     * 显示toast,时间为LENGTH_SHORT
     *
     * @param context
     * @param string
     */
    public static void showShort(Context context, String string) {
        if (isShowToast) {
            if (mToast == null) {
                mToast = Toast.makeText(context.getApplicationContext(), string, Toast.LENGTH_SHORT);
            } else {
                mToast.setDuration(Toast.LENGTH_SHORT);
                mToast.setText(string);
            }
            mToast.show();
        }

    }

    /**
     * 在UI线程运行弹出toast
     *
     * @param ctx
     * @param text
     */
    public static void showToastOnUiThread(final Activity ctx, final String text) {
        if (ctx != null) {
            ctx.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showLong(ctx, text);
                }
            });
        }
    }

}