package com.hxqc.autonews.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hxqc.mall.core.model.ImageModel;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.ImageUtil;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * 口碑 用户评价 RecyclerView
 * Created by huangyi on 16/10/17.
 */
public class CommentPhotoAdapter extends RecyclerView.Adapter<CommentPhotoAdapter.ViewHolder> {

    ArrayList<String> mList;
    Context mContext;

    public CommentPhotoAdapter(ArrayList<String> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @Override
    public CommentPhotoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment_photo, parent, false));
    }

    @Override
    public void onBindViewHolder(final CommentPhotoAdapter.ViewHolder holder, int position) {

        ImageUtil.setImageSquare(holder.mPhotoView.getContext(), holder.mPhotoView, mList.get(position));
        holder.mPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<ImageModel> temp = new ArrayList<>();
                for (String s : mList) {
                    temp.add(new ImageModel(s, s));
                }
                ActivitySwitchBase.toViewLagerPic(holder.getLayoutPosition(), temp, mContext, null);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mPhotoView;

        public ViewHolder(View view) {
            super(view);
            mPhotoView = (ImageView) view.findViewById(R.id.photo);
        }
    }

}
