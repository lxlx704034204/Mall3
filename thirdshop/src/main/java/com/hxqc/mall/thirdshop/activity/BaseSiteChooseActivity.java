package com.hxqc.mall.thirdshop.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.widget.TextView;

import com.hxqc.mall.activity.NoBackActivity;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.utils.AreaSiteUtil;
import com.hxqc.mall.thirdshop.utils.SharedPreferencesHelper;

import java.util.LinkedList;

import static com.hxqc.mall.thirdshop.utils.HomeSiteDataUtil.appendWordToStr;

/**
 * Function: 右上角包含分站列表选择Activity的基类
 *
 * @author 袁秉勇
 * @since 2016年06月27日
 */
public abstract class BaseSiteChooseActivity extends NoBackActivity {
    private final static String TAG = BaseSiteChooseActivity.class.getSimpleName();
    private Context mContext;

    protected final static String AREANAME = "areaName";
    protected final static String AREAID = "areaID";

    protected String cityGroupID;

    protected SharedPreferencesHelper sharedPreferencesHelper;

    protected String areaGroup;

    protected AreaSiteUtil areaSiteUtil;

    protected TextView mChangeCityView;


    public String getCityGroupID() {
        return cityGroupID;
    }


    abstract void onResultCallBack(Bundle bundle);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferencesHelper = new SharedPreferencesHelper(this);

        areaSiteUtil = AreaSiteUtil.getInstance(this);

        if (sharedPreferencesHelper.getHistoryCityForSpecialCar().size() > 0 && !TextUtils.isEmpty(sharedPreferencesHelper.getHistoryCityForSpecialCar().getFirst())) {
            cityGroupID = areaSiteUtil.getCityGroupID(sharedPreferencesHelper.getHistoryCityForSpecialCar().getFirst());
        } else {
            cityGroupID = sharedPreferencesHelper.getDefaultSiteData().siteGroupID;
        }
    }


    /** 初始化当前坐标 */
    protected void initLocationData() {
        String historyCity;
        String city = sharedPreferencesHelper.getCity();
        LinkedList< String > historyCityList = sharedPreferencesHelper.getHistoryCityForSpecialCar();
        if (!historyCityList.isEmpty() && !TextUtils.isEmpty(historyCity = historyCityList.getFirst())) {
            mChangeCityView.setText(historyCity);
            cityGroupID = areaSiteUtil.getCityGroupID(historyCity);
            if (!areaSiteUtil.getCityGroup(city).equals(historyCity)) {
                startSettingDialog(city);
            }
            return;
        }

        mChangeCityView.setText(areaGroup = areaSiteUtil.getWHSiteName());
        sharedPreferencesHelper.setHistoryCityForSpecialCar(areaGroup);

        if (areaSiteUtil.getCityGroup(city).endsWith(areaSiteUtil.getWHSiteName())) {
            sharedPreferencesHelper.setCityForSpecialCar(city);
            sharedPreferencesHelper.setHistoryProvinceForSpecialCar(sharedPreferencesHelper.getProvince());
        } else {
            startSettingDialog(city);
        }
    }


    /**
     * @param city 当前城市
     * @return
     */


    private void startSettingDialog(final String city) {
        if (TextUtils.isEmpty(city) || sharedPreferencesHelper.getPositionTranslateForSiteGroup()) {
            return;
        } else {
            sharedPreferencesHelper.setPositionTranslateForSiteGroup(true);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MaterialDialog);
        builder.setCancelable(false);
        builder.setTitle("提示");
        if (areaSiteUtil.adjustCity(city)) {
            builder.setMessage("您当前城市属于【" + (appendWordToStr(areaSiteUtil.getCityGroup(city))) + "】" + ",是否需要进行数据切换?"); // 您当前城市是【%@】，需要切换吗？
        } else {
            builder.setMessage("您当前城市是【" + city + "】,不在分站列表中，已为您切换到【" + (appendWordToStr(areaSiteUtil.getWHSiteName())) + "】"); // 您当前城市是【%@】，需要切换吗？
        }
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (areaSiteUtil.adjustCity(city)) {
                    areaGroup = areaSiteUtil.getCityGroup(city);
                    sharedPreferencesHelper.setCityForSpecialCar(city);
                    sharedPreferencesHelper.setHistoryProvinceForSpecialCar(areaSiteUtil.getCityProvince(city));
                } else {
                    areaGroup = areaSiteUtil.getWHSiteName();
                    sharedPreferencesHelper.setCityForSpecialCar(sharedPreferencesHelper.getDefaultSiteData().siteAreaName);
                    sharedPreferencesHelper.setHistoryProvinceForSpecialCar(areaSiteUtil.getCityProvince("湖北省"));
                }
                cityGroupID = areaSiteUtil.getCityGroupID(areaGroup);
                mChangeCityView.setText(areaGroup);
                sharedPreferencesHelper.setHistoryCityForSpecialCar(areaGroup);
                onResultCallBack(constructBundle(areaGroup, cityGroupID));
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
            String areaName;
            if (!TextUtils.isEmpty(areaName = data.getStringExtra("position"))) {
                if (areaName.contains("市")) {
                    if (!areaSiteUtil.adjustCity(areaName)) {
                        ToastHelper.showRedToast(this, "抱歉，当前城市暂且未开通该功能，敬请期待");
                        return;
                    } else {
                        areaName = areaSiteUtil.getCityGroup(areaName);
                    }
                }
                if (areaName.equals(mChangeCityView.getText().toString())) {
                    return;
                } else {
                    mChangeCityView.setText(areaName);
                }

                areaGroup = areaName;
                cityGroupID = areaSiteUtil.getCityGroupID(areaGroup);
                onResultCallBack(constructBundle(areaGroup, cityGroupID));
            }
        }
    }


    private Bundle constructBundle(String areaName, String cityGroupID) {
        Bundle bundle = new Bundle();
        bundle.putString(AREANAME, areaName);
        bundle.putString(AREAID, cityGroupID);
        return bundle;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        areaSiteUtil.destroy();
    }
}
