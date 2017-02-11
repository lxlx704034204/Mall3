package com.hxqc.mall.thirdshop.activity.auto.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.hxqc.mall.auto.config.AutoInfoContants;
import com.hxqc.mall.auto.controler.AutoSPControl;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.activity.auto.control.BaseFilterController;
import com.hxqc.mall.thirdshop.activity.auto.control.FilterControllerForSpecialCar;
import com.hxqc.mall.thirdshop.utils.AreaSiteUtil;
import com.hxqc.mall.thirdshop.utils.SharedPreferencesHelper;

import java.util.LinkedList;

import static com.hxqc.mall.thirdshop.utils.HomeSiteDataUtil.appendWordToStr;

/**
 * Function:
 *
 * @author 袁秉勇
 * @since 2016年05月18日
 */
public class ThirdShopFilterForSpecialAuto extends BaseThirdShopFilterActivity {
    private final static String TAG = ThirdShopFilterForSpecialAuto.class.getSimpleName();
    private Context mContext;

    private String cityGroupID; // 分站站点的ID
    private String areaGroup; // 分站站点名称

    private AreaSiteUtil areaSiteUtil;


    @Override
    public void initController() {
        baseFilterController = FilterControllerForSpecialCar.getInstance();
        AutoSPControl.saveAppointmentFlag(this, AutoInfoContants.FLAG_ACTIVITY_APPOINTMENT_4S);
    }


    @Override
    public BaseFilterController getController() {
        return baseFilterController;
    }


    @Override
    public void destroyController() {
        if (baseFilterController != null) baseFilterController.destroy();
    }


    @Override
    protected void initSharedPreferences() {
        sharedPreferencesHelper = new SharedPreferencesHelper(this);
        areaSiteUtil = AreaSiteUtil.getInstance(this);

        if (sharedPreferencesHelper.getHistoryCityForSpecialCar().size() > 0 && !TextUtils.isEmpty(sharedPreferencesHelper.getHistoryCityForSpecialCar().getFirst())) {
            cityGroupID = areaSiteUtil.getCityGroupID(sharedPreferencesHelper.getHistoryCityForSpecialCar().getFirst());
        } else {
            cityGroupID = sharedPreferencesHelper.getDefaultSiteData().siteGroupID;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        areaSiteUtil.destroy();
    }


    /**
     * 初始化当前坐标
     * final String latitu, final String longitu, final String c
     */
    protected void initLocationData() {
        String historyCity;
        String city = sharedPreferencesHelper.getCity();
        LinkedList< String > historyCityList = sharedPreferencesHelper.getHistoryCityForSpecialCar();
        if (!historyCityList.isEmpty() && !TextUtils.isEmpty(historyCity = historyCityList.getFirst())) {
            mChangeCityView.setText(historyCity);
            cityGroupID = areaSiteUtil.getCityGroupID(historyCity);
            baseFilterController.mFilterMap.put("siteID", cityGroupID);
            if (!areaSiteUtil.getCityGroup(city).equals(historyCity)) {
                startSettingDialog(city);
            }
            return;
        }

        mChangeCityView.setText(areaGroup = areaSiteUtil.getWHSiteName());
        sharedPreferencesHelper.setHistoryCityForSpecialCar(areaGroup);
        baseFilterController.mFilterMap.put("siteID", cityGroupID);

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
            builder.setMessage("您当前城市是【" + city + "】,不在分站列表中,已为您切换到【" + (appendWordToStr(areaSiteUtil.getWHSiteName())) + "】"); // 您当前城市是【%@】，需要切换吗？
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
                baseFilterController.mFilterMap.put("siteID", cityGroupID);
                mChangeCityView.setText(areaGroup);
                sharedPreferencesHelper.setHistoryCityForSpecialCar(areaGroup);
                getData(true);
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
            String position;
            if (!TextUtils.isEmpty(position = data.getStringExtra("position"))) {
                if (position.equals(mChangeCityView.getText().toString())) {
                    return;
                } else {
                    mChangeCityView.setText(position);
                }

                areaGroup = position;
                cityGroupID = areaSiteUtil.getCityGroupID(areaGroup);
                baseFilterController.mFilterMap.put("siteID", cityGroupID);
                getData(true);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tohome, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_to_home) {
            ActivitySwitchBase.toMain(this, 0);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
