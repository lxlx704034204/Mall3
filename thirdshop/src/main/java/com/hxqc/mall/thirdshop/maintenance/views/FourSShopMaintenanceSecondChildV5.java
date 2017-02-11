package com.hxqc.mall.thirdshop.maintenance.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.maintenance.model.maintenance.MaintenanceChildGoodsN;
import com.hxqc.mall.thirdshop.maintenance.model.maintenance.MaintenanceItem;

import java.util.ArrayList;

/**
 * @Author : 钟学东
 * @Since : 2016-12-29
 * FIXME
 * Todo
 */

public class FourSShopMaintenanceSecondChildV5 extends LinearLayout {

    private TextView mItemView;
    private LinearLayout mParentView;

    private Context context;

    private MaintenanceItem maintenanceItem;

    public interface returnItemListener{
        void returnItem(MaintenanceItem maintenanceItem);
    }

    public void setReturnItemLister(returnItemListener returnItemLister){
        this.returnItemListener = returnItemLister;
    }

    private returnItemListener returnItemListener;


    public FourSShopMaintenanceSecondChildV5(Context context) {
        super(context);
        this.context = context;
    }

    public FourSShopMaintenanceSecondChildV5(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.item_four_s_shop_second_child_v5,this);
        initView();
    }

    private void initView() {
        mItemView = (TextView) findViewById(R.id.item_name);
        mParentView = (LinearLayout) findViewById(R.id.parent);
    }

    public void  initDate(MaintenanceItem item, boolean showName, boolean chooseGoods){
        maintenanceItem = item;
        if(showName){
            mItemView.setVisibility(View.VISIBLE);
            mItemView.setText(item.name);
        }else {
            mItemView.setVisibility(View.GONE);
        }

        boolean showCount;
        if(item.goods.size() > 1){
            showCount = true;
        }else {
            showCount = false;
        }

        for(int i = 0 ; i < item.goods.size() ; i++ ){
            LayoutInflater.from(context).inflate(R.layout.item_four_s_shop_third_layer_v5,mParentView);
            FourSShopMaintenanceThirdChildV5 thirdChildV5 = (FourSShopMaintenanceThirdChildV5) mParentView.getChildAt(i);
            thirdChildV5.initDate(item.goods.get(i),showCount,i,chooseGoods);
            final int finalI = i;
            thirdChildV5.setReturnGoodsAgainListener(new FourSShopMaintenanceThirdChildV5.ReturnGoodsAgainListener() {
                @Override
                public void returnGoodsAgain(ArrayList<MaintenanceChildGoodsN> childGoodsNs) {
                    maintenanceItem.goods.remove(finalI);
                    maintenanceItem.goods.add(finalI,childGoodsNs);
                    returnItemListener.returnItem(maintenanceItem);
                }
            });
        }
    }
}
