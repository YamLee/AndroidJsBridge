package me.yamlee.jsbridge.ui;

/**
 * Activity，Fragment实现的基础逻辑View接口
 *
 * @author yamlee
 */
public interface ActionView {
    /**
     * 显示错误提示，为重写情况下表现为SnackBar提示
     *
     * @param errorMessage 错误信息
     */
    void showError(String errorMessage);

    /**
     * 显示Toast信息
     */
    void showToast(String msg);

    /**
     * 显示指定信息的加载框
     */
    void showLoading(String msg);

    /**
     * 显示默认的加载框
     */
    void showLoading();

    /**
     * 隐藏加载框
     */
    void hideLoading();

    /**
     * 弹出软键盘
     */
    void showSoftKeyBoard();

    /**
     * 隐藏软键盘
     */
    void hideSoftKeyBoard();

    /**
     * 显示加载进度条
     */
    void showProgress();

    /**
     * 隐藏加载进度条
     */
    void hideProgress();

    /**
     * 设置是否显示默认的错误界面
     */
    void setErrorPageVisible(boolean isVisible);

    /**
     * 设置是否显示指定错误信息的错误提示界面
     */
    void setErrorPageVisible(boolean isVisible, String errorText);

    /**
     * 设置是否显示空页面
     */
    void setEmptyPageVisible(boolean isVisible);

    /**
     * 设置是否显示空页面,并设置特定的空文案
     */
    void setEmptyPageVisible(boolean isVisible, String emptyText);
}
