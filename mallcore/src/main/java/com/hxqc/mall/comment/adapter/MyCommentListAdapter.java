package com.hxqc.mall.comment.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxqc.mall.comment.model.MyCommentList;
import com.hxqc.mall.comment.view.MyRatingBarView;
import com.hxqc.mall.core.R;

import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;

/**
 * @Author : 钟学东
 * @Since : 2016-05-27
 * FIXME
 * Todo  我的评论列表adapter
 */
public class MyCommentListAdapter  extends RecyclerView.Adapter{

    private Context context;
    private ArrayList<MyCommentList> commentLists;
    private MyCommentPhotoAdapter photoAdapter;


    public MyCommentListAdapter(Context context){
        this.context = context;
    }

    public void initDate(ArrayList<MyCommentList> commentLists){
        this.commentLists = commentLists;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_my_comment_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final MyCommentList myComment = commentLists.get(position);

        if(myComment.positionTag == position){
            ((ViewHolder)holder).mShopNameView.setText(myComment.shopName);
            ((ViewHolder)holder).myRatingBarView.setStar(Integer.parseInt(myComment.score));
            ((ViewHolder)holder).myRatingBarView.setEnabled(false);
            ((ViewHolder)holder).mTimeView.setText(myComment.createTime);
            ((ViewHolder)holder).mOrderIDView.setText("订单号: " + myComment.orderID);

            if(myComment.tags == null || myComment.tags.size() == 0){
                ((ViewHolder)holder).mTagFlowView.setVisibility(View.GONE);
            }else {
                ((ViewHolder)holder).mTagFlowView.setVisibility(View.VISIBLE);
                ((ViewHolder)holder).mTagFlowView.setEnabled(false);
                ((ViewHolder)holder).mTagFlowView.setAdapter(new TagAdapter<String>(myComment.tags) {
                    @Override
                    public View getView(FlowLayout parent, int position, String s) {
                        View view = LayoutInflater.from(context).inflate(R.layout.layout_flowlayout_view,parent,false);
                        TextView textView = (TextView) view.findViewById(R.id.carwash_flowlayout_textview);
                        textView.setText(s);
                        return view;
                    }
                });
            }
            if(myComment.content == null || myComment.content.equals("")){
                ((ViewHolder)holder).mCommentView.setVisibility(View.GONE);
            }else {
                ((ViewHolder)holder).mCommentView.setVisibility(View.VISIBLE);
                ((ViewHolder)holder).mCommentView.setText(myComment.content);
            }

            if(myComment.images == null || myComment.images.size() == 0){
                ((ViewHolder)holder).mRecyclerView.setVisibility(View.GONE);
            }else {
                ((ViewHolder)holder).mRecyclerView.setVisibility(View.VISIBLE);
                photoAdapter = new MyCommentPhotoAdapter(context,myComment.images);
                LinearLayoutManager layoutManager = new LinearLayoutManager(context);
                layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                ((ViewHolder)holder).mRecyclerView.setLayoutManager(layoutManager);
                ((ViewHolder)holder).mRecyclerView.setAdapter(photoAdapter);
            }

            ((ViewHolder)holder).mOrderIDView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(myComment.orderType.equals("50")){
                        ActivitySwitchBase.toAccessory4SShopOrderDetail(context,myComment.orderID);
                    }else if(myComment.orderType.equals("60")){
                        ActivitySwitchBase. to4SMaintainOrderDetail (context, myComment.orderID);
                    }else if(myComment.orderType.equals("70")){
                        ActivitySwitchBase.toRepairOrderDetail(context, myComment.orderID);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return commentLists == null ? 0 : commentLists.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        private RelativeLayout mShopTitleView;
        private TextView mShopNameView;
        private MyRatingBarView myRatingBarView;
        private TagFlowLayout mTagFlowView;
        private TextView mCommentView;
        private RecyclerView mRecyclerView;
        private TextView mTimeView;
        private TextView mOrderIDView;

        public ViewHolder(View itemView) {
            super(itemView);
            mShopTitleView = (RelativeLayout) itemView.findViewById(R.id.shop_title);
            mShopNameView = (TextView) itemView.findViewById(R.id.shop_name);
            mTimeView = (TextView) itemView.findViewById(R.id.tv_comment_post_time);
            myRatingBarView = (MyRatingBarView) itemView.findViewById(R.id.starView);
            mTagFlowView = (TagFlowLayout) itemView.findViewById(R.id.grid_view);
            mCommentView = (TextView) itemView.findViewById(R.id.comment);
            mRecyclerView = (RecyclerView) itemView.findViewById(R.id.rlv_comment_images);
            mOrderIDView = (TextView) itemView.findViewById(R.id.order_id);
        }
    }

}
