package me.yamlee.demo

import android.os.Bundle
import me.yamlee.jsbridge.model.ListIconTextModel
import me.yamlee.jsbridge.ui.BridgeWebActivity

/**
 * WebView容器界面
 * @author YamLee
 */
class WebActivity : BridgeWebActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityDelegate.addJsCallProcessor(LogTimeJscCallProcessor(activityDelegate))
        activityDelegate.loadUrl("file:///android_asset/jsbridge_test.html")
//        loadUrl("http://www.baidu.com")
    }

    override fun onClickErrorView() {
    }

    override fun onClickTitleRight(clickUri: String?) {
    }

    override fun onClickMoreMenuItem(listIconTextModel: ListIconTextModel) {
    }
}