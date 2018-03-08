package me.yamlee.demo.bridge

import android.app.Activity
import me.yamlee.jsbridge.ui.BridgeActivityDelegate

/**
 *自定义JsBridge Web界面代理实现类
 *@author LiYan
 */
class CustomWebDelegate(val activity: Activity) : BridgeActivityDelegate(activity),
        CustomComponentProvider, CustomWebActionView, CustomInteraction {
    init {
        webHeader.setBackBtnClickListener {
            finishActivity()
        }
    }
}