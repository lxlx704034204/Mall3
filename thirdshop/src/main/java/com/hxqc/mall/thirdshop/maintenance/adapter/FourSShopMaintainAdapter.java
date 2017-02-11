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
import com.hxqc.mall.thirdshop.maintenance.model.maintenance.MaintenanceReplaceGoods;
import com.hxqc.util.DebugLog;
import com.hxqc.util.DisplayTools;
import com.hxqc.util.OtherUtil;


import java.util.ArrayList;

/**
 * @Author : 钟学东
 * @Since : 2016-08-05
 * FIXME
 * Todo
 */
public class FourSShopMaintainAdapter extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<MaintenanceReplaceGoods> maintenanceReplaceGoodses;
    private DisplayMetrics metric;
    private boolean isItemCheck ;

    public void setReturnGoodsListener(ReturnGoodsListener returnGoodsListener){
        this.returnGoodsListener = returnGoodsListener;
    }

   public interface ReturnGoodsListener{
       void returnGoods(MaintenanceReplaceGoods maintenanceReplaceGoods);
   }

    private ReturnGoodsListener returnGoodsListener;

    public FourSShopMaintainAdapter(Context context, ArrayList<MaintenanceReplaceGoods> maintenanceReplaceGoodses, boolean isItemCheck){
        this.context =  context;
        this.isItemCheck = isItemCheck;
        metric = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(metric);
        this.maintenanceReplaceGoodses = maintenanceReplaceGoodses;
        DebugLog.i("TAG",maintenanceReplaceGoodses.size()+"");
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_4s_shop_first_child_item,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewGroup.LayoutParams layoutParams = ((ViewHolder)holder).mGoodsNameView.getLayoutParams();
        layoutParams.width = (int) ((metric.widthPixels -  DisplayTools.dip2px(context,4*16))/2);
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        ((ViewHolder)holder).mGoodsNameView.setLayoutParams(layoutParams);
        ((ViewHolder)holder).mGoodsNameView.setText(maintenanceReplaceGoodses.get(position).name);
        ((ViewHolder)holder).mGoodsPriceView.setText(OtherUtil.amountFormat(maintenanceReplaceGoodses.get(position).price,true));

        ((ViewHolder)holder).line.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        if(maintenanceReplaceGoodses.get(position).isSelect){
            ((ViewHolder)holder).mParentView.setBackgroundResource(R.drawable.shape_maintance_4s_shop_check);
            ((ViewHolder)holder).mGouView.setVisibility(View.VISIBLE);
        }else {
            ((ViewHolder)holder).mParentView.setBackgroundResource(R.drawable.shape_maintance_4s_shop_no_check);
            ((ViewHolder)holder).mGouView.setVisibility(View.INVISIBLE);
        }
        DebugLog.i("TAG", position + ": " + maintenanceReplaceGoodses.get(position).isSelect);


        if(isItemCheck){
            ((ViewHolder)holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for(int i = 0 ; i < maintenanceReplaceGoodses.size() ; i++){
                        if(i == position){
                            maintenanceReplaceGoodses.get(i).isSelect = true;
                        }else {
                            maintenanceReplaceGoodses.get(i).isSelect = false;
                        }
                    }
                    notifyDataSetChanged();
                    if(returnGoodsListener != null){
                        returnGoodsListener.returnGoods( maintenanceReplaceGoodses.get(position));
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return maintenanceReplaceGoodses.size();
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
