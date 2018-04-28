package me.yamlee.jsbridge.ui

import android.net.Uri
import android.view.View

/**
 * The base abstract definition of web header
 */
interface WebHeader {

    interface MenuClickListener {
        fun onClickMenu(menuModel: ListIconTextModel)
    }

    data class ListIconTextModel(var iconUri: Uri?,
                                 var clickUri: String?,
                                 var text: String?)

    fun setBackgroundColor(color: Int)

    fun setLayoutStyleMiddle()

    fun setLayoutStyleLeft()

    fun setTitle(text: String)

    fun getTitle(): String

    fun setTitleColor(color: Int)

    fun showRightBtn(text: String, btnClickListener: View.OnClickListener)

    fun showRightBtn(imageUri: Uri, btnClickListener: View.OnClickListener)

    fun hideRightBtn()

    fun showMenus(menus: List<ListIconTextModel>, listener: MenuClickListener)

    fun showCloseBtn(visible: Boolean)

    fun setCloseBtnColor(color: Int)

    fun showBackBtn(visible: Boolean)

    fun setBackBtnColor(color: Int)

    fun setDivideColor(color: Int)

    /**
     * 返回按键按钮点击事件设置
     *
     * @param listener
     */
    fun setBackBtnClickListener(listener: View.OnClickListener)

    /**
     * 关闭按钮点击事件设置
     *
     * @param listener
     */
    fun setCloseBtnClickListener(listener: View.OnClickListener)
}