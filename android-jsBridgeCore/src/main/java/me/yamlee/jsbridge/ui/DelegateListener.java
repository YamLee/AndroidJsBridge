package me.yamlee.jsbridge.ui;


/**
 * 代理部分操作监听类
 *
 * @author yamlee
 */
public interface DelegateListener {
    void onClickErrorView();

    void onClickHeaderRight(String clickUri);

    void onClickMoreMenuItem(WebHeader.ListIconTextModel menuItem);
}
