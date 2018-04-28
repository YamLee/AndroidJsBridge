package me.yamlee.bridge.ui.view;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import me.yamlee.bridge.ui.R;
import me.yamlee.jsbridge.utils.ScreenUtil;
import me.yamlee.jsbridge.ui.WebHeader;


/**
 * Simple PopWindow for list data
 *
 * @author yamlee
 */
public class SimplePopWindow extends PopupWindow implements AdapterView.OnItemClickListener {
    ListView lvContent;
    ImageView ivArrow;
    private Context mContext;
    private List<WebHeader.ListIconTextModel> simpleContentSet;
    private BaseAdapter mListAdapter;
    private PopWindowItemClickListener mListener;

    public SimplePopWindow(Context context) {
        this.mContext = context;
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.popwindow_base, null);
        lvContent = (ListView) view.findViewById(R.id.lv_content);
        ivArrow = (ImageView) view.findViewById(R.id.iv_arrow);
        setContentView(view);
        setWidth(ScreenUtil.INSTANCE.dip2px(context, 150));
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setOutsideTouchable(true);
        setFocusable(true);
        setBackgroundDrawable(new BitmapDrawable(context.getResources(),
                BitmapFactory.decodeResource(context.getResources(), android.R.color.transparent)));
        lvContent.setOnItemClickListener(this);
    }

    /**
     * 设置列表数据适配器，与setSimpleContent方法不可同时使用
     */
    public void setListAdapter(BaseAdapter mListAdapter) {
        this.mListAdapter = mListAdapter;
        lvContent.setAdapter(mListAdapter);
    }

    /**
     * 设置简单的列表内容
     */
    public void setSimpleContent(List<WebHeader.ListIconTextModel> simpleContent) {
        this.simpleContentSet = simpleContent;
        setListAdapter(new DefaultAdapter(mContext, simpleContent));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        this.dismiss();
        if (mListener != null) {
            mListener.onItemClick(view, position, id);
        }
    }

    //设置点击监听
    public void setListener(PopWindowItemClickListener listener) {
        this.mListener = listener;
    }

    /**
     * 设置箭头的右边距
     */
    public void setArrowRightMargin(int arrowRightMargin) {
        RelativeLayout.LayoutParams lpArrow = (RelativeLayout.LayoutParams) ivArrow.getLayoutParams();
        lpArrow.setMargins(0, 0, arrowRightMargin, 0);
        ivArrow.setLayoutParams(lpArrow);
    }

    public interface PopWindowItemClickListener {
        void onItemClick(View view, int position, long itemId);
    }

    static class DefaultAdapter extends BaseAdapter {
        private List<WebHeader.ListIconTextModel> content;
        private Context context;
        private LayoutInflater inflater;

        public DefaultAdapter(Context context, List<WebHeader.ListIconTextModel> defaultContent) {
            this.context = context;
            this.content = defaultContent;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return content == null ? 0 : content.size();
        }

        @Override
        public Object getItem(int position) {
            return content.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            WebHeader.ListIconTextModel item = content.get(position);
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.popwindow_default_item, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tvTitle.setText(item.getText());
            if (item.getIconUri() == null || TextUtils.isEmpty(item.getIconUri().toString())) {
                holder.sdvIcon.setVisibility(View.GONE);
            } else {
                holder.sdvIcon.setVisibility(View.VISIBLE);
                holder.sdvIcon.setImageURI(item.getIconUri());
            }
            return convertView;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView sdvIcon;
            TextView tvTitle;

            public ViewHolder(View itemView) {
                super(itemView);
                sdvIcon = itemView.findViewById(R.id.pop_sdv_icon);
                tvTitle = itemView.findViewById(R.id.pop_tv_title);
            }
        }
    }
}
