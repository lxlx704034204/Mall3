package com.hxqc.mall.thirdshop.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.views.DoubleSeekBar;
import com.hxqc.util.DebugLog;
import com.hxqc.widget.GridViewNoSlide;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Arrays;


/**
 * Function: 选车界面
 *
 * @author 袁秉勇
 * @since 2016年11月16日
 */
public class SelectCarByConditionActivity extends BackActivity implements DoubleSeekBar.OnSeekBarChangeListener {
    private final static String TAG = SelectCarByConditionActivity.class.getSimpleName();
    private Context mContext;

    private DoubleSeekBar doubleSeekBar;
    private TextView mBrandView;
    private TextView mPriceRangeView;
    private TextView mCarNumView;
    private GridViewNoSlide mChooseLevelGridView;
    private GridViewNoSlide mChooseCountryGridView;
    private QuickAdapter quickAdapterSelectLevel;
    private QuickAdapter quickAdapterSelectCountry;

    private int lastLevelClickPos = -1;
    private int lastCountryClickPos = -1;

    private String[] countries = {"中国", "美国", "德国", "日本", "韩国", "法国", "瑞典"};
    private String[] levels = {"微型车", "小型车", "紧凑型车", "中型车", "中大型车", "大型车", "跑车", "SUV", "MVP"};
    private int[] images = {R.drawable.minicar_logo, R.drawable.landaulet_logo, R.drawable.compactcar_logo, R.drawable.middle_sized_car_logo, R.drawable.middle_large_car_logo, R.drawable.large_car_logo, R.drawable.sports_car_logo, R.drawable.suv_car_logo, R.drawable.mvp_car_logo};
//    private boolean[] changeStyleForChooseCountry;
//    private boolean[] changeStyleForChooseLevel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        setContentView(R.layout.activity_select_car_by_condition);
        EventBus.getDefault().register(this);

        mBrandView = (TextView) findViewById(R.id.tv_car_brand);
        mPriceRangeView = (TextView) findViewById(R.id.tv_price_range);
        mChooseLevelGridView = (GridViewNoSlide) findViewById(R.id.gird_view_choose_level);
        mChooseCountryGridView = (GridViewNoSlide) findViewById(R.id.gird_view_choose_country);
        mCarNumView = (TextView) findViewById(R.id.tv_car_num);
        doubleSeekBar = (DoubleSeekBar) findViewById(R.id.myseekbar);
        doubleSeekBar.setOnSeekBarChangeListener(this);

        String[] strings = {"5", "8", "12", "18", "25", "35", "50", "70"};
        doubleSeekBar.setSectionText(Arrays.asList(strings));

//        changeStyleForChooseLevel = new boolean[levels.length];
        quickAdapterSelectLevel = new QuickAdapter< String >(mContext, R.layout.item_choose_car_level, Arrays.asList(levels)) {
            @Override
            protected void convert(final BaseAdapterHelper helper, String item) {
                helper.setImageResource(R.id.car_level_image, images[helper.getPosition()]);
                helper.setText(R.id.car_level_tv, levels[helper.getPosition()]);

                final View rootView = helper.getView();
                rootView.findViewById(R.id.car_level_image).setSelected(lastLevelClickPos == helper.getPosition());
                rootView.findViewById(R.id.car_level_image).setSelected(lastLevelClickPos == helper.getPosition());


//                if (rootView.findViewById(R.id.car_level_image).isSelected()) rootView.findViewById(R.id.car_level_image).setSelected(false);
//                if (rootView.findViewById(R.id.car_level_tv).isSelected()) rootView.findViewById(R.id.car_level_tv).setSelected(false);

                rootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (lastLevelClickPos == helper.getPosition()) {
                            lastLevelClickPos = -1;
                        } else {
                            lastLevelClickPos = helper.getPosition();
                        }
                        quickAdapterSelectLevel.notifyDataSetChanged();


//                        changeStyleForChooseLevel[helper.getPosition()] = !changeStyleForChooseLevel[helper.getPosition()];
//
//                        rootView.findViewById(R.id.car_level_image).setSelected(changeStyleForChooseLevel[helper.getPosition()]);
//                        rootView.findViewById(R.id.car_level_tv).setSelected(changeStyleForChooseLevel[helper.getPosition()]);

                        getData();
                    }
                });
            }
        };
        mChooseLevelGridView.setAdapter(quickAdapterSelectLevel);


//        changeStyleForChooseCountry = new boolean[countries.length]; // 建立对应的改变样式标示
        quickAdapterSelectCountry = new QuickAdapter< String >(mContext, R.layout.item_choose_car_country, Arrays.asList(countries)) {
            @Override
            protected void convert(final BaseAdapterHelper helper, String item) {
                helper.setText(R.id.car_country_tv, countries[helper.getPosition()]);

                final TextView tv = helper.getView(R.id.car_country_tv);
                tv.setSelected(lastCountryClickPos == helper.getPosition());


//                if (tv.isSelected()) tv.setSelected(false);

                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (lastCountryClickPos == helper.getPosition()) {
                            lastCountryClickPos = -1;
                        } else {
                            lastCountryClickPos = helper.getPosition();
                        }
                        quickAdapterSelectCountry.notifyDataSetChanged();

//                        changeStyleForChooseCountry[helper.getPosition()] = !changeStyleForChooseCountry[helper.getPosition()];
//                        tv.setSelected(changeStyleForChooseCountry[helper.getPosition()]);
//                        ToastHelper.showGreenToast(SelectCarByConditionActivity.this, countries[helper.getPosition()]);

                        getData();
                    }
                });
            }
        };
        mChooseCountryGridView.setAdapter(quickAdapterSelectCountry);
    }


    /** 获取符合条件的车型数量 **/
    private void getData() {
        mCarNumView.setText("正在加载...");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mCarNumView.setText("共 888 款车系符合要求");
            }
        }, 2000);
    }


    /** 选择品牌界面 **/
    public void clickToChooseBrand(View view) {
        Intent intent = new Intent(this, ShopBrandActivity.class);
        intent.putExtra(ShopBrandActivity.FROM_CHOOSE_CAR_FRAGMENT, true);
        startActivity(intent);
    }


    public void clickReset(View view) {
        mBrandView.setText("");
        mPriceRangeView.setText("");

        /** 重置样式改变标识符 **/
//        for (int i = 0; i < changeStyleForChooseCountry.length; i++) {
//            changeStyleForChooseCountry[i] = false;
//        }
        lastCountryClickPos = -1;
        quickAdapterSelectCountry.notifyDataSetChanged();

//        for (int i = 0; i < changeStyleForChooseLevel.length; i++) {
//            changeStyleForChooseLevel[i] = false;
//        }
        lastLevelClickPos = -1;
        quickAdapterSelectLevel.notifyDataSetChanged();

        doubleSeekBar.resetSeekBar();

        getData();
    }


    @Subscribe
    public void changeBrandTvEvent(String brandName) {
        DebugLog.e(TAG, "==================== execute");
        if (mBrandView != null) mBrandView.setText(brandName);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Override
    public void onProgressChanged(int minCurrentSection, String leftText, int maxCurrentSection, String rightText) {
        if (maxCurrentSection < 0 && minCurrentSection < 0) {
            mPriceRangeView.setText("不限");
        } else if (maxCurrentSection < 0) {
            mPriceRangeView.setText(leftText + " 以上");
        } else {
            mPriceRangeView.setText(leftText + " - " + rightText);
        }
    }
}
