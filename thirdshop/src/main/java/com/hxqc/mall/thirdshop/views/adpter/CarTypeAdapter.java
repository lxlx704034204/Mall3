package com.hxqc.mall.thirdshop.views.adpter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.model.AutoBaseInfoThirdShop;

import java.util.ArrayList;

/**
 *  
 * 车型列表
 * @author liaoguilong
 * @since 2015年12月1日
 */
public class CarTypeAdapter extends RecyclerView.Adapter {
    ArrayList<AutoBaseInfoThirdShop > carTypes = new ArrayList<>();
    Context mContext;

    public CarTypeAdapter(ArrayList<AutoBaseInfoThirdShop> carTypes, Context context) {
        this.carTypes = carTypes;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.t_fragment_cartype_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        AutoBaseInfoThirdShop mCarType = carTypes.get(position);
        ((ViewHolder)holder).mName.setText(mCarType.itemName);
        ((ViewHolder)holder).mNakedPrice.setText(mCarType.getItemPriceString());
        ((ViewHolder)holder).mManufacturerPrice.setText(mCarType.getItemOrigPriceString());
        if(mCarType.isInPromotion==1)
        {
            ((ViewHolder)holder).mMark.setVisibility(View.VISIBLE);
        }else 
        {
            ((ViewHolder)holder).mMark.setVisibility(View.GONE);
        }
        holder.itemView.setTag(mCarType);
    }

    @Override
    public int getItemCount() {
        return carTypes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mName;
        ImageView mMark;
        TextView mNakedPrice;
        TextView mManufacturerPrice;
        

        public ViewHolder(View itemView) {
            super(itemView);
            mName = (TextView) itemView.findViewById(R.id.cartype_item_name);
            mMark = (ImageView) itemView.findViewById(R.id.cartype_item_mark);
            mNakedPrice = (TextView) itemView.findViewById(R.id.cartype_item_nakedPrice);
            mManufacturerPrice = (TextView) itemView.findViewById(R.id.cartype_item_manufacturerPrice);
        }
    }
}
