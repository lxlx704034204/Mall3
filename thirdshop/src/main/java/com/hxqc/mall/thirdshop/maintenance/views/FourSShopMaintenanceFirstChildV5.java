package com.hxqc.mall.thirdshop.maintenance.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.maintenance.control.FourSAndQuickHelper;
import com.hxqc.mall.thirdshop.maintenance.model.maintenance.MaintenanceChildGoodsN;
import com.hxqc.mall.thirdshop.maintenance.model.maintenance.MaintenanceItem;
import com.hxqc.mall.thirdshop.maintenance.model.maintenance.MaintenanceItemGroup;
import com.hxqc.util.DebugLog;
import com.hxqc.util.OtherUtil;

/**
 * @Author : 钟学东
 * @Since : 2016-12-28
 * FIXME
 * Todo 保养第五版 4s店流程 子View
 */

public class FourSShopMaintenanceFirstChildV5 extends LinearLayout {

    /***********************************  View  ****************************************/
    private Context context;
    private ImageView mSelectView;
    private TextView mItemNameView; //项目名
    private TextView mWorkPriceView; //工时费
    private TextView mGoodCountView; //项目序号 用于确认订单页面显示

    private LinearLayout mParentView; //父控件 用于装子View
    private RelativeLayout mRlPriceView; //用于 确认订单页面隐藏
    private TextView mItemPriceView; //项目总价
    private TextView mItemNamePriceView ; //项目总价的汉语提示
    private TextView mOldPriceView; //套餐的原价

    private View line; //虚线

    private RelativeLayout mRlItemGroupView;

    /************************************  data  *************************************/
    private FourSAndQuickHelper fourSAndQuickHelper;
    private MaintenanceItemGroup itemGroup;
    private int position ;
    private String flag ;
    private boolean chooseGoods; //项目是否选中


    /************************************  interface *************************************/

    public interface CalculateMoneyListener {
        void CalculateMoney(MaintenanceItemGroup itemGroup, int position);
    }

    public void setCaulateMoenyListener(CalculateMoneyListener calculateMoneyListener) {
        this.calculateMoneyListener = calculateMoneyListener;
    }

    public interface BothGroupListener {
        void bothGroup();
    }

    public void setBothGroupListener(BothGroupListener bothGroupListener) {
        this.bothGroupListener = bothGroupListener;
    }

    private BothGroupListener bothGroupListener;

    private CalculateMoneyListener calculateMoneyListener;

    /************************************  function  *************************************/

    public FourSShopMaintenanceFirstChildV5(Context context) {
        super(context);
        this.context = context;
        fourSAndQuickHelper = FourSAndQuickHelper.getInstance();
    }

    public FourSShopMaintenanceFirstChildV5(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        fourSAndQuickHelper = FourSAndQuickHelper.getInstance();
        LayoutInflater.from(context).inflate(R.layout.item_four_s_shop_first_child_v5,this);
        initView();
        initEven();
    }

    private void initView() {
        mSelectView = (ImageView) findViewById(R.id.iv_select);
        mItemNameView = (TextView) findViewById(R.id.item_name);
        mWorkPriceView = (TextView) findViewById(R.id.work_price);
        mGoodCountView = (TextView) findViewById(R.id.goods_count);
        mParentView = (LinearLayout) findViewById(R.id.ll_parent);
        mRlPriceView = (RelativeLayout) findViewById(R.id.rl_price);
        mItemPriceView = (TextView) findViewById(R.id.item_price);
        mItemNamePriceView = (TextView) findViewById(R.id.tv1);
        mOldPriceView = (TextView) findViewById(R.id.old_price);
        line = findViewById(R.id.line);
        line.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mRlItemGroupView = (RelativeLayout) findViewById(R.id.rl_item_group);

    }

