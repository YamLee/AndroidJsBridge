package me.yamlee.bridge.ui.view

import android.content.Context
import android.content.res.TypedArray
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.graphics.drawable.DrawableCompat
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView

import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy

import me.yamlee.bridge.ui.R
import me.yamlee.bridge.util.ScreenUtil
import me.yamlee.jsbridge.ui.WebHeader

/**
 * WebView头部Title部分View自定义
 *
 * @author yamlee
 */
class WebHeaderView : FrameLayout, WebHeader {
    private var layoutRightBtn: FrameLayout? = null
    private var tvTitle: TextView? = null
    private var tvTitleRight: TextView? = null
    private var ivClose: ImageView? = null
    private var ivBack: ImageView? = null
    private var ivMenu: ImageView? = null
    private var sdvTitleRight: ImageView? = null
    private var divider: View? = null
    private var simplePopWindow: SimplePopWindow? = null
    private var layoutStyle = 0
    private var contentView: View? = null
    private var mContext: Context? = null
    private var mBackBtnListener: View.OnClickListener? = null
    private var mCloseBtnListener: View.OnClickListener? = null


    constructor(context: Context) : super(context) {
        initView(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(context, attrs)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) :
            super(context, attrs, defStyleAttr, defStyleRes) {
        initView(context, attrs)
    }

    private fun initView(context: Context, attrs: AttributeSet?) {
        mContext = context
        val a = context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.WebHeaderView,
                0, 0)
        try {
            layoutStyle = a.getInt(R.styleable.WebHeaderView_layoutStyle, 0)
        } finally {
            a.recycle()
        }
        if (layoutStyle == LAYOUT_STYLE_MIDDLE) {
            contentView = LayoutInflater.from(context).inflate(R.layout.view_web_header_layout_middle, null, false)
        } else {
            contentView = LayoutInflater.from(context).inflate(R.layout.view_web_header_layout_left, null, false)
        }
        addView(contentView)
        initView()
    }

    private fun initView() {
        tvTitle = findViewById(R.id.tv_title)
        ivClose = findViewById(R.id.iv_close)
        ivClose!!.setOnClickListener(mCloseBtnListener)
        ivBack = findViewById(R.id.iv_back)
        ivBack!!.setOnClickListener(mBackBtnListener)
        ivMenu = findViewById(R.id.iv_menu)
        sdvTitleRight = findViewById(R.id.sdv_titles_right)
        tvTitleRight = findViewById(R.id.tv_title_right)
        divider = findViewById(R.id.v_divide)
        layoutRightBtn = findViewById(R.id.fl_title_right)
    }

    override fun setLayoutStyleMiddle() {
        removeView(contentView)
        contentView = LayoutInflater.from(mContext).inflate(R.layout.view_web_header_layout_middle, null, false)
        addView(contentView)
        initView()
        requestLayout()
    }

    override fun setLayoutStyleLeft() {
        removeView(contentView)
        contentView = LayoutInflater.from(mContext).inflate(R.layout.view_web_header_layout_left, null, false)
        addView(contentView)
        initView()
        requestLayout()
    }

    override fun setTitle(text: String) {
        tvTitle!!.text = text
    }

    override fun getTitle(): String {
        return tvTitle!!.text.toString()
    }

    override fun setTitleColor(color: Int) {
        tvTitle!!.setTextColor(color)
    }

    override fun showRightBtn(text: String, btnClickListener: View.OnClickListener) {
        tvTitleRight!!.visibility = View.VISIBLE
        tvTitleRight!!.text = text
        layoutRightBtn!!.setOnClickListener(btnClickListener)

        sdvTitleRight!!.visibility = View.GONE
    }

    override fun showRightBtn(imageUri: Uri, btnClickListener: View.OnClickListener) {
        tvTitleRight!!.visibility = View.GONE

        sdvTitleRight!!.visibility = View.VISIBLE
        Glide.with(context)
                .load(imageUri)
                .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(sdvTitleRight!!)
        layoutRightBtn!!.setOnClickListener(btnClickListener)
    }

    override fun hideRightBtn() {
        tvTitleRight!!.visibility = View.GONE
        sdvTitleRight!!.visibility = View.GONE
    }

    override fun showMenus(menus: List<WebHeader.ListIconTextModel>, listener: WebHeader.MenuClickListener) {
        if (menus.isNotEmpty()) {
            ivMenu!!.visibility = View.VISIBLE
            simplePopWindow = SimplePopWindow(context)
            simplePopWindow!!.setSimpleContent(menus)
            simplePopWindow!!.setArrowRightMargin(ScreenUtil.dip2px(context.applicationContext, 22f))
            simplePopWindow!!.setListener { view, position, itemId ->
                if (position < menus.size) {
                    listener.onClickMenu(menus[position])
                }
            }
        } else {
            ivMenu!!.visibility = View.GONE
        }
    }

    override fun showCloseBtn(visible: Boolean) {
        if (visible) {
            ivClose!!.visibility = View.VISIBLE
        } else {
            ivClose!!.visibility = View.GONE
        }
    }

    override fun setCloseBtnColor(color: Int) {
        tintDrawable(ivClose, color)
    }

    private fun tintDrawable(imageView: ImageView?, color: Int) {
        if (Build.VERSION.SDK_INT < 21) {
            imageView!!.drawable.mutate().setColorFilter(color, PorterDuff.Mode.SRC_IN)
        } else {
            val wrapDrawable = DrawableCompat.wrap(imageView!!.drawable.mutate())
            DrawableCompat.setTint(wrapDrawable, color)
        }
    }

    override fun showBackBtn(visible: Boolean) {
        if (visible) {
            ivBack!!.visibility = View.VISIBLE
        } else {
            ivBack!!.visibility = View.GONE
        }
    }

    override fun setBackBtnColor(color: Int) {
        tintDrawable(ivBack, color)
    }

    override fun setDivideColor(color: Int) {
        divider!!.setBackgroundColor(color)
    }

    /**
     * 返回按键按钮点击事件设置
     *
     * @param listener
     */
    override fun setBackBtnClickListener(listener: View.OnClickListener) {
        mBackBtnListener = listener
        ivBack!!.setOnClickListener(listener)
    }

    /**
     * 关闭按钮点击事件设置
     *
     * @param listener
     */
    override fun setCloseBtnClickListener(listener: View.OnClickListener) {
        mCloseBtnListener = listener
        ivClose!!.setOnClickListener(listener)
    }

    companion object {
        private val LAYOUT_STYLE_MIDDLE = 0
        private val LAYOUT_STYLE_LEFT = 1
    }
}
