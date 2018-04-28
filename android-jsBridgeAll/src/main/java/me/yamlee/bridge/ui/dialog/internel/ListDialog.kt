package me.yamlee.bridge.ui.dialog.internel

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import android.widget.BaseAdapter

/**
 * 通用列表显示弹框
 *
 * @author yamlee
 */
object ListDialog {

    fun builder(): ListDialog.Builder {
        return Builder()
    }


    class Builder {
        private var title = ""
        private var itemArray: Array<String>? = null
        private var itemList: List<String>? = null
        private var mAdapter: BaseAdapter? = null
        private var mMaxHeight: Int = 0
        private var mMaxWidth: Int = 0

        fun setTitle(title: String): ListDialog.Builder {
            this.title = title
            return this
        }

        /**
         * 设置字符数组的数据源
         *
         * @param itemArray
         */
        fun setData(itemArray: Array<String>): ListDialog.Builder {
            this.itemArray = itemArray
            return this
        }

        /**
         * 设置字符集合类型的数据源,如果同时设置了两种类型的数据源,优先采用字符数组
         *
         * @param itemList
         */
        fun setData(itemList: List<String>): ListDialog.Builder {
            this.itemList = itemList
            return this
        }

        fun setAdapter(mAdapter: BaseAdapter): ListDialog.Builder {
            this.mAdapter = mAdapter
            return this
        }

        fun setMaxHeight(mMaxHeight: Int): ListDialog.Builder {
            this.mMaxHeight = mMaxHeight
            return this
        }

        fun setMaxWidth(mMaxWidth: Int): ListDialog.Builder {
            this.mMaxWidth = mMaxWidth
            return this
        }

        fun build(context: Context, listener: PositionClickListener<DialogInterface>): Dialog {
            var data: Array<String>?
            if (itemArray != null) {
                data = itemArray
            } else if (itemList != null) {
                data = itemList!!.toTypedArray()
            } else if (mAdapter != null) {
                val dialog = AlertDialog.Builder(context).setTitle(title)
                        .setAdapter(mAdapter) { dialog, which -> listener.onClick(which, dialog) }
                        .create()
                return dialog
            } else {

                throw RuntimeException("you should set data for ListDialog")
            }
            val dialog = AlertDialog.Builder(context).setTitle(title)
                    .setItems(data) { dialog, which -> listener.onClick(which, dialog) }
                    .create()
            return dialog
        }

    }


    interface PositionClickListener<T> {
        fun onClick(position: Int, t: T)
    }
}
