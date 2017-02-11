package com.hxqc.mall.views.autopackage;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxqc.mall.core.model.auto.Accessory;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.views.dialog.PackageInfoDialog;

import java.util.ArrayList;

import hxqc.mall.R;


/**
 * Author: wanghao
 * Date: 2015-10-09
 * FIXME
 * Todo
 */
public class AutoPackageViewComboCardHolderAdapter extends RecyclerView.Adapter< RecyclerView.ViewHolder > {

    Context context;
    private ArrayList< Accessory > accessories;//用品数组

    public interface OnItemClickListener {
        void onItemClick(View view, int position, int type);
    }

    public void setAccessories(ArrayList< Accessory > accessories) {
        this.accessories = accessories;
        notifyDataSetChanged();
    }

    public AutoPackageViewComboCardHolderAdapter(ArrayList< Accessory > accessories) {
        this.accessories = accessories;
    }

    public AutoPackageViewComboCardHolderAdapter() {
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        View sView = mInflater.inflate(R.layout.item_package_goods, parent, false);
        return new ItemHolder(sView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ItemHolder holderItem = (ItemHolder) holder;
        Accessory accessory = accessories.get(position);
        if (accessory.photo != null) {
            if (accessory.photo.size() > 0)
                ImageUtil.setImage(context, holderItem.item_img, accessory.photo.get(0).thumb);
        }
        holderItem.amount.setText(String.format("%s x %d",
                OtherUtil.stringToMoney(accessory.price), accessory.count));
        holderItem.title.setText(accessory.title);

        holderItem.item_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PackageInfoDialog(context, accessories.get(position)).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return accessories == null ? 0 : accessories.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {

        ImageView item_img;
        TextView title;
        TextView amount;

        public ItemHolder(View itemView) {
            super(itemView);
            item_img = (ImageView) itemView.findViewById(R.id.iv_p_img);
            title = (TextView) itemView.findViewById(R.id.tv_p_title);
            amount = (TextView) itemView.findViewById(R.id.tv_p_amount);
        }
    }
}
