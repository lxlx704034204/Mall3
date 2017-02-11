package com.hxqc.mall.thirdshop.maintenance.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.maintenance.model.maintenance.MaintenanceItemN;

/**
 * @Author : 钟学东
 * @Since : 2016-08-12
 * FIXME
 * Todo
 */
public class ConfirmOrderChild extends RelativeLayout {

    private TextView mGooodsNumView;
    private TextView mItemNameView;
    private TextView mGoodsNameView;

    private RelativeLayout mOneGoodsLayout;//单一商品layout
    private TextView mGoodsPriceView; //物品价格
    private TextView mWorkPriceView; //工时费
    private TextView mTextOneView; // type 材料费

    public ConfirmOrderChild(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.item_maintain_confirm_order_first_child,this);
        initView();
    }

    private void initView() {
        mGooodsNumView = (TextView) findViewById(R.id.goods_count);
        mGooodsNumView.setVisibility(View.VISIBLE);
        mItemNameView = (TextView) findViewById(R.id.item_name);
        mGoodsNameView = (TextView) findViewById(R.id.one_goods_name);
        mGoodsPriceView = (TextView) findViewById(R.id.goods_price);
        mOneGoodsLayout = (RelativeLayout) findViewById(R.id.one_goods_layout);
        mWorkPriceView = (TextView) findViewById(R.id.work_price);
        mTextOneView = (TextView) findViewById(R.id.tv1);

    }

    public void initDate(MaintenanceItemN maintenanceItemN){

        mItemNameView.setText(maintenanceItemN.name);

        mGoodsNameView.setText(maintenanceItemN.goods.get(0).name);
        float tempWorkCost = Float.valueOf(maintenanceItemN.workCost);
        if(tempWorkCost < 0.01){
            mWorkPriceView.setText("免费");
        }else {
            mWorkPriceView.setText(OtherUtil.amountFormat(maintenanceItemN.workCost,true));
        }

        if(maintenanceItemN.goods.get(0).price < 0.01){
            mOneGoodsLayout.setVisibility(GONE);
        }else {
            mGoodsPriceView.setText(OtherUtil.amountFormat(maintenanceItemN.goods.get(0).price,true));
        }

    }

    public void setGoodsNum(String string){
        mGooodsNumView.setText(string);
    }
}
