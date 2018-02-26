package me.yamlee.jsbridge.widget.dialog;

import me.yamlee.jsbridge.widget.dialog.internel.DoubleBtnConfirmDialog;
import me.yamlee.jsbridge.widget.dialog.internel.ListDialog;
import me.yamlee.jsbridge.widget.dialog.internel.LoadingDialog;
import me.yamlee.jsbridge.widget.dialog.internel.SimpleEditDialog;
import me.yamlee.jsbridge.widget.dialog.internel.SingleBtnConfirmDialog;

/**
 * Dialog工厂类
 *
 * @author yamlee
 */
public class NearDialogFactory {

    /**
     * 通用确认，取消信息提示按钮弹框
     */
    public static DoubleBtnConfirmDialog.Builder getDoubleBtnDialogBuilder() {
        return DoubleBtnConfirmDialog.builder();
    }

    /**
     * 通用确认信息提示弹框
     */
    public static SingleBtnConfirmDialog.Builder getSingleBtnDialogBuilder() {
        return SingleBtnConfirmDialog.builder();
    }

    public static LoadingDialog.Builder getLoadingDialogBuilder() {
        return LoadingDialog.builder();
    }

    /**
     * 通用列表信息显示弹框
     */
    public static ListDialog.Builder getListDialogBuilder() {
        return ListDialog.builder();
    }


    /**
     * 简单的带编辑提示框,基本样式为:标题,输入框,确认按钮
     *
     * @return SimpleEditDialog.Builder
     */
    public static SimpleEditDialog.Builder getSimpleEditDialogBuilder() {
        return SimpleEditDialog.builder();
    }

}
