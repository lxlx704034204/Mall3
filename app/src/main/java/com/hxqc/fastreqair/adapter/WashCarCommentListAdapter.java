package com.hxqc.fastreqair.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hxqc.fastreqair.model.CarWashComment;
import com.hxqc.mall.comment.view.MyRatingBarView;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.util.DebugLog;
import com.hxqc.util.DisplayTools;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import hxqc.mall.R;

/**
 * @Author : 钟学东
 * @Since : 2016-05-20
 * FIXME
 * Todo  洗车的评论列表
 */
public class WashCarCommentListAdapter extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<CarWashComment> carWashComments;
    private DisplayMetrics metric;

    public WashCarCommentListAdapter(Context context){
        this.context = context;
    }

    public void setCarWashComments(ArrayList<CarWashComment> carWashComments){
        this.carWashComments = carWashComments;
        // 获取屏幕宽高
        metric = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(metric);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_wash_car_comment,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final CarWashComment carWashComment = carWashComments.get(position);

        if(carWashComment.positionTag == position){
            DebugLog.i("TAG",carWashComment.toString());
            ImageUtil.setImage(context, ((ViewHolder) holder).imageView, carWashComment.userAvatar, R.drawable.ic_productcomment_list_user);
            ((ViewHolder)holder).mNameView.setText(carWashComment.userNickname);
            ((ViewHolder)holder).mTimeView.setText(carWashComment.createTime);
            ((ViewHolder)holder).myRatingBarView.setStar(carWashComment.score);
            ((ViewHolder)holder).myRatingBarView.setEnabled(false);
            if(carWashComment.tags == null || carWashComment.tags.size() == 0){
                ((ViewHolder)holder).mTagFlowView.setVisibility(View.GONE);
            }else {
                ((ViewHolder)holder).mTagFlowView.setVisibility(View.VISIBLE);
                ((ViewHolder)holder).mTagFlowView.setEnabled(false);
                ((ViewHolder)holder).mTagFlowView.setAdapter(new TagAdapter<String>(carWashComment.tags) {
                    @Override
                    public View getView(FlowLayout parent, int position, String s) {
                        View view = LayoutInflater.from(context).inflate(R.layout.layout_carwash_flowlayout_view,parent,false);
                        TextView textView = (TextView) view.findViewById(R.id.carwash_flowlayout_textview);
                        textView.setText(s);
                        return view;
                    }
                });
            }

            if(carWashComment.content == null || carWashComment.content.equals("")){
                ((ViewHolder)holder).mCommentView.setVisibility(View.GONE);
            }else {
                ((ViewHolder)holder).mCommentView.setVisibility(View.VISIBLE);
                ((ViewHolder)holder).mCommentView.setText(carWashComment.content);
            }

            if(carWashComment.images == null || carWashComment.images.size() == 0){
                ((ViewHolder)holder).mRecyclerView.setVisibility(View.GONE);
            }else {
                ((ViewHolder)holder).mRecyclerView.setVisibility(View.VISIBLE);
                ViewGroup.LayoutParams layoutParams = ((ViewHolder)holder).mRecyclerView.getLayoutParams();
                layoutParams.height = (int) ((metric.widthPixels - DisplayTools.dip2px(context,5*16))/3.5);
                ((ViewHolder)holder).mRecyclerView.setLayoutParams(layoutParams);

                WashCarShopPhotoAdapter photoAdapter = new WashCarShopPhotoAdapter(context, carWashComment.images);
                LinearLayoutManager layoutManager = new LinearLayoutManager(context);
                layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                ((ViewHolder)holder).mRecyclerView.setLayoutManager(layoutManager);
                ((ViewHolder)holder).mRecyclerView.setAdapter(photoAdapter);
            }
        }
    }

    @Override
    public int getItemCount() {
        return carWashComments.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        private CircleImageView imageView;
        private TextView mNameView;
        private MyRatingBarView myRatingBarView;
        private TagFlowLayout mTagFlowView;
        private TextView mCommentView;
        private RecyclerView mRecyclerView;
        private TextView mTimeView;


        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (CircleImageView) itemView.findViewById(R.id.me_avatar);
            mNameView = (TextView) itemView.findViewById(R.id.tv_username_comment);
            mTimeView = (TextView) itemView.findViewById(R.id.tv_comment_post_time);
            myRatingBarView = (MyRatingBarView) itemView.findViewById(R.id.starView);
            mTagFlowView = (TagFlowLayout) itemView.findViewById(R.id.grid_view);
            mCommentView = (TextView) itemView.findViewById(R.id.comment);
            mRecyclerView = (RecyclerView) itemView.findViewById(R.id.rlv_comment_images);
        }
    }

}
