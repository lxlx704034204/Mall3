package com.hxqc.mall.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.model.LatLng;
import com.hxqc.mall.core.R;
import com.hxqc.mall.core.controler.AMapLocationControl;
import com.hxqc.mall.core.db.areacacheutil_new.AreaCacheUtil;
import com.hxqc.mall.core.util.BaseSharedPreferencesHelper;
import com.hxqc.mall.core.util.MapUtils;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.views.SideBar;
import com.hxqc.util.DebugLog;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Function: 定位城市选择基类
 *
 * @author 袁秉勇
 * @since 2016年02月18日
 */
public abstract class BasePositionActivity extends BackActivity implements ExpandableListView.OnChildClickListener {
    private static final String TAG = "BasePositionActivity";

    protected ExpandableListView location_expand_list_view;
    protected TextView position_city;
    protected SideBar sidebar_position;
    protected ArrayMap< String, Integer > sideTagMap;
    protected BaseSharedPreferencesHelper baseSharedPreferencesHelper;
    protected View header;
    protected HashMap< String, String > mFilterMap = new HashMap<>();
    protected AreaCacheUtil areaCacheUtil;
	/**
	 * 标示当前点击是否来自于显示当前定位城市的按钮
	 **/
	protected boolean clickFromPositionCity = false;
	private boolean positionSuccess = false;
    private boolean showWholeCity = true;
    private boolean showWholeCityTitle = false;
    private AMapLocationControl instance;

    public void setShowWholeCity(boolean showWholeCity) {
        this.showWholeCity = showWholeCity;
    }

    public void setShowWholeCityTitle(boolean showWholeCityTitle) {
        this.showWholeCityTitle = showWholeCityTitle;
    }

    protected abstract void getData();

