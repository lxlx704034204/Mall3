package com.hxqc.mall.thirdshop.activity.auto.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.hxqc.mall.core.util.MapUtils;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.activity.auto.control.BaseFilterController;
import com.hxqc.mall.thirdshop.utils.SharedPreferencesHelper;

import java.util.LinkedList;

/**
 * Function:
 *
 * @author 袁秉勇
 * @since 2016年05月18日
 */
public class ThirdShopFilterForNewAuto extends BaseThirdShopFilterActivity {
    private final static String TAG = ThirdShopFilterForNewAuto.class.getSimpleName();
    private Context mContext;


    @Override
    public void initController() {
        baseFilterController = BaseFilterController.getInstance();
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
    }


    /**
     * 初始化当前坐标
     * final String latitu, final String longitu, final String c
     */
    protected void initLocationData() {
        if (sharedPreferencesHelper == null) {
            initSharedPreferences();
        }
        String city = sharedPreferencesHelper.getCity();
        LinkedList< String > historyCityList = sharedPreferencesHelper.getHistoryCity();
        if (historyCityList.isEmpty()) {
            showLocationIcon(false);
            mChangeCityView.setText("全国");
            baseFilterController.mFilterMap.put("area", "全国");
            startSettingDialog(sharedPreferencesHelper.getCity());
        } else {
            String historyCity = historyCityList.getFirst();
            if (!TextUtils.isEmpty(historyCity)) {
                mChangeCityView.setText(historyCity);
            } else {
                mChangeCityView.setText("全国");
            }
            baseFilterController.mFilterMap.put("area", mChangeCityView.getText().toString());
            if (!city.equals(historyCity)) {
                showLocationIcon(false);
                startSettingDialog(city);
            } else {
                showLocationIcon(true);
                if (searchShopAdapter != null) searchShopAdapter.setShowDistance(true);
                baseFilterController.mFilterMap.remove("area");
            }
        }

        //只有定位成功后才上传地理坐标
//        if (!TextUtils.isEmpty(latitu) && !TextUtils.isEmpty(longitu)) {
        try {
            if (!TextUtils.isEmpty(sharedPreferencesHelper.getLatitude()) && !TextUtils.isEmpty(sharedPreferencesHelper.getLongitude())) latLng = MapUtils.bd_encrypt(Double.parseDouble(sharedPreferencesHelper.getLatitude()), Double.parseDouble(sharedPreferencesHelper.getLongitude()));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        if (latLng != null) {
            baseFilterController.mFilterMap.put("latitude", Double.toString(latLng.latitude));
            baseFilterController.mFilterMap.put("longitude", Double.toString(latLng.longitude));
//            searchHashMap.put("latitude", Double.toString(latLng.latitude));
//            searchHashMap.put("longitude", Double.toString(latLng.longitude));
//                baseFilterController.mFilterMap.put("latitude", Double.toString(latLng.latitude));
//                baseFilterController.mFilterMap.put("longitude", Double.toString(latLng.longitude));
        }
    }

    //来自定位后的执行代码
//        if (!TextUtils.isEmpty(c) && !city.equals(c)) {
//            InfoSubmitSuccessConfirmDialog infoSubmitSuccessConfirmDialog = new InfoSubmitSuccessConfirmDialog
// (this, "提示：", "定位成功，是否切换到当前定位城市，并重新获取数据？", new InfoSubmitSuccessConfirmDialog.ConfirmListener() {
//                @Override
//                public void confirm() {
    //应要求,不再从SP文件中获取上次定位的位置
//                        if (!TextUtils.isEmpty(c)) {
//                            mChangeCityView.setText(c);
//                              mChangeCityView.setCompoundDrawables(null, null, null, null);//手动选择地区,不要定位的图标
//                        }

//                    if (!TextUtils.isEmpty(c)) {
//                        baseFilterController.mFilterMap.put("area", c);
//                        searchHashMap.put("area", c);
//                    }

//                        if (latLng != null) {
//                            sharedPreferencesHelper.setLatitude(latitu + "");
//                            sharedPreferencesHelper.setLongitude(longitu + "");
//                            baseFilterController.mFilterMap.put("latitude", Double.toString(latLng.latitude));
//                            baseFilterController.mFilterMap.put("longitude", Double.toString(latLng.longitude));
//                            searchHashMap.put("latitude", Double.toString(latLng.latitude));
//                            searchHashMap.put("longitude", Double.toString(latLng.longitude));
//                        }

    //定位成功后重新刷新当前数据
//                    if (RECENT_OPERATE_TYPE == 0) {
//                        if (requestType == 0) {
//                            baseFilterController.getShopData(FilterThirdShopActivity.this, mPage = DEFAULT_PATE,
// PER_PAGE, 2, baseFilterController.mFilterMap, FilterThirdShopActivity.this);
//                        } else if (requestType == 1) {
//                            baseFilterController.getCarData(FilterThirdShopActivity.this, mPage = DEFAULT_PATE,
// PER_PAGE, 2, baseFilterController.mFilterMap, FilterThirdShopActivity.this);
//                        }
//                    } else if (RECENT_OPERATE_TYPE == 1) {
//                        if (searchType == 0) {
//                            baseFilterController.getShopData(FilterThirdShopActivity.this, mPage = DEFAULT_PATE,
// PER_PAGE, 2, searchHashMap, FilterThirdShopActivity.this);
//                        } else if (searchType == 1) {
//                            baseFilterController.getCarData(FilterThirdShopActivity.this, mPage = DEFAULT_PATE,
// PER_PAGE, 2, searchHashMap, FilterThirdShopActivity.this);
//                        }
//                    }
//                }
//            });
//            infoSubmitSuccessConfirmDialog.mOffView.setVisibility(View.VISIBLE);
//            infoSubmitSuccessConfirmDialog.show();
//        }
//    }


    /**
     * @param city 当前城市
     * @return
     */
    private void startSettingDialog(final String city) {
//        android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar

        if (TextUtils.isEmpty(city) || sharedPreferencesHelper.getPositionTranslate()) {
            return;
        } else {
            sharedPreferencesHelper.setPositionTranslate(true);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MaterialDialog);
        builder.setCancelable(false);
        builder.setTitle("提示").
                setMessage("您当前城市是【" + city + "】,是否需要进行数据切换?")//您当前城市是【%@】，需要切换吗？
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showLocationIcon(true);
                        mChangeCityView.setText(city);
                        baseFilterController.mFilterMap.remove("area");
                        if (searchShopAdapter != null) searchShopAdapter.setShowDistance(true);
                        /** 如果是从新车进入该页就车辆数据，否则搜索店铺数据 */
                        getData(true);
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

        echo(baseFilterController.mFilterMap);

        if (requestCode == 1 && resultCode == 1) {
            String position;
            if (!TextUtils.isEmpty(position = data.getStringExtra("position"))) {
                if (position.equals(mChangeCityView.getText().toString())) {
                    return;
                } else {
                    mChangeCityView.setText(position);
                }

                if (position.equals(sharedPreferencesHelper.getCity())) {
                    showLocationIcon(true);
                    if (searchShopAdapter != null) searchShopAdapter.setShowDistance(true);
                    baseFilterController.mFilterMap.remove("area");
                } else {
                    showLocationIcon(false);
                    if (searchShopAdapter != null) searchShopAdapter.setShowDistance(false);
                }

                getData(true);
            }
        }
    }
}