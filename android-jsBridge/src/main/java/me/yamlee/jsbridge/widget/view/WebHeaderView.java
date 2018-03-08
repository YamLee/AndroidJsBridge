package me.yamlee.jsbridge.widget.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import me.yamlee.jsbridge.R;
import me.yamlee.jsbridge.model.ListIconTextModel;
import me.yamlee.jsbridge.utils.ScreenUtil;

/**
 * WebView头部Title部分View自定义
 *
 * @author yamlee
 */
public class WebHeaderView extends FrameLayout {
    private static int LAYOUT_STYLE_MIDDLE = 0;
    private static int LAYOUT_STYLE_LEFT = 1;
    private TextView tvTitle, tvTitleRight;
    private ImageView ivClose, ivBack, ivMenu, sdvTitleRight;
    private SimplePopWindow simplePopWindow;
    private int layoutStyle = 0;
    private View contentView;
    private Context mContext;


    public WebHeaderView(@NonNull Context context) {
        super(context);
        initView(context, null);
    }

    public WebHeaderView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public WebHeaderView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public WebHeaderView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
    }

    private void initView(Context context, @Nullable AttributeSet attrs) {
        mContext = context;
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.WebHeaderView,
                0, 0);
        try {
            layoutStyle = a.getInt(R.styleable.WebHeaderView_layoutStyle, 0);
        } finally {
            a.recycle();
        }
        if (layoutStyle == LAYOUT_STYLE_MIDDLE) {
            contentView = LayoutInflater.from(context).inflate(R.layout.view_web_header_layout_middle, null, false);
        } else {
            contentView = LayoutInflater.from(context).inflate(R.layout.view_web_header_layout_left, null, false);
        }
        addView(contentView);
        initView();
    }

    private void initView() {
        tvTitle = findViewById(R.id.tv_title);
        ivClose = findViewById(R.id.iv_close);
        ivBack = findViewById(R.id.iv_back);
        ivMenu = findViewById(R.id.iv_menu);
        sdvTitleRight = findViewById(R.id.sdv_titles_right);
        tvTitleRight = findViewById(R.id.tv_title_right);
    }

    public void setLayoutStyleMiddle() {
        removeView(contentView);
        contentView = LayoutInflater.from(mContext).inflate(R.layout.view_web_header_layout_middle, null, false);
        addView(contentView);
        initView();
        requestLayout();
    }

    public void setLayoutStyleLeft() {
        removeView(contentView);
        contentView = LayoutInflater.from(mContext).inflate(R.layout.view_web_header_layout_left, null, false);
        addView(contentView);
        initView();
        requestLayout();
    }

    public void setTitle(String text) {
        tvTitle.setText(text);
    }

    public String getTitle() {
        return tvTitle.getText().toString();
    }

    public void setTitleColor(int color) {
        tvTitle.setTextColor(color);
    }

    public void showRightBtn(String text, OnClickListener btnClickListener) {
        tvTitleRight.setVisibility(VISIBLE);
        tvTitleRight.setText(text);
        tvTitleRight.setOnClickListener(btnClickListener);

        sdvTitleRight.setVisibility(GONE);
    }

    public void showRightBtn(Uri imageUri, OnClickListener btnClickListener) {
        tvTitleRight.setVisibility(GONE);

        sdvTitleRight.setVisibility(VISIBLE);
        Glide.with(getContext())
                .load(imageUri)
                .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(sdvTitleRight);
        sdvTitleRight.setOnClickListener(btnClickListener);
    }

    public void hideRightBtn() {
        tvTitleRight.setVisibility(GONE);
        sdvTitleRight.setVisibility(GONE);
    }

    public void showMenus(final List<ListIconTextModel> menus, final MenuClickListener listener) {
        if (menus != null && menus.size() > 0) {
            ivMenu.setVisibility(VISIBLE);
            simplePopWindow = new SimplePopWindow(getContext());
            simplePopWindow.setSimpleContent(menus);
            simplePopWindow.setArrowRightMargin(ScreenUtil.dip2px(getContext().getApplicationContext(), 22f));
            simplePopWindow.setListener(new SimplePopWindow.PopWindowItemClickListener() {
                @Override
                public void onItemClick(View view, int position, long itemId) {
                    if (position < menus.size()) {
                        listener.onClickMenu(menus.get(position));
                    }
                }
            });
        } else {
            ivMenu.setVisibility(GONE);
        }
    }

    public void showCloseBtn(boolean visible) {
        if (visible) {
            ivClose.setVisibility(VISIBLE);
        } else {
            ivClose.setVisibility(GONE);
        }
    }

    public void showBackBtn(boolean visible) {
        if (visible) {
            ivBack.setVisibility(VISIBLE);
        } else {
            ivBack.setVisibility(GONE);
        }
    }

    /**
     * 返回按键按钮点击事件设置
     *
     * @param listener
     */
    public void setBackBtnClickListener(View.OnClickListener listener) {
        ivBack.setOnClickListener(listener);
    }

    /**
     * 关闭按钮点击事件设置
     *
     * @param listener
     */
    public void setCloseBtnClickListener(View.OnClickListener listener) {
        ivClose.setOnClickListener(listener);
    }


    public interface MenuClickListener {
        void onClickMenu(ListIconTextModel menuModel);
    }


}
