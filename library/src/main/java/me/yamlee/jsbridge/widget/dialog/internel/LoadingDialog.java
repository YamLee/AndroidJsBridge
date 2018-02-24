package me.yamlee.jsbridge.widget.dialog.internel;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import me.yamlee.jsbridge.R;


/**
 * 自定义loading,默认为包外不可直接访问,需要通过DialogFactory来使用
 *
 * @author yamlee
 */
public class LoadingDialog extends Dialog {
    private View mContentView;

    public LoadingDialog(Context context, View view) {
        super(context);
        initView(context, view);
    }

    public LoadingDialog(Context context, int theme, View view) {
        super(context, theme);
        initView(context, view);
    }

    private void initView(Context context, View view) {
        mContentView = view;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mContentView != null) {
            setContentView(mContentView);
        }
    }

    public static LoadingDialog.Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String msg = "";
        private boolean canTouchOutDismiss = false;

        public Builder setMsg(String msg) {
            this.msg = msg;
            return this;
        }

        public Builder setTouchOutDismiss(boolean canTouchOutDismiss) {
            this.canTouchOutDismiss = canTouchOutDismiss;
            return this;
        }

        public Dialog build(Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.layout_loading_dialog, null);
            TextView tvMsg = (TextView) view.findViewById(R.id.tv_loading_msg);
            tvMsg.setText(this.msg);
            LoadingDialog loadingDialog = new LoadingDialog(context, R.style.Theme_Near_CustomProgressDialog, view);
            loadingDialog.setCanceledOnTouchOutside(canTouchOutDismiss);
            return loadingDialog;
        }
    }
}
