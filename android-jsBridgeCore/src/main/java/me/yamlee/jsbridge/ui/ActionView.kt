package me.yamlee.jsbridge.ui

/**
 * Activity，Fragment实现的基础逻辑View接口
 *
 * @author yamlee
 */
interface ActionView {
    /**
     * 显示错误提示，为重写情况下表现为SnackBar提示
     *
     * @param errorMessage 错误信息
     */
    fun showError(errorMessage: String)

    /**
     * 显示Toast信息
     */
    fun showToast(msg: String)

    /**
     * 显示指定信息的加载框
     */
    fun showLoading(msg: String)

    /**
     * 显示默认的加载框
     */
    fun showLoading()

    /**
     * 隐藏加载框
     */
    fun hideLoading()

    /**
     * 弹出软键盘
     */
    fun showSoftKeyBoard()

    /**
     * 隐藏软键盘
     */
    fun hideSoftKeyBoard()

    /**
     * 显示加载进度条
     */
    fun showProgress()

    /**
     * 隐藏加载进度条
     */
    fun hideProgress()

    /**
     * 设置是否显示默认的错误界面
     */
    fun setErrorPageVisible(isVisible: Boolean)

    /**
     * 设置是否显示指定错误信息的错误提示界面
     */
    fun setErrorPageVisible(isVisible: Boolean, errorText: String)

    /**
     * 设置是否显示空页面
     */
    fun setEmptyPageVisible(isVisible: Boolean)

    /**
     * 设置是否显示空页面,并设置特定的空文案
     */
    fun setEmptyPageVisible(isVisible: Boolean, emptyText: String)
}
