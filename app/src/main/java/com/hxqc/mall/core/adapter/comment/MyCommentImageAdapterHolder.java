package com.hxqc.mall.core.adapter.comment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hxqc.mall.core.model.ImageModel;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.util.activityutil.ActivitySwitcher;
import com.hxqc.util.DebugLog;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * Author: wanghao
 * Date: 2015-03-19
 * FIXME
 * Todo
 */
public class MyCommentImageAdapterHolder extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    OnItemClickListener mItemClickListener;
    private ArrayList<ImageModel > mData;

    public MyCommentImageAdapterHolder(ArrayList<ImageModel> mData) {
        this.mData = mData;
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        View sView = mInflater.inflate(R.layout.item_image, parent, false);
        return new ViewHolderImage(sView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
      final  ViewHolderImage holderImage = (ViewHolderImage) holder;
        ImageUtil.setImage(context, holderImage.imageView, mData.get(position).thumbImage);
        holderImage.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 查看图片
                 */
                int location[] = new int[2] ;
                holderImage.imageView.getLocationOnScreen(location);
                Bundle bundle = new Bundle() ;
                bundle.putInt("locationX",location[0]);
                bundle.putInt("locationY",location[1]);
                bundle.putInt("width", holderImage.imageView.getWidth());
                bundle.putInt("height", holderImage.imageView.getHeight());
                DebugLog.i("p___data", location[0] + " -- " + location[1] + " -- " + holderImage.imageView.getWidth() + " -- " + holderImage.imageView.getHeight());
//                Toast.makeText(context, "0_0" + position, Toast.LENGTH_SHORT).show();
                ActivitySwitcher.toViewLagerPic(position, mData, context, bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position, int type);
    }

    public class ViewHolderImage extends RecyclerView.ViewHolder{

        ImageView imageView;

        public ViewHolderImage(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.siv_pic_thumb);

        }
    }


}
