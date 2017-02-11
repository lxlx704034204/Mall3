package com.hxqc.mall.thirdshop.accessory.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.accessory.model.AccessorySmallCategory;

import java.util.ArrayList;

/**
 * Function: 用品界面品系Fragment中GridView的Adapter
 *
 * @author 袁秉勇
 * @since 2016年02月23日
 */
public class AccessorySmallCategoryAdapter extends BaseAdapter {
    private final static String TAG = AccessorySmallCategoryAdapter.class.getSimpleName();
    private Context mContext;

    private CallBack callBack;

    public ArrayList< AccessorySmallCategory > accessorySmallCategories;


    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }


    public AccessorySmallCategoryAdapter(Context mContext, ArrayList< AccessorySmallCategory > accessorySmallCategories) {
        this.mContext = mContext;
        this.accessorySmallCategories = accessorySmallCategories;
    }


    @Override
    public int getCount() {
        return accessorySmallCategories.size();
    }


    @Override
    public String getItem(int position) {
        return accessorySmallCategories.get(position).class2ndName;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_accessory_small_category, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.accessorySmallCategoryNameView = (TextView) convertView.findViewById(R.id.accessory_small_category_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.accessorySmallCategoryNameView.setText(accessorySmallCategories.get(position).class2ndName);
        viewHolder.accessorySmallCategoryNameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = 0;
                ViewGroup viewGroup = (ViewGroup) viewHolder.accessorySmallCategoryNameView.getParent().getParent().getParent().getParent().getParent();
                if (viewGroup.getParent() instanceof RecyclerView) {
                    pos = ((RecyclerView) viewGroup.getParent()).getChildAdapterPosition(viewGroup);
                }
                callBack.onSmallCategoryClickCallBack(pos, accessorySmallCategories.get(position));
            }
        });
        return convertView;
    }


    class ViewHolder {
        TextView accessorySmallCategoryNameView;
    }

    public interface CallBack {
        void onSmallCategoryClickCallBack(int bigCategoryPosition, AccessorySmallCategory accessorySmallCategory);
    }
}
