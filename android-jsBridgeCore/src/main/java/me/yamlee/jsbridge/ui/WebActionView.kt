package me.yamlee.jsbridge.ui

import android.content.Intent


/**
 * 通用WebView界面展示
 *
 * @author yamlee
 */
interface WebActionView : ActionView {

    /**
     * 获取头部Header view
     *
     * @return
     */
    fun getHeaderView(): WebHeader

    /**
     * 显示通用弹框
     *
     * @param title   弹框标题
     * @param content 弹框nmsg
     */
    fun showAlert(title: String, content: String)

    /**
     * 修改承载网页的Activity的头部信息
     *
     * @param title   新的标题
     * @param color   标题颜色
     * @param bgColor 头部的背景颜色
     */
    fun onChangeHeader(title: String, color: Int, bgColor: Int)

    /**
     * 修改header右边按钮，设置为icon类型
     *
     * @param iconUrl  icon的链接
     * @param clickUri 右边按钮的点击链接
     */
    fun onChangeHeaderRightAsIcon(iconUrl: String, clickUri: String)

    /**
     * 修改header右边按钮，设置为文字title类型
     *
     * @param title    右边按钮文案
     * @param clickUri 右边按钮的点击链接
     */
    fun onChangeHeaderRightAsTitle(title: String, clickUri: String)

    /**
     * 设置头部右边更多按钮，点击显示菜单列表
     *
     * @param menus 列表菜单信息
     */
    fun showHeaderMoreMenus(menus: List<WebHeader.ListIconTextModel>)

    /**
     * WebView回到上一个访问过的界面
     */
    fun webViewGoBack()

    /**
     * 当WebView反回到上一个网页时，header返回键右边增加关闭的按钮
     */
    fun showClose()

    /**
     * 显示web页头部
     */
    fun showHeader(title: String)

    /**
     * 隐藏web页头部
     */
    fun hideHeader()

    /**
     * 设置标题
     *
     * @param title 标题文案
     */
    fun renderTitle(title: String)

    /**
     * 根据指定的url加载网页
     */
    fun loadUrl(url: String)

    /**
     * 设置webView的加载进度
     *
     * @param newProgress 当前webView加载进度
     */
    fun renderWebViewLoadProgress(newProgress: Int)


    interface WebLogicListener : Interaction {
        /**
         * 跳转到二维码扫描界面
         *
         * @param requestCode activityResult请求码
         */
        fun gotoScanQrcodeActivityForResult(requestCode: Int)

        /**
         * 跳转到分享界面
         *
         * @param requestCode actvityResult请求码
         */
        fun gotoShareActivityForResult(requestCode: Int)

        fun returnToMainActivity()

        /**
         * H5打开新的网页
         *
         * @param intent
         */
        fun openUriActivity(intent: Intent)

        /**
         * 清除掉先前打开的web也
         */
        fun clearTopWebActivity()

        /**
         * 根据指定的url加载网页
         */
        fun loadUrl(url: String)
    }

}

