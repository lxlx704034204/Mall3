package com.hxqc.mall.thirdshop.views.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.model.ShopInfo;
import com.hxqc.mall.thirdshop.utils.ActivitySwitcherThirdPartShop;

/**
 * Created by zhaofan.
 */
public class NewCarShopListAdapter extends BaseListViewAdapter<ShopInfo, BaseListViewAdapter.BaseViewHolder> {
   Context context;
    public NewCarShopListAdapter(Context context) {
        super(context);
        this.context = context;
    }


    @Override
    protected BaseViewHolder createViewHolder(int position, ViewGroup parent) {
        return new ViewHolder(LayoutInflater.from(
                parent.getContext()).inflate(R.layout.item_newcarmodellist_shop, parent, false));
    }

    @Override
    protected void bindViewHolder(BaseViewHolder holder, View convertView, final int position, final ShopInfo data) {
        ViewHolder viewHodler = (ViewHolder) holder;
        viewHodler.shop_title.setText(data.shopTitle);
        viewHodler.phone.setText(data.shopTel);
        viewHodler.address.setText(data.getShopLocation().address);
        viewHodler.promotion.setText(data.promotion);

        convertView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ActivitySwitcherThirdPartShop.toShopDetails(context,data.shopID);
            }
        });


    }


    private class ViewHolder extends BaseViewHolder {
        private TextView shop_title, phone, address, promotion;

        public ViewHolder(View itemView) {
            super(itemView);
            shop_title = getView(R.id.shop_title);
            phone = getView(R.id.phone);
            address = getView(R.id.address);
            promotion = getView(R.id.promotion);

        }
    }

}

