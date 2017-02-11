package com.hxqc.mall.thirdshop.maintenance.views;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.maintenance.adapter.FourSShopMaintainAdapterN;
import com.hxqc.mall.thirdshop.maintenance.adapter.MaintainItemDecoration;
import com.hxqc.mall.thirdshop.maintenance.control.FourSAndQuickHelper;
import com.hxqc.mall.thirdshop.maintenance.model.maintenance.MaintenanceChildGoodsN;
import com.hxqc.util.DebugLog;
import com.hxqc.util.OtherUtil;

import java.util.ArrayList;

/**
 * @Author : 钟学东
 * @Since : 2016-12-29
 * FIXME
 * Todo
 */

public class FourSShopMaintenanceThirdChildV5 extends LinearLayout implements FourSShopMaintainAdapterN.ReturnGoodsListener {

    private TextView mGoodsCountView; //商品数量
    private TextView mGoodsNameView; //商品名
    private TextView mGoodsPriceView; //商品价格
    private TextView mGoodsPriceNameView; //商品价格的中文提示语
    private RecyclerView mRecyclerView;
    private RelativeLayout mRlGoodView; //商品名的RelativeLayout 用于显示隐藏

    private FourSShopMaintainAdapterN maintainAdapter;

    private Context context;
    private ArrayList<MaintenanceChildGoodsN> childGoodsNs;
    private FourSAndQuickHelper fourSAndQuickHelper;

    public interface ReturnGoodsAgainListener{
        void returnGoodsAgain(ArrayList<MaintenanceChildGoodsN> childGoodsNs);
    }

    private ReturnGoodsAgainListener returnGoodsAgainListener;

    public void setReturnGoodsAgainListener(ReturnGoodsAgainListener l){
        returnGoodsAgainListener = l;
    }

    public FourSShopMaintenanceThirdChildV5(Context context) {
        super(context);
        this.context = context;
    }

    public FourSShopMaintenanceThirdChildV5(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        fourSAndQuickHelper = FourSAndQuickHelper.getInstance();
        LayoutInflater.from(context).inflate(R.layout.item_four_s_shop_third_child_v5,this);
        initView();
    }

    private void initView() {
        mGoodsCountView = (TextView) findViewById(R.id.goods_count);
        mGoodsNameView = (TextView) findViewById(R.id.goods_name);
        mGoodsPriceView = (TextView) findViewById(R.id.goods_price);
        mGoodsPriceNameView = (TextView) findViewById(R.id.tv1);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.addItemDecoration(new MaintainItemDecoration(0, 0, 16, 0));
        mRlGoodView = (RelativeLayout) findViewById(R.id.rl_good);
    }

    /**
     *
     * @param showCount  一个项目包含两个以上商品 就显示count
     * @param position
     * @param chooseGoods 项目组是否被选中
     */
    public void initDate(ArrayList<MaintenanceChildGoodsN> childGoodsNs , boolean showCount, int position, boolean chooseGoods){
        this.childGoodsNs = childGoodsNs;
        String flag = fourSAndQuickHelper.getMaintenanceItemGroupFlag();

        for(MaintenanceChildGoodsN childGoodsN : childGoodsNs){
            if(childGoodsN.choose == 1){
                mGoodsNameView.setText(childGoodsN.name);
            }
        }

        // 计算规则 （price-dicountG）*count）
        fourSAndQuickHelper.CalculateGoodMoenyFor4S(childGoodsNs.get(0), new FourSAndQuickHelper.CalculateGoodsMoneyFor4SHandle() {
            @Override
            public void onCalculateGoodsMoney(float goodsAmount,float goodsSale) {
                mGoodsPriceView.setText(OtherUtil.amountFormat(goodsAmount ,true));
            }
        });

        DebugLog.i("TAG", "chooseGoods   " + chooseGoods + "");
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        if(showCount){ // 有多个商品时
            int num = position + 1;
            mGoodsCountView.setVisibility(View.VISIBLE);
            mGoodsCountView.setText(num + ".");
            if(childGoodsNs.size() ==1 || flag.equals("2")){//没有替换商品的时候 或者用于确认订单 4s home页面时
                mRecyclerView.setVisibility(View.GONE);
                mGoodsPriceView.setVisibility(View.VISIBLE);
                mGoodsPriceNameView.setVisibility(View.VISIBLE);
            }else{// 有替换商品的时候
                mRecyclerView.setVisibility(View.VISIBLE);
                mGoodsPriceView.setVisibility(View.GONE);
                mGoodsPriceNameView.setVisibility(View.GONE);
                maintainAdapter = new FourSShopMaintainAdapterN(context, childGoodsNs,chooseGoods);
                mRecyclerView.setLayoutManager(layoutManager);
                mRecyclerView.setAdapter(maintainAdapter);
                maintainAdapter.setReturnGoodsListener(this);
            }
        }else { //一个商品时
            mGoodsCountView.setVisibility(View.GONE);
            if (childGoodsNs.size() ==1 || flag.equals("2")){ //没有替换商品的时候或者用于确认订单 4s home页面时
                mRlGoodView.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
            }else{ // 有替换商品的时候
                mRlGoodView.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
                maintainAdapter = new FourSShopMaintainAdapterN(context, childGoodsNs,chooseGoods);
                mRecyclerView.setAdapter(maintainAdapter);
                mRecyclerView.setLayoutManager(layoutManager);
                maintainAdapter.setReturnGoodsListener(this);
            }
        }
    }



    @Override
    public void returnGoods(int position) {
        for(int i = 0 ; i < childGoodsNs.size() ; i++){
            if(i == position){
                childGoodsNs.get(i).choose = 1; //选中
            }else {
                childGoodsNs.get(i).choose = 0;
            }
        }
        returnGoodsAgainListener.returnGoodsAgain(childGoodsNs);
    }
}
