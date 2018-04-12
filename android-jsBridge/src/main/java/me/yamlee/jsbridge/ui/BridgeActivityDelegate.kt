package me.yamlee.jsbridge.ui

import android.app.Activity
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Handler
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import me.yamlee.jsbridge.*

import me.yamlee.bridge.ui.model.ListIconTextModel
import me.yamlee.bridge.ui.util.SnackBarUtils
import me.yamlee.bridge.ui.dialog.BridgeDialogFactory
import me.yamlee.bridge.ui.view.BridgeWebView
import me.yamlee.bridge.ui.view.WebHeaderView
import me.yamlee.bridge.util.InputTypeUtil
import me.yamlee.bridge.util.ToastUtil
import timber.log.Timber

/**
 * Js Bridge父类Activity代理类，用来解决不想继承BridgeWebActivity的情况
 *
 * @author yamlee
 */
abstract class BridgeActivityDelegate(private val mActivity: Activity) : NativeComponentProvider,
        WebActionView, WebActionView.WebLogicListener {

    companion object {
        const val REQUEST_CODE_NEW_WEB_ACTIVITY = 15
        const val ARG_FLAG_FINISH_ACTIVITY = "finish_flag"
    }


    private val mAppContext: Context = mActivity.applicationContext
    private var mDelegateListener: DelegateListener? = null

    val contentView: View

    protected val webHeader: WebHeaderView
    protected val mWebContainer: FrameLayout
    protected val urlLoadingProgress: ProgressBar
    protected val webView: BridgeWebView
    protected val defaultErrorView: View
    protected val tvDefaultError: TextView?
    protected var loadingDialog: Dialog? = null

    protected var mWebViewClient: WebViewClient? = null
    protected var mChromeClient: WebChromeClient? = null

    init {
        val inflater = LayoutInflater.from(mActivity.applicationContext)

        defaultErrorView = inflater.inflate(R.layout.include_page_error, null)
        tvDefaultError = defaultErrorView.findViewById(R.id.common_tv_error)

        contentView = inflater.inflate(R.layout.fragment_near_web, null, false)
        webHeader = contentView.findViewById(R.id.v_title)
        mWebContainer = contentView.findViewById(R.id.ll_web_container)
        webView = contentView.findViewById(R.id.webView)
        setWebView()
        urlLoadingProgress = contentView.findViewById(R.id.pb_web_view)
    }

    /**
     * 设置默认的client
     */
    private fun setWebView() {
        webView.webChromeClient = onCreateWebChromeClient()
        webView.webViewClient = onCreateWebViewClient()
    }

    fun setDelegateListener(listener: DelegateListener) {
        this.mDelegateListener = listener
    }


    override fun provideWebLogicView(): WebActionView {
        return this
    }

    override fun provideWebInteraction(): WebActionView.WebLogicListener {
        return this
    }

    override fun provideApplicationContext(): Context {
        return mActivity.applicationContext
    }

    override fun provideActivityContext(): Activity {
        return mActivity
    }

    override fun showError(errorMessage: String) {
        SnackBarUtils.showShortSnackBar(mActivity.window.decorView, errorMessage)
    }

    override fun showLoading(msg: String) {
        if (loadingDialog != null && loadingDialog!!.isShowing) {
            loadingDialog!!.dismiss()
        }
        loadingDialog = BridgeDialogFactory.getLoadingDialogBuilder()
                .setMsg(msg)
                .setTouchOutDismiss(false)
                .build(mActivity)
        loadingDialog!!.show()
    }

    override fun showLoading() {
        showLoading(mAppContext.getString(R.string.loading_please_wait))
    }

    override fun hideLoading() {
        if (loadingDialog != null && loadingDialog!!.isShowing) {
            loadingDialog!!.dismiss()
        }
    }

    override fun showSoftKeyBoard() {
        InputTypeUtil.openSoftKeyBoard(mAppContext, mActivity.window.decorView)
    }

    override fun hideSoftKeyBoard() {
        InputTypeUtil.closeSoftKeyBoard(mAppContext, mActivity.window.decorView)
    }

    override fun showProgress() {
        urlLoadingProgress.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        urlLoadingProgress.visibility = View.GONE
    }

    override fun renderWebViewLoadProgress(newProgress: Int) {
        urlLoadingProgress.progress = newProgress
    }


    override fun setErrorPageVisible(isVisible: Boolean) {
        setErrorPageVisible(isVisible, null)
    }

    override fun setErrorPageVisible(isVisible: Boolean, errorText: String?) {
        if (mWebContainer != null) {
            if (isVisible) {
                if (!TextUtils.isEmpty(errorText) && tvDefaultError != null) {
                    tvDefaultError.text = errorText
                }
                if (mWebContainer !== defaultErrorView.parent) {
                    mWebContainer.addView(defaultErrorView)
                }
                defaultErrorView.visibility = View.VISIBLE
                defaultErrorView.setOnClickListener { mDelegateListener?.onClickErrorView() }

            } else {
                mWebContainer.removeView(defaultErrorView)
            }
        }
    }

    override fun setEmptyPageVisible(isVisible: Boolean) {
        setErrorPageVisible(isVisible, mAppContext.getString(R.string.no_data))
    }

    override fun setEmptyPageVisible(isVisible: Boolean, emptyText: String) {
        setErrorPageVisible(isVisible, emptyText)
    }

    override fun showAlert(title: String, content: String) {
        BridgeDialogFactory.getSingleBtnDialogBuilder()
                .setTitle(title)
                .setMsg(content)
                .build(mActivity).show()
    }

    override fun showToast(msg: String) {
        ToastUtil.showShort(mAppContext, msg)
    }

    override fun onChangeHeader(title: String, color: Int, bgColor: Int) {
        webHeader.title = title
        if (color != 0) {
            webHeader.setTitleColor(color)
        }
        if (bgColor != 0) {
            webHeader.setBackgroundColor(bgColor)
        }
    }

    override fun onChangeHeaderRightAsIcon(iconUrl: String, clickUri: String) {
        webHeader.showRightBtn(Uri.parse(iconUrl)) { mDelegateListener?.onClickHeaderRight(clickUri) }
    }

    override fun onChangeHeaderRightAsTitle(title: String, clickUri: String) {
        webHeader.showRightBtn(title, { mDelegateListener?.onClickHeaderRight(clickUri) })
    }

    override fun showHeaderMoreMenus(menus: List<ListIconTextModel>) {
        webHeader.showMenus(menus, { mDelegateListener?.onClickMoreMenuItem(it) })
    }

    override fun webViewGoBack() {
        webView.goBack()

    }

    override fun showClose() {
        webHeader.showCloseBtn(true)
    }

    override fun showHeader(title: String) {
        webHeader.visibility = View.VISIBLE
        //初始化标题
        if (!TextUtils.isEmpty(title)) {
            webHeader.title = title
        }
    }

    override fun hideHeader() {
        webHeader.visibility = View.GONE
    }

    override fun renderTitle(title: String) {
        webHeader.title = title
    }

    override fun loadUrl(url: String) {
        webView.loadUrl(url)
    }

    override fun getHeaderView(): WebHeaderView {
        return webHeader
    }

    override fun startActivity(intent: Intent?, activityClass: Class<out Activity>?) {
        try {
            if (intent?.resolveActivity(mActivity.packageManager) != null) {
                intent.`package` = mActivity.packageName
                mActivity.startActivity(intent)
            } else {
                Toast.makeText(mAppContext,
                        R.string.have_some_problem_please_contacts_user_service, Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Timber.e(e)
            Toast.makeText(mAppContext,
                    R.string.have_some_problem_please_contacts_user_service, Toast.LENGTH_SHORT).show()
        }
    }

    override fun startActivity(intent: Intent?) {
        try {
            if (intent?.resolveActivity(mActivity.packageManager) != null) {
                mActivity.startActivity(intent)
            } else {
                Toast.makeText(mAppContext,
                        R.string.have_some_problem_please_contacts_user_service, Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Timber.e(e)
            Toast.makeText(mAppContext,
                    R.string.have_some_problem_please_contacts_user_service, Toast.LENGTH_SHORT).show()
        }
    }

    override fun startOutsideActivity(intent: Intent) {

    }

    override fun startActivityForResult(intent: Intent, requestCode: Int) {
        try {
            mActivity.startActivityForResult(intent, requestCode)
        } catch (e: ActivityNotFoundException) {
            Timber.e(e)
        }
    }

    override fun startActivityForResult(intent: Intent?, requestCode: Int, activityClass: Class<out Activity>) {
        try {
            intent?.setClass(mActivity, activityClass)
            mActivity.startActivityForResult(intent, requestCode)
        } catch (e: ActivityNotFoundException) {
            Timber.e(e)
        }
    }

    override fun finishActivity() {
        mActivity.finish()
    }

    override fun finishActivityDelay(millis: Long) {
        Handler().postDelayed({ finishActivity() }, millis)
    }

    override fun finishActivityWithAnim() {
        mActivity.finish()
        mActivity.overridePendingTransition(R.anim.activity_animation_right_left,
                R.anim.activity_animation_right_left_exit)
    }

    override fun finishActivityWithAnim(enterAnim: Int, exitAnim: Int) {
        mActivity.finish()
        mActivity.overridePendingTransition(enterAnim, exitAnim)
    }

    override fun setActivityResult(resultCode: Int, data: Intent) {
        mActivity.setResult(resultCode, data)
    }

    override fun gotoScanQrcodeActivityForResult(requestCode: Int) {
        showToast("跳转到二维码界面，暂未实现")
    }

    override fun gotoShareActivityForResult(requestCode: Int) {
        showToast("跳转到分享界面，暂未实现")
    }

    override fun returnToMainActivity() {
        showToast("跳转到主界面，暂未实现")

    }

    override fun openUriActivity(intent: Intent) {
        startActivityForResult(intent, REQUEST_CODE_NEW_WEB_ACTIVITY)
    }

    override fun clearTopWebActivity() {
        val intent = Intent()
        intent.putExtra(ARG_FLAG_FINISH_ACTIVITY, true)
        setActivityResult(Activity.RESULT_OK, intent)
        finishActivity()
    }

    override fun onCreateWebViewClient(): WebViewClient {
        if (mWebViewClient == null) {
            val wvBridgeHandler = object : WVJBWebViewClient.WVJBHandler {
                override fun request(data: Any?, callback: WVJBWebViewClient.WVJBResponseCallback?) {

                }

            }
            mWebViewClient = DefaultWebViewClient(webView, wvBridgeHandler, this)
            (mWebViewClient as DefaultWebViewClient).enableLogging()
        }

        return mWebViewClient!!
    }

    override fun onCreateWebChromeClient(): WebChromeClient {
        if (mChromeClient == null) {
            mChromeClient = DefaultChromeClient(mAppContext)
        }
        return mChromeClient!!
    }

    override fun addJsCallProcessor(processor: JsCallProcessor) {
        val myWebViewClient: QFHybridWebViewClient = onCreateWebViewClient() as QFHybridWebViewClient
        myWebViewClient.registerJsCallProcessor(processor)
    }


    open inner class DefaultWebViewClient(webView: WebView, wvjbHandler: WVJBWebViewClient.WVJBHandler,
                                             componentProvider: NativeComponentProvider)
        : QFHybridWebViewClient(webView, wvjbHandler, componentProvider) {
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            showProgress()
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            hideProgress()
        }

    }

    open inner class DefaultChromeClient(context: Context) : BridgeWebChromeClient(context) {
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
            renderWebViewLoadProgress(newProgress)
        }

        override fun onReceivedTitle(view: WebView?, title: String?) {
            super.onReceivedTitle(view, title)
            renderTitle(title ?: "")
        }
    }

}
