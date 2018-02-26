package me.yamlee.jsbridge.widget.dialog.internel;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import me.yamlee.jsbridge.R;
import me.yamlee.jsbridge.utils.ScreenUtil;


/**
 * 通用确认和取消按钮提示弹框
 *
 * @author yamlee
 */
public class DoubleBtnConfirmDialog extends Dialog {
    public DoubleBtnConfirmDialog(Context context) {
        super(context);
    }

    public DoubleBtnConfirmDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected DoubleBtnConfirmDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public static DoubleBtnConfirmDialog.Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String title;
        private String msg;
        private String confirmBtnText = "";
        private String cancelBtnText = "";
        private boolean canTouchOutDismiss = true;
        private DoubleBtnClickListener doubleBtnClickListener;
        private Dialog mDialog = null;
        private int enhanceConfirm = EnhanceBtn.ENHANCE_CONFIRM;
        private View mDialogView;

        public Builder setTouchOutDismiss(boolean canTouchOutDismiss) {
            this.canTouchOutDismiss = canTouchOutDismiss;
            return this;
        }

        public Builder setDoubleBtnClickListener(DoubleBtnClickListener doubleBtnClickListener) {
            this.doubleBtnClickListener = doubleBtnClickListener;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setMsg(String msg) {
            this.msg = msg;
            return this;
        }

        public Builder setConfirmBtnText(String confirmBtnText) {
            this.confirmBtnText = confirmBtnText;
            return this;
        }

        public Builder setCancelBtnText(String cancelBtnText) {
            this.cancelBtnText = cancelBtnText;
            return this;
        }

        public Builder setEnhanceConfirm(@EnhanceBtn.EnhanceBtnDef int enhanceState) {
            this.enhanceConfirm = enhanceState;
            return this;
        }

        public Builder setDialogView(View mDialogView) {
            this.mDialogView = mDialogView;
            return this;
        }

        public Dialog build(Context context) {
            TextView tvTitle, tvMsg, tvConfirm, tvCancel;
            if (TextUtils.isEmpty(confirmBtnText)) {
                confirmBtnText = context.getString(R.string.text_confirm);
            }
            if (TextUtils.isEmpty(cancelBtnText)) {
                cancelBtnText = context.getString(R.string.text_cancel);
            }
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mDialog = new Dialog(context, R.style.publish_dialog);
            if (mDialogView == null) {
                mDialogView = inflater.inflate(R.layout.dialog_common_double, null);
            }
            tvTitle = (TextView) mDialogView.findViewById(R.id.dialog_tv_title);
            if (TextUtils.isEmpty(title)) {
                tvTitle.setVisibility(View.GONE);
            }
            tvTitle.setText(title);
            tvMsg = (TextView) mDialogView.findViewById(R.id.dialog_tv_content);
            Paint msgPaint = tvMsg.getPaint();
            if (msgPaint != null) {
                float msgLength = msgPaint.measureText(String.valueOf(msg));
                int tvMsgWidth = ScreenUtil.getScreenWidth(context) - ScreenUtil.dip2px(context, 40);
                if (msgLength < tvMsgWidth) {
                    tvMsg.setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL);
                }
            }
            tvMsg.setText(msg);
            tvConfirm = (TextView) mDialogView.findViewById(R.id.dialog_tv_confirm);
            tvConfirm.setText(confirmBtnText);
            tvConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (doubleBtnClickListener != null) {
                        doubleBtnClickListener.onConfirm();
                    }
                    mDialog.dismiss();
                }
            });
            tvCancel = (TextView) mDialogView.findViewById(R.id.dialog_tv_cancel);
            tvCancel.setText(cancelBtnText);
            tvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (doubleBtnClickListener != null) {
                        doubleBtnClickListener.onCancel();
                    }
                    mDialog.dismiss();
                }
            });
            switch (enhanceConfirm) {
                case EnhanceBtn.ENHANCE_NONE:
                    tvConfirm.setBackgroundResource(R.drawable.shape_orange_corner_edge);
                    tvConfirm.setTextColor(context.getResources().getColor(R.color.palette_orange));
                    tvCancel.setBackgroundResource(R.drawable.shape_orange_corner_edge);
                    tvCancel.setTextColor(context.getResources().getColor(R.color.palette_orange));
                    break;
                case EnhanceBtn.ENHANCE_CONFIRM:
                    tvConfirm.setBackgroundResource(R.drawable.shape_orange_corner_solid);
                    tvConfirm.setTextColor(context.getResources().getColor(R.color.palette_white));
                    tvCancel.setBackgroundResource(R.drawable.shape_orange_corner_edge);
                    tvCancel.setTextColor(context.getResources().getColor(R.color.palette_orange));
                    break;
                case EnhanceBtn.ENHANCE_CANCEL:
                    tvConfirm.setBackgroundResource(R.drawable.shape_orange_corner_edge);
                    tvConfirm.setTextColor(context.getResources().getColor(R.color.palette_orange));
                    tvCancel.setBackgroundResource(R.drawable.shape_orange_corner_solid);
                    tvCancel.setTextColor(context.getResources().getColor(R.color.palette_white));
                    break;

            }
            mDialog.setCanceledOnTouchOutside(canTouchOutDismiss);
            mDialog.setContentView(mDialogView);
            return mDialog;
        }
    }

    public interface DoubleBtnClickListener {
        void onConfirm();

        void onCancel();
    }
}
