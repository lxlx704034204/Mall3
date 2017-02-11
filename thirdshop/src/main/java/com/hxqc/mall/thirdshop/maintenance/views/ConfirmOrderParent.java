//package com.hxqc.mall.thirdshop.maintenance.views;
//
//import android.content.Context;
//import android.util.AttributeSet;
//import android.view.LayoutInflater;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.hxqc.mall.core.util.OtherUtil;
//import com.hxqc.mall.thirdshop.R;
//import com.hxqc.mall.thirdshop.maintenance.control.FourSAndQuickHelper;
//import com.hxqc.mall.thirdshop.maintenance.model.maintenance.MaintenanceGoods;
//import com.hxqc.mall.thirdshop.maintenance.model.maintenance.MaintenanceItemN;
//
//import java.util.ArrayList;
//
///**
// * @Author : 钟学东
// * @Since : 2016-03-08
// * FIXME
// * Todo 确认订单 订单详情View 的父布局
// */
//public class ConfirmOrderParent extends LinearLayout {
//
//    private TextView mNumView;
//    private TextView mNameView;
//    private LinearLayout mParentView;
//    private Context context;
//    private MaintenanceItemN maintenanceItemN;
//    private FourSAndQuickHelper fourSAndQuickHelper;
//
//    public ConfirmOrderParent(Context context) {
//        super(context);
//        this.context = context;
//        fourSAndQuickHelper = FourSAndQuickHelper.getInstance();
//    }
//
//    public ConfirmOrderParent(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        this.context = context;
//        fourSAndQuickHelper = FourSAndQuickHelper.getInstance();
//        LayoutInflater.from(context).inflate(R.layout.item_confirm_order_first_layer_item, this);
//        initView();
//    }
//
//    //4s店 快修店
//    public void setDate(MaintenanceItemN maintenanceItemN , String shopID) {
//        this.maintenanceItemN = maintenanceItemN;
//        ArrayList<MaintenanceGoods> maintenanceGoodses = maintenanceItemN.goods;
//
//        for (int i = 0; i < maintenanceGoodses.size(); i++) {
//            LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.item_maintain_min_child, mParentView);
//
//            MaintainMinChild maintainMinChild = (MaintainMinChild) linearLayout.getChildAt(i);
//            maintainMinChild.initDate(maintenanceItemN.goods.get(i),shopID);
//
//        }
//        LinearLayout laborLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.layout_item_labor_hour, mParentView);
//        calculateMoney(laborLayout);
//    }
//
//    private void calculateMoney(final LinearLayout ll_laborHour) {
//        TextView mLaborHour = (TextView) ll_laborHour.findViewById(R.id.labor_price);
//        mLaborHour.setText(OtherUtil.CalculateRangeMoney(maintenanceItemN.workCost,true));
//
//        fourSAndQuickHelper.CalculateItemMoney(maintenanceItemN.goods, maintenanceItemN.workCost, new FourSAndQuickHelper.CalculateItemMoneyHandle() {
//            @Override
//            public void onSuccess(float goodsAmount, String amount) {
//                TextView mGoodsPriceView = (TextView) ll_laborHour.findViewById(R.id.accessory);
//                TextView mAmountView = (TextView) ll_laborHour.findViewById(R.id.payable);
//                mGoodsPriceView.setText(OtherUtil.amountFormat(goodsAmount,true));
//                mAmountView.setText(OtherUtil.CalculateRangeMoney(amount,true));
//            }
//        });
//    }
//
//    private void initView() {
//        mNumView = (TextView) findViewById(R.id.number);
//        mNameView = (TextView) findViewById(R.id.name);
//        mParentView = (LinearLayout) findViewById(R.id.ll);
//    }
//
//    public void setmNumView(String num) {
//        mNumView.setText(num);
//    }
//
//    public void setmNameView(String name) {
//        mNameView.setText(name);
//    }
//
//}
