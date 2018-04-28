package me.yamlee.jsbridge.utils

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.os.Build
import android.util.DisplayMetrics
import android.view.Display
import android.view.View
import android.view.Window
import android.view.WindowManager

import java.lang.reflect.Field
import java.lang.reflect.Method


/**
 * Screen utils like get screen height ,width method and so on
 *
 * @author yamlee
 */
object ScreenUtil {

    /**
     * 获取屏幕的宽度
     *
     * @param context
     * @return
     */
    fun getScreenWidth(context: Context): Int {
        val manager = context
                .getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = manager.defaultDisplay
        return display.width
    }

    /**
     * 获取屏幕的高度
     *
     * @param context
     * @return
     */
    fun getScreenHeight(context: Context): Int {
        val manager = context
                .getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = manager.defaultDisplay
        return display.height
    }

    /**
     * 获取屏幕中控件顶部位置的高度--即控件顶部的Y点
     *
     * @return
     */
    fun getScreenViewTopHeight(view: View): Int {
        return view.top
    }

    /**
     * 获取屏幕中控件底部位置的高度--即控件底部的Y点
     *
     * @return
     */
    fun getScreenViewBottomHeight(view: View): Int {
        return view.bottom
    }

    /**
     * 获取屏幕中控件左侧的位置--即控件左侧的X点
     *
     * @return
     */
    fun getScreenViewLeftHeight(view: View): Int {
        return view.left
    }

    /**
     * 获取屏幕中控件右侧的位置--即控件右侧的X点
     *
     * @return
     */
    fun getScreenViewRightHeight(view: View): Int {
        return view.right
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    fun dip2px(context: Context?, dpValue: Float): Int {
        if (context == null) {
            return 0
        }
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    fun px2dip(context: Context?, pxValue: Float): Int {
        if (context == null) {
            return 0
        }
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    fun displayMetrics(activity: Activity): DisplayMetrics {
        val metrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(metrics)
        return metrics
    }

    /**
     * 获取屏幕宽度
     */
    fun getScreenWidth(activity: Activity): Int {
        val metrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(metrics)
        return metrics.widthPixels
    }

    /**
     * 获取屏幕高度
     */
    fun getScreenHeight(activity: Activity): Int {
        val metrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(metrics)
        return metrics.heightPixels
    }

    /**
     * 获取状态栏高度
     *
     * @param activity
     * @return
     */
    fun getStatusHeight(activity: Activity): Int {
        val stausHeight: Int
        val frame = Rect()
        activity.window.decorView.getWindowVisibleDisplayFrame(frame)
        stausHeight = frame.top
        return stausHeight
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    /**
     * 修改状态栏为全透明
     *
     * @param activity
     */
    @TargetApi(19)
    fun transparencyStatusBar(activity: Activity): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = activity.window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    // | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
            )
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    //                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
            //            window.setNavigationBarColor(Color.TRANSPARENT);

            return true
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val window = activity.window
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            return true
        }
        return false
    }

    /**
     * 设置状态栏背景与界面布局顶部连接
     *
     * @param activity
     * @param topLayout
     */
    fun setStatusBarTransparentWithLayoutTop(activity: Activity?, topLayout: View?): Boolean {
        if (activity == null || topLayout == null) {
            return false
        }
        //设置状态栏关联
        val transparencyResult = transparencyStatusBar(activity)
        if (transparencyResult) {
            val paddingBottom = topLayout.paddingBottom
            val paddingRight = topLayout.paddingRight
            val paddingLeft = topLayout.paddingLeft
            val paddingTop = topLayout.paddingTop
            val statusBarHeight = getStatusBarHeight(activity.applicationContext)
            topLayout.setPadding(paddingLeft, paddingTop + statusBarHeight, paddingRight, paddingBottom)
            return true
        } else {
            return false
        }
    }

    /**
     * 如果是魅族flyme系统，获取smart bar高度
     */
    fun getSmartBarHeight(context: Context): Int {
        try {
            val c = Class.forName("com.android.internal.R\$dimen")
            val obj = c.newInstance()
            val field = c.getField("mz_action_button_min_height")
            val height = Integer.parseInt(field.get(obj).toString())
            return context.resources.getDimensionPixelSize(height)
        } catch (e: Exception) {
            e.printStackTrace()
            return 0
        }

    }

    /**
     * 通过反射，获取包含虚拟键的整体屏幕高度
     */
    fun getFullScreenHeight(activity: Activity): Int {
        var screenHeight = 0
        val display = activity.windowManager.defaultDisplay
        val dm = DisplayMetrics()
        val c: Class<*>
        try {
            c = Class.forName("android.view.Display")
            val method = c.getMethod("getRealMetrics", DisplayMetrics::class.java)
            method.invoke(display, dm)
            screenHeight = dm.heightPixels
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return screenHeight
    }

    /**
     * 获取底部虚拟按键高度
     */
    fun getNavigationBarHeight(context: Context): Int {
        //set padding distance to bottom navigation bar if device has bottom navigation bar
        val identifier = context.resources.getIdentifier("navigation_bar_height", "dimen", "android")
        return context.resources.getDimensionPixelSize(identifier)
    }
}