package me.yamlee.jsbridge.ui;

import me.yamlee.bridge.ui.model.ListIconTextModel;

/**
 * 代理部分操作监听类
 * @author yamlee
 */
public interface DelegateListener {
    void onClickErrorView();

    void onClickHeaderRight(String clickUri);

    void onClickMoreMenuItem(ListIconTextModel menuItem);
}
