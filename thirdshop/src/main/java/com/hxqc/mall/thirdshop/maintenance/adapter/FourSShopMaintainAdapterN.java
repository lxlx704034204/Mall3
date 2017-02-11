package com.hxqc.mall.thirdshop.maintenance.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.maintenance.control.FourSAndQuickHelper;
import com.hxqc.mall.thirdshop.maintenance.model.maintenance.MaintenanceChildGoodsN;
import com.hxqc.util.DebugLog;
import com.hxqc.util.DisplayTools;
import com.hxqc.util.OtherUtil;

import java.util.ArrayList;

/**
 * @Author : 钟学东
 * @Since : 2016-12-30
 * FIXME
 * Todo
 */

public class FourSShopMaintainAdapterN extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<MaintenanceChildGoodsN> childGoodsNs;
    private DisplayMetrics metric;
    private boolean isItemCheck ;  //项目组是否被勾选
    private FourSAndQuickHelper fourSAndQuickHelper;

    public void setReturnGoodsListener(ReturnGoodsListener returnGoodsListener){
        this.returnGoodsListener = returnGoodsListener;
    }

    public interface ReturnGoodsListener{
        void returnGoods(int position);
    }

    private ReturnGoodsListener returnGoodsListener;

    public FourSShopMaintainAdapterN(Context context, ArrayList<MaintenanceChildGoodsN> childGoodsNs, boolean isItemCheck){
        this.context =  context;
        this.isItemCheck = isItemCheck;
        fourSAndQuickHelper = FourSAndQuickHelper.getInstance();
        metric = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(metric);
        this.childGoodsNs = childGoodsNs;
        DebugLog.i("TAG",childGoodsNs.size()+"");
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_4s_shop_first_child_item,null);
        return new FourSShopMaintainAdapterN.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ViewGroup.LayoutParams layoutParams = ((ViewHolder)holder).mGoodsNameView.getLayoutParams();
        layoutParams.width = (int) ((metric.widthPixels -  DisplayTools.dip2px(context,4*16))/2);
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        ((ViewHolder)holder).mGoodsNameView.setLayoutParams(layoutParams);

        MaintenanceChildGoodsN childGoodsN = childGoodsNs.get(position);
        ((ViewHolder)holder).mGoodsNameView.setText(childGoodsN.name);
        // 计算规则 （price-dicountG）*count）
        fourSAndQuickHelper.CalculateGoodMoenyFor4S(childGoodsN, new FourSAndQuickHelper.CalculateGoodsMoneyFor4SHandle() {
            @Override
            public void onCalculateGoodsMoney(float goodsAmount,float goodsSale) {
                ((ViewHolder)holder).mGoodsPriceView.setText(OtherUtil.amountFormat(goodsAmount ,true));
            }
        });

        //虚线
        ((ViewHolder)holder).line.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        if(childGoodsN.choose ==1){
            if(isItemCheck){
                ((ViewHolder)holder).mParentView.setBackgroundResource(R.drawable.shape_maintance_4s_shop_check);
                ((ViewHolder)holder).mGouView.setVisibility(View.VISIBLE);
            }
        }else {
            ((ViewHolder)holder).mParentView.setBackgroundResource(R.drawable.shape_maintance_4s_shop_no_check);
            ((ViewHolder)holder).mGouView.setVisibility(View.INVISIBLE);
        }


        if(isItemCheck){
            ((ViewHolder)holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for(int i = 0 ; i < childGoodsNs.size() ; i++){
                        if(i == position){
                            childGoodsNs.get(i).choose = 1;
                        }else {
                            childGoodsNs.get(i).choose = 0;
                        }
                    }
                    notifyDataSetChanged();
                    if(returnGoodsListener != null){
                        returnGoodsListener.returnGoods(position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return childGoodsNs.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private LinearLayout mParentView;
        private TextView mGoodsNameView;
        private ImageView mGouView;
        private TextView mGoodsPriceView;
        private View line;

        public ViewHolder(View itemView) {
            super(itemView);
            mParentView = (LinearLayout) itemView.findViewById(R.id.rl_parent);
            mGoodsNameView = (TextView) itemView.findViewById(R.id.goods_name);
            mGouView = (ImageView) itemView.findViewById(R.id.gou);
            mGoodsPriceView = (TextView) itemView.findViewById(R.id.goods_price);
            line = itemView.findViewById(R.id.line);
        }
    }
}
