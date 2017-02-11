package com.hxqc.fastreqair.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hxqc.fastreqair.model.CommentImage;
import com.hxqc.mall.core.model.ImageModel;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.util.DebugLog;
import com.hxqc.util.DisplayTools;

import java.util.ArrayList;

import hxqc.mall.R;


/**
 * @Author : 钟学东
 * @Since : 2016-05-19
 * FIXME
 * Todo 洗车评论照片的adapter
 */
public class WashCarShopPhotoAdapter extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<CommentImage> commentImages;
    private DisplayMetrics metric;
    private ArrayList<ImageModel> imageModels = new ArrayList<>(); //用于显示大图的list

    public WashCarShopPhotoAdapter(Context context, ArrayList<CommentImage> commentImages){
        this.commentImages = commentImages;
        this.context = context;
        // 获取屏幕宽高
        metric = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(metric);
        for(CommentImage commentImage : commentImages){
            ImageModel imageModel = new ImageModel();
            imageModel.largeImage = commentImage.largeImage;
            imageModel.thumbImage = commentImage.thumbImage;
            imageModels.add(imageModel);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_wash_car_photo_adapter,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ViewGroup.LayoutParams layoutParams = ((ViewHolder)holder).imageView.getLayoutParams();
        DebugLog.i("TAG",(int) ((metric.widthPixels - 2*16)/3.5) + "");

        layoutParams.width = (int) ((metric.widthPixels -  DisplayTools.dip2px(context,5*16))/3.5);
        layoutParams.height = (int) ((metric.widthPixels - DisplayTools.dip2px(context,5*16))/3.5);
        ((ViewHolder)holder).imageView.setLayoutParams(layoutParams);
        ImageUtil.setImage(context, ((ViewHolder) holder).imageView, commentImages.get(position).thumbImage);

        ((ViewHolder)holder).imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int location[] = new int[2];
                Bundle bundle = new Bundle();
                bundle.putInt("locationX", location[0]);
                bundle.putInt("locationY", location[1]);
                bundle.putInt("width", metric.widthPixels);
                bundle.putInt("height",metric.widthPixels * 9 / 16);
                ActivitySwitchBase.toViewLagerPic(position, imageModels, context, bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return commentImages.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image);
        }
    }
}
