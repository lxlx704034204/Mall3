package com.hxqc.autonews.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxqc.mall.photolibrary.model.ImageItem;
import com.hxqc.mall.photolibrary.util.ImageDisplayer;

import java.util.List;

import hxqc.mall.R;

/**
 * 口碑 发表评价 照片Adapter
 * Created by huangyi on 16/10/14.
 */
public class SendCommentAdapter extends BaseAdapter {

    //单次最多发送图片数
    public static final int MAX_IMAGE_SIZE = 6;

    private Context mContext;
    private List<ImageItem> mDataList;

    public SendCommentAdapter(Context context, List<ImageItem> dataList) {
        this.mContext = context;
        this.mDataList = dataList;
    }

    @Override
    public int getCount() {
        // 多返回一个用于展示添加图标
        if (mDataList == null) {
            return 1;
        } else if (mDataList.size() == MAX_IMAGE_SIZE) {
            return MAX_IMAGE_SIZE;
        } else {
            return mDataList.size() + 1;
        }
    }

    @Override
    public Object getItem(int position) {
        if (mDataList != null && mDataList.size() == MAX_IMAGE_SIZE) {
            return mDataList.get(position);
        } else if (mDataList == null || position - 1 < 0 || position > mDataList.size()) {
            return null;
        } else {
            return mDataList.get(position - 1);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ViewHolderTxt")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = View.inflate(mContext, R.layout.item_publish, null);
        ImageView imageIv = (ImageView) convertView.findViewById(R.id.item_grid_image);
        TextView hintView = (TextView) convertView.findViewById(R.id.item_grid_hint);
        if (isShowAddItem(position)) {
            imageIv.setImageResource(R.drawable.ic_photo);
            imageIv.setBackgroundResource(R.color.bg_gray);
        } else {
            hintView.setVisibility(View.GONE);
            ImageItem item = mDataList.get(position);
            ImageDisplayer.getInstance(mContext).displayBmp(imageIv, item.thumbnailPath, item.sourcePath);
        }

        return convertView;
    }

    private boolean isShowAddItem(int position) {
        int size = mDataList == null ? 0 : mDataList.size();
        return position == size;
    }

}
