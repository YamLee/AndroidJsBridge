package me.yamlee.bridge.ui.dialog.internel;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import me.yamlee.bridge.ui.R;
import me.yamlee.jsbridge.utils.ScreenUtil;

/**
 * Created by yamlee on 15/12/28.
 * 单个按钮对话框
 */
public class SingleBtnConfirmDialog {
    public static SingleBtnConfirmDialog.Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String title;
        private CharSequence msg;
        private String confirmBtnText;
        private int enhanceConfirm = EnhanceBtn.ENHANCE_CONFIRM;
        private boolean canTouchOutDismiss;
        private Dialog mDialog = null;
        private View dialogView;
        private OnConfirmClickListener confirmClickListener;

        private int gravity = Gravity.CENTER_VERTICAL;

        public Builder setTouchOutDismiss(boolean canTouchOutDismiss) {
            this.canTouchOutDismiss = canTouchOutDismiss;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setMsg(CharSequence msg) {
            this.msg = msg;
            return this;
        }

        public Builder setEnhanceConfirm(@EnhanceBtn.EnhanceBtnDef int enhanceState) {
            this.enhanceConfirm = enhanceState;
            return this;
        }

        /**
         * add by chenfeiyue
         * 增加Gravity属性
         *
         * @param gravity Layout.Gravity属性
         */
        public Builder setGravity(int gravity) {
            this.gravity = gravity;
            return this;
        }

        public Builder setConfirmBtnText(String confirmBtnText) {
            this.confirmBtnText = confirmBtnText;
            return this;

        }

        public Builder setConfirmClickListener(OnConfirmClickListener confirmClickListener) {
            this.confirmClickListener = confirmClickListener;
            return this;
        }

        public Builder setDialogView(View dialogView) {
            this.dialogView = dialogView;
            return this;
        }

        public Dialog build(Context context) {
            TextView tvTitle, tvMsg, tvConfirm;
            if (TextUtils.isEmpty(confirmBtnText)) {
                confirmBtnText = context.getString(R.string.text_confirm);
            }

            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mDialog = new Dialog(context, R.style.publish_dialog);
            if (dialogView == null) {
                dialogView = inflater.inflate(R.layout.dialog_common_single, null);
            }

            tvMsg = (TextView) dialogView.findViewById(R.id.dialog_tv_content);
            tvMsg.setGravity(gravity);
            Paint msgPaint = tvMsg.getPaint();
            if (msgPaint != null) {
                float msgLength = msgPaint.measureText(String.valueOf(msg));
                int tvMsgWidth = ScreenUtil.getScreenWidth(context) - ScreenUtil.dip2px(context, 40);
                if (msgLength < tvMsgWidth) {
                    tvMsg.setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL);
                }
            }
            tvMsg.setText(msg);
            tvTitle = (TextView) dialogView.findViewById(R.id.dialog_tv_title);
            if (TextUtils.isEmpty(title)) {
                tvTitle.setVisibility(View.GONE);
            } else {
                tvTitle.setVisibility(View.VISIBLE);
                tvTitle.setText(title);
            }
            tvConfirm = (TextView) dialogView.findViewById(R.id.dialog_tv_confirm);
            if (!TextUtils.isEmpty(confirmBtnText)) {
                tvConfirm.setText(confirmBtnText);
            }
            tvConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (confirmClickListener != null) {
                        confirmClickListener.onConfirm();
                    }
                    mDialog.dismiss();
                }
            });

            switch (enhanceConfirm) {
                case EnhanceBtn.ENHANCE_NONE:
                    tvConfirm.setBackgroundResource(R.drawable.shape_orange_corner_edge);
                    tvConfirm.setTextColor(context.getResources().getColor(R.color.palette_orange));
                    break;
                case EnhanceBtn.ENHANCE_CONFIRM:
                    tvConfirm.setBackgroundResource(R.drawable.shape_orange_corner_solid);
                    tvConfirm.setTextColor(context.getResources().getColor(R.color.palette_white));
                    break;
                case EnhanceBtn.ENHANCE_CANCEL:
                    tvConfirm.setBackgroundResource(R.drawable.shape_orange_corner_edge);
                    tvConfirm.setTextColor(context.getResources().getColor(R.color.palette_orange));
                    break;
            }
            mDialog.setContentView(dialogView);
            mDialog.setCanceledOnTouchOutside(canTouchOutDismiss);

            return mDialog;
        }

        public interface OnConfirmClickListener {
            void onConfirm();
        }
    }

}
