package me.yamlee.jsbridge.ui;

import android.content.Intent;

import java.util.List;

import me.yamlee.jsbridge.model.ListIconTextModel;


/**
 * 通用WebView界面展示
 *
 * @author yamlee
 */
public interface NearWebLogicView extends NearLogicView {
    /**
     * 显示通用弹框
     *
     * @param title   弹框标题
     * @param content 弹框nmsg
     */
    void showAlert(String title, String content);

    /**
     * 展示toast信息
     *
     * @param msg 展示的内容
     */
    void showToast(String msg);


    /**
     * 修改承载网页的Activity的头部信息
     *
     * @param title   新的标题
     * @param color   标题颜色
     * @param bgColor 头部的背景颜色
     */
    void onChangeHeader(String title, int color, int bgColor);

    /**
     * 修改header右边按钮，设置为icon类型
     *
     * @param iconUrl  icon的链接
     * @param clickUri 右边按钮的点击链接
     */
    void onChangeHeaderRightAsIcon(String iconUrl, String clickUri);

    /**
     * 修改header右边按钮，设置为文字title类型
     *
     * @param title    右边按钮文案
     * @param clickUri 右边按钮的点击链接
     */
    void onChangeHeaderRightAsTitle(String title, String clickUri);

    /**
     * 设置头部右边更多按钮，点击显示菜单列表
     *
     * @param menus 列表菜单信息
     */
    void showHeaderMoreMenus(List<ListIconTextModel> menus);

    /**
     * WebView回到上一个访问过的界面
     */
    void webViewGoBack();

    /**
     * 当WebView反回到上一个网页时，header返回键右边增加关闭的按钮
     */
    void showClose();

    /**
     * 显示web页头部
     */
    void showHeader(String title);

    /**
     * 隐藏web页头部
     */
    void hideHeader();

    /**
     * 设置标题
     *
     * @param title 标题文案
     */
    void renderTitle(String title);

    /**
     * 根据指定的url加载网页
     */
    void loadUrl(String url);

    /**
     * 设置webView的加载进度
     *
     * @param newProgress 当前webView加载进度
     */
    void renderWebViewLoadProgress(int newProgress);


    interface WebLogicListener extends NearInteraction {
        /**
         * 跳转到二维码扫描界面
         *
         * @param requestCode activityResult请求码
         */
        void gotoScanQrcodeActivityForResult(int requestCode);

        /**
         * 跳转到分享界面
         *
         * @param requestCode actvityResult请求码
         */
        void gotoShareActivityForResult(int requestCode);

        void returnToMainActivity();

        /**
         * H5打开新的网页
         *
         * @param intent
         */
        void openUriActivity(Intent intent);

        /**
         * 清除掉先前打开的web也
         */
        void clearTopWebActivity();
    }

}

