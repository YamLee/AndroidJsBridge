package me.yamlee.jsbridge.widget.dialog.internel;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * 通用列表显示弹框
 *
 * @author yamlee
 */
public class ListDialog {

    public static ListDialog.Builder builder() {
        return new Builder();
    }


    public static class Builder {
        private String title = "";
        private String[] itemArray;
        private List<String> itemList;
        private BaseAdapter mAdapter;
        private int mMaxHeight;
        private int mMaxWidth;

        public ListDialog.Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        /**
         * 设置字符数组的数据源
         *
         * @param itemArray
         */
        public ListDialog.Builder setData(String[] itemArray) {
            this.itemArray = itemArray;
            return this;
        }

        /**
         * 设置字符集合类型的数据源,如果同时设置了两种类型的数据源,优先采用字符数组
         *
         * @param itemList
         */
        public ListDialog.Builder setData(List<String> itemList) {
            this.itemList = itemList;
            return this;
        }

        public ListDialog.Builder setAdapter(BaseAdapter mAdapter) {
            this.mAdapter = mAdapter;
            return this;
        }

        public ListDialog.Builder setMaxHeight(int mMaxHeight) {
            this.mMaxHeight = mMaxHeight;
            return this;
        }

        public ListDialog.Builder setMaxWidth(int mMaxWidth) {
            this.mMaxWidth = mMaxWidth;
            return this;
        }

        public Dialog build(Context context, final PositionClickListener<DialogInterface> listener) {
            String[] data = new String[0];
            if (itemArray != null) {
                data = itemArray;
            } else if (itemList != null) {
                data = itemList.toArray(new String[itemList.size()]);
            } else if (mAdapter != null) {
                Dialog dialog = new AlertDialog.Builder(context).setTitle(title)
                        .setAdapter(mAdapter, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                listener.onClick(which, dialog);
                            }
                        })
                        .create();
                return dialog;
            } else {
                //noinspection HardCodedStringLiteral
                throw new RuntimeException("you should set data for ListDialog");
            }
            Dialog dialog = new AlertDialog.Builder(context).setTitle(title)
                    .setItems(data, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            listener.onClick(which, dialog);
                        }
                    })
                    .create();
            return dialog;
        }

    }


    public interface PositionClickListener<T> {
        void onClick(int position, T t);
    }
}
