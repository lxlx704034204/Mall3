package com.hxqc.mall.thirdshop.views.adpter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.utils.ActivitySwitcherThirdPartShop;

import java.util.List;


/**
 * 口碑 用户评价 RecyclerView
 * Created by huangyi on 16/10/17.
 */
public class CommentPhotoAdapter extends RecyclerView.Adapter<CommentPhotoAdapter.ViewHolder> {

    List<String> mList;

    public CommentPhotoAdapter(List<String> mList) {
        this.mList = mList;
    }

    @Override
    public CommentPhotoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment_photo, parent, false));
    }

    @Override
    public void onBindViewHolder(final CommentPhotoAdapter.ViewHolder holder, final int position) {
        ImageUtil.setImageCenterCrop(holder.mPhotoView.getContext(),holder.mPhotoView, mList.get(position));

        holder.mPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //     ActivitySwitchBase.toViewLagerPic(holder.getLayoutPosition(), temp, holder.mPhotoView.getContext(), null);
                ActivitySwitcherThirdPartShop.toPhotoView(holder.mPhotoView.getContext(), mList.toArray(new String[mList.size()]), position);
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
