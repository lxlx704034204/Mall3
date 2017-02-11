package com.hxqc.mall.thirdshop.views.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hxqc.mall.core.model.auto.PickupPointT;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.model.newcar.ShopModelPrice;

/**
 * 询价中的经销商列表的adapter
 * Created by 赵帆
 */
public class AskPriceShopAdapter extends BaseListViewAdapter<ShopModelPrice.ShopInfoPriceBean, BaseListViewAdapter.BaseViewHolder> {
    private ListView lv;

    public AskPriceShopAdapter(Context context) {
        super(context);
    }

    public AskPriceShopAdapter(Context context, ListView lv) {
        super(context);
        this.lv = lv;
    }


    @Override
    protected BaseViewHolder createViewHolder(int position, ViewGroup parent) {
        return new ViewHolder(LayoutInflater.from(
                parent.getContext()).inflate(R.layout.item_askprice_shop, parent, false));
    }

    @Override
    protected void bindViewHolder(BaseViewHolder holder, View convertView, final int position, final ShopModelPrice.ShopInfoPriceBean data) {
        ViewHolder viewHodler = (ViewHolder) holder;
        viewHodler.shop_title.setText(data.shopName);
        viewHodler.address.setText(data.getShopLocation().address);
        viewHodler.price.setText("¥" + OtherUtil.formatPriceSingleOrRange(data.modelPrice + ""));


        //导航
        viewHodler.addressImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ActivitySwitchBase.toAMapNvai(getContext(),
                        new PickupPointT(data.getShopLocation().address,
                                data.getShopLocation().latitude + "", data.getShopLocation().longitude + "",
                                data.getShopLocation().tel));
            }
        });


        //checkbox
        convertView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                lv.setItemChecked(position, !lv.isItemChecked(position));
                notifyDataSetChanged();
            }
        });

        viewHodler.checkbox.setChecked(lv.isItemChecked(position));

    }


    private class ViewHolder extends BaseViewHolder {
        private TextView shop_title, address, price;
        private CheckBox checkbox;
        private ImageView addressImg;

        public ViewHolder(View itemView) {
            super(itemView);
            shop_title = getView(R.id.shop_title);
            address = getView(R.id.address);
            price = getView(R.id.price);
            checkbox = getView(R.id.checkbox);
            addressImg = getView(R.id.img1);
        }
    }

}
