package me.yamlee.bridge.ui.view

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet

/**
 * 字体加粗样式自定义
 *
 *
 * strokeWidth = (float) (0.13 * density + 0.5f);
 *
 * @author yamlee
 */
class BoldTextView : android.support.v7.widget.AppCompatTextView {
    constructor(context: Context) : super(context) {
        init(true, context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(true, context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(true, context)
    }

    protected fun init(bold: Boolean, context: Context) {
        setTextBold(bold, context)
    }

    private fun setTextBold(bold: Boolean, context: Context) {
        //设置字体加粗
        if (bold) {
            paint.style = Paint.Style.FILL_AND_STROKE
            val density = context.resources.displayMetrics.density
            val strokeWidth = (0.13 * density + 0.5f).toFloat()
            paint.strokeWidth = strokeWidth
        } else {
            paint.style = Paint.Style.FILL_AND_STROKE
            paint.strokeWidth = 0f
        }
    }
}
