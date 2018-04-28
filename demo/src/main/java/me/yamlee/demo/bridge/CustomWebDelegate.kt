package me.yamlee.demo.bridge

import android.app.Activity
import android.view.View
import me.yamlee.bridge.ui.DefaultWebDelegate

/**
 *自定义JsBridge Web界面代理实现类
 *@author LiYan
 */
class CustomWebDelegate(activity: Activity) : DefaultWebDelegate(activity),
        CustomComponentProvider, CustomWebActionView, CustomInteraction {

    init {
        webHeader.setBackBtnClickListener(View.OnClickListener { finishActivity() })
    }
}