package com.hxqc.mall.auto.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxqc.mall.auto.model.MyAuto;
import com.hxqc.mall.core.R;
import com.hxqc.mall.core.util.ImageUtil;

import java.util.ArrayList;

/**
 * Author:胡仲俊
 * Date: 2016 - 03 - 10
 * FIXME
 * Todo 车辆信息列表
 */
public class AutoInfoAdapterV2 extends BaseAdapter {

    private static final String TAG = "AutoInfoAdapterV2";
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private int isDefault;//默认条目
    private ArrayList<MyAuto> mAutoGroups;

    public AutoInfoAdapterV2(Context context, ArrayList<MyAuto> autoGroups) {
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mAutoGroups = autoGroups;
//        DebugLog.i(TAG, autoGroups.get(0).toString());
    }

    public void notifyData(ArrayList<MyAuto> autoGroups) {
        this.mAutoGroups = autoGroups;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mAutoGroups != null ? mAutoGroups.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.item_auto_info_v2, null);
            viewHolder.mAutoInfoBrandThumb = (ImageView) convertView.findViewById(R.id.item_auto_info_log);
            viewHolder.mAutoInfoType = (TextView) convertView.findViewById(R.id.item_auto_info_type);
            viewHolder.mAutoInfoPlateNum = (TextView) convertView.findViewById(R.id.item_auto_info_plate_num);
            viewHolder.mAutoDrivingDistance = (TextView) convertView.findViewById(R.id.item_auto_info_driving_distance);

            viewHolder.mAutoIsDefaultView = (ImageView) convertView.findViewById(R.id.auto_is_default);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (mAutoGroups.get(position).isDefault .equals("0") ) {
            if (mAutoGroups.get(position).isDefault .equals("20")) {
                isDefault = position;
                viewHolder.mAutoIsDefaultView.setVisibility(View.GONE);
            } else {
                viewHolder.mAutoIsDefaultView.setVisibility(View.GONE);
            }
        }
        setValue(viewHolder, mAutoGroups.get(position));
        return convertView;
    }

    /**
     * @param viewHolder
     * @param myAutoV2
     */
    public void setValue(ViewHolder viewHolder, MyAuto myAutoV2) {
        viewHolder.mAutoInfoType.setText(myAutoV2.autoModel);
        if(TextUtils.isEmpty(myAutoV2.plateNumber)) {
            viewHolder.mAutoInfoPlateNum.setVisibility(View.GONE);
        } else {
            viewHolder.mAutoInfoPlateNum.setVisibility(View.VISIBLE);
            viewHolder.mAutoInfoPlateNum.setText(myAutoV2.plateNumber);
        }
        viewHolder.mAutoDrivingDistance.setText(String.format("%s", myAutoV2.drivingDistance));
        if (TextUtils.isEmpty(myAutoV2.brandThumb)) {
            viewHolder.mAutoInfoBrandThumb.setImageResource(R.drawable.pic_normal_square);
        } else {
            ImageUtil.setImageSquare(mContext, viewHolder.mAutoInfoBrandThumb, myAutoV2.brandThumb);
        }
    }

    public int getIsDefault() {
        return isDefault;
    }

    public ArrayList<MyAuto> getmAutoGroups() {
        return mAutoGroups;
    }

    class ViewHolder {
        private ImageView mAutoIsDefaultView;
        private ImageView mAutoInfoBrandThumb;
        private TextView mAutoInfoType;
        private TextView mAutoInfoPlateNum;
        private TextView mAutoDrivingDistance;
    }


}
