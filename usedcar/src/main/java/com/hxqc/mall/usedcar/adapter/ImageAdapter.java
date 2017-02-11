package com.hxqc.mall.usedcar.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.usedcar.R;
import com.hxqc.mall.usedcar.model.Image;
import com.hxqc.util.DisplayTools;

import java.util.ArrayList;

/**
 * 车辆详情页图集ListView adapter
 * Created by huangyi on 16/1/8.
 */
public class ImageAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<Image> mImage;
    LayoutInflater mLayoutInflater;

    public ImageAdapter(Context mContext, ArrayList<Image> mImage) {
        this.mContext = mContext;
        this.mImage = mImage;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return null == mImage ? 0 : mImage.size();
    }

    @Override
    public Image getItem(int position) {
        return mImage.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_image_hy, null);
            mViewHolder = new ViewHolder();
            mViewHolder.mMoreView = (TextView) convertView.findViewById(R.id.image_more);
            mViewHolder.mPhotoView = (ImageView) convertView.findViewById(R.id.image_photo);
            mViewHolder.mTitleView = (TextView) convertView.findViewById(R.id.image_title);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        Image model = mImage.get(position);

        if (position == 9) {
            mViewHolder.mMoreView.setVisibility(View.VISIBLE);
        } else {
            mViewHolder.mMoreView.setVisibility(View.GONE);
        }
        if (position < 9) {
            if (TextUtils.isEmpty(model.title)) {
                mViewHolder.mTitleView.setVisibility(View.GONE);
            } else {
                mViewHolder.mTitleView.setVisibility(View.VISIBLE);
                mViewHolder.mTitleView.setText(model.title);
            }
        } else {
            mViewHolder.mTitleView.setVisibility(View.GONE);
        }
        ViewGroup.LayoutParams params = mViewHolder.mPhotoView.getLayoutParams();
        params.height = (int) (DisplayTools.getScreenWidth(mContext) / 1.75);
        mViewHolder.mPhotoView.setLayoutParams(params);
        ImageUtil.setImageSquare(mContext, mViewHolder.mPhotoView, model.small_path);
        return convertView;
    }

    class ViewHolder {
        TextView mMoreView;
        ImageView mPhotoView;
        TextView mTitleView;
    }

}
