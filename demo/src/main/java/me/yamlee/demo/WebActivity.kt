package me.yamlee.demo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import me.yamlee.demo.bridge.CustomWebDelegate
import me.yamlee.demo.jscall.LogTimeJscCallProcessor
import me.yamlee.demo.jscall.SetHeaderRightProcessor
import me.yamlee.jsbridge.ui.DelegateListener
import me.yamlee.jsbridge.ui.WebHeader

/**
 * WebView容器界面
 * @author YamLee
 */
class WebActivity : BaseActivity() {

    companion object {
        const val ARG_URL = "url"

        fun getIntent(url: String, context: Activity): Intent {
            val intent = Intent(context, WebActivity::class.java)
            intent.putExtra(ARG_URL, url)
            return intent
        }
    }

    private lateinit var delegate: CustomWebDelegate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        delegate = CustomWebDelegate(this)
        setContentView(delegate.contentView)
        delegate.setDelegateListener(object : DelegateListener {
            override fun onClickErrorView() {

            }

            override fun onClickHeaderRight(clickUri: String?) {
            }

            override fun onClickMoreMenuItem(menuItem: WebHeader.ListIconTextModel?) {
            }

        })
        addJsCallProcessor()
        val url = intent?.getStringExtra(ARG_URL)
        if (!TextUtils.isEmpty(url)) {
            delegate.loadUrl(url!!)
        } else {
            delegate.loadUrl("file:///android_asset/jsbridge_test.html")
        }
//        loadUrl("http://www.baidu.com")
    }

    private fun addJsCallProcessor() {
        delegate.addJsCallProcessor(LogTimeJscCallProcessor(delegate))
        delegate.addJsCallProcessor(SetHeaderRightProcessor(delegate))
    }
}