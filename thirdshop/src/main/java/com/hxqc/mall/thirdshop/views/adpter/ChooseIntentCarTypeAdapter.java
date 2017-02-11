package com.hxqc.mall.thirdshop.views.adpter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.model.promotion.SalesItem;

import java.util.ArrayList;

/**
 * Function:选择意向车型Adapter
 *
 * @author 袁秉勇
 * @since 2015年12月03日
 */
public class ChooseIntentCarTypeAdapter extends RecyclerView.Adapter {
    ArrayList<SalesItem > salesItems = new ArrayList<>();
    Context mContext;
    IntentCarItemChooseListener intentCarItemChooseListener;

    public ChooseIntentCarTypeAdapter(ArrayList<SalesItem> carTypes, Context context) {
        this.salesItems = carTypes;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.t_fragment_cartype_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final SalesItem salesItem = salesItems.get(position);
        ((ViewHolder)holder).mName.setText(salesItem.baseInfo.itemName);
        ((ViewHolder)holder).mNakedPrice.setText(getPrice(salesItem.baseInfo.itemPrice + ""));
        ((ViewHolder)holder).mManufacturerPrice.setText(getPrice(salesItem.baseInfo.itemOrigPrice + ""));
        if(salesItem.baseInfo.isInPromotion == 1)
        {
            ((ViewHolder)holder).mMark.setVisibility(View.VISIBLE);
        }else
        {
            ((ViewHolder)holder).mMark.setVisibility(View.GONE);
        }
        ((ViewHolder)holder).mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentCarItemChooseListener.onIntentCarChoosed(salesItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return salesItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout mContentView;
        TextView mName;
        ImageView mMark;
        TextView mNakedPrice;
        TextView mManufacturerPrice;


        public ViewHolder(View itemView) {
            super(itemView);
            mContentView = (LinearLayout) itemView.findViewById(R.id.content);
            mName = (TextView) itemView.findViewById(R.id.cartype_item_name);
            mMark = (ImageView) itemView.findViewById(R.id.cartype_item_mark);
            mNakedPrice = (TextView) itemView.findViewById(R.id.cartype_item_nakedPrice);
            mManufacturerPrice = (TextView) itemView.findViewById(R.id.cartype_item_manufacturerPrice);
        }
    }

    public void setIntentCarItemChooseListener(IntentCarItemChooseListener intentCarItemChooseListener) {
        this.intentCarItemChooseListener = intentCarItemChooseListener;
    }

    public interface IntentCarItemChooseListener {
        void onIntentCarChoosed(SalesItem salesItem);
    }

    public String getPrice(String price) {
        return OtherUtil.amountFormat(price);
    }
}