    public abstract boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id);

    /** 将controler中的HashMap赋值给当前Activity中的mFilterMap **/
    protected abstract void initFilterMap();

    protected void initSharePreferenceHelper() {
        baseSharedPreferencesHelper = new BaseSharedPreferencesHelper(this);
    }

    protected void setContentView() {
        setContentView(R.layout.activity_position);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView();

        initSharePreferenceHelper();

        initView();

        getLocationControl().requestLocation();

        areaCacheUtil = AreaCacheUtil.getInstance(this);

        getData();
        bindData();

        if (gPSIsOPen(this)) {
            DebugLog.d(TAG, "onCreate: " + "定位开启");
        } else {
            DebugLog.d(TAG, "onCreate: " + "定位关闭");
            startSettingDialog().show();
        }
//        if (gGpsPermission(this)) {
//            Log.d(TAG, "onCreate: " + "定位权限开启");
//        } else {
//            Log.d(TAG, "onCreate: " + "定位权限关闭");
//        }
    }

    /** 初始化视图 **/
    private void initView() {
        initFilterMap();
        initListData();
        initHeaderView();
    }

    protected void initListData() {
        header = LayoutInflater.from(this).inflate(R.layout.activity_position_header, null, false);
        location_expand_list_view = (ExpandableListView) findViewById(R.id.location_expand_list_view);
        location_expand_list_view.addHeaderView(header);
        location_expand_list_view.setGroupIndicator(null);
        location_expand_list_view.setChildDivider(getResources().getDrawable(R.color.divider));

        /** 在实现类中完成expandListView的子类点击事件 **/
        location_expand_list_view.setOnChildClickListener(this);
        initSidebar();
    }

    private void initHeaderView() {
        TextView whole_china = (TextView) header.findViewById(R.id.whole_china);
        if (showWholeCity) {
            whole_china.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finishSelfWithResult("全国");
                }
            });
        } else {
            if (showWholeCityTitle) {
                whole_china.setText("可选城市");
            } else {
                whole_china.setVisibility(View.GONE);
            }
        }

        initHistoryCity();
        initPositionCity();
    }

    private void initHistoryCity() {
        View latest_city_bar1 = header.findViewById(R.id.latest_city_bar1);
        GridLayout latest_city_bar2 = (GridLayout) header.findViewById(R.id.latest_city_bar2);


        LinkedList< String > historyCityList = initHistoryCityList();

        if (historyCityList.isEmpty()) {
            latest_city_bar1.setVisibility(View.GONE);
            latest_city_bar2.setVisibility(View.GONE);
        } else {
            for (String city : historyCityList) {
                latest_city_bar2.addView(getAHistoryCityButton(latest_city_bar2, city));
            }
        }
    }

    protected LinkedList< String > initHistoryCityList() {
        return baseSharedPreferencesHelper.getHistoryCity();
    }

    private void initPositionCity() {
        position_city = (TextView) header.findViewById(R.id.position_city);
        position_city.setText("定位中...");
        position_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (positionSuccess) {
                    String cityName = ((TextView) v).getText().toString();
                    clickFromPositionCity = true;
                    finishSelfWithResult(cityName);
                } else {
                    if (gPSIsOPen(BasePositionActivity.this)) {
                        getLocationControl().requestLocation();
                    } else {
                        startSettingDialog().show();
                    }
                }
            }
        });
    }


    private void initSidebar() {
        sidebar_position = (SideBar) findViewById(R.id.sidebar_position);
        sidebar_position.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(int index, String s, StringBuffer s1) {
                Integer p = sideTagMap.get(s);
                if (p != null) {
                    location_expand_list_view.setSelectedGroup(p);
                }
            }
        });
    }


    private void bindData() {
        Intent intent = getIntent();
        String position = intent.getStringExtra("position");
        if (TextUtils.isEmpty(position)) {
            getSupportActionBar().setTitle("当前地区-全国");
        } else {
            getSupportActionBar().setTitle("当前城市-" + position);
        }
    }


    private TextView getAHistoryCityButton(ViewGroup parent, final String cityName) {
        TextView textview_history_position = (TextView) getLayoutInflater().inflate(R.layout.textview_history_position, parent, false);
        textview_history_position.setText(cityName);
        textview_history_position.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishSelfWithResult(cityName);
            }
        });
        return textview_history_position;
    }


    /**
     * 选择城市后finish(),并带值返回上一页
     *
     * @param cityName
     */
    protected void finishSelfWithResult(String cityName) {
        if (TextUtils.isEmpty(cityName) || "全国".equals(cityName)) {
            mFilterMap.remove("area");
            setHistory("全国");
        } else {
            mFilterMap.put("area", cityName);
            setHistory(cityName);
        }
        Intent intent = new Intent();
        intent.putExtra("position", cityName);
        if (clickFromPositionCity) {
            intent.putExtra("clickFromPositionCity", true);
            clickFromPositionCity = false;
        }
        DebugLog.i("home_data", "finishSelfWithResult position " + cityName);
        setResult(1, intent);
        finish();
    }


    /**
     * 选择城市后finish(),并带值返回上一页
     *
     * @param cityName
     */
    protected void finishSelfWithResult(String cityName, String cityID) {
        if (TextUtils.isEmpty(cityName) || "全国".equals(cityName)) {
            mFilterMap.remove("area");
            setHistory("全国");
        } else {
            mFilterMap.put("area", cityName);
            setHistory(cityName);
        }
        Intent intent = new Intent();
        intent.putExtra("position", cityName);
        intent.putExtra("cityID", cityID);
        setResult(1, intent);
        finish();
    }


    protected void setHistory(String cityName) {
        baseSharedPreferencesHelper.setHistoryCity(cityName);
    }


    /**
     * 刷新shared文件中位置信息
     *
     * @param location
     */
    private void refreshSharedLocation(AMapLocation location) {
        baseSharedPreferencesHelper.setCity(location.getCity());
        baseSharedPreferencesHelper.setProvince(location.getProvince());
        baseSharedPreferencesHelper.setLatitude(location.getLatitude() + "");
        baseSharedPreferencesHelper.setLongitude(location.getLongitude() + "");
    }


    /**
     * 启动dialog,确定调转设置定位
     *
     * @return
     */
    private AlertDialog startSettingDialog() {
        return new AlertDialog.Builder(this, R.style.MaterialDialog).setTitle("定位服务未开启").setMessage("请在系统设置中开启定位服务").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(intent, 2);
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        }).create();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if (gPSIsOPen(this)) {
                getLocationControl().requestLocation();
            } else {
                ToastHelper.showRedToast(this, "定位服务未开启");
            }
        }
    }


    /**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     *
     * @param context
     * @return true 表示开启
     */
    public boolean gPSIsOPen(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        return gps || network;
    }


    /**
     * 该方法不起作用
     *
     * @param context
     * @return
     */
    private boolean gGpsPermission(Context context) {
        PackageManager pm = getPackageManager();
        boolean permission_ACCESS_COARSE_LOCATION = (PackageManager.PERMISSION_GRANTED == pm.checkPermission("android.permission.ACCESS_COARSE_LOCATION", context.getPackageName()));
        boolean permission_ACCESS_FINE_LOCATION = (PackageManager.PERMISSION_GRANTED == pm.checkPermission("android.permission.ACCESS_FINE_LOCATION", context.getPackageName()));

        return permission_ACCESS_COARSE_LOCATION || permission_ACCESS_FINE_LOCATION;
    }


    /**
     * 获取Amplocation,调用requesLocation启动定位
     *
     * @return
     */
    private AMapLocationControl getLocationControl() {
        instance = AMapLocationControl.getInstance().setUpLocation(getApplicationContext());
        instance.setCoreLocationListener(new AMapLocationControl.onCoreLocationListener() {
            @Override
            public void onLocationChange(AMapLocation aMapLocation) {
                DebugLog.d(TAG, "onLocationChange: " + aMapLocation.getCity() + aMapLocation.getProvince() + aMapLocation.getLongitude() + aMapLocation.getLatitude());
                if (TextUtils.isEmpty(aMapLocation.getCity().trim())) {
                    position_city.setText("定位失败,请点击重试");
                    positionSuccess = false;
                } else {
//                    getSupportActionBar().setTitle("当前城市-" + aMapLocation.getCity());
                    LatLng latLng = null;
                    try {
                        latLng = MapUtils.bd_encrypt(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    if (latLng != null) {
                        mFilterMap.put("latitude", Double.toString(latLng.latitude));
                        mFilterMap.put("longitude", Double.toString(latLng.longitude));
                    }
                    position_city.setText(aMapLocation.getCity());
                    refreshSharedLocation(aMapLocation);
                    positionSuccess = true;
                }
            }
        });
        instance.setOnGetLocationFailCallBack(new AMapLocationControl.OnGetLocationFailCallBack() {
            @Override
            public void onLocationFail(AMapLocation aMapLocation) {
                DebugLog.d(TAG, "onLocationFail: ");
                position_city.setText("定位失败,请点击重试");
                positionSuccess = false;
            }
        });
        return instance;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        DebugLog.e(TAG, " ---------------> onDestroy");
        if (areaCacheUtil != null) areaCacheUtil.close();
        if (instance != null) {
//            instance.setCoreLocationListener(null);
//            instance.setOnGetLocationFailCallBack(null);
            instance.unregistListener();
//            instance.onDestroy();
        }
    }


    public static class SettingFragment extends DialogFragment {
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return super.onCreateView(inflater, container, savedInstanceState);
        }


        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return super.onCreateDialog(savedInstanceState);
        }
    }
}