    /**
     * @param itemGroup
     * @param flag 1 4s店铺  2 确认订单
     */
    public void     initDate(final MaintenanceItemGroup itemGroup, final String flag, final int position){
        this.itemGroup = itemGroup;
        this.position = position;
        this.flag = flag;
        fourSAndQuickHelper.setMaintenanceItemGroupFlag(flag);

        fourSAndQuickHelper.CalculateItemMoneyFor4S(itemGroup, new FourSAndQuickHelper.CalculateItemGroupMoneyFor4SHandle() {
            @Override
            public void onCalculateItemGroupMoney(float itemAmount, float workCoast, float goodsAmount ,float oldPrice ,boolean isDiscount) {
                if(isDiscount){
                    mItemNamePriceView.setText("套餐价 ");
                    mItemPriceView.setText(OtherUtil.amountFormat(itemAmount,true));
                    mOldPriceView.setVisibility(View.VISIBLE);
                    mOldPriceView.setText("原价  "+ OtherUtil.amountFormat(oldPrice,true));
                    mOldPriceView.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG); //中划线
                }else {
                    mItemNamePriceView.setText("单项小计 ");
                    mItemPriceView.setText(OtherUtil.amountFormat(itemAmount,true));
                    mOldPriceView.setVisibility(View.GONE);
                }

                mItemNameView.setText(itemGroup.name);
                //小于1分就认为是免费
                Double tempWorkCost = Double.valueOf(workCoast);
                if (tempWorkCost < 0.01) {
                    mWorkPriceView.setText("免费");
                } else {
                    mWorkPriceView.setText(com.hxqc.mall.core.util.OtherUtil.amountFormat(workCoast, true));
                }
                if(flag.equals("1")){ //4s店
                    mRlItemGroupView.setBackgroundColor(getResources().getColor(R.color.gray));
                    mGoodCountView.setVisibility(View.GONE);
                    mSelectView.setVisibility(View.VISIBLE);
                    mRlPriceView.setVisibility(View.VISIBLE);
                    if(itemGroup.isCheck){
                        mSelectView.setImageResource(R.drawable.maintain_for_4s_check);
                        chooseGoods = true;
                    }else {
                        mSelectView.setImageResource(R.drawable.maintain_for_4s_no_check);
                        chooseGoods = false;
                    }
                }else { //确认订单
                    int num = position + 1;
                    mGoodCountView.setVisibility(View.VISIBLE);
                    mGoodCountView.setText(num + ".");
                    mSelectView.setVisibility(View.INVISIBLE);
                    mRlPriceView.setVisibility(View.GONE);
                }

            }
        });

        initChildView();
    }

    //初始化子view
    private void initChildView() {
        boolean showName;
        if(itemGroup.items.size() > 1){ //有多个项目 显示项目名
            showName = true;
        }else {
            showName = false;
        }
        mParentView.removeAllViews();
        for(int i = 0 ; i < itemGroup.items.size() ; i++){
            LayoutInflater.from(context).inflate(R.layout.item_four_s_shop_second_layer_v5,mParentView);
            FourSShopMaintenanceSecondChildV5 secondChildV5 = (FourSShopMaintenanceSecondChildV5) mParentView.getChildAt(i);
            secondChildV5.initDate(itemGroup.items.get(i),showName,chooseGoods);
            final int finalI = i;
            secondChildV5.setReturnItemLister(new FourSShopMaintenanceSecondChildV5.returnItemListener() {
                @Override
                public void returnItem(MaintenanceItem maintenanceItem) {
                    itemGroup.items.remove(finalI);
                    itemGroup.items.add(finalI,maintenanceItem);
                    initDate(itemGroup,flag,position);
                    calculateMoneyListener.CalculateMoney(itemGroup,position);
                }
            });
        }
    }


    private void initEven() {
        mRlItemGroupView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemGroup.deleteable == 1) { //是否可以删除 默认1为可以删除 ，0为不可删除 boolean
                    if (itemGroup.isCheck) {
                        itemGroup.isCheck = false;
                        chooseGoods = false;
                        mSelectView.setImageResource(R.drawable.maintain_for_4s_no_check);
                    } else {
                        itemGroup.isCheck = true;
                        chooseGoods = true;
                        mSelectView.setImageResource(R.drawable.maintain_for_4s_check);
                        fourSAndQuickHelper.getMutexItemsFor4S(itemGroup,itemGroup.isCheck);
                    }
                    fourSAndQuickHelper.getBothItemsFor4s(itemGroup,itemGroup.isCheck);
                    if (bothGroupListener != null) {
                        bothGroupListener.bothGroup();
                    }
                    if (calculateMoneyListener != null) {
                        calculateMoneyListener.CalculateMoney(itemGroup, position);
                    }
                    initDate(itemGroup,flag,position);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MaterialDialog);
                    builder.setMessage(itemGroup.name + "为必选").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).create().show();
                }
            }
        });
    }
}
