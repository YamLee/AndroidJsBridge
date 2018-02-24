package me.yamlee.jsbridge.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import me.yamlee.jsbridge.NativeComponentProvider
import me.yamlee.jsbridge.model.ListIconTextModel
import me.yamlee.jsbridge.utils.SnackBarUtils
import me.yamlee.jsbridge.widget.dialog.NearDialogFactory

/**
 * 包含JSBridge功能的Web Activity类
 * @author LiYan
 */
open class BridgeWebActivity : AppCompatActivity(), NativeComponentProvider, NearWebLogicView,
        NearWebLogicView.WebLogicListener {

    override fun provideWebLogicView(): NearWebLogicView {
        return this
    }

    override fun provideWebInteraction(): NearWebLogicView.WebLogicListener {
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

    override fun showLoading(msg: String?) {
    }

    override fun showLoading() {
    }

    override fun startNearActivity(intent: Intent?, activityClass: Class<out Activity>?) {
    }

    override fun showToast(msg: String?) {
    }

    override fun hideLoading() {
    }

    override fun showSoftKeyBoard() {
    }

    override fun hideSoftKeyBoard() {
    }

    override fun onChangeHeader(title: String?, color: Int, bgColor: Int) {
    }

    override fun setErrorPageVisible(isVisible: Boolean) {
    }

    override fun startNearActivity(intent: Intent?) {
    }

    override fun setErrorPageVisible(isVisible: Boolean, errorText: String?) {
    }

    override fun onChangeHeaderRightAsIcon(iconUrl: String?, clickUri: String?) {
    }

    override fun setEmptyPageVisible(isVisible: Boolean) {
    }

    override fun startOutsideActivity(intent: Intent?) {
    }

    override fun setEmptyPageVisible(isVisible: Boolean, emptyText: String?) {
    }

    override fun onChangeHeaderRightAsTitle(title: String?, clickUri: String?) {
    }

    override fun showProgress() {
    }

    override fun hideProgress() {
    }

    override fun startNearActivityForResult(intent: Intent?, requestCode: Int) {
    }

    override fun showHeaderMoreMenus(menus: MutableList<ListIconTextModel>?) {
    }

    override fun webViewGoBack() {
    }

    override fun showClose() {
    }

    override fun showHeader(title: String?) {
    }

    override fun startNearActivityForResult(intent: Intent?, requestCode: Int, activityClass: Class<out Activity>?) {
    }

    override fun hideHeader() {
    }

    override fun renderTitle(title: String?) {
    }

    override fun loadUrl(url: String?) {
    }

    override fun finishActivity() {
    }

    override fun finishActivityDelay(millis: Long) {
    }

    override fun renderWebViewLoadProgress(newProgress: Int) {
    }

    override fun finishActivityWithAnim() {
    }

    override fun finishActivityWithAnim(enterAnim: Int, exitAnim: Int) {
    }

    override fun gotoScanQrcodeActivityForResult(requestCode: Int) {
    }

    override fun setActivityResult(resultCode: Int, data: Intent?) {
    }

    override fun gotoShareActivityForResult(requestCode: Int) {
    }

    override fun returnToMainActivity() {
    }

    override fun openUriActivity(intent: Intent?) {
    }

    override fun clearTopWebActivity() {
    }

}