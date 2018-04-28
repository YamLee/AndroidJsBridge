package me.yamlee.bridge.ui.dialog.internel;

import android.app.Dialog;
import android.content.Context;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import me.yamlee.bridge.ui.R;
import timber.log.Timber;

/**
 * 简单的带编辑提示框,基本样式为:标题,输入框,确认按钮
 *
 * @author yamlee
 */
public class SimpleEditDialog extends Dialog {
    private Context mContext;

    public SimpleEditDialog(Context context) {
        super(context);
    }

    public SimpleEditDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected SimpleEditDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public void setEditMsg(String msg) {
        try {
            EditText editText = (EditText) findViewById(R.id.et_dialog_simple_edit_content);
            if (editText != null) {
                if (!TextUtils.isEmpty(msg)) {
                    editText.setText(msg);
                    editText.setSelection(msg.length());
                } else {
                    editText.setText("");

                }
            }
        } catch (IndexOutOfBoundsException e) {
            //如果设置的edit最大长度setSelection可能会包indexOutOfBound异常
            Timber.e(e);
        }
    }

    public void setDialogTitlle(String msg) {
        TextView title = (TextView) findViewById(R.id.tv_dialog_simple_edit_title);
        if (title != null) {
            if (!TextUtils.isEmpty(msg)) {
                title.setText(msg);
            }
        }
    }

    private void initView(Context context) {
        mContext = context;
    }

    public static SimpleEditDialog.Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String title;
        private String confirmBtnText = "";
        private String editTextHint = "";
        private boolean canTouchOutDismiss;
        SimpleEditDialog mDialog = null;
        private int maxLength;
        private OnConfirmClickListener confirmClickListener;


        public Builder setTouchOutDismiss(boolean canTouchOutDismiss) {
            this.canTouchOutDismiss = canTouchOutDismiss;
            return this;
        }

        public Builder setEditInputMaxLength(int length) {
            this.maxLength = length;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setEditHint(String hintMsg) {
            this.editTextHint = hintMsg;
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

        public SimpleEditDialog build(Context context) {

            TextView tvTitle, tvConfirm;

            View mDialogView;
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mDialog = new SimpleEditDialog(context, R.style.publish_dialog);
            mDialogView = inflater.inflate(
                    R.layout.view_dialog_simple_edit, null);
            final EditText editText = (EditText) mDialogView.findViewById(R.id.et_dialog_simple_edit_content);
            if (maxLength != 0) {
                InputFilter[] inputFilters = new InputFilter[1];
                inputFilters[0] = new InputFilter.LengthFilter(maxLength);
                editText.setFilters(inputFilters);
            }
            if (!TextUtils.isEmpty(editTextHint)) {
                editText.setHint(editTextHint);
            }
            tvTitle = (TextView) mDialogView.findViewById(R.id.tv_dialog_simple_edit_title);
            if (TextUtils.isEmpty(title)) {
                title = context.getString(R.string.common_dialog_title);
            }
            tvTitle.setText(title);

            tvConfirm = (TextView) mDialogView.findViewById(R.id.tv_dialog_simple_edit_confirm);
            if (TextUtils.isEmpty(confirmBtnText)) {
                confirmBtnText = context.getString(R.string.confirm);
            }
            tvConfirm.setText(confirmBtnText);
            tvConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (confirmClickListener != null) {
                        confirmClickListener.onConfirm(editText.getText().toString());
                    }
                }
            });

            mDialog.setContentView(mDialogView);
            mDialog.setCanceledOnTouchOutside(canTouchOutDismiss);
            return mDialog;
        }

        public interface OnConfirmClickListener {
            void onConfirm(String editMsg);
        }
    }

}
