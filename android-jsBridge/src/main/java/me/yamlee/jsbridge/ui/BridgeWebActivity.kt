package me.yamlee.jsbridge.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import me.yamlee.jsbridge.JsCallProcessor
import me.yamlee.jsbridge.model.ListIconTextModel

/**
 * 包含JSBridge功能的Web Activity类
 * @author LiYan
 */
abstract class BridgeWebActivity : AppCompatActivity(), BridgeWebAction {

    protected lateinit var activityDelegate: BridgeActivityDelegate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityDelegate = BridgeActivityDelegate(this)
        activityDelegate.setDelegateListener(object : DelegateListener {
            override fun onClickErrorView() {
                this@BridgeWebActivity.onClickErrorView()
            }

            override fun onClickHeaderRight(clickUri: String?) {
                this@BridgeWebActivity.onClickTitleRight(clickUri)
            }

            override fun onClickMoreMenuItem(menuItem: ListIconTextModel?) {
                this@BridgeWebActivity.onClickMoreMenuItem(menuItem!!)
            }

        })
        setContentView(activityDelegate.contentView)
    }

    protected abstract fun onClickErrorView()


    protected abstract fun onClickTitleRight(clickUri: String?)


    protected abstract fun onClickMoreMenuItem(listIconTextModel: ListIconTextModel)

    override fun loadUrl(url: String) {
        activityDelegate.loadUrl(url)
    }

    override fun onCreateWebViewClient(): WebViewClient {
        return activityDelegate.onCreateWebViewClient()
    }

    override fun onCreateWebChromeClient(): WebChromeClient {
        return activityDelegate.onCreateWebChromeClient()
    }

    override fun addJsCallProcessor(processor: JsCallProcessor) {
        activityDelegate.addJsCallProcessor(processor)
    }
}