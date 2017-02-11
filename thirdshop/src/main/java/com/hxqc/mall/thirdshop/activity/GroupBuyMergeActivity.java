package com.hxqc.mall.thirdshop.activity;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxqc.mall.activity.WebActivity;
import com.hxqc.mall.core.api.ApiUtil;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.BaseSharedPreferencesHelper;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.api.ThirdPartShopClient;
import com.hxqc.mall.thirdshop.utils.ActivitySwitcherThirdPartShop;
import com.hxqc.mall.thirdshop.utils.AreaSiteUtil;
import com.hxqc.mall.thirdshop.utils.SharedPreferencesHelper;
import com.hxqc.util.DebugLog;

import java.util.HashMap;
import java.util.LinkedList;

import static com.hxqc.mall.thirdshop.utils.HomeSiteDataUtil.appendWordToStr;

/**
 * Function: 团购会页面
 *
 * @author 袁秉勇
 * @since 2017年01月23日
 */
public class GroupBuyMergeActivity extends WebActivity {
    public final static String SHOWTOHOME = "toHome";
    private final static String TAG = GroupBuyMergeActivity.class.getSimpleName();
    private TextView mChangeCityView;
    private ImageView mToHomeView;
    private boolean showToHome = false;

    private SharedPreferencesHelper sharedPreferencesHelper;
    private AreaSiteUtil areaSiteUtil;
    private String cityGroupID;
    private String areaGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showToHome = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA) != null ? getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getBoolean(SHOWTOHOME, false) : getIntent().getExtras() != null && getIntent().getExtras().getBoolean(SHOWTOHOME, false);

        mChangeCityView = (TextView) findViewById(R.id.change_city);
        mChangeCityView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivitySwitcherThirdPartShop.toSpecialCarChoosePositon(GroupBuyMergeActivity.this, 1, ((TextView) v).getText().toString());
            }
        });

        mToHomeView = (ImageView) findViewById(R.id.to_home);
        mToHomeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivitySwitchBase.toMain(GroupBuyMergeActivity.this, 0);
            }
        });

        if (showToHome) {
            mToHomeView.setVisibility(View.VISIBLE);
            mChangeCityView.setVisibility(View.GONE);
        }

        initSharedPreferences();
        initLocationData();

        loadUrl();
    }


    @Override
    protected void initActionBar() {
        // 此处不需要使用ActionBar
    }


    @Override
    protected Toolbar initToolBar() {
        Toolbar toolbar = (Toolbar) getLayoutInflater().inflate(R.layout.activity_groupbuy, parent, false);
        return toolbar;
    }


    protected void initSharedPreferences() {
        sharedPreferencesHelper = new SharedPreferencesHelper(this);
        areaSiteUtil = AreaSiteUtil.getInstance(this);

        if (sharedPreferencesHelper.getHistoryCityForSpecialCar().size() > 0 && !TextUtils.isEmpty(sharedPreferencesHelper.getHistoryCityForSpecialCar().getFirst())) {
            cityGroupID = areaSiteUtil.getCityGroupID(sharedPreferencesHelper.getHistoryCityForSpecialCar().getFirst());
        } else {
            cityGroupID = sharedPreferencesHelper.getDefaultSiteData().siteGroupID;
        }
    }


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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == 1) {
            String position;
            if (!TextUtils.isEmpty(position = data.getStringExtra("position"))) {
                if (data.getBooleanExtra("clickFromPositionCity", false)) {
                    position = areaSiteUtil.getCityGroup(position);
                }
                if (position.equals(mChangeCityView.getText().toString())) {
                    return;
                } else {
                    mChangeCityView.setText(position);
//                    mWebView.clearHistory(); // 清除历史回退栈
                    needClearHistory = true;
                }

                areaGroup = position;
                cityGroupID = areaSiteUtil.getCityGroupID(areaGroup);
                loadUrl();
            }
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

//                    mWebView.clearHistory(); // 清除历史回退栈
                    needClearHistory = true;
                } else {
                    areaGroup = areaSiteUtil.getWHSiteName();
                    sharedPreferencesHelper.setCityForSpecialCar(sharedPreferencesHelper.getDefaultSiteData().siteAreaName);
                    sharedPreferencesHelper.setHistoryProvinceForSpecialCar(areaSiteUtil.getCityProvince("湖北省"));
                }
                sharedPreferencesHelper.setSpecialCarAreaHistoryPinYing(areaSiteUtil.getPinYin(areaGroup));
                cityGroupID = areaSiteUtil.getCityGroupID(areaGroup);
//                baseFilterController.mFilterMap.put("siteID", cityGroupID);
                mChangeCityView.setText(areaGroup);
                sharedPreferencesHelper.setHistoryCityForSpecialCar(areaGroup);
//                getData(true);
                loadUrl();
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        }).create().show();
    }


    @Override
    public void loadUrl() {
        if (mChangeCityView == null && mToHomeView == null) return;

        String site = new BaseSharedPreferencesHelper(GroupBuyMergeActivity.this).getSpecialCarAreaHistoryPinYing();
        String url = new ThirdPartShopClient().getGrouponURL(site);

        this.setTitle("团购汇");

        if (!TextUtils.isEmpty(url)) {
            DebugLog.e(getClass().getName(), url);
            HashMap< String, String > map = new HashMap<>();

            map.put("token", UserInfoHelper.getInstance().getToken(this));
            if (url.startsWith("/")) {
                mWebView.loadUrl(ApiUtil.AccountHostURL + url, map);
            } else {
                mWebView.loadUrl(url, map);
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (areaSiteUtil != null) areaSiteUtil.destroy();
    }
}
