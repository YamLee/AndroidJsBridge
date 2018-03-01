package me.yamlee.jsbridge.widget.view;

import android.content.Context;
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
    private TextView tvTitle, tvTitleRight;
    private ImageView ivClose, ivBack, ivMenu, sdvTitleRight;
    private SimplePopWindow simplePopWindow;


    public WebHeaderView(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public WebHeaderView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public WebHeaderView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public WebHeaderView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_web_header, this, true);
        tvTitle = findViewById(R.id.tv_title);
        ivClose = findViewById(R.id.iv_close);
        ivBack = findViewById(R.id.iv_back);
        ivMenu = findViewById(R.id.iv_menu);
        sdvTitleRight = findViewById(R.id.sdv_titles_right);
        tvTitleRight = findViewById(R.id.tv_title_right);
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
        tvTitleRight.setVisibility(GONE);
        tvTitleRight.setText(text);
        tvTitleRight.setOnClickListener(btnClickListener);

        sdvTitleRight.setVisibility(VISIBLE);
    }

    public void showRightBtn(Uri imageUri, OnClickListener btnClickListener) {
        tvTitleRight.setVisibility(VISIBLE);

        sdvTitleRight.setVisibility(GONE);
        sdvTitleRight.setImageURI(imageUri);
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

    public void showCloseBtn() {
        ivClose.setVisibility(VISIBLE);
    }

    public void hideCloseBtn() {
        ivClose.setVisibility(GONE);
    }


    public interface MenuClickListener {
        void onClickMenu(ListIconTextModel menuModel);
    }


}
