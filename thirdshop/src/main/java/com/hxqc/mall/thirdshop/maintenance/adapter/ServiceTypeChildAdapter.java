package com.hxqc.mall.thirdshop.maintenance.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.maintenance.model.maintenance.ServiceItem;

import java.util.ArrayList;

/**
 * Author:胡仲俊
 * Date: 2016 - 03 - 22
 * FIXME
 * Todo
 */
public class ServiceTypeChildAdapter extends BaseAdapter {

    private LayoutInflater mLayoutInflater;
    private ArrayList<ServiceItem> mServiceItems;
    private Context mContext;
    private int flag = 0;

    public ServiceTypeChildAdapter(Context context, ArrayList<ServiceItem> serviceItems) {
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        this.mServiceItems = serviceItems;
        if (!serviceItems.isEmpty()) {
            if (serviceItems.size() % 3 == 0) {
                flag = serviceItems.size();
            } else if (serviceItems.size() % 3 == 1) {
                flag = serviceItems.size() + 2;
                for (int i = 0; i < 2; i++) {
                    serviceItems.add(new ServiceItem());
                }
            } else if (serviceItems.size() % 3 == 2) {
                flag = serviceItems.size() + 1;
                serviceItems.add(new ServiceItem());
            }
        } else {
            flag = 0;
        }
    }

    @Override
    public int getCount() {
        return flag;
    }

    @Override
    public Object getItem(int position) {
        return mServiceItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemViewHolder itemViewHolder;
        if (convertView == null) {
            itemViewHolder = new ItemViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.item_service_type_child, parent, false);
//            itemViewHolder.mItemImgView = (SelectableRoundedImageView) convertView.findViewById(R.id.service_type_repair_item_img);
            itemViewHolder.mItemContentView = (TextView) convertView.findViewById(R.id.service_type_repair_item_content);
            convertView.setTag(itemViewHolder);
        } else {
            itemViewHolder = (ItemViewHolder) convertView.getTag();
        }
//        ImageUtil.setImage(mContext, itemViewHolder.mItemImgView, mServiceItems.get(position).thumb);
        itemViewHolder.mItemContentView.setText(mServiceItems.get(position).itemName);

        return convertView;
    }

    class ItemViewHolder {

        //        SelectableRoundedImageView mItemImgView;
        TextView mItemContentView;
    }
}
