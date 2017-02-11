package com.hxqc.mall.thirdshop.accessory.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.hxqc.mall.core.model.ImageModel;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.util.ScreenUtil;

import java.util.ArrayList;

/**
 * 相册页面GridView Adapter
 * Created by huangyi on 16/1/8.
 */
public class PhotoAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<ImageModel> mImageModel;
    LayoutInflater mLayoutInflater;

    public PhotoAdapter(Context mContext, ArrayList<ImageModel> mImageModel) {
        this.mContext = mContext;
        this.mImageModel = mImageModel;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mImageModel.size();
    }

    @Override
    public ImageModel getItem(int position) {
        return mImageModel.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_photo, null);
            mViewHolder = new ViewHolder();
            mViewHolder.mPhotoView = (ImageView) convertView.findViewById(R.id.photo);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        int width = ScreenUtil.getScreenWidth(mContext) - ScreenUtil.dip2px(mContext, 16);
        mViewHolder.mPhotoView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, width / 3));
        ImageUtil.setImageSquare(mContext, mViewHolder.mPhotoView, mImageModel.get(position).thumbImage);
        return convertView;
    }

    class ViewHolder {
        ImageView mPhotoView;
    }

}
