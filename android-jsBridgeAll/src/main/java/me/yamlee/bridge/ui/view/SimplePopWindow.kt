package me.yamlee.bridge.ui.view

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.PopupWindow
import android.widget.RelativeLayout
import android.widget.TextView

import me.yamlee.bridge.ui.R
import me.yamlee.jsbridge.utils.ScreenUtil
import me.yamlee.jsbridge.ui.WebHeader


/**
 * Simple PopWindow for list data
 *
 * @author yamlee
 */
class SimplePopWindow(private val mContext: Context) : PopupWindow(), AdapterView.OnItemClickListener {
    private lateinit var lvContent: ListView
    private lateinit var ivArrow: ImageView
    private var simpleContentSet: List<WebHeader.ListIconTextModel>? = null
    private var mListAdapter: BaseAdapter? = null
    private var mListener: PopWindowItemClickListener? = null

    init {
        initView(mContext)
    }

    private fun initView(context: Context) {
        val view = LayoutInflater.from(context).inflate(R.layout.popwindow_base, null)
        lvContent = view.findViewById<View>(R.id.lv_content) as ListView
        ivArrow = view.findViewById<View>(R.id.iv_arrow) as ImageView
        contentView = view
        width = ScreenUtil.dip2px(context, 150f)
        height = ViewGroup.LayoutParams.WRAP_CONTENT
        isOutsideTouchable = true
        isFocusable = true
        setBackgroundDrawable(BitmapDrawable(context.resources,
                BitmapFactory.decodeResource(context.resources, android.R.color.transparent)))
        lvContent.onItemClickListener = this
    }

    /**
     * 设置列表数据适配器，与setSimpleContent方法不可同时使用
     */
    fun setListAdapter(mListAdapter: BaseAdapter) {
        this.mListAdapter = mListAdapter
        lvContent.adapter = mListAdapter
    }

    /**
     * 设置简单的列表内容
     */
    fun setSimpleContent(simpleContent: List<WebHeader.ListIconTextModel>) {
        this.simpleContentSet = simpleContent
        setListAdapter(DefaultAdapter(mContext, simpleContent))
    }

    override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        this.dismiss()
        if (mListener != null) {
            mListener!!.onItemClick(view, position, id)
        }
    }

    //设置点击监听
    fun setListener(listener: PopWindowItemClickListener) {
        this.mListener = listener
    }

    /**
     * 设置箭头的右边距
     */
    fun setArrowRightMargin(arrowRightMargin: Int) {
        val lpArrow = ivArrow.layoutParams as RelativeLayout.LayoutParams
        lpArrow.setMargins(0, 0, arrowRightMargin, 0)
        ivArrow.layoutParams = lpArrow
    }

    interface PopWindowItemClickListener {
        fun onItemClick(view: View, position: Int, itemId: Long)
    }

    internal class DefaultAdapter(private val context: Context, private val content: List<WebHeader.ListIconTextModel>?) : BaseAdapter() {
        private val inflater: LayoutInflater = LayoutInflater.from(context)

        override fun getCount(): Int {
            return content?.size ?: 0
        }

        override fun getItem(position: Int): Any {
            return content!![position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var myView = convertView
            val holder: ViewHolder
            val (iconUri, _, text) = content!![position]
            if (myView == null) {
                myView = inflater.inflate(R.layout.popwindow_default_item, null)
                holder = ViewHolder(myView)
                myView!!.tag = holder
            } else {
                holder = myView.tag as ViewHolder
            }
            holder.tvTitle.text = text
            if (iconUri == null || TextUtils.isEmpty(iconUri.toString())) {
                holder.sdvIcon.visibility = View.GONE
            } else {
                holder.sdvIcon.visibility = View.VISIBLE
                holder.sdvIcon.setImageURI(iconUri)
            }
            return myView
        }

        internal inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var sdvIcon: ImageView = itemView.findViewById(R.id.pop_sdv_icon)
            var tvTitle: TextView = itemView.findViewById(R.id.pop_tv_title)
        }
    }
}
