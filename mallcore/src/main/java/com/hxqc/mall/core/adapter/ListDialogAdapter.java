package com.hxqc.mall.core.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxqc.mall.core.R;

/**
 * 说明:列表对话框Adapter
 *
 * author: 吕飞
 * since: 2015-05-05
 * Copyright:恒信汽车电子商务有限公司
 */
public class ListDialogAdapter extends BaseAdapter {
    LayoutInflater mInflater;
    Context mContext;
    String[] mItemText;
    int[] mImgResId;

    public ListDialogAdapter(Context context, String[] itemText) {
        intiData(context, itemText);
    }

    public ListDialogAdapter(Context context, String[] itemText, int[] imgResId) {
        intiData(context, itemText);
        this.mImgResId = imgResId;
    }

    private void intiData(Context context, String[] itemText) {
        this.mContext = context;
        this.mItemText = itemText;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mItemText.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListDialogHolder mListDialogHolder;
        if (convertView == null) {
            mListDialogHolder = new ListDialogHolder();
            convertView = mInflater.inflate(R.layout.item_list_dialog, parent, false);
            mListDialogHolder.mItemView = (TextView) convertView.findViewById(R.id.item);
            mListDialogHolder.mItemIconView = (ImageView) convertView.findViewById(R.id.item_icon);
            convertView.setTag(mListDialogHolder);
        } else {
            mListDialogHolder = (ListDialogHolder) convertView.getTag();
        }
        mListDialogHolder.mItemView.setText(mItemText[position]);
        if (mImgResId != null) {
            mListDialogHolder.mItemIconView.setImageResource(mImgResId[position]);
        }
        return convertView;
    }

    public class ListDialogHolder {
        TextView mItemView;
        ImageView mItemIconView;
    }
}
