package me.yamlee.jsbridge.ui

import android.app.Activity
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import me.yamlee.jsbridge.NativeComponentProvider
import me.yamlee.jsbridge.QFHybridWebViewClient
import me.yamlee.jsbridge.R
import me.yamlee.jsbridge.model.ListIconTextModel
import me.yamlee.jsbridge.utils.InputTypeUtil
import me.yamlee.jsbridge.utils.ScreenUtil
import me.yamlee.jsbridge.utils.SnackBarUtils
import me.yamlee.jsbridge.utils.ToastUtil
import me.yamlee.jsbridge.widget.dialog.NearDialogFactory
import me.yamlee.jsbridge.widget.view.NearWebView
import me.yamlee.jsbridge.widget.view.SimplePopWindow
import timber.log.Timber

/**
 * 包含JSBridge功能的Web Activity类
 * @author LiYan
 */
abstract class BridgeWebActivity : AppCompatActivity(), NativeComponentProvider, WebActionView,
        WebActionView.WebLogicListener {

    companion object {
        const val REQUEST_CODE_NEW_WEB_ACTIVITY = 15
        const val ARG_FLAG_FINISH_ACTIVITY = "finish_flag"
    }

    protected var loadingDialog: Dialog? = null
    var defaultErrorView: View? = null
    var tvDefaultError: TextView? = null

    protected lateinit var webView: NearWebView
    protected lateinit var vTitle: View
    protected lateinit var tvTitle: TextView
    protected lateinit var tvTitleRight: TextView
    protected lateinit var ivClose: ImageView
    protected lateinit var ivBack: ImageView
    protected lateinit var ivMenu: ImageView
    protected lateinit var sdvTitleRight: ImageView
    protected lateinit var simplePopWindow: SimplePopWindow
    protected lateinit var webViewClient: QFHybridWebViewClient
    private lateinit var pbWebLoading: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.fragment_near_web)

        webView = findViewById(R.id.webView)
        vTitle = findViewById(R.id.v_title)
        tvTitle = findViewById(R.id.tv_title)
        ivClose = findViewById(R.id.iv_close)
        ivBack = findViewById(R.id.iv_back)
        ivMenu = findViewById(R.id.iv_menu)
        sdvTitleRight = findViewById(R.id.sdv_titles_right)
        tvTitleRight = findViewById(R.id.tv_title_right)
        pbWebLoading = findViewById(R.id.pb_web_view)


        val inflater = LayoutInflater.from(applicationContext)
        defaultErrorView = inflater.inflate(R.layout.include_page_error, null)
        tvDefaultError = defaultErrorView?.findViewById(R.id.common_tv_error) as TextView
    }

    override fun provideWebLogicView(): WebActionView {
        return this
    }

    override fun provideWebInteraction(): WebActionView.WebLogicListener {
        return this
    }

    override fun provideApplicationContext(): Context {
        return applicationContext
    }

    override fun provideActivityContext(): Activity {
        return this
    }

    override fun showError(errorMessage: String?) {
        SnackBarUtils.showShortSnackBar(window.decorView, errorMessage)
    }

    override fun showAlert(title: String?, content: String?) {
        NearDialogFactory.getSingleBtnDialogBuilder()
                .setTitle(title)
                .setMsg(content)
                .build(this).show()
    }

    override fun showLoading() {
        showLoading(getString(R.string.loading_please_wait))
    }

    override fun showLoading(msg: String?) {
        if (loadingDialog != null && loadingDialog?.isShowing == true) {
            loadingDialog?.dismiss()
        }
        loadingDialog = NearDialogFactory.getLoadingDialogBuilder()
                .setMsg(msg)
                .setTouchOutDismiss(false)
                .build(this)
        loadingDialog?.show()
    }

    override fun hideLoading() {
        if (loadingDialog != null && loadingDialog!!.isShowing) {
            loadingDialog?.dismiss()
        }
    }

    override fun showToast(msg: String?) {
        ToastUtil.showShort(applicationContext, msg)
    }

    override fun showSoftKeyBoard() {
        InputTypeUtil.openSoftKeyBoard(applicationContext, window.decorView)
    }

    override fun hideSoftKeyBoard() {
        InputTypeUtil.closeSoftKeyBoard(applicationContext, window.decorView)
    }


    override fun setErrorPageVisible(isVisible: Boolean) {
        setErrorPageVisible(isVisible, null)
    }


    override fun setErrorPageVisible(isVisible: Boolean, errorText: String?) {
        val view = window.decorView
        if (view != null) {
            val rootView = view.rootView as ViewGroup
            val contentView = rootView.findViewById(R.id.layout_content) as ViewGroup
            if (contentView == null) {
                Timber.d("content view is null")
                return
            }
            if (isVisible) {
                if (!TextUtils.isEmpty(errorText) && tvDefaultError != null) {
                    tvDefaultError?.text = errorText
                }
                if (contentView !== defaultErrorView?.parent) {
                    contentView.addView(defaultErrorView)
                }
                defaultErrorView?.visibility = View.VISIBLE
                defaultErrorView?.setOnClickListener(View.OnClickListener { onClickErrorView() })

            } else {
                contentView.removeView(defaultErrorView)
            }
        }
    }

    protected abstract fun onClickErrorView()

    override fun setEmptyPageVisible(isVisible: Boolean) {
        setErrorPageVisible(isVisible, getString(R.string.no_data))
    }


    override fun setEmptyPageVisible(isVisible: Boolean, emptyText: String?) {
        setErrorPageVisible(isVisible, emptyText)
    }

    override fun showProgress() {
        pbWebLoading.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        pbWebLoading.visibility = View.GONE
    }

    override fun renderWebViewLoadProgress(newProgress: Int) {
        pbWebLoading.progress = newProgress
    }

    override fun onChangeHeader(title: String?, color: Int, bgColor: Int) {
        tvTitle.setText(title)
        if (color != 0) {
            tvTitle.setTextColor(color)
        }
        if (bgColor != 0) {
            vTitle.setBackgroundColor(bgColor)
        }
    }


    override fun onChangeHeaderRightAsIcon(iconUrl: String?, clickUri: String?) {
        tvTitleRight.visibility = View.GONE
        sdvTitleRight.visibility = View.VISIBLE
        sdvTitleRight.setImageURI(Uri.parse(iconUrl))
        sdvTitleRight.setOnClickListener { onClickTitleRight(clickUri) }
    }

    protected abstract fun onClickTitleRight(clickUri: String?)

    override fun onChangeHeaderRightAsTitle(title: String?, clickUri: String?) {
        sdvTitleRight.visibility = View.GONE
        tvTitleRight.visibility = View.VISIBLE
        tvTitleRight.text = title
        tvTitleRight.setOnClickListener { onClickTitleRight(clickUri) }
    }

    override fun showHeaderMoreMenus(menus: MutableList<ListIconTextModel>?) {
        if (menus != null && menus.size > 0) {
            ivMenu.visibility = View.VISIBLE
            simplePopWindow = SimplePopWindow(this)
            simplePopWindow.setSimpleContent(menus)
            simplePopWindow.setArrowRightMargin(ScreenUtil.dip2px(applicationContext, 22f))
            simplePopWindow.setListener { view, position, itemId ->
                if (position < menus.size) {
                    onClickMoreMenuItem(menus[position])
                }
            }
        } else {
            ivMenu.visibility = View.GONE
        }
    }

    protected abstract fun onClickMoreMenuItem(listIconTextModel: ListIconTextModel)

    override fun webViewGoBack() {
        webView.goBack()
    }

    override fun showClose() {
        ivClose.visibility = View.VISIBLE
    }

    override fun showHeader(title: String?) {
        vTitle.visibility = View.VISIBLE
        //初始化标题
        if (!TextUtils.isEmpty(title)) {
            tvTitle.text = title
        }
    }

    override fun hideHeader() {
        vTitle.visibility = View.GONE
    }

    override fun renderTitle(title: String?) {
        tvTitle.text = title
    }

    override fun loadUrl(url: String?) {
        webView.loadUrl(url)
    }

    override fun openUriActivity(intent: Intent?) {
        startNearActivityForResult(intent, REQUEST_CODE_NEW_WEB_ACTIVITY)
    }


    override fun startNearActivity(intent: Intent?, activityClass: Class<out Activity>?) {
        try {
            if (intent != null && intent.resolveActivity(packageManager) != null) {
                intent.`package` = packageName
                startActivity(intent)
            } else {
                Toast.makeText(applicationContext,
                        R.string.have_some_problem_please_contacts_user_service, Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Timber.e(e)
            Toast.makeText(applicationContext,
                    R.string.have_some_problem_please_contacts_user_service, Toast.LENGTH_SHORT).show()
        }
    }

    override fun startNearActivityForResult(intent: Intent?, requestCode: Int) {
        try {
            startActivityForResult(intent, requestCode)
        } catch (e: ActivityNotFoundException) {
            Timber.e(e)
        }
    }

    override fun startNearActivity(intent: Intent?) {
        try {
            if (intent != null && intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            } else {
                Toast.makeText(applicationContext,
                        R.string.have_some_problem_please_contacts_user_service, Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Timber.e(e)
            Toast.makeText(applicationContext,
                    R.string.have_some_problem_please_contacts_user_service, Toast.LENGTH_SHORT).show()
        }
    }

    override fun startOutsideActivity(intent: Intent?) {

    }


    override fun startNearActivityForResult(intent: Intent?, requestCode: Int, activityClass: Class<out Activity>?) {
        try {
            intent?.setClass(this, activityClass)
            startActivityForResult(intent, requestCode)
        } catch (e: ActivityNotFoundException) {
            Timber.e(e)
        }

    }

    override fun finishActivity() {
        finish()
    }

    override fun finishActivityDelay(millis: Long) {
        Handler().postDelayed({ finish() }, millis)
    }


    override fun finishActivityWithAnim() {
        finish()
        overridePendingTransition(R.anim.activity_animation_right_left,
                R.anim.activity_animation_right_left_exit)
    }

    override fun finishActivityWithAnim(enterAnim: Int, exitAnim: Int) {
        finish()
        overridePendingTransition(enterAnim, exitAnim)
    }

    override fun gotoScanQrcodeActivityForResult(requestCode: Int) {
        intent.data = Uri.parse("nearmcht://view-scan-qrcode")
        startActivityForResult(intent, requestCode)
//        showToast("跳转到二维码界面，暂未实现");
    }

    override fun setActivityResult(resultCode: Int, data: Intent?) {
        setResult(resultCode, data)
    }

    override fun gotoShareActivityForResult(requestCode: Int) {
        val intent = Intent()

        showToast("跳转到分享界面，暂未实现")
    }

    override fun returnToMainActivity() {
//        val intent = Intent(this, MainActivity::class.java)
//        startActivity(intent)
//        finish()
        showToast("跳转到二维码界面，暂未实现")
    }

    override fun clearTopWebActivity() {
        val intent = Intent()
        intent.putExtra(ARG_FLAG_FINISH_ACTIVITY, true)
        setActivityResult(Activity.RESULT_OK, intent)
        finishActivity()
    }

}