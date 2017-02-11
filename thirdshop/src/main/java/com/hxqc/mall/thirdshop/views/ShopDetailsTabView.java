package com.hxqc.mall.thirdshop.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.hxqc.mall.thirdshop.R;

/**
 * liaoguilong
 * Created by CPR113 on 2016/3/16.
 * 店铺详情--TAB
 */
public class ShopDetailsTabView extends LinearLayout implements View.OnClickListener {

    public RadioButton
            grp1,
            grp2,
            grp3,
            grp4,
            grp5,
            grp6,
            grp7,
            grp8,
            grp9;
    public Button mSelectBut;
    private TabChick mTabChick;

    public ShopDetailsTabView(Context context) {
        this(context, null);
    }

    public ShopDetailsTabView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShopDetailsTabView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.activity_shopdetails_tab, this);
        grp1= (RadioButton) findViewById(R.id.shopdetails_grp_1);
        grp2= (RadioButton) findViewById(R.id.shopdetails_grp_2);
        grp3= (RadioButton) findViewById(R.id.shopdetails_grp_3);
        grp4= (RadioButton) findViewById(R.id.shopdetails_grp_4);
        grp5= (RadioButton) findViewById(R.id.shopdetails_grp_5);
        grp6= (RadioButton) findViewById(R.id.shopdetails_grp_6);
        grp7= (RadioButton) findViewById(R.id.shopdetails_grp_7);
        grp8= (RadioButton) findViewById(R.id.shopdetails_grp_8);
        grp9= (RadioButton) findViewById(R.id.shopdetails_grp_9);//分期购车功能 开发中 暂时隐藏
        grp1.setOnClickListener(this);
        grp2.setOnClickListener(this);
        grp3.setOnClickListener(this);
        grp4.setOnClickListener(this);
        grp5.setOnClickListener(this);
        grp6.setOnClickListener(this);
        grp7.setOnClickListener(this);
        grp8.setOnClickListener(this);
        grp9.setOnClickListener(this);
    }

    public void setTabChick(TabChick tabChick) {
        this.mTabChick = tabChick;
    }



    @Override
    public void onClick(View v) {
        check((Button)v);
        if(mTabChick ==null)
            return;
         if(((Button) v).getText().equals(v.getContext().getResources().getString(R.string.shop_details_tab_1)))
         {
             mTabChick.toHomePage(v);
         }else if(((Button) v).getText().equals(v.getContext().getResources().getString(R.string.shop_details_tab_2))){
             mTabChick.toModelsQuote(v);
         }else if(((Button) v).getText().equals(v.getContext().getResources().getString(R.string.shop_details_tab_3))){
             mTabChick.toMaintenance(v);
         }else if(((Button) v).getText().equals(v.getContext().getResources().getString(R.string.shop_details_tab_4))){
             mTabChick.toReserveMaintain(v);
         }else if(((Button) v).getText().equals(v.getContext().getResources().getString(R.string.shop_details_tab_5))){
             mTabChick.toSeckill(v);
         }else if(((Button) v).getText().equals(v.getContext().getResources().getString(R.string.shop_details_tab_6))){
             mTabChick.toSalesP(v);
         }else if(((Button) v).getText().equals(v.getContext().getResources().getString(R.string.shop_details_tab_7))){
             mTabChick.toRescue(v);
         }else if(((Button) v).getText().equals(v.getContext().getResources().getString(R.string.shop_details_tab_8))){
             mTabChick.toAccessorySale(v);
         }else if(((Button) v).getText().equals(v.getContext().getResources().getString(R.string.shop_details_tab_9))){
             mTabChick.toInstallmentBuyCar(v);
         }
    }

    public void check(Button but) {
        if (mSelectBut != null && but == mSelectBut) {
            return;
        }
        if (mSelectBut != null) {
            setCheckedStateForView(mSelectBut, false);
        }
        if (but !=null) {
            setCheckedStateForView(but, true);
        }
       this.mSelectBut=but;
    }

    private void setCheckedStateForView(Button button, boolean checked) {
              button.setSelected(checked);
    }

    /**
     * 清理当前选中的TAB
     */
    public  void cleanTabCheck(){
        if(mSelectBut !=null)
        setCheckedStateForView(mSelectBut,false);
    }


    public interface TabChick {
        /**
         * to首页
         * @param v
         */
        void toHomePage(View v);

        /**
         * to车型报价
         * @param v
         */
        void toModelsQuote(View v);

        /**
         *用品
         * @param v
         */
        void toAccessorySale(View v);

        /**
         *促销信息
         * @param v
         */
        void toSalesP(View v);

        /**
         *维修保养
         * @param v
         */
        void toMaintenance(View v);

        /**
         *紧急救援
         * @param v
         */
        void toRescue(View v);


        /**
         *修车预约
         * @param v
         */
        void toReserveMaintain(View v);

        /**
         * 限时特价车
         * @param v
         */
        void toSeckill(View v);


        /**
         * 分期购车
         * @param v
         */
        void toInstallmentBuyCar(View v);
    }

}
