package com.hxqc.mall.core.adapter.comment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxqc.fastreqair.activity.CarWashSendCommentActivity;
import com.hxqc.mall.photolibrary.model.ImageItem;
import com.hxqc.mall.photolibrary.util.CustomConstants;
import com.hxqc.mall.photolibrary.util.ImageDisplayer;

import java.util.ArrayList;
import java.util.List;

import hxqc.mall.R;

/**
 * Author: wanghao
 * Date: 2015-05-15
 * FIXME
 * Todo
 */
public class ImagePublishAdapter extends BaseAdapter {


    private List<ImageItem> mDataList = new ArrayList<>();
    private Context mContext;

    public ImagePublishAdapter(Context context, List<ImageItem> dataList)
    {
        this.mContext = context;
        this.mDataList = dataList;
    }

    @Override
    public int getCount() {
        // 多返回一个用于展示添加图标
        if (mDataList == null)
        {
            return 1;
        }
        else if (mDataList.size() == CustomConstants.MAX_IMAGE_SIZE)
        {
            return CustomConstants.MAX_IMAGE_SIZE;
        }
        else
        {
            return mDataList.size() + 1;
        }
    }

    @Override
    public Object getItem(int position) {
        if (mDataList != null
                && mDataList.size() == CustomConstants.MAX_IMAGE_SIZE)
        {
            return mDataList.get(position);
        }

        else if (mDataList == null || position - 1 < 0
                || position > mDataList.size())
        {
            return null;
        }
        else
        {
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
        TextView hintView= (TextView) convertView.findViewById(R.id.item_grid_hint);
        if (isShowAddItem(position))
        {
            imageIv.setImageResource(R.drawable.ic_photo);
            imageIv.setBackgroundResource(R.color.bg_gray);
            if(mContext instanceof CarWashSendCommentActivity) //如果是洗车发表评论
            {
                hintView.setText("晒出洗车效果和店铺图片帮助千万用户发现好店铺");
                if(mDataList.size()>0)
                hintView.setVisibility(View.GONE);
                else
                hintView.setVisibility(View.VISIBLE);
            }
        }
        else
        {
            hintView.setVisibility(View.GONE);
            final ImageItem item = mDataList.get(position);
            ImageDisplayer.getInstance(mContext).displayBmp(imageIv,
                    item.thumbnailPath, item.sourcePath);
        }

        return convertView;
    }

    private boolean isShowAddItem(int position)
    {
        int size = mDataList == null ? 0 : mDataList.size();
        return position == size;
    }
}
