package com.hxqc.mall.core.adapter;

import android.content.Context;
import android.widget.BaseAdapter;

import com.hxqc.mall.core.model.auto.PickupPointT;

/**
 * Function: 用于控制地图选店展示列表的BaseAdapter
 *
 * @author 袁秉勇
 * @since 2016年06月02日
 */
public abstract class BaseMapListAdapter extends BaseAdapter {
    private final static String TAG = BaseMapListAdapter.class.getSimpleName();
    private Context mContext;
    protected ClickCallBack clickCallBack;


    public void setClickCallBack(ClickCallBack callBack) {
        this.clickCallBack = callBack;
    }


    protected int selectedPosition = -1;


    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }


    public interface ClickCallBack {
        void clickCallBack(PickupPointT pickupPointT);
    }
}
