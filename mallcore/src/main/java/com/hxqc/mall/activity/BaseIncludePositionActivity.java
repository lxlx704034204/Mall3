package com.hxqc.mall.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.widget.TextView;

import com.hxqc.mall.core.R;
import com.hxqc.mall.core.db.areacacheutil_new.AreaCacheUtil;
import com.hxqc.mall.core.util.BaseSharedPreferencesHelper;
import com.hxqc.util.DebugLog;

import java.util.LinkedList;

/**
 * Function: 基类包含定位信息的Activity
 *
 * @author 袁秉勇
 * @since 2016年03月31日
 */
public class BaseIncludePositionActivity extends NoBackActivity {
    private final static String TAG = BaseIncludePositionActivity.class.getSimpleName();
    protected BaseSharedPreferencesHelper sharedPreferencesHelper;
    protected TextView mChangeCityView; //城市定位选择按钮
    protected AreaCacheUtil areaCacheUtil;
    protected String choosedCity; //判断是否存在历史城市后得到的城市数据
	private Context mContext;

    private void initLocationData() {
        if (sharedPreferencesHelper == null) {
            sharedPreferencesHelper = new BaseSharedPreferencesHelper(this);
        }

        String historyCity;
        String city = sharedPreferencesHelper.getCity();//当前定位城市
        LinkedList< String > historyCityList = sharedPreferencesHelper.getHistoryCity();

        if (historyCityList.isEmpty()) {
            mChangeCityView.setText("全国");
        } else {
            historyCity = historyCityList.getFirst();
            if (!TextUtils.isEmpty(historyCity)) {
                mChangeCityView.setText(historyCity);
            } else {
                mChangeCityView.setText("全国");
            }
        }

        String lastCity = !TextUtils.isEmpty(mChangeCityView.getText().toString()) ? mChangeCityView.getText().toString() : "全国";

        boolean isShowTipDialog = false;

        areaCacheUtil = AreaCacheUtil.getInstance(this);
        final AreaCacheUtil   cheUtil = AreaCacheUtil.getInstance(this);
        if (areaCacheUtil.checkExistCity(AreaCacheUtil.NEWENERGY, lastCity)) {
            choosedCity = lastCity;
        } else {
            LinkedList< String > historyCityList1 = sharedPreferencesHelper.getHistoryCity();
            if (!historyCityList1.isEmpty() && !TextUtils.isEmpty(historyCityList1.getFirst())) {
                mChangeCityView.setText(historyCityList1.getFirst());
                choosedCity = historyCityList1.getFirst();
                city = historyCityList1.getFirst();
            } else {
                mChangeCityView.setText("武汉市");
                sharedPreferencesHelper.setHistoryCity("武汉市");
                choosedCity = "武汉市";
                city = "武汉市";
                isShowTipDialog = true;

                AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MaterialDialog);
                builder.setCancelable(false);

                final String finalCity = city;
                builder.setTitle("提示").
                        setMessage("当期城市暂未开通该功能，已自动为您获取 武汉市 数据")//您当前城市是【%@】，需要切换吗？
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (!TextUtils.isEmpty(finalCity) && areaCacheUtil.checkExistCity(AreaCacheUtil.NEWENERGY, finalCity)) {

                                            if (!finalCity.equals(choosedCity)) {
                                                startSettingDialog(finalCity);
                                            }
                                        }
                                    }
                                }, 500);
                            }
                        }).create().show();
            }
        }

        if (!isShowTipDialog) {
            if (!TextUtils.isEmpty(city) && areaCacheUtil.checkExistCity(AreaCacheUtil.NEWENERGY, city)) {
                if (!city.equals(choosedCity)) {
                    startSettingDialog(city);
                }
            }
        }
    }


    /**
     * @param city 当前城市
     * @return
     */
    private void startSettingDialog(final String city) {
        if (TextUtils.isEmpty(city) || sharedPreferencesHelper.getPositionTranslate()) {
            return;
        } else {
            sharedPreferencesHelper.setPositionTranslate(true);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MaterialDialog);
        builder.setCancelable(false);
        builder.setTitle("提示").
                setMessage("您当前城市是【" + city + "】,需要切换吗？")//您当前城市是【%@】，需要切换吗？
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mChangeCityView.setText(city);
                        choosedCity = city;
                        sharedPreferencesHelper.setHistoryCity(city);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        }).create().show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 1) {
            mChangeCityView.setText(data.getStringExtra("position"));
            if (!sharedPreferencesHelper.getCity().equals(data.getStringExtra("position"))) {
                DebugLog.e(getClass().getName(), "I don't believe!!");
                choosedCity = data.getStringExtra("position");
                if (choosedCity.equals("全国")) choosedCity = "武汉市";
            }
        }
    }
}
