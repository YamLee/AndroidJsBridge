package me.yamlee.jsbridge.ui


/**
 * 代理部分操作监听类
 *
 * @author yamlee
 */
interface DelegateListener {
    fun onClickErrorView()

    fun onClickHeaderRight(clickUri: String)

    fun onClickMoreMenuItem(menuItem: WebHeader.ListIconTextModel)
}
